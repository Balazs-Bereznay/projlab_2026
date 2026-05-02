package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        try (BufferedReader br = new BufferedReader(new FileReader(fajlnev))) {
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
        File celMappa = new File("out");
        if (!celMappa.exists()) {
            celMappa.mkdirs(); // Létrehozzuk az out mappát, ha még nincs
        }

        Path celUtvonal = Paths.get("out", fajlnev);

        try {
            // Átmásoljuk a temp.txt-t a célhelyre, felülírva ha már létezik
            Files.copy(forras.toPath(), celUtvonal, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Állapot sikeresen mentve: " + celUtvonal.toString());
        } catch (IOException e) {
            System.err.println("Hiba a mentés során: " + e.getMessage());
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
        if (szavak == null) {
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

    public void EntitasTorol(String id){
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
                    beolvasFajlbol(szavak[1]);
                    break;
                case "save":
                    allapotMentese(szavak[1]);
                    break;
                case "reset":
                    tesztKornyezetAlaphelyzet();
                    break;
                case "create":
                    entitasLetrehoz(szavak[1], szavak[2]);
                    break;
                 case "delete":
                     entitasTorol(szavak[1]);
                     break;
                case "list":
                    listazasKezelo(szavak);
                    break;
                case "tick":
                    int ido = (szavak.length > 1) ? Integer.parseInt(szavak[1]) : 1;
                    szimulacioTick(ido);
                    break;
                case "help":
                    helpKiiras();
                    break;


                // --- Egyedi entitás parancsok (2 paraméteres overload) ---
                // ! Minden az ProtoEntitas két paraméteres parancsFeldolgoz-t hívó esetnek ugyan az lenne a törzse,
                // ! ezért a set, move stb. esetek végén nincs break és így közülük bármelyik következik be
                // ! a purchase-nál megírt törzs fog lefutni. (Nem kell azokhoz semmit írni.)
                case "set":
                case "move":
                case "clean":
                case "add_condition":
                case "list_shop":
                case "add": {
                    ProtoEntitas celpont = katalogus.keres(szavak[1]);
                    if (celpont != null) {
                        celpont.parancsFeldolgoz(cmd, parametereketVag(szavak, 2));
                    }
                    break;
                }
                /// Az info parancsnál kell a kiíráshoz is az entitás azonosítója
                case "info": {
                    ProtoEntitas celpont = katalogus.keres(szavak[1]);
                    if (celpont != null) {
                        celpont.parancsFeldolgoz(cmd, parametereketVag(szavak, 1));
                    } else {
                        System.out.println("Entity not found!");
                    }
                    break;
                }

                case "purchase": {
                    // eset: purchase <id> <valami> [mennyiseg]
                    // legalább 3 hosszúnak kell lennie a tömbnek
                    if (szavak.length < 3) {
                        break;
                    }

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
                        if (szavak.length < 4) {
                            break;
                        }

                        ProtoEntitas cel = katalogus.keres(szavak[3]);

                        if (cel == null) {
                            break;
                        }

                        // Meghívjuk a háromparaméteres változatot
                        // cmd = "purchase", cel = a cél objektum, args = [item, celId, ...]
                        gazda.parancsFeldolgoz(cmd, cel, parametereketVag(szavak, 2));
                    }
                    break;
                }

                // --- Kapcsolati parancsok (3 paraméteres overload) ---
                case "assign":
                    ProtoEntitas gazdi = katalogus.keres(szavak[1]);
                    ProtoEntitas destionation = katalogus.keres(szavak[2]);
                    if (gazdi != null && destionation != null) {
                        gazdi.parancsFeldolgoz(cmd, destionation, parametereketVag(szavak, 3));
                    }
                    break;
                    
                case "remove":
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
                "      Lista uritesehez hasznalhato ertek: empty vagy {}",
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
                "  Jarmu: sebesseg, tapadas, elakadt, baleset, megcsuszott, kijeloltUtvonal",
                "  Auto: utonToltottIdo",
                "  Busz: bevetel, megallokLista, erintettLista, tervezettUtvonal",
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
            }
            for (Auto auto : katalogus.osszesOfType(Auto.class)) {
                auto.lep();
            }
            System.out.println("Tick " + (i + 1) + " lefutott.");
        }
    }

    public static void main(String[] args) {
        Prototipus proto = new Prototipus();

        proto.futtat();
    }
}
