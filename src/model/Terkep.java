package model;

import java.util.*;

/** Számontartja az összes csomópontot és az összes utat,
 *  valamint útvonalat generál az autók számára.
 */
public class Terkep  implements ProtoEntitas{
    /// A játékban lévő összes út listája.
    private ArrayList<Ut> elLista;
    /// A játékban lévő összes csomópont listája.
    private ArrayList<Csomopont> csomopontLista;

    /**
     * Feldolgozza a sószóró fejre érkező, egyszerű prototípus-parancsokat.
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

                String elTartalom = (this.elLista == null || this.elLista.isEmpty())
                        ? ""
                        : String.join(", ", this.elLista.stream().map(Object::toString).toList());
                String elStr = "{ " + elTartalom + (elTartalom.isEmpty() ? "" : " ") + "}";

                String csomopontTartalom = (this.csomopontLista == null || this.csomopontLista.isEmpty())
                        ? ""
                        : String.join(", ", this.csomopontLista.stream().map(Object::toString).toList());
                String csomopontStr = "{ " + csomopontTartalom + (csomopontTartalom.isEmpty() ? "" : " ") + "}";

                String infoKimenet = """
                    %s:
                    elLista: %s
                    csomopontLista: %s
                    """.formatted(
                        currentId,
                        elStr,
                        csomopontStr
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
        masik.parancsFeldolgozTerkeppel(parancs, this, args);
    }

    @Override
    public void parancsFeldolgozUttal(String parancs, Ut ut, List<String> args) {
        if (parancs.equals("assign")) {
            this.elLista.add(ut);
            System.out.println("Út a térképhez adva.");
        }
    }

    @Override
    public void parancsFeldolgozCsomoponttal(String parancs, Csomopont cs, List<String> args) {
        if (parancs.equals("assign")) {
            this.csomopontLista.add(cs);
            System.out.println("Csomópont a térképhez adva.");
        }
    }



    ///Konstruktorok
    public Terkep(ArrayList<Ut> elLista, ArrayList<Csomopont> csomopontLista){
        this.elLista = (elLista != null) ? new ArrayList<>(elLista) : new ArrayList<>();
        this.csomopontLista = (csomopontLista != null) ? new ArrayList<>(csomopontLista) : new ArrayList<>();
    }



    public Terkep(){

        this(new ArrayList<>(), new ArrayList<>());
    }

    ///Getterek és setterek
    /**
     * Visszatér az elLista tagváltozó értékével, amely a térképen található összes út objektumot tartalmazza.
     * @return Az összes élet tartalmazó lista.
     */
    public ArrayList<Ut> getElLista(){ return elLista; }

    /**
     * Az elLista nevű tagváltozó értékét beállítja a paraméterül kapott lista értékére.
     * @param elLista Az új, éleketet tartalmazó lista.
     */
    public void setElLista(ArrayList<Ut> elLista){ this.elLista = elLista; };

    /**
     * Visszatér a csomopontLista tagváltozó értékével, amely a térképen található összes csomópont objektumot tartalmazza.
     * @return Az összes csomópontot tartalmazó lista.
     */
    public ArrayList<Csomopont> getCsomopontLista(){ return csomopontLista; }

    /**
     *  A csomopontLista nevű tagváltozó értékét beállítja a paraméterül kapott lista értékére.
     * @param csomopontLista Az új, csomópontokat tartalmazó lista.
     */
    public void setCsomopontLista(ArrayList<Csomopont> csomopontLista){ this.csomopontLista = csomopontLista; };

    ///További metódusok

    ///------------------------------------------------------------------------------------------------------------------------
    /**
     * Egy kezdő- és egy végpont alapján BFS segítségével megkeresi a legrövidebb utat az irányítatlan gráfban.
     * @param kezdopont A kiindulási csomópont.
     * @param vegpont Az a legvégső csomópont, ahová az autónak érkeznie kell.
     * @return Az útvonalat reprezentáló Ut objektumokból álló lista, vagy üres lista, ha nincs kapcsolat.
     */
    public ArrayList<Ut> utvonalTervezes(Csomopont kezdopont, Csomopont vegpont) {
        ArrayList<Ut> utvonal = new ArrayList<>();

        if (kezdopont == null || vegpont == null || kezdopont.equals(vegpont)) {
            return utvonal;
        }

        Queue<Csomopont> sor = new LinkedList<>();      // BFS-hez szükséges adatstruktúrák
        Set<Csomopont> latogatott = new HashSet<>();

        // Eltároljuk, hogy egy adott csomópontba MELYIK csomópontból és MELYIK úton érkeztünk
        // Kulcs: az aktuális csomópont, Érték: az Ut, amin keresztül ráléptünk
        Map<Csomopont, Ut> bejovoUt = new HashMap<>();

        // Kezdőpont inicializálása
        sor.add(kezdopont);
        latogatott.add(kezdopont);

        boolean celMegtalalva = false;

        // BFS bejárás
        while (!sor.isEmpty()) {
            Csomopont aktualis = sor.poll();

            // Ha elértük a célpontot, befejezzük a keresést
            if (aktualis.equals(vegpont)) {
                celMegtalalva = true;
                break;
            }

            // Végigmegyünk az aktuális csomópontból kiinduló összes úton
            for (Ut ut : aktualis.getUtLista()) {

                // Mivel az út irányítatlan, meg kell vizsgálnunk, melyik a "másik" vége
                Csomopont szomszed;
                if (ut.getVegpont1().equals(aktualis)) {
                    szomszed = ut.getVegpont2();
                } else {
                    szomszed = ut.getVegpont1();
                }

                // Ha még nem jártunk ebben a szomszédos csomópontban
                if (!latogatott.contains(szomszed)) {
                    latogatott.add(szomszed);       // Megjelöljük látogatottként
                    bejovoUt.put(szomszed, ut);     // Feljegyezzük, hogyan jutottunk ide
                    sor.add(szomszed);              // Berakjuk a sorba a további feldolgozáshoz
                }
            }
        }

        // Útvonal visszafejtése a végponttól a kezdőpontig
        if (celMegtalalva) {
            Csomopont lepes = vegpont;

            while (!lepes.equals(kezdopont)) {
                Ut hasznaltUt = bejovoUt.get(lepes);
                utvonal.add(hasznaltUt);

                // Visszalépés a megelőző csomópontra
                if (hasznaltUt.getVegpont1().equals(lepes)) {
                    lepes = hasznaltUt.getVegpont2();
                } else {
                    lepes = hasznaltUt.getVegpont1();
                }
            }

            // Mivel a végponttól haladtunk a kezdőpont felé, a listát meg kell fordítani
            Collections.reverse(utvonal);

        } else {
            System.out.println("Nem található érvényes útvonal a célállomásra.");
        }

        return utvonal;
    }

    /**
     * Hozzáfűz egy Ut típusú objektumot az elLista végére.
     * Ellenőrzi, hogy a paraméterként kapott út objektum létezik-e, és nem szerepel-e már a nyilvántartásban.
     * @param ut Az az út, ami bekerül az elLista listába.
     */
    public void addUt(Ut ut) {
        if (ut != null && !elLista.contains(ut)) {
            elLista.add(ut);
        }
    }

    /**
     * Hozzáfűz egy Csomopont típusú objektumot a csomopontLista végére.
     * Ellenőrzi, hogy a paraméterként kapott csomópont objektum létezik-e, és nem szerepel-e már a nyilvántartásban.
     * @param csomopont Az a csomópont, ami bekerül az csomopontLista listába.
     */
    public void addCsomopont(Csomopont csomopont) {
        if (csomopont != null && !csomopontLista.contains(csomopont)) {
            csomopontLista.add(csomopont);
        }
    }
}
