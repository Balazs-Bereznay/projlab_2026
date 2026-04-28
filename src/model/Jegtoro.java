package model;

/**
 * Speciális kotrófej, amely a jeges útfelületek feltörésére szolgál.
 * Nem tisztítja meg az útegységet, csak a jeget átalakítja hóvá.
 */
public class Jegtoro extends Fej {
    /**
     * Feltöri a jeget az adott útegységen, hogy az utána már
     * takaríthatóvá váljon más fejekkel.
     * @param utegyseg A jeges útegység.
     * @return Igaz, ha sikeres volt a jég feltörése, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        if(utegyseg.getJegMagassag() > 0){
            utegyseg.jegtores();
            System.out.println("A hókotró sikeresen használta a jégtörő fejet.");
            return true;
        }
        System.out.println("Nem volt sikeres a jégtörő fej használata.");
        return false;
    }
}
