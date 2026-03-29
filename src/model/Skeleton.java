package model;

import java.util.Scanner;

/*
 * Skeleton osztály – segédfüggvények összefoglalása
 *
 * A Skeleton osztály feladata, hogy konzolos felületet biztosítson a
 * szkeleton tesztesetek futtatásához. A tényleges tesztlogika később
 * kerül bele az egyes teszteset-metódusokba, ezért azok most üresek.
 *
 * A jelenlegi osztályban az alábbi segédfüggvények találhatók:
 *
 * - menuKiir():
 *   Kiírja a felhasználónak az elérhető tesztesetek listáját.
 *
 * - tesztFuttat(int sorszam):
 *   A kiválasztott menüpont alapján meghívja a megfelelő teszteset-metódust.
 *
 * - tesztInditas(String tesztNev):
 *   Kiírja a teszteset indulásához tartozó alapinformációkat,
 *   például a teszt nevét és az inicializálásról szóló üzeneteket.
 *
 * - tesztLezaras(String eredmeny):
 *   Lezárja a tesztesetet, és kiírja annak eredményét.
 *
 * - hivas(String hivo, String muvelet):
 *   Egy metódushívás kezdetét naplózza a dokumentációban használt
 *   ">[...]" formátumban, és növeli a behúzási szintet.
 *
 * - visszater(String hivo, String ertek):
 *   Egy metódushívás végét naplózza a dokumentációban használt
 *   "<[...]" formátumban, és csökkenti a behúzási szintet.
 *
 * - igenNemBeker(String kerdes):
 *   Konzolon keresztül igen/nem típusú választ kér be a felhasználótól.
 *
 * - egeszBeker(String kerdes):
 *   Konzolon keresztül egész számot kér be a felhasználótól.
 *
 * - kiirBehuzva(String szoveg):
 *   A megadott szöveget a pillanatnyi behúzási szintnek megfelelően írja ki,
 *   így alkalmas hívási hierarchia szemléltetésére.
 *
 * Ezek a függvények azért készültek, hogy az egyes tesztesetek később
 * egységes formában legyenek megvalósíthatók és naplózhatók.
 */
public class Skeleton {

    private static final String[] TESZTESETEK = {
            "Hóesés egy útegységre",
            "Erőforrás vásárlása",
            "Úttisztítás söprő fejjel",
            "Összecsúszás szimuláció",
            "Busz teljes menetrendi kör",
            "Takarítás hóhányóval és bevétel elszámolása",
            "Hókotró mozgása útvonalon és csomóponton",
            "Jégtörés folyamata",
            "Kritikus vereség szimuláció baleset miatt",
            "Buszfejlesztés vásárlása",
            "Fejcsere a boltban",
            "NPC torlódás kezelése",
            "Sárkány fej használata",
            "Sószóró fej hosszútávú hatása",
            "Jégre hulló hó",
            "Komplex vásárlási lánc",
            "Megcsúszás jégen",
            "NPC útvonaltervezés",
            "Pénz hozzáadása a kasszához",
            "Autó sikeres sávváltása",
            "Mély hóban elakadás",
            "Játék vége",
            "Buszmegálló érintése",
            "Jégképződés taposás miatt"
    };

    private final Scanner scanner;
    private int behuzas;

    public Skeleton() {
        this.scanner = new Scanner(System.in);
        this.behuzas = 0;
    }

    public static void main(String[] args) {
        Skeleton skeleton = new Skeleton();
        skeleton.futtat();
    }

    /**
     * Elindítja a skeleton konzolos felületét.
     */
    public void futtat() {
        boolean fut = true;

        while (fut) {
            menuKiir();
            int valasztas = egeszBeker("Válassz tesztesetet (0 = kilépés):");

            if (valasztas == 0) {
                System.out.println("[Kilépés]");
                fut = false;
            } else if (valasztas < 0 || valasztas > TESZTESETEK.length) {
                System.out.println("[Érvénytelen választás]");
            } else {
                tesztFuttat(valasztas);
                System.out.println();
            }
        }
    }

    /**
     * Kiírja az elérhető teszteseteket.
     */
    private void menuKiir() {
        System.out.println("=== Skeleton tesztesetek ===");
        for (int i = 0; i < TESZTESETEK.length; i++) {
            System.out.println((i + 1) + ". " + TESZTESETEK[i]);
        }
        System.out.println("0. Kilépés");
    }

    /**
     * A kiválasztott tesztesethez tartozó metódust indítja el.
     * @param sorszam A teszteset sorszáma.
     */
    private void tesztFuttat(int sorszam) {
        switch (sorszam) {
            case 1:
                hoesesEgyUtegysegre();
                break;
            case 2:
                eroforrasVasarlasa();
                break;
            case 3:
                uttisztitasSoproFejjel();
                break;
            case 4:
                osszecsuszasSzimulacio();
                break;
            case 5:
                buszTeljesMenetrendiKor();
                break;
            case 6:
                takaritasHohanyoval();
                break;
            case 7:
                hokotroMozgasa();
                break;
            case 8:
                jegtoresFolyamata();
                break;
            case 9:
                kritikusVeresegSzimulacio();
                break;
            case 10:
                buszfejlesztesVasarlasa();
                break;
            case 11:
                fejcsereABoltban();
                break;
            case 12:
                npcTorlodasKezelese();
                break;
            case 13:
                sarkanyFejHasznalata();
                break;
            case 14:
                soszoroFejHosszutavuHatasa();
                break;
            case 15:
                jegreHulloHo();
                break;
            case 16:
                komplexVasarlasiLanc();
                break;
            case 17:
                megcsuszasJegen();
                break;
            case 18:
                npcUtvonaltervezes();
                break;
            case 19:
                penzHozzaadasaAKasszahoz();
                break;
            case 20:
                autoSikeresSavvaltasa();
                break;
            case 21:
                melyHobanElakadas();
                break;
            case 22:
                jatekVege();
                break;
            case 23:
                buszmegalloErintese();
                break;
            case 24:
                jegkepzodesTaposasMiatt();
                break;
            default:
                System.out.println("[Nincs ilyen teszteset]");
        }
    }

    private void hoesesEgyUtegysegre() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void eroforrasVasarlasa() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void uttisztitasSoproFejjel() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void osszecsuszasSzimulacio() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void buszTeljesMenetrendiKor() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void takaritasHohanyoval() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void hokotroMozgasa() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void jegtoresFolyamata() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void kritikusVeresegSzimulacio() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void buszfejlesztesVasarlasa() {

    }

    private void fejcsereABoltban() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void npcTorlodasKezelese() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void sarkanyFejHasznalata() {
        tesztInditas("Sárkány fej használata");

        // Inicializálás
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);
        Sarkany s = new Sarkany(ny);
        Hokotro h = new Hokotro(s);     // Hókotró létrehozása és fej beállítása
        Utegyseg u = new Utegyseg();

        h.setUtegyseg(u);               // A jármű pozíciójának beállítása a teszthez
        u.havazas(5);

        hivas("h:Hokotro", "takarit()");
        h.takarit();
        visszater("h:Hokotro", "takarit()");

        tesztLezaras("Siker");
    }

    private void soszoroFejHosszutavuHatasa() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void jegreHulloHo() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void komplexVasarlasiLanc() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void megcsuszasJegen() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void npcUtvonaltervezes() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void penzHozzaadasaAKasszahoz() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void autoSikeresSavvaltasa() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void melyHobanElakadas() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void jatekVege() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void buszmegalloErintese() {
        // TODO: A teszteset implementációja később kerül ide.
    }

    private void jegkepzodesTaposasMiatt() {
        tesztInditas("Jégképződés taposás miatt");

        /// Inicializálás
        Busz b = new Busz();
        Utegyseg ueKov = new Utegyseg(4, 0.0, null, null, null, null, 10, 0, false, 0);
        Utegyseg ueAkt = new Utegyseg(0, 0.0, b, ueKov, null, null, 0, 0, false, 0);
        Sav s = new Sav(ueAkt);
        ArrayList<Sav> savok = new ArrayList<>();
        savok.add(s);
        Ut ut = new Ut(savok, null, null, false);
        b.setUtegyseg(ueAkt);



        /* /// Inicializálás
        Busz b = new Busz();
        Ut ut = new Ut();
        Sav s = new Sav();
        Utegyseg ueAkt = new Ut();
        Utegyseg ueKov = new Ut();

        // Egységek összekötése
        ArrayList<Sav> savok = new ArrayList<>();
        savok.add(s);
        ut.setSavok(savok);
        s.setElsoUtegyseg(ueAkt);
        ueAkt.setKovetkezoUtegyseg(ueKov);
        ueAkt.setJarmu(b);
        b.setUtegyseg(ueAkt);*/






    }

    /**
     * Előkészíti a teszteset futását.
     * @param tesztNev A teszteset neve.
     */
    private void tesztInditas(String tesztNev) {
        this.behuzas = 0;
        System.out.println("[Teszteset: " + tesztNev + "]");
        System.out.println("[Teszteset inicializálása....]");
        System.out.println("[Inicializálás sikeresen lefutott]");
    }

    /**
     * Lezárja a tesztesetet.
     * @param eredmeny A teszteset eredménye.
     */
    private void tesztLezaras(String eredmeny) {
        this.behuzas = 0;
        System.out.println("[Teszt sikeresen lefutott]");
        System.out.println("[Eredmény: " + eredmeny + "]");
    }

    /**
     * Naplózza egy metódushívás kezdetét.
     * @param hivo Az objektum neve.
     * @param muvelet A hívott művelet.
     */
    private void hivas(String hivo, String muvelet) {
        kiirBehuzva(">[" + hivo + ": " + muvelet + "]");
        behuzas++;
    }

    /**
     * Naplózza egy metódushívás végét.
     * @param hivo Az objektum neve.
     * @param ertek A visszatérési érték vagy jelzés.
     */
    private void visszater(String hivo, String ertek) {
        if (behuzas > 0) {
            behuzas--;
        }
        kiirBehuzva("<[" + hivo + ": " + ertek + "]");
    }

    /**
     * Bekér egy igen/nem választ.
     * @param kerdes A megjelenítendő kérdés.
     * @return Igaz, ha a válasz igen.
     */
    private boolean igenNemBeker(String kerdes) {
        kiirBehuzva("?[" + kerdes + "]");
        System.out.print("$ ");
        String valasz = scanner.nextLine().trim().toLowerCase();

        return valasz.equals("i")
                || valasz.equals("igen")
                || valasz.equals("y")
                || valasz.equals("yes")
                || valasz.equals("true");
    }

    /**
     * Bekér egy egész számot.
     * @param kerdes A megjelenítendő kérdés.
     * @return A megadott egész szám.
     */
    private int egeszBeker(String kerdes) {
        while (true) {
            System.out.println("?[" + kerdes + "]");
            System.out.print("$ ");
            String sor = scanner.nextLine().trim();

            try {
                return Integer.parseInt(sor);
            } catch (NumberFormatException e) {
                System.out.println("[Érvénytelen szám, próbáld újra]");
            }
        }
    }

    /**
     * Kiír egy sort a jelenlegi behúzási szinttel.
     * @param szoveg A kiírandó szöveg.
     */
    private void kiirBehuzva(String szoveg) {
        for (int i = 0; i < behuzas; i++) {
            System.out.print("    ");
        }
        System.out.println(szoveg);
    }
}