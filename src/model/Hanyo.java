package model;

/**
 * A kotrófejek egyik fajtája, amely a havat elhányja az útról.
 */
public class Hanyo extends Fej {
    public Hanyo() {
        super(200);
    }

    /**
     * A havat a hókotró az út szélére hányja attól függetlenül, hogy az út hány sávos.
     * @param utegyseg A tisztítandó útegység.
     * @return Igaz, ha az útegység megtisztul a metódus meghívása után, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        System.out.println("A hókotró használja a hányó fejet.");

        return false;
    }
}
