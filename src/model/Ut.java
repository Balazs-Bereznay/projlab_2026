package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Két csomópontot összekötő több sávból álló útszakasz, nyilvántartja ha
 * az útszakaszon baleset történt, illetve, ha alagútról lenne szó.
 */
public class Ut implements ProtoEntitas {
    private Csomopont vegpont1;
    private Csomopont vegpont2;
    private ArrayList<Sav> savok;
    private boolean alagut;

    ///Konstruktorok
    public Ut(ArrayList<Sav> savok, Csomopont vegpont1, Csomopont vegpont2, boolean alagut){
        this.savok = (savok != null) ? new ArrayList<>(savok) : new ArrayList<>();
        this.vegpont1 = vegpont1;
        this.vegpont2 = vegpont2;
        this.alagut = alagut;
    }

    public Ut(){
        this(new ArrayList<>(), null, null, false);
    }

    ///Getterek és setterek
    public Csomopont getVegpont1(){ return vegpont1; }
    public void setVegpont1(Csomopont vegpont1){ this.vegpont1 = vegpont1; }

    public Csomopont getVegpont2(){ return vegpont2; }
    public void setVegpont2(Csomopont vegpont2){ this.vegpont2 = vegpont2; }

    public boolean getAlagut(){ return alagut; }
    public void setAlagut(boolean alagut){ this.alagut = alagut; }

    public ArrayList<Sav> getSavok(){ return savok; }
    public void setSavok(ArrayList<Sav> savok){ this.savok = savok; };


    @Override
    public void parancsFeldolgoz(String parancs, List<String> args) {}

    @Override
    public void parancsFeldolgoz(String parancs, ProtoEntitas cel, List<String> args) {
        cel.parancsFeldolgozUttal(parancs, this, args);
    }

    @Override
    public void parancsFeldolgozCsomoponttal(String parancs, Csomopont csomopont, List<String> args) {
        if ("assign".equals(parancs)) {
            csomopont.addUt(this);
            System.out.println("Út sikeresen a csomóponthoz rendelve.");
        } else if ("remove".equals(parancs)) {
            csomopont.removeUt(this);
            System.out.println("Út eltávolítva a csomópontból.");
        }
    }

    @Override
    public void parancsFeldolgozAutoval(String parancs, Auto auto, List<String> args) {
        if ("assign".equals(parancs)) {
            auto.getKijeloltUtvonal().add(this);
            System.out.println("Út sikeresen hozzáadva az autó útvonalához.");
        } else if ("remove".equals(parancs)) {
            auto.getKijeloltUtvonal().remove(this);
            System.out.println("Út eltávolítva az autó útvonalából.");
        }
    }

    @Override
    public void parancsFeldolgozSavval(String parancs, Sav sav, List<String> args){
        if (parancs.equals("assign")) {
            this.addSav(sav); // Az út listájába felvesszük a sávot
           // sav.setElsoUtegyseg(this);  // A sávnak is beállítjuk, hogy melyik úthoz tartozik
            System.out.println("Sáv sikeresen az úthoz rendelve.");
        }
    }



    ///További metódusok
    /**
     * Végigjárja az út összes sávját és útegységét,
     * baleseti helyzeteket keresve a megcsúszott járművek között.
     */
    public void balesetetKeres() {
        // Végigjárjuk az út összes sávját
        for (Sav sav : savok) {
            // Minden sávban lekérjük az első útegységet
            Utegyseg aktualis = sav.getElsoUtegyseg();

            // Bejárjuk a sávot az útegységek láncolatán keresztül
            while (aktualis != null) {
                Jarmu jarmu = aktualis.getJarmu();

                // Ellenőrizzük, hogy van-e ott jármű, és az megcsúszott-e
                if (jarmu != null && jarmu.isMegcsuszott()) {
                    // Megpróbálunk baleseti partnert keresni
                    Jarmu partner = jarmu.KeresPartner();

                    // Ha találtunk partnert, mindkettőnél kiváltjuk a baleset eseményt
                    if (partner != null) {
                        jarmu.baleset();
                        partner.baleset();
                    }
                }

                // Továbblépünk a következő útegységre
                aktualis = aktualis.getKovetkezoUtegyseg();
            }
        }
    }

    /**
     * Hozzáfűz egy Sav típusú objektumot az savok végéres.
     * @param sav Az a sáv, ami bekerül az savok listába.
     */
    public void addSav(Sav sav) {
        if (sav != null && !savok.contains(sav)) {
            savok.add(sav);
        }
    }
}

