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
     * Végigiterál az út egyégein, megcsúszott járművet keresve.
     */
    public void balesetetKeres(){
        System.out.println("Baleset keresése.");
    }
}

