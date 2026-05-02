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


    /**
     * Adatok kiírásához, naplózásához szükséges
     * @param id Az entitás azonosítója, amiről összegyűjti az adatot egy string-be
     * @param katalogus A nyilvántartó, amiben az objektumok vannak
     * @return Az entitás adatai egy stringben
     */
    public String info(String id, ObjektumKatalogus katalogus) {
        String v1Id = katalogus.getId(this.vegpont1);
        String v2Id = katalogus.getId(this.vegpont2);

        List<String> savIdk = this.savok.stream()
                .map(katalogus::getId)
                .toList();
        String savokStr = "{ " + String.join(", ", savIdk) + " }";

        return """
                %s:
                vegpont1: %s
                vegpont2: %s
                savok: %s
                alagut: %b
                """.formatted(
                id,
                v1Id,
                v2Id,
                savokStr,
                this.alagut
        );
    }

    @Override
        public void parancsFeldolgoz(String parancs, List<String> args) {
            if (parancs == null || args == null) {
                return;
            }

            switch (parancs) {
                case "set":
                    if (args.size() < 2) return;
                    String tulajdonsag = args.get(0).toLowerCase();
                    String ertek = args.get(1);

                    try {
                        switch (tulajdonsag) {
                            case "alagut":
                                this.alagut = Boolean.parseBoolean(ertek);
                                break;

                            default:
                                break;
                        }
                    } catch (NumberFormatException e) {
                        // Itt kapjuk el, ha a parseInt vagy parseDouble elszállt
                        System.out.println("Hiba: Ervenytelen szamformatum!");
                    }
                    break;

                default:
                    break;
            }
        }

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
        } else if("add".equals(parancs) && args.get(0).equals("utLista")){    // Kb. ugyanazt csinálja, mint az assign (?)
            csomopont.addUt(this);
            System.out.println("Út sikeresen a csomópont út listájához hozzáadva.");
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

    @Override
    public void parancsFeldolgozBusszal(String parancs, Busz busz, List<String> args) {
        if (parancs.equals("add") && args.get(0).equals("kijeloltUtvonal")) {
            busz.addKijeloltUt(this);
            System.out.println("Út sikeresen hozzáadva a busz kijelölt útvonalához.");
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
                if (jarmu != null && jarmu.getMegcsuszott()) {
                    // Megpróbálunk baleseti partnert keresni
                    Jarmu partner = jarmu.keresPartner();

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

