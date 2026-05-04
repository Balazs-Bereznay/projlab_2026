package model;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Prototipus {
    // A központi nyilvántartó, ahol minden objektum lakik
    private final ObjektumKatalogus katalogus = new ObjektumKatalogus();

    // A FIFO sor, ami a feldolgozandó parancsokat tárolja
    private final Queue<String> parancsSor = new ArrayDeque<>();

    /**
     * A test mappából beolvashatunk egy teszthez tartozó fájlt.
     * A metódus feldolgozza a fájlban felsorolt parancsokat és továbbítja azokat a parancsSor FIFO-ba
     * @param fajlnev
     */
    public void beolvasFajlbol(String fajlnev) {
        try (BufferedReader br = new BufferedReader(new FileReader("test/input" + File.separator + fajlnev))) {
            String sor;
            while ((sor = br.readLine()) != null) {
                sor = sor.trim();
                if (sor.isEmpty()) {
                    continue;
                }
                // Beletesszük a FIFO-ba
                parancsSor.add(sor);
            }
            System.out.println("Sikeres beolvasás: " + fajlnev);
        } catch (IOException e) {
            System.err.println("Hiba a fájl beolvasásakor: " + e.getMessage());
        }
    }

    /**
     * Az out mappába kimenti a napló aktuális tartalmát.
     * A naplóba az info <id> paranccsal lehet kiiratni egy objektum aktuális állapotát.
     * @param fajlnev
     */
    public void allapotMentese(String fajlnev) {
        File forras = new File("temp.txt");
        if (!forras.exists()) {
            System.out.println("Nincs mit menteni (a temp.txt üres vagy nem létezik).");
            return;
        }

        // Cél útvonal összeállítása (out mappa + fájlnév)
        File celMappa = new File("test/output");
        if (!celMappa.exists()) {
            celMappa.mkdirs(); // Létrehozzuk a test/output mappát, ha még nincs
        }

        if (!fajlnev.endsWith(".txt")) {
            fajlnev += ".txt";
        }


        Path celUtvonal = Paths.get("test/output", fajlnev);

        try {
            // Átmásoljuk a temp.txt-t a célhelyre, felülírva ha már létezik
            Files.copy(forras.toPath(), celUtvonal, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Állapot sikeresen mentve: " + celUtvonal.toString());
        } catch (IOException e) {
            System.err.println("Hiba a mentés során: " + e.getMessage());
        }
    }

    /**
     * Kiírja a temp.txt-be a paraméterként kapott string-et
     * @param uzenet
     */
    public void naplozas(String uzenet) {
        // A 'true' paraméter a FileWriter-nél jelenti az 'append' módot
        try (FileWriter fw = new FileWriter("temp.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(uzenet);

        } catch (IOException e) {
            System.err.println("Nem sikerült a temp.txt-be írni: " + e.getMessage());
        }
    }

    /**
     * Törli a korábban létrehozott objektumokat és az ideiglenes naplófájlt is
     */
    public void tesztKornyezetAlaphelyzet() {
        // 1. Memória ürítése (ObjektumKatalógus)
        katalogus.alaphelyzet();

        // 2. Ideiglenes naplófájl törlése (temp.txt)
        File tempFile = new File("temp.txt");
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    /**
     * Ha null-t kap paraméternek, akkor kilistázza az összes objektumot.
     * Ha paraméterként egy típus nevét kapja, akkor csak az adott típus összes objektumát listázza ki.
     * @param szavak
     */
    public void listazasKezelo(String[] szavak) {
        List<String> idLista;

        // Ellenőrizzük, hogy van-e megadva típus (pl. "list Auto" -> szavak[1] = "Auto")
        // Ha a szavak tömb null, vagy nincs benne paraméter, null-t küldünk a katalógusnak
        if (szavak == null || szavak.length < 2) {
            idLista = katalogus.osszesIdLeker(null);
            System.out.println("Osszes objektum listaja:");
        } else {
            String tipus = szavak[1];
            idLista = katalogus.osszesIdLeker(tipus);
            System.out.println(tipus + " tipusu objektumok listaja:");
        }

        // Eredmény megjelenítése
        if (idLista.isEmpty()) {
            System.out.println(" ");
        } else {
            for (String id : idLista) {
                System.out.println("- " + id);
            }
        }
    }

    public void entitasLetrehoz(String osztaly, String id)
    {
        ProtoEntitas ujEntitas = null;
        osztaly = osztaly.substring(0, 1).toUpperCase() + osztaly.substring(1).toLowerCase();

        switch (osztaly)
        {
            case "Utegyseg" : ujEntitas = new Utegyseg();   break;
            case "Csomopont" : ujEntitas = new Csomopont(); break;
            case "Ut" : ujEntitas = new Ut(); break;
            case "Sav" : ujEntitas = new Sav(); break;

            case "Hokotro" : ujEntitas = new Hokotro(); break;
            case "Auto" : ujEntitas = new Auto(); break;
            case "Busz" : ujEntitas = new Busz(); break;

            case "Zuzalekszoro" : ujEntitas = new Zuzalekszoro(); break;
            case "Sopro" : ujEntitas = new Sopro(); break;
            case "Jegtoro" : ujEntitas = new Jegtoro(); break;
            case "Sarkany" : ujEntitas = new Sarkany(); break;
            case "Soszoro" : ujEntitas = new Soszoro(); break;
            case "Hanyo" : ujEntitas = new Hanyo(); break;

            case "Nyilvantarto" : ujEntitas = new Nyilvantarto(); break;
            case "Jatekos" : ujEntitas = new Jatekos(); break;
            case "Bolt" : ujEntitas = new Bolt(); break;

            default:
                System.out.println("Ismeretlen entitas tipus:" + osztaly);
                return;

        }
        katalogus.hozzaad(id, ujEntitas);
    }

    public void entitasTorol(String id){
        katalogus.torol(id);
    }

    private List<String> parametereketVag(String[] darabok, int honnan) {
        if (darabok.length <= honnan) {
            return new ArrayList<>();
        }

        // Létrehozunk egy tömbböt, csak az adott index utáni elemekkel
        String[] masolat = Arrays.copyOfRange(darabok, honnan, darabok.length);

        // A ProtoEntitas metódusai List típust várnak
        return Arrays.asList(masolat);
    }

    public void parancsSorFeldolgoz() {
        while (!parancsSor.isEmpty()) {
            String sor = parancsSor.poll();
            String[] szavak = sor.split(" ");
            String cmd = szavak[0];

            switch (cmd) {
                // --- Rendszerszintű parancsok (Prototípus osztály végzi) ---
                case "load":
                    if (szavak.length < 2) { System.out.println("Használat: load <fajlnev>"); break; }
                    beolvasFajlbol(szavak[1]);
                    break;
                case "save":
                    String nev = szavak.length > 1 ? szavak[1] : "nevtelen";
                        allapotMentese(nev);
                    break;
                case "reset":
                    tesztKornyezetAlaphelyzet();
                    break;
                case "run_test":
                    if (szavak.length >= 2) {
                        tesztFuttatas(szavak[1]);
                    } else {
                        tesztFuttatas(null);
                    }
                break;
                case "create":
                    if (szavak.length < 3) { System.out.println("Használat: create <osztaly> <id>"); break; }
                    entitasLetrehoz(szavak[1], szavak[2]);
                    break;
                 case "delete":
                     if (szavak.length < 2) { System.out.println("Használat: delete <id>"); break; }
                     entitasTorol(szavak[1]);
                     break;
                case "list":
                    listazasKezelo(szavak);
                    break;
                case "list_tests":
                    teszteketListaz();
                    break;
                case "tick":
                    try {
                        int ido = (szavak.length > 1) ? Integer.parseInt(szavak[1]) : 1;
                        szimulacioTick(ido);
                    } catch (NumberFormatException e) {
                        System.out.println("Használat: tick [ido]");
                    }
                    break;
                case "help":
                    helpKiiras();
                    break;
                case "quit":
                    System.out.println("Kilépés...");
                    System.exit(0);
                    break;


                // --- Egyedi entitás parancsok (2 paraméteres overload) ---
                // ! Minden az ProtoEntitas két paraméteres parancsFeldolgoz-t hívó esetnek ugyan az lenne a törzse,
                // ! ezért a set, move stb. esetek végén nincs break és így közülük bármelyik következik be
                // ! a purchase-nál megírt törzs fog lefutni. (Nem kell azokhoz semmit írni.)
                case "move":
                case "clean":
                case "add_condition":
                case "list_shop": {
                    if (szavak.length < 2) { System.out.println("Használat: " + cmd + " <id> [parameterek]"); break; }
                    ProtoEntitas celpont = katalogus.keres(szavak[1]);
                    if (celpont != null) {
                        celpont.parancsFeldolgoz(cmd, parametereketVag(szavak, 2));
                    }
                    break;
                }
                case "info":
                    if (szavak.length < 2) { System.out.println("Használat: info <id>"); break; }
                    ProtoEntitas celpontt = katalogus.keres(szavak[1]);
                    if (celpontt != null) {
                        String str = celpontt.info(szavak[1], katalogus);
                        naplozas(str);
                        System.out.println(str);
                    }
                    break;

                case "set": {
                    if (szavak.length < 4) { System.out.println("Használat: set <id> <attributum> <ertek>"); break; }

                    String gazdaId = szavak[1];
                    String itemTipus = szavak[2];

                    ProtoEntitas gazda = katalogus.keres(gazdaId);
                    if (gazda == null) {
                        break;
                    }

                    // só vagy biokerozin (ilyenkor nem kell megadni másik referenciát)
                    if (itemTipus.equalsIgnoreCase("kovetkezoutegyseg")
                            || itemTipus.equalsIgnoreCase("jobbutegyseg")
                            || itemTipus.equalsIgnoreCase("balutegyseg")) {

                        ProtoEntitas cel = katalogus.keres(szavak[3]);

                        if (cel == null) {
                            break;
                        }

                        // Meghívjuk a háromparaméteres változatot
                        gazda.parancsFeldolgoz(cmd, cel, parametereketVag(szavak, 2));

                    } else {
                        // Meghívjuk a kétparaméteres változatot
                        gazda.parancsFeldolgoz(cmd, parametereketVag(szavak, 2));
                        }
                    break;
                }

                case "purchase": {
                    // eset: purchase <id> <valami> [mennyiseg]
                    // legalább 3 hosszúnak kell lennie a tömbnek
                    if (szavak.length < 3) { System.out.println("Használat: purchase <id> <tipus> [mennyiseg|celId]"); break; }

                    String gazdaId = szavak[1];
                    String itemTipus = szavak[2];

                    ProtoEntitas gazda = katalogus.keres(gazdaId);
                    if (gazda == null) {
                        break;
                    }

                    // só vagy biokerozin (ilyenkor nem kell megadni másik referenciát)
                    if (itemTipus.equalsIgnoreCase("so") || itemTipus.equalsIgnoreCase("biokerozin")) {
                        // Meghívjuk a kétparaméteres változatot
                        gazda.parancsFeldolgoz(cmd, parametereketVag(szavak, 2));
                    }
                    else {
                        // purchase <id> <valami> <masik id> [id]
                        if (szavak.length < 4) { System.out.println("Használat: purchase <id> <tipus> <celId>"); break; }

                        ProtoEntitas cel = katalogus.keres(szavak[3]);

                        if (cel == null) {
                            break;
                        }

                        // Meghívjuk a háromparaméteres változatot
                        gazda.parancsFeldolgoz(cmd, cel, parametereketVag(szavak, 2));
                    }
                    break;
                }
                case "add":
                    if (szavak.length < 4) { System.out.println("Használat: add <id> <penz|so|biokerozin|...> <ertek|celId>"); break; }

                    String addGazdaId = szavak[1];
                    String item = szavak[2];

                    ProtoEntitas addGazda = katalogus.keres(addGazdaId);
                    if (addGazda == null) {
                        break;
                    }

                    if(item.equalsIgnoreCase("penz")){
                        // Kétparaméteres változat
                        addGazda.parancsFeldolgoz(cmd, parametereketVag(szavak, 2));
                    }else{
                        ProtoEntitas hozzaadni = katalogus.keres(szavak[3]);

                        if (hozzaadni == null) {
                            break;
                        }

                        // Háromparaméteres változatot
                        addGazda.parancsFeldolgoz(cmd, hozzaadni, parametereketVag(szavak, 2));
                    }
                    break;

                // --- Kapcsolati parancsok (3 paraméteres overload) ---
                case "assign":
                    if (szavak.length < 3) { System.out.println("Használat: assign <gazdaId> <celId>"); break; }
                    ProtoEntitas gazdi = katalogus.keres(szavak[1]);
                    ProtoEntitas destionation = katalogus.keres(szavak[2]);
                    if (gazdi != null && destionation != null) {
                        gazdi.parancsFeldolgoz(cmd, destionation, parametereketVag(szavak, 3));
                    }
                    break;
                    
                case "remove":
                    if (szavak.length < 3) { System.out.println("Használat: remove <gazdaId> <celId>"); break; }
                    ProtoEntitas gazda = katalogus.keres(szavak[1]);
                    ProtoEntitas cel = katalogus.keres(szavak[2]);
                    if (gazda != null && cel != null) {
                        gazda.parancsFeldolgoz(cmd, cel, parametereketVag(szavak, 3));
                    }
                    break;

                default:
                    System.out.println("Ismeretlen parancs: " + cmd);
            }
        }
    }

    public void futtat() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;

            String sor = scanner.nextLine().trim();
            if (sor.isEmpty()) continue;
            if (sor.equalsIgnoreCase("exit")) break;

            // Berakjuk a FIFO végére
            parancsSor.add(sor);

            // Meghívjuk a feldolgozót, ami addig megy, amíg a FIFO ki nem ürül
            parancsSorFeldolgoz();
        }
        scanner.close();
    }

    /**
     * kilistázza a test/input mappában található tesztekhez tartozó bemeneti fájlok neveit.
     */
    private void teszteketListaz() {
        File mappa = new File("test" + File.separator + "input");

        if (!mappa.exists() || !mappa.isDirectory()) {
            System.out.println("Hiba: A 'test/input' mappa nem letezik!");
            return;
        }

        File[] fajlok = mappa.listFiles(File::isFile);

        if (fajlok == null || fajlok.length == 0) {
            System.out.println("A 'test/input' mappa ures.");
            return;
        }

        Arrays.sort(fajlok, Comparator.comparing(File::getName));
        System.out.println("Elerheto tesztfajlok:");
        for (File fajl : fajlok) {
            System.out.println("- " + fajl.getName());
        }
    }

    /**
     * Futtatja a paramként megkapott tesztet, ha a teszt null, akkor futtatja az összes tesztet.
     * Teszt futtatása alatt azt kell érteni, hogy betölti az adott nevű tesztet, kimenti a naplófájlt és
     * alaphelyzetbe állítja a tesztelőkörnyezetet.
     * @param tesztNev
     */
    private void tesztFuttatas(String tesztNev) {
        if (tesztNev == null) {
            File mappa = new File("test/input");
            if (mappa.exists() && mappa.isDirectory()) {
                String[] fajlok = mappa.list();
                if (fajlok != null) {
                    for (String fajl : fajlok) {
                        // Csak a .txt fájlokat futtatjuk, és elkerüljük a végtelen ciklust (saját kimenetünket ne töltsük be)
                        if (fajl.endsWith(".txt") && !fajl.startsWith("out_")) {
                            tesztFuttatas(fajl);
                        }
                    }
                }
            } else {
                System.out.println("Hiba: A 'test' mappa nem talalhato.");
            }
            return;
        }

        System.out.println("--- Futtatas: " + tesztNev + " ---");

        beolvasFajlbol(tesztNev);
        parancsSor.add("save " + tesztNev);
        parancsSor.add("reset ");

    }

    /**
     * Kiirja a prototipus bemeneti nyelvenek rovid, strukturalt leirasat.
     *
     * <p>A help celja, hogy a parancsfajtak, a parameter-sorrend es a
     * legfontosabb peldak egy helyen, gyorsan attekinthetoen jelenjenek meg.
     * A parancsok elso szava mindig a parancs neve, az ezt koveto szavak pedig
     * a parancs parameterei.</p>
     */
    private void helpKiiras() {
        System.out.println(String.join(System.lineSeparator(),
                "",
                "==================== Prototipus help ====================",
                "",
                "Altalanos forma:",
                "  <parancs> [parameterek...]",
                "",
                "Alapszabalyok:",
                "  - Egy sor egy parancsot tartalmaz.",
                "  - A parancs neve mindig az elso szo.",
                "  - Az objektumokra a create paranccsal megadott id-val lehet hivatkozni.",
                "  - A referencia tipusu kapcsolatok beallitasa assign/remove paranccsal tortenik.",
                "  - A set parancs elsosorban egyszeru ertekeket allit.",
                "",
                "Rendszerszintu parancsok:",
                "  load <fajlnev>",
                "      Parancsokat olvas be a megadott fajlbol, es beteszi oket a parancssorba.",
                "",
                "  save <fajlnev>",
                "      Elmenti az aktualis prototipus-allapotot a megadott fajlba.",
                "",
                "  list_tests",
                "      Kiírja, hogy milyen nevű teszt bemenetek érhetőek el.",
                "",
                "  reset",
                "      Tiszta lapot nyit egy új tesztesetnek: az eddigi objektumok elvesznek.",
                "",
                "  create <osztaly> <id>",
                "      Letrehoz egy objektumot, es eltarolja a megadott id alatt.",
                "      Pelda: create Auto auto1",
                "",
                "  delete <id>",
                "      Torli a megadott id-ju objektumot a katalogusbol.",
                "",
                "  list [szuro]",
                "      Kilistazza a prototipusban nyilvantartott objektumokat vagy egy szurt reszuket.",
                "",
                "  tick [ido]",
                "      Lefuttatja a szimulacio kovetkezo lepeseit. Ha nincs ido megadva, az alapertelmezett ertek 1.",
                "",
                "  help",
                "      Kiirja ezt a sugot.",
                "",
                "  quit",
                "      Kilep a programbol.",
                "",
                "Letrehozhato tipusok:",
                "  Utegyseg, Csomopont, Ut, Sav, Hokotro, Auto, Busz",
                "  Zuzalekszoro, Sopro, Jegtoro, Sarkany, Soszoro, Hanyo",
                "  Nyilvantarto, Jatekos, Bolt",
                "",
                "Egy objektumot erinto parancsok:",
                "  set <id> <attributum> <ertek>",
                "      Beallitja az adott objektum egyszeru attributumat.",
                "      Pelda: set auto1 sebesseg 3",
                "      Pelda: set busz1 bevetel 100",
                "",
                "  move <id> <irany>",
                "      Mozgatja az adott jarmuvet vagy savvaltasra utasitja.",
                "      Tipikus iranyok: -f, forward, -l, -r, bal, jobb",
                "      Pelda: move auto1 -f",
                "",
                "  clean <id> [parameterek...]",
                "      Takaritasi muveletet ker az adott objektumtol.",
                "",
                "  add_condition <id> [parameterek...]",
                "      Feltetel vagy palyaallapot hozzaadasat ker az adott objektumtol.",
                "",
                "  purchase <id> [parameterek...]",
                "      Vasarlasi muveletet ker az adott objektumtol.",
                "",
                "Ket objektum kozotti kapcsolati parancsok:",
                "  assign <gazdaId> <celId>",
                "      Letrehoz vagy beallit egy kapcsolatot ket objektum kozott.",
                "      Pelda: assign auto1 cp1",
                "      Pelda: assign busz1 cp2",
                "",
                "  remove <gazdaId> <celId>",
                "      Megszuntet egy korabban letrehozott kapcsolatot ket objektum kozott.",
                "      Pelda: remove busz1 megallo1",
                "      Pelda: remove auto1 u1",
                "",
                "Gyakori attributumok:",
                "  Jarmu: sebesseg, tapadas, elakadt, baleset, megcsuszott",
                "  Auto: utonToltottIdo",
                "  Busz: bevetel",
                "",
                "Megjegyzes:",
                "  Ha egy parancs ket objektum kozotti kapcsolatot allit, hasznalj assign/remove parancsot.",
                "  Ha egy egyszeru szam, logikai ertek allitasa kell, hasznalj set parancsot.",
                "",
                "==========================================================",
                ""));
    }

    private void szimulacioTick(int n) {
        for (int i = 0; i < n; i++) {
            for (Utegyseg ue : katalogus.osszesOfType(Utegyseg.class)) {
                ue.havazas(1);
                ue.soOlvasztas();
            }
            for (Auto auto : katalogus.osszesOfType(Auto.class)) {
                auto.lep();
            }
            for (Busz busz : katalogus.osszesOfType(Busz.class)) {
                busz.lep();
            }
            for (Hokotro hokotro : katalogus.osszesOfType(Hokotro.class)) {
                hokotro.lep();
            }
            System.out.println("Tick " + (i + 1) + " lefutott.");
        }
    }

    public static void main(String[] args) {
        Prototipus proto = new Prototipus();
        // kitörli a temp.txt, így ha abban maradt korábbról naplózás, akkor sem fog bezavarni
        proto.tesztKornyezetAlaphelyzet();
        proto.futtat();

    }
}
