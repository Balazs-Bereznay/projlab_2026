package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy játékos által irányított jármű, amelynek elsődleges feladata,
 * hogy az utakat letisztítsa a hótól és jégtől.
 * A takarítással bevételt termel a közös kasszába,
 * valamint csökkenti a balesetek számát az utakon.
 */
public class Hokotro extends Jarmu implements Iranyithato, ProtoEntitas  {
    private static int BEVETEL = 10;
    private Fej fej;
    private int zuzalekMennyiseg;
    private int zuzalekLimit;
    List<Utegyseg> tervezettUtvonal;

    public Hokotro(Fej fej) {
        super();
        this.fej = fej;
    }

    public Hokotro() {
        this(null);
    }

    @Override
    public void parancsFeldolgoz(String parancs, List<String> args, ObjektumKatalogus katalogus) {
        if ("move".equals(parancs)) {
            if (args.isEmpty()) return;
            String irany = args.get(0);
            if ("-f".equalsIgnoreCase(irany) || "forward".equalsIgnoreCase(irany)) {
                lep();
            } else {
                savValtas(irany);
            }
        } else if ("clean".equals(parancs)) {
            takarit();

        } else if("info".equals(parancs)){
            String currentId = args.get(0);

            String utegysegStr = (this.getUtegyseg() != null) ? this.getUtegyseg().toString() : "null";
            String nyStr = (this.getNyilvantarto() != null) ? this.getNyilvantarto().toString() : "null";

            // A fej típusának (osztálynevének) lekérése, ha nem null
            String fejStr = (this.fej != null) ? this.fej.getClass().getSimpleName() : "null";

            String utvonalTartalom = (this.kijeloltUtvonal == null || this.kijeloltUtvonal.isEmpty())
                    ? ""
                    : String.join(", ", this.kijeloltUtvonal.stream().map(Object::toString).toList());
            String utvonalStr = "{ " + utvonalTartalom + (utvonalTartalom.isEmpty() ? "" : " ") + "}";

            String infoKimenet = """
                %s:
                sebesseg: %d
                utegyseg: %s
                tapadas: %d
                elakadt: %b
                baleset: %b
                megcsuszott: %b
                nyilvantarto: %s
                kijeloltUtvonal: %s
                fej: %s
                bevetel: %d
                zuzalekMennyiseg: %d
                zuzalekLimit: %d
                """.formatted(
                    currentId,
                    this.getSebesseg(),
                    utegysegStr,
                    this.getTapadas(),
                    this.elakadt,
                    this.baleset,
                    this.megcsuszott,
                    nyStr,
                    utvonalStr,
                    fejStr,
                    Hokotro.getBevetel(), // A statikus BEVETEL lekérése
                    this.zuzalekMennyiseg,
                    this.zuzalekLimit
            );

            System.out.print(infoKimenet);
            System.out.println("Info displayed");
        }
    }

    @Override
    public void parancsFeldolgoz(String parancs, ProtoEntitas cel, List<String> args) {
        cel.parancsFeldolgozHokotroval(parancs, this, args);
    }

    @Override
    public void parancsFeldolgozJatekossal(String parancs, Jatekos jatekos, List<String> args) {
        if (args.isEmpty()) return;

        String item = args.get(0).toLowerCase(); // Az item neve a lista eleje

        switch (item) {
            case "hokotro":
                Jatekos.getBolt().hokotroVasarol(jatekos, this);
                break;
            case "soszoro":
                Jatekos.getBolt().soszoroVasarol(this);
                break;
            case "jegtoro":
                Jatekos.getBolt().jegtoroVasarol(this);
                break;
            case "zuzalekszoro":
                Jatekos.getBolt().zuzalekszoroVasarol(this);
                break;
            case "sarkany":
                Jatekos.getBolt().sarkanyVasarol(this);
                break;
            case "sopro":
                Jatekos.getBolt().soproVasarol(this);
                break;
            case "hanyo":
                Jatekos.getBolt().hanyoVasarol(this);
                break;
            case "zuzalek":
                int mennyiseg = 1;
                try {
                    mennyiseg = args.size() >= 3 ? Integer.parseInt(args.get(2)) : 1;
                } catch (NumberFormatException e) {
                }

                Jatekos.getBolt().zuzalekVasarol(this, mennyiseg);
                break;
            default:
                break;
        }
    }

    /**
     * A hókotróra felszerelt fej használatával takarítja a jármű alatti útegységet.
     * A művelet sikeres elvégzése után a jármű bevételt generál a rendszer számára.
     */
    public void takarit() {
        // Ellenőrizzük, hogy a 'fej' tagváltozó nem null (létezik-e objektum)
        if (this.fej == null) {
            return; // Ha hiányzik, kilépünk a metódusból
        }

        // Ellenőrizzük, hogy az 'utegyseg' tagváltozó nem null (van-e hol takarítani)
        if (this.utegyseg == null) {
            return; // Ha hiányzik, kilépünk a metódusból
        }

        // Ha mindkettő rendelkezésre áll, meghívjuk a fej hasznal metódusát
        // az aktuális útegység átadásával.
        if (this.fej.hasznal(this.utegyseg)) {
            nyilvantarto.penzNovel(BEVETEL);
        }
    }

    /**
     * Iranyithato interfész függvénye.
     * Elmenti a kapott útvonalat a tervezettUtvonal tagváltozóba.
     */
    @Override
    public void setKijeloltUtegysegek(List<Utegyseg> utegysegLista) {
        this.tervezettUtvonal = utegysegLista;
    }

    /**
     * A hókotró zúzalékkészletének növelésére szolgál.
     * @param mennyiseg A hozzáadni kívánt mennyiség.
     * @return Igaz, ha történt módosítás, hamis egyébként.
     */
    public int zuzalekNovel(int mennyiseg) {
        // Ha a paraméterként kapott mennyiseg nem pozitív
        if (mennyiseg <= 0) {
            return 0;
        }

        int szabadKapacitas = zuzalekLimit - zuzalekMennyiseg;

        // Ha a tartály már elérte a zuzalekLimit értékét (nincs szabad hely)
        if (szabadKapacitas <= 0) {
            return 0;
        }

        int hozzaadando = mennyiseg;

        // Ha a mennyiség több, mint amennyi belefér, csak a limitig töltjük
        if (mennyiseg > szabadKapacitas) {
            hozzaadando = szabadKapacitas;
        }

        this.zuzalekMennyiseg += hozzaadando;
        return hozzaadando;
    }

    /**
     * A hókotró zúzalékkészletének csökkentésére szolgál.
     * @param mennyiseg A levonni kívánt mennyiség.
     * @return Igaz, ha sikeres a levonás, hamis ha nincs elég készlet vagy érvénytelen a paraméter.
     */
    public boolean zuzalekLevon(int mennyiseg) {
        // Ha a paraméterként kapott mennyiseg nem pozitív
        if (mennyiseg <= 0) {
            return false;
        }

        // Ha van elegendő zúzalék a levonáshoz
        if (this.zuzalekMennyiseg >= mennyiseg) {
            this.zuzalekMennyiseg -= mennyiseg;
            return true;
        } else {
            // Ha nincs elegendő zúzalék
            return false;
        }
    }

    public Fej getFej() {
        return fej;
    }

    public void setFej(Fej fej) {
        this.fej = fej;
    }

    /**
     * Visszatér a bevetel tagváltozó értékével.
     */
    public static int getBevetel() {
        return BEVETEL;
    }

    /**
     * A bevetel nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public static void setBevetel(int bevetel) {
        Hokotro.BEVETEL = bevetel;
    }

    /**
     * Visszatér a zuzalekMennyiseg tagváltozó értékével.
     */
    public int getZuzalekMennyiseg() {
        return zuzalekMennyiseg;
    }

    /**
     * A zuzalekMennyiseg nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public void setZuzalekMennyiseg(int zuzalekMennyiseg) {
        this.zuzalekMennyiseg = zuzalekMennyiseg;
    }

    /**
     * Visszatér a zuzalekLimit tagváltozó értékével.
     */
    public int getZuzalekLimit() {
        return zuzalekLimit;
    }

    /**
     * A zuzalekLimit nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public void setZuzalekLimit(int zuzalekLimit) {
        this.zuzalekLimit = zuzalekLimit;
    }
}