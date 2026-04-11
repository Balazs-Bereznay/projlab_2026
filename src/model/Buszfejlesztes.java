package model;

/**
 * A buszok különböző paramétereinek javításáért felelős absztrakt alaposztály. 
 * Meghatározza az adott fejlesztések költségét.
 */
public abstract class Buszfejlesztes {

    /**
     * A fejlesztés által érintett busz, amelynek a paramétereit javítja.
     */
    protected Busz busz;

    /**
     * A fejlesztés bolti ára.
     */
    protected int ar;

    /** A buszok sebességének növelésének mértéke, amennyivel a buszok
     * gyorsabban haladhatnak a fejlesztés alkalmazása után.
     */
    protected int novelesMerteke;

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