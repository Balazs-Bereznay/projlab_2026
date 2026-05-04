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

    public Bolt bolt;

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
     * Adatok kiírásához, naplózásához szükséges
     * @param id Az entitás azonosítója, amiről összegyűjti az adatot egy string-be
     * @param katalogus A nyilvántartó, amiben az objektumok vannak
     * @return Az entitás adatai egy stringben
     */
    public String info(String id, ObjektumKatalogus katalogus) {
        String nyId = katalogus.getId(this.nyilvantarto);
        String boltId = katalogus.getId(this.bolt);

        String flottaTartalom = String.join(", ", this.flotta.stream().map(katalogus::getId).toList());
        String flottaStr = "{ " + flottaTartalom + (flottaTartalom.isEmpty() ? "" : " ") + "}";

        return """
                %s:
                flotta: %s
                nyilvantarto: %s
                bolt: %s
                """.formatted(
                id,
                flottaStr,
                nyId,
                boltId
        );
    }

    /**
     * Feldolgozza a játékosra érkező, egyszerű prototípus-parancsokat.
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

    @Override
    public void parancsFeldolgozNyilvantartoval(String parancs, Nyilvantarto ny,  List<String> args) {
        this.nyilvantarto = ny;
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

    public Bolt getBolt() {
        return bolt;
    }
    public void setBolt(Bolt bolt) { this.bolt = bolt;}
}
