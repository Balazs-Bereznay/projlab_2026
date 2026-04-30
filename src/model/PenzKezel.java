package model;

/**
 * Felületet biztosít a játékosok, járművek és a bolt számára, hogy a közös kassza értékét növeljék egy munka elvégzése következtében,
 * vagy pedig csökkentsék egy tranzakció során.
 */

public interface PenzKezel {

    /**
     * Megnöveli a közös kasszában lévő egyenlegnek az értékét a megadott paraméter értékével.
     * @param mennyiseg annak az értéke amennyivel növelni akarjuk a bankunkban lévő pénz értékét
     */
    void penzNovel(int mennyiseg);

    /**
     * Levonja a megadott értéket a közös kasszából.
     * @param mennyiség annak az értéke amennyivel csökkenteni akarjuk a bankunkban lévő pénz értékét
     */

    void penzLevon(int mennyiség);

}
