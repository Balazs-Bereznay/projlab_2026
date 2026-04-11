package model;

/**
 * A buszok jövedelmezőségét növelő fejlesztés. Alkalmazásával a busz minden megálló 
 * érintésekor vagy menet végén több pénzt generál.
 */
public class Hozamfejlesztes extends Buszfejlesztes {

    public Hozamfejlesztes(int ar, int novelesMerteke) {
        this.ar = ar;
        this.novelesMerteke = novelesMerteke;
    }

    /**
     * Megnöveli a busz által teljesített körökért megszerezhető 
     * maximális bevétel értékét.
     */
    @Override
    public void fejleszt() {
        System.out.println("Hozamfejlesztés alkalmazva.");
        if(busz == null){
            return;
        }
        busz.setSebesseg(busz.getBevetel() + novelesMerteke);
        busz.nyilvantarto.penzLevon(ar);
    }
}