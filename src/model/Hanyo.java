package model;

/**
 * Egy tisztítófej a hókotróhoz, ami a havat (vagy a zúzalékot, ha van rajta) elhányja az útról.
 */
public class Hanyo extends Fej {
    /**
     * A havat (illetve zúzalékot) a hókotró az út szélére hányja attól függetlenül, hogy az út hány sávos.
     * @param utegyseg A tisztítandó útegység.
     * @return Igaz, ha hó (is) el lett takarítva, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        if(utegyseg.getZuzalek()){
            utegyseg.setZuzalek(false);
        }
        if (utegyseg.getHoMagassag() > 0){
            utegyseg.tisztulas();
            System.out.println("A hókotró sikeresen használta a hányó fejet.");
            return true;
        }
        System.out.println("Nem volt sikeres a hányó fej használata.");
        return false;
    }
}
