package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class Prototipus {
    // A központi nyilvántartó, ahol minden objektum lakik
    private final ObjektumKatalogus katalogus = new ObjektumKatalogus();

    // A FIFO sor, ami a feldolgozandó parancsokat tárolja
    private final Queue<String> parancsSor = new ArrayDeque<>();

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
                case "create":
                    entitasLetrehoz(szavak[1], szavak[2]);
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
                // ! a purshe-nál megírt törzs fog lefutni. (Nem kell azokhoz semmit írni.)
                case "set":
                case "move":
                case "clean":
                case "add_condition":
                case "purchase":
                    ProtoEntitas celpont = katalogus.keres(szavak[1]);
                    if (celpont != null) {
                        celpont.parancsFeldolgoz(cmd, parametereketVag(szavak, 2));
                    }
                    break;

                // --- Kapcsolati parancsok (3 paraméteres overload) ---
                case "assign":
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
}
