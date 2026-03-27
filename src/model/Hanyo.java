package model;

/**
 * A kotrófejek egyik fajtája, amely a havat elhányja az útról.
 */
public class Hanyo extends Fej {
    public Hanyo(int ertek) {
        super(ertek);
    }

    /**
     * A havat a hókotró az út szélére hányja attól függetlenül, hogy az út hány sávos.
     * @param utegyseg A tisztítandó útegység.
     * @return Igaz, ha az útegység megtisztul a metódus meghívása után, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        boolean tiszta = utegyseg.getHoMagassag() == 0;

        utegyseg.tisztulas();   /// hoMagassag-ot 0-ra állítja

        /**
         * Csak akkor térjen vissza igazzal, ha eredetileg nem volt tiszta
         * és visszatéréskor már teljesen tiszta az útegység, tehát nincs rajta jég sem
         */
        return utegyseg.getJegMagassag() == 0 && !tiszta;
    }
}
