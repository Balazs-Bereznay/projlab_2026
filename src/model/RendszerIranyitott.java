package model;

/**
 * Azokat az objektumokat jelölő interfész, amelyeket a rendszer irányít.
 */
public interface RendszerIranyitott {

    /**
     * Útvonalat keres a megadott térképen.
     * @param terkep A térkép, amelyen az útvonal keresése történik.
     */
    void utvonalKeres(Terkep terkep);
}