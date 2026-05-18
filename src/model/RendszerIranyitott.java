package model;

/**
 * Azokat az objektumokat jelolo interfesz, amelyeket a rendszer iranyit.
 */
public interface RendszerIranyitott {

    /**
     * Utvonalat keres a megadott terkepen.
     *
     * @param terkep a terkep, amelyen az utvonal keresese tortenik
     */
    void utvonalKeres(Terkep terkep);
}
