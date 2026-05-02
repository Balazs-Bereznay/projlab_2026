package model;

import java.util.List;

/**
 * Egy tisztíıtófej, ami képes egy útegységre zúzalékot kiszórni, hogy meggátolja annak
 * csúszósságát.
 */
public class Zuzalekszoro extends Fej implements ProtoEntitas{
    ///A játékban lévő közös nyilvántartó.
    private Nyilvantarto nyilvantarto;
    /// Egy használattal egyszerre ekkora adag zúzalékot tud kiszórni az adott útegységre.
    private static final int ZUZALEK_ADAG = 5;

    public Zuzalekszoro(Nyilvantarto ny) {
        this.nyilvantarto = ny;
    }

    public Zuzalekszoro() {
        this(null);
    }

    /**
     * Feldolgozza a zúzalékszóró fejre érkező, egyszerű prototípus-parancsokat.
     *
     * @param parancs a feldolgozandó parancs neve
     * @param args a parancs további paraméterei
     */
    @Override
    public void parancsFeldolgoz(String parancs, List<String> args, ObjektumKatalogus katalogus) {
        if (parancs == null) {
            return;
        }

        switch (parancs) {
            case "info":
                String currentId = args.get(0);
                String nyStr = (this.nyilvantarto != null) ? this.nyilvantarto.toString() : "null";

                String infoKimenet = """
                    %s:
                    zuzalekAdag: %d
                    nyilvantarto: %s
                    """.formatted(
                        currentId,
                        ZUZALEK_ADAG,
                        nyStr
                );

                System.out.print(infoKimenet);
                System.out.println("Info displayed");
                break;

            default:
                break;
        }
    }

    /**
     * Ha a szükséges zúzalék mennyiség rendelkezésre áll, akkor azt kiszórja az útegységre.
     * @param utegyseg Az aktuális útszakasz egysége, amelyre kiszórja a zúzalékot.
     * @return Igaz, ha volt elég zúzalék és céljának megfelelően, jeges útegységen lett használva a fej, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        Hokotro hokotro = (Hokotro)utegyseg.getJarmu();
        if(hokotro.zuzalekLevon(ZUZALEK_ADAG)){
            utegyseg.setZuzalek(true);
            utegyseg.setLetaposottsag(0);
            if(utegyseg.getJeges()) {
                utegyseg.setJeges(false);
                System.out.println("A hókotró sikeresen használta a zúzalékszóró fejet.");
                return true;
            }
        }
        System.out.println("Nem volt sikeres a zúzalékszóró fej használata.");
        return false;
    }

    /**
     * Visszatér a nyilvantarto tagváltozó értékével.
     * @return A közös nyilvántartó, amit a Bolt tud kezelni.
     */
    public Nyilvantarto getNyilvantarto() {
        return nyilvantarto;
    }

    /**
     * A nyilvantarto nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     * @param nyilvantarto Az új nyilvántartó, amit a Bolt tud kezelni.
     */
    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        this.nyilvantarto = nyilvantarto;
    }
}
