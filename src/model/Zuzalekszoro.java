package model;

/**
 * Egy tisztíıtófej, ami képes egy útegységre zúzalékot kiszórni, hogy meggátolja annak
 * csúszósságát.
 */
public class Zuzalekszoro extends Fej {
    ///A játékban lévő közös nyilvántartó.
    private Nyilvantarto nyilvantarto;
    /// Egy használattal egyszerre ekkora adag zúzalékot tud kiszórni az adott útegységre.
    private static final int ZUZALEK_ADAG = 5;

    public Zuzalekszoro(Nyilvantarto ny) {
        this.nyilvantarto = ny;
    }

    /**
     * Ha a szükséges zúzalék mennyiség rendelkezésre áll, akkor azt kiszórja az útegységre.
     * @param utegyseg Az aktuális útszakasz egysége, amelyre kiszórja a zúzalékot.
     * @return Igaz, ha volt elég zúzalék, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        if(utegyseg.getJarmu().zuzalekLevon(ZUZALEK_ADAG)){
            utegyseg.setZuzalek(true);
            System.out.println("A hókotró sikeresen használta a zúzalékszóró fejet.");
            return true;
        }
        System.out.println("Nem volt sikeres a zúzalékszóró fej használata.");
        return false;
    }

    /**
     * Visszatér a nyilvantarto tagváltozó értékével.
     * @return A közös nyilvántartó, amit a Bolt tud kezelni.
     */
    public Nyilvantarto getNyilvantarto() {
        return nyilvantarto;
    }

    /**
     * A nyilvantarto nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     * @param nyilvantarto Az új nyilvántartó, amit a Bolt tud kezelni.
     */
    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        this.nyilvantarto = nyilvantarto;
    }
}
