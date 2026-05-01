package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Az úthálózat szakaszait (Ut) összekötő csatlakozási pontok. Kezeli a járművek be és
 * kilépését a csomópontba és a hozzá csatlakozó utakra.
 */
public class Csomopont  implements ProtoEntitas {
    private ArrayList<Ut> utLista;
    private boolean celpont;
    private boolean buszmegallo;
    private String azonosito;

    private static int azonositoSzamlalo = 0;   ///új

    ///Konstruktorok
    public Csomopont(ArrayList<Ut> utLista, boolean celpont, boolean buszmegallo){
        this.utLista = (utLista != null) ? new ArrayList<>(utLista) : new ArrayList<>();
        this.celpont = celpont;
        this.buszmegallo = buszmegallo;

        azonositoSzamlalo++;
        this.azonosito = String.valueOf(azonositoSzamlalo);
    }

    public Csomopont(){
        this(new ArrayList<>(), false, false);
    }

    @Override
    public void parancsFeldolgoz(String parancs, Ut ut, List<String> args) {
        if (parancs.equals("assign")) {
            this.addUt(ut);
            //ott allitjuk be ami meg nem volt beallitva
            if(ut.getVegpont1() == null){
                ut.setVegpont1(this);
            }
            if(ut.getVegpont1() != null &&  ut.getVegpont2() == null){
                ut.setVegpont2(this);
            }
            System.out.println("Út sikeresen a csomóponthoz rendelve.");
        } else if (parancs.equals("remove")) {
            this.removeUt(ut);
            System.out.println("Út sikeresen eltávolítva a csomópontról.");
        } else {
            ProtoEntitas.super.parancsFeldolgoz(parancs, ut, args);
        }
    }

    ///Getterek és setterek
    public boolean getCelpont(){ return celpont; }
    public void setCelpont(boolean celpont){ this.celpont = celpont; }

    public boolean getBuszmegallo(){ return buszmegallo; }
    public void setBuszmegallo(boolean buszmegallo){ this.buszmegallo = buszmegallo; }

    public String getAzonosito(){ return azonosito; }
    public void setAzonosito(String azonosito){ this.azonosito = azonosito; }

    public ArrayList<Ut> getUtLista(){ return utLista; }
    public void setSavok(ArrayList<Ut> utLista){ this.utLista = utLista; };

    ///További metódusok
    /**
     * Kezeli a csomópontra érkező jármű eseményét.
     * Ha a csomópont buszmegálló és a jármű egy Busz, értesíti a járművet.
     * * @param jarmu A csomóponthoz érkező jármű objektum.
     */
    public void jarmuErkezik(Jarmu jarmu) {
        if (jarmu == null) {
            return;
        }

        // Ellenőrizzük a csomópont állapotát és a jármű típusát
        if (this.buszmegallo && jarmu instanceof Busz) {
            Busz busz = (Busz) jarmu;

            // Meghívjuk a busz metódusát, átadva az aktuális csomópontot (this)
            busz.megalloErintese(this);
        }
    }

    /**
     * Kezeli a jármű távozását a csomópontból a tervezett útvonala alapján.
     * Megkeresi a következő utat, majd megpróbálja ráhelyezni a járművet egy szabad sávra.
     * * @param jarmu A távozni kívánó jármű.
     */
    public void jarmuTavozik(Jarmu jarmu) {
        if (jarmu == null) {
            return;
        }

        List<Ut> utvonal = jarmu.getKijeloltUtvonal();

        if (utvonal == null) {
            return;
        }

        Ut kovetkezoUt = null;

        // A következő út megkeresése az útvonalban.
        // Olyan út-párt keresünk, ahol mindkét út ehhez a csomóponthoz csatlakozik.
        for (int i = 0; i <= utvonal.size() - 2; i++) {
            Ut aktualisUt = utvonal.get(i);
            Ut utanaKovetkezoUt = utvonal.get(i + 1);

            boolean aktualisKapcsolodik = (aktualisUt.getVegpont1() == this || aktualisUt.getVegpont2() == this);
            boolean kovetkezoKapcsolodik = (utanaKovetkezoUt.getVegpont1() == this || utanaKovetkezoUt.getVegpont2() == this);

            // Megnézzük, hogy ez a csomópont a "kapocs" a két út között
            if (aktualisKapcsolodik && kovetkezoKapcsolodik) {
                kovetkezoUt = utanaKovetkezoUt;
                break;
            }
        }

        // Ha nem találtunk érvényes következő utat, a jármű nem tud továbbmenni
        if (kovetkezoUt == null) {
            return;
        }

        // Próbálunk egy szabad sávot keresni a következő úton
        for (Sav sav : kovetkezoUt.getSavok()) {
            Utegyseg cel = sav.getElsoUtegyseg();

            // Ha a sávnak van kezdő útegysége
            if (cel != null) {
                // Megpróbálunk rálépni
                boolean sikeres = cel.ralep(jarmu);

                // Ha sikerült rálépni az első útegységre, a távozás sikeres
                if (sikeres) {
                    return;
                }
            }
        }

    }

    /**
     * Hozzáfűz egy Ut típusú objektumot az utLista végére.
     * @param ut Az az út, ami bekerül az utLista listába.
     */
    public void addUt(Ut ut) {
        if (ut != null && !utLista.contains(ut)) {
            utLista.add(ut);
        }
    }
    public void removeUt(Ut ut) {
        if (utLista.contains(ut)) {
            utLista.remove(ut);
        }
    }
}
