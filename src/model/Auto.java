package model;

import java.util.ArrayList;

/**
 * A rendszer által irányított jármű, amely önállóan keres útvonalat.
 */
public class Auto extends Jarmu implements RendszerIranyitott {
    private Csomopont kezdopont;
    private Csomopont celpont;
    private int utonToltottIdo;

    public Auto() {
        super();
        this.kezdopont = null;
        this.celpont = null;
        this.utonToltottIdo = 0;
    }

    public Auto(Csomopont kezdopont, Csomopont celpont, int sebesseg, Utegyseg utegyseg, int tapadas) {
        super(sebesseg, utegyseg, tapadas);
        this.kezdopont = kezdopont;
        this.celpont = celpont;
        this.utonToltottIdo = 0;
    }

    /**
     * Megtervezi az autó útvonalát a kezdőpont és a célpont között.
     * @param kezdopont Az indulási csomópont.
     * @param vegpont Az érkezési csomópont.
     */
    public void utvonalTervezes(Csomopont kezdopont, Csomopont vegpont) {
        this.kezdopont = kezdopont;
        this.celpont = vegpont;

        System.out.println("Auto utvonalTervezes() meghivva.");
        this.kijeloltUtvonal = new ArrayList<>();
    }

    /**
     * Útvonalat keres az autó számára a térkép alapján.
     * @param terkep A térkép, amelyen az útvonal keresése történik.
     */
    @Override
    public void utvonalKeres(Terkep terkep) {
        System.out.println("Auto utvonalKeres(Terkep) meghivva.");
        this.kijeloltUtvonal = new ArrayList<>();
    }

    /**
     * Az autó egy lépését modellezi.
     * Sikeres lépés esetén nő az úton töltött idő.
     */
    @Override
    public void lep() {
        super.lep();
        if (!baleset && !elakadt) {
            utonToltottIdo++;
            System.out.println("Auto uton toltott ideje novelve: " + utonToltottIdo);
        }
    }

    public Csomopont getKezdopont() {
        return kezdopont;
    }

    public void setKezdopont(Csomopont kezdopont) {
        this.kezdopont = kezdopont;
    }

    public Csomopont getCelpont() {
        return celpont;
    }

    public void setCelpont(Csomopont celpont) {
        this.celpont = celpont;
    }

    public int getUtonToltottIdo() {
        return utonToltottIdo;
    }

    public void setUtonToltottIdo(int utonToltottIdo) {
        this.utonToltottIdo = utonToltottIdo;
    }

    @Override
    public String toString() {
        return super.toString()
                + " Auto{kezdopont=" + kezdopont
                + ", celpont=" + celpont
                + ", utonToltottIdo=" + utonToltottIdo
                + "}";
    }
}