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
        Sopro sopro = new Sopro();
        this.fej = sopro;
    }

    /**
     * Adatok kiírásához, naplózásához szükséges
     * @param id Az entitás azonosítója
     * @param katalogus Az objektumkatalógus a hivatkozások feloldásához
     * @return Az entitás adatai stringként
     */
    public String info(String id, ObjektumKatalogus katalogus) {
        String utegysegId = katalogus.getId(this.getUtegyseg());
        String nyId = katalogus.getId(this.getNyilvantarto());
        String fejId = katalogus.getId(this.getFej());
        if(fejId.equals("null")) fejId = fej.getClass().getSimpleName();

        // Útvonal ID-k listázása (Jarmu-től örökölt kijeloltUtvonal használatával)
        String utvonalTartalom = String.join(", ", this.kijeloltUtvonal.stream().map(katalogus::getId).toList());
        String utvonalStr = "{ " + utvonalTartalom + (utvonalTartalom.isEmpty() ? "" : " ") + "}";

        return """
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
                zuzalekMennyiseg: %d
                zuzalekLimit: %d
                bevetel: %d
                """.formatted(
                id,
                this.getSebesseg(),
                utegysegId,
                this.getTapadas(),
                this.elakadt,
                this.baleset,
                this.megcsuszott,
                nyId,
                utvonalStr,
                fejId,
                this.zuzalekMennyiseg,
                this.zuzalekLimit,
                Hokotro.getBevetel()
        );
    }

    @Override
    public void parancsFeldolgozNyilvantartoval(String parancs, Nyilvantarto ny1, List<String> args) {
        this.nyilvantarto = ny1;
    }

    @Override
    public void parancsFeldolgoz(String parancs, List<String> args) {
        if (parancs == null || args == null) {
            return;
        }

        switch (parancs) {
            case "move":
                if (args.isEmpty()) return;
                String irany = args.get(0);
                if ("-f".equalsIgnoreCase(irany) || "forward".equalsIgnoreCase(irany)) {
                    lep();
                } else {
                    savValtas(irany);
                }
                break;
            case "clean":
                takarit();
                break;
            case "set":
                super.parancsFeldolgoz(parancs, args);
                if (args.size() < 2) {
                    return;
                }

                String tulajdonsag = args.get(0).toLowerCase();
                String ertek = args.get(1);

                try {
                    switch (tulajdonsag) {
                        case "bevetel":
                            setBevetel(Integer.parseInt(ertek));
                            break;
                        case "zuzalekmennyiseg":
                            this.zuzalekMennyiseg = Integer.parseInt(ertek);
                            break;
                        case "zuzaleklimit":
                            this.zuzalekLimit = Integer.parseInt(ertek);
                            break;
                        default:
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Hiba: Ervenytelen szamformatum!");
                }
                break;
            default:
                break;
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
                jatekos.getBolt().hokotroVasarol(jatekos, this);
                break;
            case "soszoro":
                jatekos.getBolt().soszoroVasarol(this);
                break;
            case "jegtoro":
                jatekos.getBolt().jegtoroVasarol(this);
                break;
            case "zuzalekszoro":
                jatekos.getBolt().zuzalekszoroVasarol(this);
                break;
            case "sarkany":
                jatekos.getBolt().sarkanyVasarol(this);
                break;
            case "sopro":
                jatekos.getBolt().soproVasarol(this);
                break;
            case "hanyo":
                jatekos.getBolt().hanyoVasarol(this);
                break;
            case "zuzalek":
                int mennyiseg = 1;
                try {
                    mennyiseg = args.size() >= 3 ? Integer.parseInt(args.get(2)) : 1;
                } catch (NumberFormatException e) {
                }

                jatekos.getBolt().zuzalekVasarol(this, mennyiseg);
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
            if(nyilvantarto != null)
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

    /**
     * Hozzáfűz egy Utegyseg típusú objektumot a tervezettUtvonal végére.
     * @param ue Az az útegység, ami bekerül a tervezettUtvonal listába.
     */
    public void addTervezettUtegyseg(Utegyseg ue) {
        if (ue != null && !tervezettUtvonal.contains(ue)) {
            tervezettUtvonal.add(ue);
        }
    }
    public void removeTervezettUtegyseg(Utegyseg ue) {
        if (tervezettUtvonal.contains(ue)) {
            tervezettUtvonal.remove(ue);
        }
    }
}
