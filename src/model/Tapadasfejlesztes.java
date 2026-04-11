package model;

/**
 * A busznak a tapadását javító fejlesztés, aminek eredményeképpen 
 * kisebb eséllyel fog megcsúszni a jeges útszakaszokon.
 */
public class Tapadasfejlesztes extends Buszfejlesztes {

    public Tapadasfejlesztes(int ar, int novelesMerteke) {
        this.ar = ar;
        this.novelesMerteke = novelesMerteke;
    }

    /**
     * Megnöveli a busz tapadását, így amikor jeges útegységen 
     * halad át, kisebb eséllyel fog megcsúszni, elkerülve az összecsúszást (balesetet).
     */
    @Override
    public void fejleszt(Busz busz) {
        System.out.println("Tapadásfejlesztés alkalmazva.");
        if(busz == null){
            return;
        }
        busz.setTapadas(busz.getTapadas() + novelesMerteke);
        busz.nyilvantarto.penzLevon(ar);
    }
}