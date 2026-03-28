package model;

/**
 * Speciális kotrófej, amely a jeges útfelületek feltörésére szolgál.
 * Nem tisztítja meg az útegységet, csak a jeget átalakítja hóvá.
 */
public class Jegtoro extends Fej {
    public Jegtoro(int ertek) {
        super(ertek);
    }

    /**
     * Feltöri a jeget az adott útegységen, hogy az utána már
     * takaríthatóvá váljon más fejekkel.
     * @param utegyseg A jeges útegység.
     * @return Igaz, ha az útegység megtisztul a metódus meghívása után, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        System.out.println("A hókotró használja a hányó fejet.");
        utegyseg.jegtores();

        /// Nem tudja teljesen megtisztítani az útegységet
        return false;
    }
}
