package model;

import java.util.ArrayList;

/**
 * Azokat az objektumokat jelölő interfész, amelyeket a játékos irányít.
 */
public interface Iranyithato {

    /**
     * Beállítja az objektum által követett útvonalat.
     * @param utegysegLista A kiválasztott útvonal útegységei.
     */
    void utvonalatValaszt(ArrayList<Utegyseg> utegysegLista);
}