package model;

import java.util.ArrayList;

/** Számontartja az összes csomópontot és az összes utat,
 *  valamint útvonalat generál az autók számára.
 */
public class Terkep {
    private ArrayList<Ut> elLista;
    private ArrayList<Csomopont> csomopontLista;

    ///Konstruktorok
    public Terkep(ArrayList<Ut> elLista, ArrayList<Csomopont> csomopontLista){
        this.elLista = (elLista != null) ? new ArrayList<>(elLista) : new ArrayList<>();
        this.csomopontLista = (csomopontLista != null) ? new ArrayList<>(csomopontLista) : new ArrayList<>();
    }

    public Terkep(){
        this(new ArrayList<>(), new ArrayList<>());
    }

    ///További metódusok
    /**
     * Egy kezdő- és egy végpont alapján útvonalat generál az adott autó számára.
     * @param kezdopont Az a kiinduló csomópont, ahonnan az autó indul.
     * @param vegpont Az a legvégső csomópont, ahová az autónak érkeznie kell.
     * @return Az autó útvonalát reprezentáló utakból álló llista.
     */
    public /*ArrayList<Ut>()*/ void utvonalTervezes(Csomopont kezdopont, Csomopont vegpont){
        System.out.println("Elkészült az útvonal.");
    }
}
