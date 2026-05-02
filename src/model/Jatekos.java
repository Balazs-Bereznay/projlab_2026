package model;

import model.Iranyithato;
import model.Nyilvantarto;

import java.util.ArrayList;
import java.util.List;

/**
 * A felhasználót jelképező osztály, aki a hókotrók és buszok flottáját kezeli,
 * dönthet a jármű útvonalának kijelölésében, illetve, hogy a boltban milyen
 * tranzakciókat hajt végre.
 */
public class Jatekos implements ProtoEntitas {

    private static final Bolt bolt = new Bolt();

    /**
     * A játékoshoz tartozó irányítható járművek (hókotrók, buszok) listája.
     */
    private List<Iranyithato> flotta;

    /**
     * Kapcsolat a nyilvántartó rendszerrel, amely a globális adatokat kezeli.
     */
    private Nyilvantarto nyilvantarto;

    public Jatekos() {
        this.flotta = new ArrayList<>();
    }

    public Jatekos(Nyilvantarto nyilvantarto) {
        this.flotta = new ArrayList<>();
        this.nyilvantarto = nyilvantarto;
    }

    /**
     * Feldolgozza a játékosra érkező, egyszerű prototípus-parancsokat.
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

                String flottaTartalom = (this.flotta == null || this.flotta.isEmpty())
                        ? ""
                        : String.join(", ", this.flotta.stream().map(Object::toString).toList());
                String flottaStr = "{ " + flottaTartalom + (flottaTartalom.isEmpty() ? "" : " ") + "}";

                String infoKimenet = """
                    %s:
                    flotta: %s
                    nyilvantarto: %s
                    """.formatted(
                        currentId,
                        flottaStr,
                        nyStr
                );

                System.out.print(infoKimenet);
                System.out.println("Info displayed");
                break;

            case "purchase":
                String item = args.get(0).toLowerCase();
                int mennyiseg = args.size() >= 2 ? Integer.parseInt(args.get(1)) : 1;

                switch (item) {
                    case "so":
                        bolt.soVasarol(mennyiseg);
                        //System.out.println("Sikeres vasarlas: So hozzaadva a keszlethez.");
                        break;

                    case "biokerozin":
                        bolt.biokerozinVasarol(mennyiseg);
                        //System.out.println("Sikeres vasarlas: Biokerozin feltoltve.");
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void parancsFeldolgoz(String parancs, ProtoEntitas cel, List<String> args) {
        cel.parancsFeldolgozJatekossal(parancs, this, args);
    }


    public List<Iranyithato> getFlotta() {
        System.out.println("Lekérdezték a játékos flottáját.");
        return flotta;
    }

    public void setFlotta(List<Iranyithato> flotta) {
        System.out.println("Beállították a játékos flottáját.");
        this.flotta = flotta;
    }

    public Nyilvantarto getNyilvantarto() {
        System.out.println("Lekérdezték a játékos nyilvántartóját.");
        return nyilvantarto;
    }

    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        System.out.println("Beállították a játékos nyilvántartóját.");
        this.nyilvantarto = nyilvantarto;
    }

    public static Bolt getBolt() {
        return bolt;
    }
}