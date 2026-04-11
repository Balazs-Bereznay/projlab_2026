package model;

/**
 * A járművek maximális haladási sebességét növelő fejlesztés. 
 * Segítségével a buszok gyorsabban teljesíthetik útvonalaikat.
 */
public class Sebessegfejlesztes extends Buszfejlesztes {

     public Sebessegfejlesztes(int ar, int novelesMerteke) {
         this.ar = ar;
        this.novelesMerteke = novelesMerteke;
    }

    /**
     * Megnöveli a buszoknak a haladási sebességét, így az 
     * gyorsabban teljesítheti a köreit.
     */
    @Override
    public void fejleszt() {
        System.out.println("Sebességfejlesztés alkalmazva.");
        if(busz == null){
            return;
        }
        busz.setSebesseg(busz.getSebesseg() + novelesMerteke);
        busz.nyilvantarto.penzLevon(ar);
    }
}