package model;

/**
 * Tisztítófej, amely a haladási iránynak megfelelően jobbra lévő útegységre
 * söpri a havat a hókotró sávjából.
 */
public class Sopro extends Fej {
    public Sopro() {
        super(100);
    }

    /**
     * Jobbra lévő útegységre söpri át a vékonyabb hóréteget.
     * Ha nincs ilyen sáv, a havat az út szélére túrja.
     * @param utegyseg Az aktuális útegység, ahonnan a hó elkerül.
     * @return Igaz, ha az útegység megtisztul a metódus meghívása után, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        System.out.println("A hókotró használja a söprő fejet.");

        return false;
    }
}
