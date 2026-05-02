package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A kiszórt sóval a jegesedés, vagy a hó megszüntetésére, illetve a havazás megelőzésére
 * használható tisztító fej, ami só erőforrás használatával működik.
 */
class Soszoro extends Fej implements ProtoEntitas {
    ///A játékban lévő közös nyilvántartó.
    private Nyilvantarto nyilvantarto;
    /// Egy használattal egyszerre ekkora adag sót tud kiszórni az adott útegységre.
    private static final int SO_ADAG = 5;

    public Soszoro(Nyilvantarto ny) {
        this.nyilvantarto = ny;
    }
    public Soszoro() {
        this(null);
    }

    /**
     * Feldolgozza a sószóró fejre érkező, egyszerű prototípus-parancsokat.
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
                    soAdag: %d
                    nyilvantarto: %s
                    """.formatted(
                        currentId,
                        SO_ADAG,
                        nyStr
                );

                System.out.print(infoKimenet);
                System.out.println("Info displayed");
                break;

            default:
                break;
        }
    }

    @Override
    public void parancsFeldolgozNyilvantartoval(String parancs, Nyilvantarto ny, List<String> args) {
        if (parancs.equals("assign")) {
            this.setNyilvantarto(ny);
            System.out.println("Nyilvántartó sikeresen a fejhez rendelve.");
        }
    }

    /**
     * Só kiszórásával megkezdi a jég és hó olvasztását, illetve megelőzi
     * újabb hó lerakódását az adott útegységen.
     * A sózás csak akkor hajtódik végre, ha a sólevonás sikeres volt.
     * @param utegyseg Az aktuális útegység, amelyre a sót ki kell szórni.
     * @return Igaz, ha volt elég só, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        if(nyilvantarto.soLevon(SO_ADAG)) {
            utegyseg.sozas(SO_ADAG);
            System.out.println("A hókotró sikeresen használta a sószóró fejet.");
            return true;
        }
        System.out.println("Nem volt sikeres a sószóró fej használata.");
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