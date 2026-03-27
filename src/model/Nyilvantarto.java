package model;

/**
 * Ez a játék nyilvántartója, ami tárolja az összes globális adatot a játék során (pénz, só, kerozin).
 * Illetve figyeli a játékmenetet és ő dönti el, hogy mikor következik be a vereség, a munkába be nem ért autósok száma alapján.
 */

public class Nyilvantarto implements EroforrasKezelo, PenzKezel {

    /**
     * a közösen tárolt egyenleg
     */
    private int penz;
    /**
     * a közösen tárolt só mennyisége
     */
    private int so;
    /**
     * a közösen tárolt biokerozin mennyisége
     */
    private int biokerozin;
    /**
     * a játék végéhez fontos, a munkába be nem érte NPC-k száma
     */
    private int nemBeertAutokSzama;

    public Nyilvantarto(int p, int s, int b, int n){
        this.penz = p;
        this.so = s;
        this.biokerozin = b;
        this.nemBeertAutokSzama = n;
    }

    /**
     * Megvizsgálja, hogy a munkahelyükre be nem ért autók száma meghalad-e egy előre meghatározott határt, ami a játék végét jelenti.
     */
    public void ellenorizJatekVege(){}

    /**
     * Megnöveli a munkahelyükre be nem ért autók számlálóját.
     */
    public void nemBeertAutokNovel(){}

    /**
     * növeljük a nyilvátartott só mennyiségét
     * @param mennyiseg annak az értéke amennyivel növelni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public void soNovel(int mennyiseg) {}

    /**
     * csökkentjük a nyilvátartott só mennyiségét
     * @param mennyiseg annak az értéke amennyivel csökkenteni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public void soLevon(int mennyiseg){}

    /**
     * csökkentjük a nyilvátartott biokerozin mennyiségét
     * @param mennyiseg annak az értéke amennyivel csökkenteni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public void biokerozinLevon(int mennyiseg) {}

    /**
     * növeljük a nyilvátartott biokerozin mennyiségét
     * @param mennyiseg annak az értéke amennyivel növelni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public void biokerozinNovel(int mennyiseg) {}

    /**
     * növeljük a bankunkban lévő pénzösszeget.
     * @param mennyiseg annak az értéke amennyivel növelni akarjuk a bankunkban lévő pénz értékét
     */
    @Override
    public void penzNovel(int mennyiseg) {}

    /**
     * csökkentjük a bankunkban lévő pénzösszeget.
     * @param mennyiseg annak az értéke amennyivel csökkenteni akarjuk a bankunkban lévő pénz értékét
     */
    @Override
    public void penzLevon(int mennyiseg) {}

    /**
     * A közös kasszában lévő pénz mennyiségének a gettere.
     * @return A közös kassza aktuális egyenlege.
     */
    public int getPenz() {
        return penz;
    }

    /**
     * A közös kasszában lévő pénz mennyiségének a settere.
     * @param penz Az új pénzösszeg, amit a kasszában tárolni szeretnénk.
     */
    public void setPenz(int penz) {
        this.penz = penz;
    }

    /**
     * A raktáron lévő só mennyiségének a gettere.
     * @return Az aktuálisan elérhető só mennyisége.
     */
    public int getSo() {
        return so;
    }

    /**
     * A raktáron lévő só mennyiségének a settere.
     * @param so A beállítani kívánt sómennyiség.
     */
    public void setSo(int so) {
        this.so = so;
    }

    /**
     * A rendelkezésre álló biokerozin mennyiségének a gettere.
     * @return Az aktuális biokerozin készlet.
     */
    public int getBiokerozin() {
        return biokerozin;
    }

    /**
     * A rendelkezésre álló biokerozin mennyiségének a settere.
     * @param biokerozin Az új biokerozin mennyiség.
     */
    public void setBiokerozin(int biokerozin) {
        this.biokerozin = biokerozin;
    }

    /**
     * A munkahelyükre be nem ért autók aktuális számának a gettere.
     * @return Az elakadt autók száma.
     */
    public int getNemBeertAutokSzama() {
        return nemBeertAutokSzama;
    }

    /**
     * A munkahelyükre be nem ért autók számának a settere.
     * @param nem Az elakadt autók új értéke.
     */
    public void setNemBeertAutokSzama(int nem) {
        this.nemBeertAutokSzama = nem;
    }

}
