package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A rendszer altal iranyitott kozlekedo jarmuvet megvalosito osztaly.
 *
 * <p>Az Auto felel az automatikusan mozgo auto kezdopontjanak, celpontjanak
 * es uton toltott idejenek nyilvantartasaert. A Jarmu leszarmazottjakent
 * hasznalja a kozos mozgasi es allapotkezelesi muveleteket, utvonalat pedig
 * a rendszeriranyitott mukodeshez tartozo utvonalkereses alapjan kap.</p>
 */
public class Auto extends Jarmu implements RendszerIranyitott, ProtoEntitas {

    /**
     * Az auto kiindulasi csomopontja.
     */
    private Csomopont kezdopont;

    /**
     * Az auto celkent hasznalt csomopontja.
     */
    private Csomopont celpont;

    /**
     * Az auto altal uton toltott ido.
     */
    private int utonToltottIdo;

    /**
     * Meghatarozza a kezdopont es celpont alapjan az NPC auto legrovidebb utvonalat.
     *
     * <p>Ha a terkep vagy valamelyik vegpont nem all rendelkezesre, akkor ures
     * utvonalat allit be es ad vissza. Egyebkent a terkep utvonaltervezo
     * metodusat hasznalja, az eredmenyt pedig az orokolt kijeloltUtvonal
     * attributumban is eltarolja.</p>
     *
     * @param terkep a palyat es az uthalozatot tartalmazo terkep
     * @return az auto altal kovetendo utak listaja
     */
    public List<Ut> utvonalKereses(Terkep terkep) {
        if (terkep == null || kezdopont == null || celpont == null) {
            this.kijeloltUtvonal = new ArrayList<>();
            return this.kijeloltUtvonal;
        }

        this.kijeloltUtvonal = new ArrayList<>(terkep.utvonalTervezes(kezdopont, celpont));
        return this.kijeloltUtvonal;
    }

    /**
     * A RendszerIranyitott interfesz utvonalkeresesi muvelete.
     *
     * <p>Az Auto reszletes metodustervben szereplo utvonalKereses metodusra
     * tovabbit, mert az adja vissza es tarolja el az auto utvonalat.</p>
     *
     * @param terkep a palyat es az uthalozatot tartalmazo terkep
     */
    @Override
    public void utvonalKeres(Terkep terkep) {
        utvonalKereses(terkep);
    }

    /**
     * Feldolgozza az autora erkezo prototipus-parancsokat.
     *
     * <p>A prototipus aktualis parancselosztoja a ProtoEntitas interfeszen
     * keresztul adja at az entitasokra vonatkozo muveleteket. Az Auto a move
     * parancsnal az orokolt mozgasi muveleteket hasznalja. Set parancsnal az
     * Auto sajat es a Jarmu osztalybol orokolt, szovegbol biztonsagosan
     * atalakithato attributumait kezeli. A referencia tipusu attributumokat ez
     * a ket parameteres hivas nem allitja.</p>
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
                    case "utonToltottIdo":
                        try {
                            setUtonToltottIdo(Integer.parseInt(value));
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
                        if ("empty".equalsIgnoreCase(value)
                                || "{}".equals(value)) {
                            this.kijeloltUtvonal = new ArrayList<>();
                        }
                        break;
                    case "kezdopont":
                    case "celpont":
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
     * Visszaadja az auto kezdopontjat.
     *
     * @return az auto kiindulasi csomopontja
     */
    public Csomopont getKezdopont() {
        return kezdopont;
    }

    /**
     * Beallitja az auto kezdopontjat.
     *
     * @param kezdopont az auto uj kiindulasi csomopontja
     */
    public void setKezdopont(Csomopont kezdopont) {
        this.kezdopont = kezdopont;
    }

    /**
     * Visszaadja az auto celpontjat.
     *
     * @return az auto celkent hasznalt csomopontja
     */
    public Csomopont getCelpont() {
        return celpont;
    }

    /**
     * Beallitja az auto celpontjat.
     *
     * @param celpont az auto uj celpontja
     */
    public void setCelpont(Csomopont celpont) {
        this.celpont = celpont;
    }

    /**
     * Visszaadja az auto uton toltott idejet.
     *
     * @return az uton toltott ido
     */
    public int getUtonToltottIdo() {
        return utonToltottIdo;
    }

    /**
     * Beallitja az auto uton toltott idejet.
     *
     * @param utonToltottIdo az uj uton toltott ido
     */
    public void setUtonToltottIdo(int utonToltottIdo) {
        this.utonToltottIdo = utonToltottIdo;
    }
}
