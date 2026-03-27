package model;

import java.util.ArrayList;

/**
 * A játékban szereplő járművek absztrakt ősosztálya.
 * Tartalmazza a közös tulajdonságokat és alapvető működéseket.
 */
public abstract class Jarmu {
    protected int sebesseg;
    protected Utegyseg utegyseg;
    protected ArrayList<Ut> kijeloltUtvonal;
    protected int tapadas;
    protected boolean elakadt;
    protected boolean baleset;
    protected boolean megcsuszott;

    public Jarmu() {
        this.sebesseg = 1;
        this.utegyseg = null;
        this.kijeloltUtvonal = new ArrayList<>();
        this.tapadas = 1;
        this.elakadt = false;
        this.baleset = false;
        this.megcsuszott = false;
    }

    public Jarmu(int sebesseg, Utegyseg utegyseg, int tapadas) {
        this.sebesseg = sebesseg;
        this.utegyseg = utegyseg;
        this.kijeloltUtvonal = new ArrayList<>();
        this.tapadas = tapadas;
        this.elakadt = false;
        this.baleset = false;
        this.megcsuszott = false;
    }

    /**
     * A jármű megpróbál egyet lépni a kijelölt útvonalon.
     */
    public void lep() {
        System.out.println(getClass().getSimpleName() + " lep() meghivva.");

        if (baleset) {
            System.out.println("A jarmu balesetet szenvedett, nem tud lepni.");
            return;
        }

        if (elakadt) {
            System.out.println("A jarmu elakadt, nem tud lepni.");
            return;
        }

        if (utegyseg == null) {
            System.out.println("A jarmu nincs utegysegen.");
            return;
        }

        System.out.println("A jarmu megprobal tovabblepni a kijelolt utvonalon.");
    }

    /**
     * A jármű megcsúszását modellezi.
     */
    public void csuszik() {
        this.megcsuszott = true;
        System.out.println(getClass().getSimpleName() + " megcsuszott.");
    }

    /**
     * A jármű balesetét modellezi.
     */
    public void baleset() {
        this.baleset = true;
        System.out.println(getClass().getSimpleName() + " balesetet szenvedett.");
    }

    /**
     * A jármű elakadását modellezi.
     */
    public void elakad() {
        this.elakadt = true;
        System.out.println(getClass().getSimpleName() + " elakadt.");
    }

    public void setKijeloltUtvonal(ArrayList<Ut> kijeloltUtvonal) {
        this.kijeloltUtvonal = kijeloltUtvonal;
    }

    public ArrayList<Ut> getKijeloltUtvonal() {
        return kijeloltUtvonal;
    }

    public int getSebesseg() {
        return sebesseg;
    }

    public void setSebesseg(int sebesseg) {
        this.sebesseg = sebesseg;
    }

    public Utegyseg getUtegyseg() {
        return utegyseg;
    }

    public void setUtegyseg(Utegyseg utegyseg) {
        this.utegyseg = utegyseg;
    }

    public int getTapadas() {
        return tapadas;
    }

    public void setTapadas(int tapadas) {
        this.tapadas = tapadas;
    }

    public boolean isElakadt() {
        return elakadt;
    }

    public boolean isBaleset() {
        return baleset;
    }

    public boolean isMegcsuszott() {
        return megcsuszott;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{sebesseg=" + sebesseg
                + ", tapadas=" + tapadas
                + ", elakadt=" + elakadt
                + ", baleset=" + baleset
                + ", megcsuszott=" + megcsuszott
                + "}";
    }
}