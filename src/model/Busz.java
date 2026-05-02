package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A jatekos altal uzemeltetett buszt megvalosito jarmuosztaly.
 *
 * <p>A Busz felel a busz utvonalahoz tartozo vegallomasok es megallok
 * nyilvantartasaert, az erintett megallok feljegyzeseert, valamint a
 * teljesitett utvonal alapjan jaro bevetel kiszamitasert. A Jarmu
 * leszarmazottjakent hasznalja a kozos mozgasi es allapotkezelesi
 * muveleteket.</p>
 */
public class Busz extends Jarmu implements Iranyithato, ProtoEntitas {

    /**
     * A busz egyik vegallomasa.
     */
    private Csomopont vegallomas1;

    /**
     * A busz masik vegallomasa.
     */
    private Csomopont vegallomas2;

    /**
     * A busz utvonalahoz tartozo koztes megallok listaja.
     */
    private List<Csomopont> megallokLista = new ArrayList<>();

    /**
     * A teljesites soran mar erintett megallok listaja.
     */
    private List<Csomopont> erintettLista = new ArrayList<>();

    /**
     * Egy erintett megallo utan jaro beveteli ertek.
     */
    private int bevetel;

    /**
     * Az Iranyithato interfeszen keresztul valasztott utegyseg-alapu utvonal.
     */
    private List<Utegyseg> tervezettUtvonal = new ArrayList<>();

    @Override
    public void parancsFeldolgozJatekossal(String parancs, Jatekos jatekos, List<String> args) {
        if (args.isEmpty()) return;

        String item = args.get(0).toLowerCase(); // Az item neve a lista eleje

        switch (item) {
            case "sebessegfejlesztes":
                Jatekos.getBolt().sebessegFejlesztes(this, 10);
                break;
            case "hozamfejlesztes":
                Jatekos.getBolt().hozamFejlesztes(this, 10);
                break;
            case "tapadasfejlesztes":
                Jatekos.getBolt().tapadasFejlesztes(this, 10);
                break;
            default:
                break;
        }
    }

    /**
     * Rogziti, hogy a busz erintett egy ervenyes megallot.
     *
     * <p>Ervenyes megallonak szamit a ket vegallomas, valamint a megallokLista
     * barmely eleme. Egy megallo egy teljesitesen belul csak egyszer kerulhet be
     * az erintettLista listaba.</p>
     *
     * @param megallo az erintett csomopont
     */
    public void megalloErintese(Csomopont megallo) {
        if (megallo == null) {
            return;
        }

        boolean ervenyesMegallo = megallo == vegallomas1
                || megallo == vegallomas2
                || megallokLista.contains(megallo);

        if (ervenyesMegallo && !erintettLista.contains(megallo)) {
            erintettLista.add(megallo);
        }
    }

    /**
     * Kiszamitja a busz altal teljesitett utvonal alapjan jaro jutalmat.
     *
     * <p>A szamitas alapja az erintettLista merete es a bevetel attributum
     * erteke. A metodus nem modositja a kozos penzkeszletet, csak visszaadja a
     * buszhoz tartozo jutalomerteket.</p>
     *
     * @return a kiszamitott jutalom
     */
    public int jutalomKiszamitasa() {
        return erintettLista.size() * bevetel;
    }

    /**
     * Beallitja, hogy az Iranyithato interfesz altal kapott utvonal a
     * tervezettUtvonal attributumba keruljon.
     *
     * @param utegysegLista a busz altal kovetendo utegysegek listaja
     */
    @Override
    public void setKijeloltUtegysegek(List<Utegyseg> utegysegLista) {
        if (utegysegLista == null) {
            this.tervezettUtvonal = new ArrayList<>();
        } else {
            this.tervezettUtvonal = new ArrayList<>(utegysegLista);
        }
    }

    /**
     * Feldolgozza a buszra erkezo, egyszeru prototipus-parancsokat.
     *
     * @param parancs a feldolgozando parancs neve
     * @param args a parancs tovabbi parameterei
     */
    @Override
    public void parancsFeldolgoz(String parancs, List<String> args) {
        if (parancs == null || args == null) {
            return;
        }

        switch (parancs) {
            case "move":
                if (args.isEmpty()) {
                    return;
                }

                String irany = args.get(0);
                if ("-f".equalsIgnoreCase(irany) || "forward".equalsIgnoreCase(irany)) {
                    lep();
                } else {
                    savValtas(irany);
                }
                break;
            case "set":
                if (args.size() < 2) {
                    return;
                }

                String property = args.get(0);
                String value = args.get(1);

                switch (property) {
                    case "sebesseg":
                        try {
                            setSebesseg(Integer.parseInt(value));
                        } catch (NumberFormatException ignored) {
                            return;
                        }
                        break;
                    case "tapadas":
                        try {
                            setTapadas(Integer.parseInt(value));
                        } catch (NumberFormatException ignored) {
                            return;
                        }
                        break;
                    case "bevetel":
                        try {
                            setBevetel(Integer.parseInt(value));
                        } catch (NumberFormatException ignored) {
                            return;
                        }
                        break;
                    case "elakadt":
                        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                            this.elakadt = Boolean.parseBoolean(value);
                        }
                        break;
                    case "baleset":
                        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                            this.baleset = Boolean.parseBoolean(value);
                        }
                        break;
                    case "megcsuszott":
                        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                            this.megcsuszott = Boolean.parseBoolean(value);
                        }
                        break;
                    case "kijeloltUtvonal":
                        if ("empty".equalsIgnoreCase(value) || "{}".equals(value)) {
                            this.kijeloltUtvonal = new ArrayList<>();
                        }
                        break;
                    case "megallokLista":
                        if ("empty".equalsIgnoreCase(value) || "{}".equals(value)) {
                            this.megallokLista = new ArrayList<>();
                        }
                        break;
                    case "erintettLista":
                        if ("empty".equalsIgnoreCase(value) || "{}".equals(value)) {
                            this.erintettLista = new ArrayList<>();
                        }
                        break;
                    case "tervezettUtvonal":
                        if ("empty".equalsIgnoreCase(value) || "{}".equals(value)) {
                            this.tervezettUtvonal = new ArrayList<>();
                        }
                        break;
                    case "vegallomas1":
                    case "vegallomas2":
                    case "utegyseg":
                    case "nyilvantarto":
                        break;
                    default:
                        break;
                }
                break;
            case "clean":
            case "add_condition":
            case "list_shop":
            case "purchase":
                break;
            case "info":
                String currentId = args.get(0);

                String utegysegStr = (this.getUtegyseg() != null) ? this.getUtegyseg().toString() : "null";
                String nyStr = (this.getNyilvantarto() != null) ? this.getNyilvantarto().toString() : "null";
                String v1Str = (this.vegallomas1 != null) ? this.vegallomas1.toString() : "null";
                String v2Str = (this.vegallomas2 != null) ? this.vegallomas2.toString() : "null";

                // kijeloltUtvonal formázása
                String utvonalTartalom = (this.kijeloltUtvonal == null || this.kijeloltUtvonal.isEmpty())
                        ? ""
                        : String.join(", ", this.kijeloltUtvonal.stream().map(Object::toString).toList());
                String utvonalStr = "{ " + utvonalTartalom + (utvonalTartalom.isEmpty() ? "" : " ") + "}";

                // megallokLista formázása
                String megallokTartalom = (this.megallokLista == null || this.megallokLista.isEmpty())
                        ? ""
                        : String.join(", ", this.megallokLista.stream().map(Object::toString).toList());
                String megallokStr = "{ " + megallokTartalom + (megallokTartalom.isEmpty() ? "" : " ") + "}";

                // erintettLista formázása
                String erintettTartalom = (this.erintettLista == null || this.erintettLista.isEmpty())
                        ? ""
                        : String.join(", ", this.erintettLista.stream().map(Object::toString).toList());
                String erintettStr = "{ " + erintettTartalom + (erintettTartalom.isEmpty() ? "" : " ") + "}";

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
                    vegallomas1: %s
                    vegallomas2: %s
                    megallokLista: %s
                    erintettLista: %s
                    bevetel: %d
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
                        v1Str,
                        v2Str,
                        megallokStr,
                        erintettStr,
                        this.bevetel
                );

                System.out.print(infoKimenet);
                System.out.println("Info displayed");
                break;
            default:
                break;
        }
    }

    /**
     * Feldolgozza a buszra erkezo, masik prototipus-entitast is hasznalo
     * parancsokat.
     *
     * <p>A cel objektum tipusat nem a Busz vizsgalja. A double dispatch
     * mukodes szerint a cel objektum kapja meg a buszt, es a sajat
     * {@code parancsFeldolgozBusszal} metodusaban donti el, mit jelent vele az
     * adott parancs.</p>
     *
     * @param parancs a feldolgozando parancs neve
     * @param cel a parancs masik erintett objektuma
     * @param args a parancs tovabbi parameterei
     */
    @Override
    public void parancsFeldolgoz(String parancs, ProtoEntitas cel, List<String> args) {
        if (parancs == null || cel == null) {
            return;
        }

        cel.parancsFeldolgozBusszal(parancs, this, args);
    }

    /**
     * Visszaadja az elso vegallomast.
     *
     * @return az elso vegallomas
     */
    public Csomopont getVegallomas1() {
        return vegallomas1;
    }

    /**
     * Beallitja az elso vegallomast.
     *
     * @param vegallomas1 az uj elso vegallomas
     */
    public void setVegallomas1(Csomopont vegallomas1) {
        this.vegallomas1 = vegallomas1;
    }

    /**
     * Visszaadja a masodik vegallomast.
     *
     * @return a masodik vegallomas
     */
    public Csomopont getVegallomas2() {
        return vegallomas2;
    }

    /**
     * Beallitja a masodik vegallomast.
     *
     * @param vegallomas2 az uj masodik vegallomas
     */
    public void setVegallomas2(Csomopont vegallomas2) {
        this.vegallomas2 = vegallomas2;
    }

    /**
     * Visszaadja a busz megalloinak listajat.
     *
     * @return a megallok listaja
     */
    public List<Csomopont> getMegallokLista() {
        return megallokLista;
    }

    /**
     * Beallitja a busz megalloinak listajat.
     *
     * @param megallokLista az uj megallolista
     */
    public void setMegallokLista(List<Csomopont> megallokLista) {
        if (megallokLista == null) {
            this.megallokLista = new ArrayList<>();
        } else {
            this.megallokLista = new ArrayList<>(megallokLista);
        }
    }

    /**
     * Visszaadja az erintett megallok listajat.
     *
     * @return az erintett megallok listaja
     */
    public List<Csomopont> getErintettLista() {
        return erintettLista;
    }

    /**
     * Beallitja az erintett megallok listajat.
     *
     * @param erintettLista az uj erintett megallolista
     */
    public void setErintettLista(List<Csomopont> erintettLista) {
        if (erintettLista == null) {
            this.erintettLista = new ArrayList<>();
        } else {
            this.erintettLista = new ArrayList<>(erintettLista);
        }
    }

    /**
     * Visszaadja a beveteli erteket.
     *
     * @return a beveteli ertek
     */
    public int getBevetel() {
        return bevetel;
    }

    /**
     * Beallitja a beveteli erteket.
     *
     * @param bevetel az uj beveteli ertek
     */
    public void setBevetel(int bevetel) {
        this.bevetel = bevetel;
    }
}
