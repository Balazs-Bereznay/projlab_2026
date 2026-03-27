package model;

/**
 * A hókotróra felszerelhető kotrófejek absztrakt ősosztálya.
 * Meghatározza, hogy az adott fej milyen tulajdonságokkal rendelkezik,
 * és milyen módon befolyásolja az útviszonyokat.
 */
public abstract class Fej {
    /// Megadja, hogy mennyiért lehet megvásárolni a fejet
    protected int ertek;

    public Fej(int ertek) {
        this.ertek = ertek;
    }

    /**
     * Végrehajtja a hókotróra szerelt fejhez tartozó takarítási logikát
     * a megadott útegységen.
     * @param utegyseg Az aktuális útszakasz egysége, amelyen a takarítás történik.
     */
    public abstract boolean hasznal(Utegyseg utegyseg);

    public int getErtek() {
        return ertek;
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }
}