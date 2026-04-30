package model;

/**
 * Tisztítófej, amely a haladási iránynak megfelelően jobbra lévő útegységre
 * söpri a havat (vagy a zúzalékot, ha van rajta) a hókotró sávjából.
 */
public class Sopro extends Fej {
    /**
     * Jobbra lévő útegységre söpri át a vékonyabb hóréteget (vagy a zúzalékot, ha van rajta).
     * Ha nincs ilyen sáv, a havat (illetve zúzalékot) az út szélére túrja.
     * @param utegyseg Az aktuális útegység, ahonnan a hó (illetve zúzalék) elkerül.
     * @return Igaz, ha hó (is) el lett söpörve, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        if(utegyseg.getZuzalek()) {
            utegyseg.setZuzalek(false);
            if(utegyseg.getJobbUtegyseg() != null) {
                utegyseg.getJobbUtegyseg().setZuzalek(true);
            }
        }
        if(utegyseg.getHoMagassag() > 0) {
            if(utegyseg.getJobbUtegyseg() != null) {
                utegyseg.getJobbUtegyseg().havazas(utegyseg.getHoMagassag());
            }
            utegyseg.tisztulas();
            System.out.println("A hókotró sikeresen használta a söprő fejet.");
            return true;
        }
        System.out.println("Nem volt sikeres a söprő fej használata.");
        return false;
    }
}
