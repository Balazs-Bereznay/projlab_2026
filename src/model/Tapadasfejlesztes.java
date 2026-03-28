package model;

/**
 * A busznak a tapadását javító fejlesztés, aminek eredményeképpen 
 * kisebb eséllyel fog megcsúszni a jeges útszakaszokon.
 */
public class Tapadasfejlesztes extends Buszfejlesztes {

    /**
     * Megnöveli a busz tapadását, így amikor jeges útegységen 
     * halad át, kisebb eséllyel fog megcsúszni, elkerülve az összecsúszást (balesetet).
     */
    @Override
    public void fejleszt() {
        System.out.println("Tapadásfejlesztés alkalmazva.");
        // TODO:
    }
}