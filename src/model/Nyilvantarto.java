package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Ez a játék nyilvántartója, ami tárolja az összes globális adatot a játék során (pénz, só, kerozin).
 * Illetve figyeli a játékmenetet és ő dönti el, hogy mikor következik be a vereség, a munkába be nem ért autósok száma alapján.
 */

public class Nyilvantarto implements EroforrasKezelo, PenzKezel, ProtoEntitas {

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

    private boolean jatekVege;

    private int nemBeertAutokLimit;

    public Nyilvantarto(int p, int s, int b, int n){
        this.penz = p;
        this.so = s;
        this.biokerozin = b;
        this.nemBeertAutokSzama = n;
    }

    public Nyilvantarto() {
        this(0, 0, 0, 0);
    }

    /**
     * Feldolgozza a nyilvántartóra érkező, egyszerű prototípus-parancsokat.
     *
     * @param parancs a feldolgozandó parancs neve
     * @param args a parancs további paraméterei
     */
    @Override
    public void parancsFeldolgoz(String parancs, List<String> args) {
        if (parancs == null) {
            return;
        }

        switch (parancs) {
            case "info":
                String currentId = args.get(0);

                String infoKimenet = """
                    %s:
                    penz: %d
                    so: %d
                    biokerozin: %d
                    nemBeertAutokSzama: %d
                    nemnemBeertAutokLimit: %d
                    jatekVege: %b
                    """.formatted(
                        currentId,
                        this.penz,
                        this.so,
                        this.biokerozin,
                        this.nemBeertAutokSzama,
                        this.nemBeertAutokLimit,
                        this.jatekVege
                );

                System.out.print(infoKimenet);
                System.out.println("Info displayed");
                break;

            default:
                break;
        }
    }

    @Override
    public void parancsFeldolgoz(String parancs, ProtoEntitas masik, List<String> args) {
        masik.parancsFeldolgozNyilvantartoval(parancs, this, args);
    }

    @Override
    public void parancsFeldolgozFejjel(String parancs, Fej fej, List<String> args) {
        // Visszapasszoljuk a fejnek, mert ő csinálja a tényleges bekötést
        fej.parancsFeldolgozNyilvantartoval(parancs, this, args);
    }

    /**
     * Megvizsgálja, hogy a munkahelyükre be nem ért autók száma meghalad-e egy előre meghatározott határt, ami a játék végét jelenti.
     */
    public boolean ellenorizJatekVege(){
        if(nemBeertAutokSzama > nemBeertAutokLimit){ /// ide mostcsak irtam egy szamot, nem tudom hogy mennyi lesz madj pontosan.
            System.out.println("A játék végetért: Túl sok ember nem ért be időben a munkahelyére:  " + this.nemBeertAutokSzama+"autó akadt el");
            return true;
        }
        else
            return false;
    }

    /**
     * Megnöveli a munkahelyükre be nem ért autók számlálóját.
     */
    public void nemBeertAutokNovel(int mennyiseg){
        this.nemBeertAutokSzama+=mennyiseg;
        System.out.println("Egy újabb autó nem ért be a munkahelyére.");
    }

    /**
     * növeljük a nyilvátartott só mennyiségét
     * @param mennyiseg annak az értéke amennyivel növelni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public void soNovel(int mennyiseg) {
        so+= mennyiseg;
        System.out.println(mennyiseg + " egység só hozzáadva a raktárhoz.");

    }

    /**
     * csökkentjük a nyilvátartott só mennyiségét
     * @param mennyiseg annak az értéke amennyivel csökkenteni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public boolean soLevon(int mennyiseg){
        System.out.println(mennyiseg + " egység só elhasználva.");

        if(so - mennyiseg >= 0){
            so -= mennyiseg;
            return true;
        }
        else {  return  false;}
    }

    /**
     * csökkentjük a nyilvátartott biokerozin mennyiségét
     * @param mennyiseg annak az értéke amennyivel csökkenteni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public boolean biokerozinLevon(int mennyiseg) {

        System.out.println(mennyiseg + " egység biokerozin elhasználva.");

        if(biokerozin - mennyiseg >= 0){
            biokerozin -= mennyiseg;
            return true;
        }
        else {  return  false;}
    }

    /**
     * növeljük a nyilvátartott biokerozin mennyiségét
     * @param mennyiseg annak az értéke amennyivel növelni szeretnénk,
     *  a rendelkezésre álló mennyiséget.
     */
    @Override
    public void biokerozinNovel(int mennyiseg) {
        biokerozin += mennyiseg;
        System.out.println(mennyiseg + " egység biokerozin betöltve a raktárhoz.");
    }

    /**
     * növeljük a bankunkban lévő pénzösszeget.
     * @param mennyiseg annak az értéke amennyivel növelni akarjuk a bankunkban lévő pénz értékét
     */
    @Override
    public void penzNovel(int mennyiseg) {
        penz += mennyiseg;
        System.out.println(" A közös kassza " + mennyiseg + " tallérral nőtt.");
    }

    /**
     * csökkentjük a bankunkban lévő pénzösszeget.
     * @param mennyiseg annak az értéke amennyivel csökkenteni akarjuk a bankunkban lévő pénz értékét
     */
    @Override
    public void penzLevon(int mennyiseg) {
        penz -= mennyiseg;
        System.out.println( mennyiseg + " tallér levonva a közös kasszából.");
    }

    /**
     * A közös kasszában lévő pénz mennyiségének a gettere.
     * @return A közös kassza aktuális egyenlege.
     */
    public int getPenz() {
        System.out.println("Lekérdezték az aktuális egyenleget: " + this.penz);
        return penz;
    }

    /**
     * A közös kasszában lévő pénz mennyiségének a settere.
     * @param penz Az új pénzösszeg, amit a kasszában tárolni szeretnénk.
     */
    public void setPenz(int penz) {
        this.penz = penz;
        System.out.println("A közös kassza egyenlege felülírva, az új érték: " + this.penz);

    }

    /**
     * A raktáron lévő só mennyiségének a gettere.
     * @return Az aktuálisan elérhető só mennyisége.
     */
    public int getSo() {
        System.out.println("Lekérdezték az aktuális sókészletet: " + this.so);
        return so;
    }

    /**
     * A raktáron lévő só mennyiségének a settere.
     * @param so A beállítani kívánt sómennyiség.
     */
    public void setSo(int so) {
        this.so = so;
        System.out.println("A sókészlet állapota felülírva, az új érték: " + this.so);
    }

    /**
     * A rendelkezésre álló biokerozin mennyiségének a gettere.
     * @return Az aktuális biokerozin készlet.
     */
    public int getBiokerozin() {
        System.out.println("Lekérdezték az aktuális biokerozin készletet: " + this.biokerozin);
        return biokerozin;
    }

    /**
     * A rendelkezésre álló biokerozin mennyiségének a settere.
     * @param biokerozin Az új biokerozin mennyiség.
     */
    public void setBiokerozin(int biokerozin) {
        this.biokerozin = biokerozin;
        System.out.println("A biokerozin készlet felülírva, az új érték: " + this.biokerozin);
    }

    /**
     * A munkahelyükre be nem ért autók aktuális számának a gettere.
     * @return Az elakadt autók száma.
     */
    public int getNemBeertAutokSzama() {
        System.out.println("Lekérdezték a be nem ért autók számát: " + this.nemBeertAutokSzama);
        return nemBeertAutokSzama;

    }

    /**
     * A munkahelyükre be nem ért autók számának a settere.
     * @param nem Az elakadt autók új értéke.
     */
    public void setNemBeertAutokSzama(int nem) {
        this.nemBeertAutokSzama = nem;
        System.out.println("A be nem ért autók számlálója felülírva, az új érték: " + this.nemBeertAutokSzama);
    }

    public boolean isJatekVege() {return jatekVege;}
    public void setJatekVege(boolean ertek) { jatekVege = ertek; }

    public int getNemBeertAutokLimit() {return nemBeertAutokLimit;}
    public void setNemBeertAutokLimit(int nem) { nemBeertAutokLimit = nem; }
}
