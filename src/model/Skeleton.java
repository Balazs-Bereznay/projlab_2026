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

            tesztInditas("Takarítás hóhányóval és bevétel elszámolása (Sikeres és Sikertelen eset)");
            /// inicializalas
            Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);
            Utegyseg u1 = new Utegyseg();
            Hanyo hny = new Hanyo();
            Hokotro j = new Hokotro(hny);

            j.setFej(hny);
            u1.setJarmu(j);
            j.setUtegyseg(u1);
            j.setNyilvantarto(ny);


            hivas("j:Hokotro", "takarit()");
            hivas("hny:Hanyo", "hasznal(u1)");
            int hoMagassag = egeszBeker("Hány centi hó van az útegységen?");

            if (hoMagassag > 0) {
                hivas("u1:Utegyseg", "tisztul()");
                visszater("u1:Utegyseg", "void");
                visszater("hny:Hanyo", "true");
                hivas("ny:Nyilvantarto", "penzNovel(bevetel)");
                ny.penzNovel(10);
                visszater("ny:Nyilvantarto", "void");
                visszater("j:Hokotro", "void");
                tesztLezaras("Sikeres takarítás: Az útegység megtisztult, a bevétel jóváírásra került.");

            } else {
                visszater("hny:Hanyo", "false (sikertelen)");
                visszater("j:Hokotro", "void");
                tesztLezaras("Sikertelen takarítás: Tiszta úton a takarítás érdemi része elmarad, nincs bevétel.");
            }

    }

    private void hokotroMozgasa() {
        tesztInditas("Hokotrü mozgása útvonalon és csomóponton");

        /// inicializálás
        Utegyseg u1  = new Utegyseg();
        Utegyseg u2 = new Utegyseg();
        Csomopont cs  = new Csomopont();
        Sopro sp = new Sopro();

        Hokotro h = new Hokotro(sp);

        u1.setJarmu(h);
        h.setUtegyseg(u1);

        boolean csomopont = igenNemBeker("csomóponton keresztül halad tovább a hokotró?");
        if(!csomopont) {
            hivas("u2:Utegyseg", "ralep(this)");
            visszater("u2:Utegyseg", "true");

            hivas("j:Hokotro", "sikeresLepes(u2)");

            visszater("j:Hokotro", "void");
            visszater("j:Hokotro", "void");

            tesztLezaras("Sikeres alapmozgás: A hókotró akadálytalanul rálépett a következő útegységre.");
        }
        else{
            hivas("cs:Csomopont", "jarmuTavozik(this)");
            visszater("cs:Csomopont", "void");

            hivas("cs:Csomopont", "jarmuErkezik(this)");
            visszater("cs:Csomopont", "void");

            hivas("u2:Utegyseg", "ralep(this)");
            visszater("u2:Utegyseg", "true");

            hivas("h:Hokotro", "sikeresLepes(u2)");
            visszater("h:Hokotro", "void");

            visszater("h:Hokotro", "void");

            tesztLezaras("Sikeres csomóponti mozgás: a hókotró új útra fordult és rálépett az első útegységére");

        }
    }

    private void jegtoresFolyamata() {

        tesztInditas("jegtores folyamata");

        //Inicializalas
        Utegyseg u1 = new Utegyseg();
        Jegtoro jt = new Jegtoro();
        Hokotro h = new Hokotro(jt);
        u1.setJarmu(h);
        h.setUtegyseg(u1);
        u1.setJegMagassag(1);


        hivas("h:Hokotro","takarit()");
        h.takarit();

        hivas("jt:Jegtoro","hasznal(u1)");
        jt.hasznal(u1);

        hivas("u1:Utegyseg", "jegtores()");
        u1.setJegMagassag(egeszBeker("milyen magas a jég az utegységen?"));
        if(u1.getJegMagassag() > 0){
            u1.jegtores();
            visszater("u1:Utegyseg", "void");
            visszater("jt:Jegtoro", "true");
            visszater("j:Hokotro", "void");
            tesztLezaras("Sikeres az útegységen lévő jég fel lett törve.");

        }
        else{
            visszater("u1:Utegyseg", "void");
            visszater("jt:Jegtoro", "false");
            visszater("j:Hokotro", "void");
            tesztLezaras("Sikertelen-nincs jég az útegységen.");
        }

    }

    private void kritikusVeresegSzimulacio() {

        tesztInditas("kritikus vereseg szimulacio");

        //Inicializáció
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);
        Utegyseg u1 = new  Utegyseg();
        Utegyseg u2  = new Utegyseg();
        Auto a1 = new Auto();
        Auto a2 = new Auto();

        u1.setJarmu(a1);
        a1.setUtegyseg(u1);
        u2.setJarmu(a2);
        a2.setUtegyseg(u2);
        a1.setNyilvantarto(ny);
        a2.setNyilvantarto(ny);

        hivas("a1:Auto","baleset()");
        a1.baleset();
        hivas("ny:Nyilvantarto","nemBeertAutokNovel()");
        ny.nemBeertAutokNovel(2);
        visszater("ny:Nyilvantarto","void");
        visszater("a1:Auto","void");

        hivas("ny:Nyilvantarto","ellenorizJatekVege()");
        if(igenNemBeker("Meghaladta a be nem ért autók száma a küszöbértéket?")){

        ny.setNemBeertAutokSzama(16);
        ny.ellenorizJatekVege();
        visszater("ny:Nyilvantarto","true");
        tesztLezaras("Sikeresen lefutott a teszt, a jateknek tul sok be nem érő autos miatt végetért");
        }
        else{
            visszater("ny:Nyilvantarto","false");
            tesztLezaras("Sikertelen teszt, mivel a be nem érő autosok száman em haladja meg a küszöbértéket, a jaték nem ért véget.");
        }

    }

    private void buszfejlesztesVasarlasa() {
        tesztInditas("Buszfejlesztés vásárlása");

        // Inicializálás
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);
        Bolt b = new Bolt(0, 0);
        //b.setNyilvantarto(ny);

        Busz busz = new Busz();
        Sebessegfejlesztes sf = new Sebessegfejlesztes();


        hivas("b:Bolt", "sebessegFejlesztes(busz)");
        b.sebessegFejlesztes(busz);

        hivas("sf:Sebessegfejlesztes", "getAr()");
        sf.getAr();
        visszater("sf:Sebessegfejlesztes", "ar");

        hivas("ny:Nyilvantarto", "penzLevon(mennyiseg)");
        ny.penzLevon(0);

        if (igenNemBeker("Van elég pénz?")) {
            visszater("ny:Nyilvantarto", "true");

            hivas("sf:Sebessegfejlesztes", "fejleszt()");
            sf.fejleszt();

            hivas("busz:Busz", "setSebesseg(megnoveltErtek)");
            busz.setSebesseg(0);
            visszater("busz:Busz", "void");

            visszater("sf:Sebessegfejlesztes", "void");
            visszater("b:Bolt", "void");

            tesztLezaras("Siker");
        } else {
            visszater("ny:Nyilvantarto", "false");
            visszater("b:Bolt", "void");

            tesztLezaras("Sikertelen");
        }
    }

    private void fejcsereABoltban() {
        tesztInditas("Fejcsere a boltban");

        // Inicializálás
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);
        Bolt b = new Bolt(0, 0);
        //b.setNyilvantarto(ny);

        Sopro sopro = new Sopro();
        Hokotro h = new Hokotro(sopro);

        Hanyo hanyo = new Hanyo();


        hivas("b:Bolt", "hanyoVasarol(h)");
        b.hanyoVasarol(h);

        hivas("ny:Nyilvantarto", "penzLevon(ar)");
        ny.penzLevon(0);
        if (igenNemBeker("Van elég pénz?")) {
            visszater("ny:Nyilvantarto", "true");

            hivas("h:Hokotro", "setFej(hanyo)");
            h.setFej(hanyo);
            visszater("h:Hokotro", "void");

            visszater("b:Bolt", "void");

            tesztLezaras("Siker");
        }
        else {
            visszater("ny:Nyilvantarto", "false");
            visszater("b:Bolt", "void");

            tesztLezaras("Sikertelen");
        }
    }

    private void npcTorlodasKezelese() {
        tesztInditas("NPC torlódás kezelése");

        // Inicializálás
        Utegyseg u1 = new Utegyseg();
        Utegyseg u2 = new Utegyseg();
        Utegyseg u3 = new Utegyseg();
        Auto a1 = new Auto();
        Auto a2 = new Auto();
        u1.setKovetkezoUtegyseg(u2);
        u1.setBalUtegyseg(u3);
        u2.setJarmu(a1);
        u2.setBlokkolt(true);


        hivas("a2:Auto", "lep()");
        a2.lep();

        hivas("u2:Utegyseg", "getBlokkolt()");
        u2.getBlokkolt();
        visszater("u2:Utegyseg", "true");

        hivas("u1:Utegyseg", "getBalUtegyseg()");
        u1.getBalUtegyseg();
        visszater("u1:Utegyseg", "u3");

        hivas("u3:Utegyseg", "getBlokkolt()");
        u3.getBlokkolt();

        if (igenNemBeker("Szabad a szomszédos sáv?")) {
            visszater("u3:Utegyseg", "false");

            hivas("u1:Utegyseg", "setJarmu(null)");
            u1.setJarmu(null);
            visszater("u1:Utegyseg", "void");

            hivas("u3:Utegyseg", "elfogad(a2)");
            u3.setJarmu(a2);
            visszater("u3:Utegyseg", "void");

            visszater("a2:Auto", "void");

            tesztLezaras("Siker");
        } else {
            visszater("u3:Utegyseg", "true");
            visszater("a2:Auto", "void");

            tesztLezaras("Sikertelen");
        }
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

        hivas("f:Fej", "hasznal()");
        s.hasznal(u);

        hivas("f:Fej", "aktival()");
        s.aktival(u);

        hivas("ny:Nyilvantarto", "biokerozinLevon()");
        ny.biokerozinLevon(0);

        if (igenNemBeker("Van elég biokerozin?")) {
            visszater("ny:Nyilvantarto", "true");
            visszater("f:Fej", "true");

            hivas("u:Utegyseg", "jegtores()");
            u.jegtores();
            visszater("u:Utegyseg", "void");

            hivas("u:Utegyseg", "tisztulas()");
            u.tisztulas();
            visszater("u:Utegyseg", "void");

            visszater("f:Fej", "true");

            visszater("h:Hokotro", "void");

            tesztLezaras("Siker");
        }
        else {
            visszater("ny:Nyilvantarto", "false");
            visszater("f:Fej", "false");
            visszater("f:Fej", "false");
            visszater("h:Hokotro", "void");

            tesztLezaras("Sikertelen");
        }
    }

    private void soszoroFejHosszutavuHatasa() {
        tesztInditas("Sószóró fej hosszútávú hatása");

        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);
        Soszoro sosz = new Soszoro(ny);
        Hokotro h = new Hokotro(sosz);
        Utegyseg u = new Utegyseg();
        u.havazas(5);


        hivas("h:Hokotro", "takarit()");
        h.takarit();

        hivas("sosz:Soszoro", "hasznal(u)");
        sosz.hasznal(u);

        hivas("sosz:Soszoro", "aktival(u)");
        sosz.aktival(u);

        hivas("ny:Nyilvantarto", "soLevon(mennyiseg)");
        ny.soLevon(0);

        if (igenNemBeker("Van elég só a fej használatához?")) {
            visszater("ny:Nyilvantarto", "true");
            visszater("sosz:Soszoro", "true");

            hivas("u:Utegyseg", "sozas(mennyiseg)");
            u.sozas(0);
            visszater("u:Utegyseg", "void");

            visszater("sosz:Soszoro", "true");
            visszater("h:Hokotro", "void");

            if (igenNemBeker("Van még só az útegységen?")) {
                if (igenNemBeker("Van hó az útegységen?")) {
                    hivas("u:Utegyseg", "hoOlvad()");
                    u.soOlvasztas();
                    visszater("u:Utegyseg", "void");

                    hivas("u:Utegyseg", "setSoMennyiseg(soMennyiseg-1)");
                    u.setSoMennyiseg(0);
                    visszater("u:Utegyseg", "void");

                    tesztLezaras("Siker");
                } else {
                    tesztLezaras("Véget ért - Nincs hó");
                }
            } else {
                tesztLezaras("Véget ért - Nincs só");
            }
        } else {
            visszater("ny:Nyilvantarto", "false");
            visszater("sosz:Soszoro", "false");
            visszater("sosz:Soszoro", "false");
            visszater("h:Hokotro", "void");
            tesztLezaras("Sikertelen");
        }
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