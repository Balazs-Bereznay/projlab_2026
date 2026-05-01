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
            case "purchase":
                break;
            default:
                break;
        }
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
