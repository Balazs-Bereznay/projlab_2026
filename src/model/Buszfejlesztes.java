package model;

/**
 * A buszok különböző paramétereinek javításáért felelős absztrakt alaposztály. 
 * Meghatározza az adott fejlesztések költségét.
 */
public abstract class Buszfejlesztes {

    /**
     * A fejlesztés bolti ára.
     */
    protected int ar;

    public int getAr() {
        System.out.println("Lekérdezték a buszfejlesztés árát.");
        return ar;
    }

    public void setAr(int ar) {
        System.out.println("Beállították a buszfejlesztés árát.");
        this.ar = ar;
    }

    /**
     * Végrehajtja a fejlesztésnek a paraméterváltozásait a beruházó buszra.
     */
    public abstract void fejleszt();
}