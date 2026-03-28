package model;

/**
 *Egységesen nyilvántartja a játékban használt fogyóeszközöket (só, biokerozin),
 *  lehetővé teszi ezeknek a növelését vásárláskor, illetve csökkentését használatkor."
 */

public interface EroforrasKezelo {

    /**
     * A közös készletnek a sómennyiségét növeli
     * @param mennyiseg annak az értéke amennyivel növelni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    void soNovel(int mennyiseg);

    /**
     * A közös készletnek a sómennyiségét csökkenti
     * @param mennyiseg annak az értéke amennyivel csökkenteni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    boolean soLevon(int mennyiseg);

    /**
     * A közös készletnek a biokerozin mennyiségét növeli
     * @param mennyiseg annak az értéke amennyivel növelni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    void biokerozinNovel(int mennyiseg);

    /**
     * A közös készletnek a biokerozin mennyiségét csökkenteni
     * @param mennyiseg annak az értéke amennyivel csökkenteni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    boolean biokerozinLevon(int mennyiseg);
}
