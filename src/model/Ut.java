package model;

import java.util.ArrayList;

/**
 * Két csomópontot összekötő több sávból álló útszakasz, nyilvántartja ha
 * az útszakaszon baleset történt, illetve, ha alagútról lenne szó.
 */
public class Ut {
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

