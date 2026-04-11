package model;

import java.util.Scanner;
import java.util.ArrayList;

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
            "Jégképződés taposás miatt",
            "Komplex teszteset"
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
                case 25:
                    komplex();
                    break;
            default:
                System.out.println("[Nincs ilyen teszteset]");
        }
    }

    private  void komplex(){

        tesztInditas("Komplex teszteset");

        /// inicializalas
        Utegyseg u1 = new Utegyseg();
        Utegyseg startB = new Utegyseg();
        Utegyseg startA1 = new Utegyseg();
        Utegyseg startA2 = new Utegyseg();
        Utegyseg startA3 = new Utegyseg();

        Busz b = new Busz();
        Auto a1 = new Auto();
        Auto a2 = new Auto();
        Auto a3 = new Auto();

        a1.setUtegyseg(startA1);
        startA1.setJarmu(a1);

        a2.setUtegyseg(startA2);
        startA2.setJarmu(a2);

        a3.setUtegyseg(startA3);
        startA3.setJarmu(a3);

        b.setUtegyseg(startB);
        startB.setJarmu(b);

        /// busz letapossa a havat jeg lesz belole
        hivas("b:Busz", "lep(u1)");

        hivas("u1:Utegyseg", "ralep(this)");
        hivas("u1:Utegyseg", "taposodas()");
        visszater("u1:Utegyseg", "void");

        if (igenNemBeker("Elérte a letaposottság a küszöböt?")) {
            hivas("u1:Utegyseg", "jegesedes()");
            visszater("u1:Utegyseg", "void");
        }

        visszater("u1:Utegyseg", "true");
        visszater("b:Busz", "void");

        u1.setJarmu(null); // levesszuk a buszt onnan, hogy az auto oda tujdon lepni

        /// a1 auto lepese megcsuszasa és baleset
        hivas("a1:Auto", "lep(u1)");
        hivas("u1:Utegyseg", "ralep(this)");


        if (igenNemBeker("Megcsúszik-e az a1 jármű? (I/N)")) {
            hivas("a1:Auto", "csuszik()");
            visszater("a1:Auto", "void");

            hivas("u1:Utegyseg", "balesetetKeres()");

            if (igenNemBeker("Van-e elérhető karambolpartner a közelben? (I/N)")) {
                hivas("u1:Utegyseg", "KeresPartner() -> a2");
                visszater("u1:Utegyseg", "a2");

                hivas("a2:Auto", "baleset()");
                kiirBehuzva("    [Megjegyzés: A baleset blokkolja a sávot.]");
                visszater("u1:Utegyseg", "baleset rögzítve");

            }
            else{  visszater("u1:Utegyseg", "void");}


            visszater("a1:Auto", "void");


        }
        visszater("u1:Utegyseg", "void");
        visszater("a1:Auto", "void");

        u1.setJarmu(null); // itt is ugyanezt tesszuk

        hivas("a3:Auto", "lep(u1)");

        hivas("u1:Utegyseg", "getBlokkolt()");
        visszater("u1:Utegyseg", "true");
        if (!igenNemBeker("Van szabad szomszédos sáv? (I/N)")) {
            kiirBehuzva("    [Megjegyzés: a3 megáll, torlódás alakul ki]");
        } else {
            kiirBehuzva("    [Megjegyzés: a3 sikeresen sávot váltott.]");
        }

        visszater("a3:Auto", "void");

        tesztLezaras("Naplózás (teszt lefutott)");


    }

    private void hoesesEgyUtegysegre() {
        tesztInditas("Hóesés egy útegységre");

        Utegyseg u = new Utegyseg();

        boolean jeges = igenNemBeker("Jeges az útegység?");

        if (jeges) {
            u.setJegMagassag(1);
        }

        hivas("u:Utegyseg", "havazas(1)");
        u.havazas(1);
        visszater("u:Utegyseg", "void");

        tesztLezaras(
                "hoMagassag=" + u.getHoMagassag()
                        + ", jegMagassag=" + u.getJegMagassag()
                        + ", blokkolt=" + u.getBlokkolt()
        );
    }

    private void eroforrasVasarlasa() {
        tesztInditas("Erőforrás vásárlása");

        Bolt bolt = new Bolt(10, 20);
        Nyilvantarto ny = new Nyilvantarto(1000, 0, 0, 0);

        boolean sotVesz = igenNemBeker("Sót szeretnél vásárolni? (nem esetén biokerozint veszünk)");
        int mennyiseg = egeszBeker("Add meg a vásárolni kívánt mennyiséget:");

        if (sotVesz) {
            int fizetendo = bolt.getSoAr() * mennyiseg;

            hivas("bolt:Bolt", "soVasarol(" + mennyiseg + ")");
            bolt.soVasarol(mennyiseg);
            visszater("bolt:Bolt", "void");

            hivas("ny:Nyilvantarto", "penzLevon(" + fizetendo + ")");
            ny.penzLevon(fizetendo);
            visszater("ny:Nyilvantarto", "void");

            hivas("ny:Nyilvantarto", "soNovel(" + mennyiseg + ")");
            ny.soNovel(mennyiseg);
            visszater("ny:Nyilvantarto", "void");

            tesztLezaras("Sikeres sóvásárlás, mennyiség=" + mennyiseg
                    + ", fizetendő=" + fizetendo
                    + ", aktuális sókészlet=" + ny.getSo());
        } else {
            int fizetendo = bolt.getBiokerozinAr() * mennyiseg;

            hivas("bolt:Bolt", "setBiokerozinVasarol(" + mennyiseg + ")");
            bolt.setBiokerozinVasarol(mennyiseg);
            visszater("bolt:Bolt", "void");

            hivas("ny:Nyilvantarto", "penzLevon(" + fizetendo + ")");
            ny.penzLevon(fizetendo);
            visszater("ny:Nyilvantarto", "void");

            hivas("ny:Nyilvantarto", "biokerozinNovel(" + mennyiseg + ")");
            ny.biokerozinNovel(mennyiseg);
            visszater("ny:Nyilvantarto", "void");

            tesztLezaras("Sikeres biokerozin-vásárlás, mennyiség=" + mennyiseg
                    + ", fizetendő=" + fizetendo
                    + ", aktuális biokerozin készlet=" + ny.getBiokerozin());
        }
    }

    private void uttisztitasSoproFejjel() {
        tesztInditas("Úttisztítás Söprő fejjel");

        Utegyseg u1 = new Utegyseg();
        Utegyseg jobbSzomszed = new Utegyseg();
        Sopro sopro = new Sopro();
        Hokotro hokotro = new Hokotro(sopro);
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);

        hokotro.setUtegyseg(u1);
        u1.setJarmu(hokotro);

        u1.setHoMagassag(3);

        boolean vanJobbSzomszed = igenNemBeker("Van a menetirány szerinti jobb oldalon szomszédos útegység?");

        if (vanJobbSzomszed) {
            u1.setJobbUtegyseg(jobbSzomszed);
        }

        hivas("hokotro:Hokotro", "getFej().hasznal(u1)");
        boolean sikeres = hokotro.getFej().hasznal(u1);
        visszater("hokotro:Hokotro", String.valueOf(sikeres));

        if (sikeres) {
            hivas("ny:Nyilvantarto", "penzNovel(10)");
            ny.penzNovel(10);
            visszater("ny:Nyilvantarto", "void");
        }

        if (vanJobbSzomszed) {
            tesztLezaras(
                    "sikeres=" + sikeres
                            + ", u1.hoMagassag=" + u1.getHoMagassag()
                            + ", u1.jegMagassag=" + u1.getJegMagassag()
                            + ", jobbSzomszed.hoMagassag=" + jobbSzomszed.getHoMagassag()
            );
        } else {
            tesztLezaras(
                    "sikeres=" + sikeres
                            + ", u1.hoMagassag=" + u1.getHoMagassag()
                            + ", u1.jegMagassag=" + u1.getJegMagassag()
                            + ", a hó az út szélére került"
            );
        }
    }

    private void osszecsuszasSzimulacio() {
        tesztInditas("Összecsúszás szimuláció");

        Utegyseg u1 = new Utegyseg();
        Utegyseg u2 = new Utegyseg();

        Auto a1 = new Auto();
        Auto a2 = new Auto();

        u1.setJegMagassag(1);
        u1.setMegcsuszasEsely(1.0);

        u1.setKovetkezoUtegyseg(u2);

        a1.setUtegyseg(u1);
        u1.setJarmu(a1);

        a2.setUtegyseg(u2);
        u2.setJarmu(a2);

        hivas("a1:Auto", "lep()");
        a1.lep();
        visszater("a1:Auto", "void");

        boolean megcsuszik = igenNemBeker("Megcsússzon a jármű?");

        if (megcsuszik) {
            hivas("a1:Auto", "csuszik()");
            a1.csuszik();
            visszater("a1:Auto", "void");

            hivas("a1:Auto", "KeresPartner()");
            a1.KeresPartner();
            visszater("a1:Auto", "void");
        }

        tesztLezaras(
                "a1.baleset=" + a1.isBaleset()
                        + ", a2.baleset=" + a2.isBaleset()
                        + ", u1.blokkolt=" + u1.getBlokkolt()
                        + ", u2.blokkolt=" + u2.getBlokkolt()
        );
    }

    private void buszTeljesMenetrendiKor() {
        tesztInditas("Busz teljes menetrendi kör");

        // Csomópontok
        Csomopont veg1 = new Csomopont();
        Csomopont meg1 = new Csomopont();
        Csomopont meg2 = new Csomopont();
        Csomopont veg2 = new Csomopont();

        veg1.setAzonosito("Vegallomas1");
        meg1.setAzonosito("Megallo1");
        meg2.setAzonosito("Megallo2");
        veg2.setAzonosito("Vegallomas2");

        veg1.setBuszmegallo(true);
        meg1.setBuszmegallo(true);
        meg2.setBuszmegallo(true);
        veg2.setBuszmegallo(true);

        // Utegysegek a teljes körhöz:
        // veg1 -> meg1 -> meg2 -> veg2 -> meg2 -> meg1 -> veg1
        Utegyseg u11 = new Utegyseg();
        Utegyseg u12 = new Utegyseg();

        Utegyseg u21 = new Utegyseg();
        Utegyseg u22 = new Utegyseg();

        Utegyseg u31 = new Utegyseg();
        Utegyseg u32 = new Utegyseg();

        Utegyseg u41 = new Utegyseg();
        Utegyseg u42 = new Utegyseg();

        Utegyseg u51 = new Utegyseg();
        Utegyseg u52 = new Utegyseg();

        Utegyseg u61 = new Utegyseg();
        Utegyseg u62 = new Utegyseg();

        // Utegyseg-láncok
        u11.setKovetkezoUtegyseg(u12);
        u21.setKovetkezoUtegyseg(u22);
        u31.setKovetkezoUtegyseg(u32);
        u41.setKovetkezoUtegyseg(u42);
        u51.setKovetkezoUtegyseg(u52);
        u61.setKovetkezoUtegyseg(u62);

        // Sávok
        Sav s1 = new Sav(u11);
        Sav s2 = new Sav(u21);
        Sav s3 = new Sav(u31);
        Sav s4 = new Sav(u41);
        Sav s5 = new Sav(u51);
        Sav s6 = new Sav(u61);

        // Utak
        Ut ut1 = new Ut();
        ut1.setVegpont1(veg1);
        ut1.setVegpont2(meg1);
        ut1.addSav(s1);

        Ut ut2 = new Ut();
        ut2.setVegpont1(meg1);
        ut2.setVegpont2(meg2);
        ut2.addSav(s2);

        Ut ut3 = new Ut();
        ut3.setVegpont1(meg2);
        ut3.setVegpont2(veg2);
        ut3.addSav(s3);

        Ut ut4 = new Ut();
        ut4.setVegpont1(veg2);
        ut4.setVegpont2(meg2);
        ut4.addSav(s4);

        Ut ut5 = new Ut();
        ut5.setVegpont1(meg2);
        ut5.setVegpont2(meg1);
        ut5.addSav(s5);

        Ut ut6 = new Ut();
        ut6.setVegpont1(meg1);
        ut6.setVegpont2(veg1);
        ut6.addSav(s6);

        // Kapcsolatok a csomópontok és utak között
        veg1.addUt(ut1);
        veg1.addUt(ut6);

        meg1.addUt(ut1);
        meg1.addUt(ut2);
        meg1.addUt(ut5);
        meg1.addUt(ut6);

        meg2.addUt(ut2);
        meg2.addUt(ut3);
        meg2.addUt(ut4);
        meg2.addUt(ut5);

        veg2.addUt(ut3);
        veg2.addUt(ut4);

        // Busz létrehozása
        Busz busz = new Busz(veg1, veg2, 1, null, 1);
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);

        ArrayList megallok = new ArrayList();
        megallok.add(meg1);
        megallok.add(meg2);
        busz.setMegallokLista(megallok);

        ArrayList utvonal = new ArrayList();
        utvonal.add(ut1);
        utvonal.add(ut2);
        utvonal.add(ut3);
        utvonal.add(ut4);
        utvonal.add(ut5);
        utvonal.add(ut6);
        busz.setKijeloltUtvonal(utvonal);

        ArrayList utegysegLista = new ArrayList();
        utegysegLista.add(u11);
        utegysegLista.add(u12);
        utegysegLista.add(u21);
        utegysegLista.add(u22);
        utegysegLista.add(u31);
        utegysegLista.add(u32);
        utegysegLista.add(u41);
        utegysegLista.add(u42);
        utegysegLista.add(u51);
        utegysegLista.add(u52);
        utegysegLista.add(u61);
        utegysegLista.add(u62);

        hivas("busz:Busz", "utvonalatValaszt(utegysegLista)");
        busz.utvonalatValaszt(utegysegLista);
        visszater("busz:Busz", "void");

        // --- veg1 -> meg1 ---
        hivas("veg1:Csomopont", "jarmuTavozik(busz)");
        veg1.jarmuTavozik(busz);
        visszater("veg1:Csomopont", "void");

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u11:Utegyseg", "ralep(busz)");
        boolean sikeres = u11.ralep(busz);
        visszater("u11:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u11)");
            busz.sikeresLepes(u11);
            visszater("busz:Busz", "void");
        }

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u12:Utegyseg", "ralep(busz)");
        sikeres = u12.ralep(busz);
        visszater("u12:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u12)");
            busz.sikeresLepes(u12);
            visszater("busz:Busz", "void");
        }

        hivas("meg1:Csomopont", "jarmuErkezik(busz)");
        meg1.jarmuErkezik(busz);
        visszater("meg1:Csomopont", "void");

        hivas("busz:Busz", "megalloErintese(meg1)");
        busz.megalloErintese(meg1);
        visszater("busz:Busz", "void");

        // --- meg1 -> meg2 ---
        hivas("meg1:Csomopont", "jarmuTavozik(busz)");
        meg1.jarmuTavozik(busz);
        visszater("meg1:Csomopont", "void");

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u21:Utegyseg", "ralep(busz)");
        sikeres = u21.ralep(busz);
        visszater("u21:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u21)");
            busz.sikeresLepes(u21);
            visszater("busz:Busz", "void");
        }

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u22:Utegyseg", "ralep(busz)");
        sikeres = u22.ralep(busz);
        visszater("u22:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u22)");
            busz.sikeresLepes(u22);
            visszater("busz:Busz", "void");
        }

        hivas("meg2:Csomopont", "jarmuErkezik(busz)");
        meg2.jarmuErkezik(busz);
        visszater("meg2:Csomopont", "void");

        hivas("busz:Busz", "megalloErintese(meg2)");
        busz.megalloErintese(meg2);
        visszater("busz:Busz", "void");

        // --- meg2 -> veg2 ---
        hivas("meg2:Csomopont", "jarmuTavozik(busz)");
        meg2.jarmuTavozik(busz);
        visszater("meg2:Csomopont", "void");

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u31:Utegyseg", "ralep(busz)");
        sikeres = u31.ralep(busz);
        visszater("u31:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u31)");
            busz.sikeresLepes(u31);
            visszater("busz:Busz", "void");
        }

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u32:Utegyseg", "ralep(busz)");
        sikeres = u32.ralep(busz);
        visszater("u32:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u32)");
            busz.sikeresLepes(u32);
            visszater("busz:Busz", "void");
        }

        hivas("veg2:Csomopont", "jarmuErkezik(busz)");
        veg2.jarmuErkezik(busz);
        visszater("veg2:Csomopont", "void");

        hivas("busz:Busz", "megalloErintese(veg2)");
        busz.megalloErintese(veg2);
        visszater("busz:Busz", "void");

        // --- veg2 -> meg2 ---
        hivas("veg2:Csomopont", "jarmuTavozik(busz)");
        veg2.jarmuTavozik(busz);
        visszater("veg2:Csomopont", "void");

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u41:Utegyseg", "ralep(busz)");
        sikeres = u41.ralep(busz);
        visszater("u41:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u41)");
            busz.sikeresLepes(u41);
            visszater("busz:Busz", "void");
        }

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u42:Utegyseg", "ralep(busz)");
        sikeres = u42.ralep(busz);
        visszater("u42:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u42)");
            busz.sikeresLepes(u42);
            visszater("busz:Busz", "void");
        }

        hivas("meg2:Csomopont", "jarmuErkezik(busz)");
        meg2.jarmuErkezik(busz);
        visszater("meg2:Csomopont", "void");

        hivas("busz:Busz", "megalloErintese(meg2)");
        busz.megalloErintese(meg2);
        visszater("busz:Busz", "void");

        // --- meg2 -> meg1 ---
        hivas("meg2:Csomopont", "jarmuTavozik(busz)");
        meg2.jarmuTavozik(busz);
        visszater("meg2:Csomopont", "void");

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u51:Utegyseg", "ralep(busz)");
        sikeres = u51.ralep(busz);
        visszater("u51:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u51)");
            busz.sikeresLepes(u51);
            visszater("busz:Busz", "void");
        }

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u52:Utegyseg", "ralep(busz)");
        sikeres = u52.ralep(busz);
        visszater("u52:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u52)");
            busz.sikeresLepes(u52);
            visszater("busz:Busz", "void");
        }

        hivas("meg1:Csomopont", "jarmuErkezik(busz)");
        meg1.jarmuErkezik(busz);
        visszater("meg1:Csomopont", "void");

        hivas("busz:Busz", "megalloErintese(meg1)");
        busz.megalloErintese(meg1);
        visszater("busz:Busz", "void");

        // --- meg1 -> veg1 ---
        hivas("meg1:Csomopont", "jarmuTavozik(busz)");
        meg1.jarmuTavozik(busz);
        visszater("meg1:Csomopont", "void");

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u61:Utegyseg", "ralep(busz)");
        sikeres = u61.ralep(busz);
        visszater("u61:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u61)");
            busz.sikeresLepes(u61);
            visszater("busz:Busz", "void");
        }

        hivas("busz:Busz", "lep()");
        busz.lep();
        visszater("busz:Busz", "void");

        hivas("u62:Utegyseg", "ralep(busz)");
        sikeres = u62.ralep(busz);
        visszater("u62:Utegyseg", String.valueOf(sikeres));

        if (sikeres) {
            hivas("busz:Busz", "sikeresLepes(u62)");
            busz.sikeresLepes(u62);
            visszater("busz:Busz", "void");
        }

        hivas("veg1:Csomopont", "jarmuErkezik(busz)");
        veg1.jarmuErkezik(busz);
        visszater("veg1:Csomopont", "void");

        hivas("busz:Busz", "megalloErintese(veg1)");
        busz.megalloErintese(veg1);
        visszater("busz:Busz", "void");

        hivas("busz:Busz", "jutalomKiszamitasa()");
        int bevetel = busz.jutalomKiszamitasa();
        visszater("busz:Busz", String.valueOf(bevetel));

        hivas("ny:Nyilvantarto", "penzNovel(" + bevetel + ")");
        ny.penzNovel(bevetel);
        visszater("ny:Nyilvantarto", "void");

        tesztLezaras(
                "bevetel=" + bevetel
                        + ", erintettMegallokSzama=" + busz.getErintettLista().size()
                        + ", utvonalHossza=" + busz.getKijeloltUtvonal().size()
        );
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
        b.setNyilvantarto(ny);

        Busz busz = new Busz();
        Sebessegfejlesztes sf = new Sebessegfejlesztes(5, 5);


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
            sf.fejleszt(busz);

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
        b.setNyilvantarto(ny);

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

            hivas("u3:Utegyseg", "setJarmu(a2)");
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


        if (igenNemBeker("Van elég biokerozin?")) {
            ny.biokerozinLevon(0);

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

            System.out.println("=== Következő iteráció ===");
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
        tesztInditas("Jégre hulló hó");

        // Inicializálás
        Utegyseg u = new Utegyseg();

        hivas("u:Utegyseg", "havazas(mennyiseg)");
        if (igenNemBeker("Jeges az útegység? (I/N)")) {
            visszater("u:Utegyseg", "true");
            System.out.println("Jégmagasság beállítása 2-re a teszthez.");
            u.setJegMagassag(2);
            int jegMagassagElotte = u.getJegMagassag();
            int mennyiseg = egeszBeker("Mennyi hó hullik az útegységre?");
            u.havazas(mennyiseg);

            int jegMagassagUtana = u.getJegMagassag();
            System.out.println("[Jégmagasság változás: " + jegMagassagElotte + " -> " + jegMagassagUtana + "]");

            visszater("u:Utegyseg", "void");
            tesztLezaras("Sikeres: Jeges útegységen a hó jégréteget növelt.");
        } else {
            visszater("u:Utegyseg", "false");
            visszater("u:Utegyseg", "void");
            tesztLezaras("Sikertelen: Az útegység nem jeges, a jégréteg nem nőtt.");
        }
    }

    private void komplexVasarlasiLanc() {
        // TODO: A teszteset implementációja később kerül ide.
        tesztInditas("Komplex vásárlási lánc");

        // Inicializálás (kommunikációs diagram szerint)
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);
        hivas("ny:Nyilvantarto", "penzNovel(sok_penz)");
        ny.penzNovel(egeszBeker("Mennyi induló pénzt kapjon a kassza?"));
        visszater("ny:Nyilvantarto", "void");

        Bolt b = new Bolt(0, 0);
        hivas("b:Bolt", "setNyilvantarto(ny)");
        // A korábbi tesztekhez igazodva, ha nincs ilyen metódus, ez a sor kikommentelhető.
        b.setNyilvantarto(ny);
        visszater("b:Bolt", "void");

        Hokotro h = new Hokotro(new Sopro());
        Sarkany s = new Sarkany(ny);

        // 1) hókotró vásárlása
        hivas("b:Bolt", "hokotroVasarol()");
        if (igenNemBeker("Sikeres a penzLevon(ar) a hókotró vásárlásnál? (I/N)")) {
            visszater("ny:Nyilvantarto", "true");
            visszater("b:Bolt", "void");
        } else {
            visszater("ny:Nyilvantarto", "false");
            visszater("b:Bolt", "void");
            tesztLezaras("Sikertelen: Hókotró vásárlásnál nem volt elegendő pénz.");
            return;
        }

        // 2) fejcsere sárkány fejre
        hivas("b:Bolt", "fejCsere(s)");
        if (igenNemBeker("Sikeres a penzLevon(ar) a fejcserénél? (I/N)")) {
            visszater("ny:Nyilvantarto", "true");

            hivas("h:Hokotro", "setFej(s)");
            h.setFej(s);
            visszater("h:Hokotro", "void");

            visszater("b:Bolt", "void");
        } else {
            visszater("ny:Nyilvantarto", "false");
            visszater("b:Bolt", "void");
            tesztLezaras("Sikertelen: Fejcsere nem történt meg pénzhiány miatt.");
            return;
        }

        // 3) biokerozin vásárlása
        int mennyiseg = egeszBeker("Mennyi biokerozint vásároljon?");
        hivas("b:Bolt", "biokerozinVasarol(mennyiseg)");
        if (igenNemBeker("Sikeres a penzLevon(ar) a biokerozin vásárlásnál? (I/N)")) {
            visszater("ny:Nyilvantarto", "true");

            hivas("ny:Nyilvantarto", "biokerozinNovel(mennyiseg)");
            ny.biokerozinNovel(mennyiseg);
            visszater("ny:Nyilvantarto", "void");

            visszater("b:Bolt", "void");
            tesztLezaras("Sikeres: A komplex vásárlási lánc végrehajtva.");
        } else {
            visszater("ny:Nyilvantarto", "false");
            visszater("b:Bolt", "void");
            tesztLezaras("Sikertelen: Biokerozin vásárlásnál nem volt elegendő pénz.");
        }
    }

    private void megcsuszasJegen() {
        // TODO: A teszteset implementációja később kerül ide.
        tesztInditas("Megcsúszás jégen");

        // Inicializálás (kommunikációs diagram szerint)
        Utegyseg uJelenlegi = new Utegyseg();
        Utegyseg uJeges = new Utegyseg();
        Auto a = new Auto();

        hivas("uJelenlegi:Utegyseg", "setKovetkezoUtegyseg(uJeges)");
        uJelenlegi.setKovetkezoUtegyseg(uJeges);
        visszater("uJelenlegi:Utegyseg", "void");

        hivas("uJeges:Utegyseg", "jegesedes()");
        uJeges.jegesedes(2);
        visszater("uJeges:Utegyseg", "void");

        hivas("a:Auto", "setUtegyseg(uJelenlegi)");
        a.setUtegyseg(uJelenlegi);
        uJelenlegi.setJarmu(a);
        visszater("a:Auto", "void");

        // Szekvencia diagram szerinti lep() folyamat
        hivas("a:Auto", "lep()");

        hivas("uJelenlegi:Utegyseg", "getKovetkezoUtegyseg()");
        Utegyseg kov = uJelenlegi.getKovetkezoUtegyseg();
        visszater("uJelenlegi:Utegyseg", "uJeges");

        hivas("uJeges:Utegyseg", "ralep(a)");
        boolean ralepSikeres = kov.ralep(a);

        if (ralepSikeres) {
            visszater("uJeges:Utegyseg", "true");

            hivas("uJeges:Utegyseg", "megcsuszas()");
            if (igenNemBeker("Megcsúszik a jármű? (I/N)")) {
                visszater("uJeges:Utegyseg", "true");

                hivas("a:Auto", "csuszik()");
                a.csuszik();
                visszater("a:Auto", "void");

                visszater("uJeges:Utegyseg", "void");
                visszater("a:Auto", "void");

                tesztLezaras("Sikeres: A jármű megcsúszott a jeges útegységen.");
            } else {
                visszater("uJeges:Utegyseg", "false");
                visszater("uJeges:Utegyseg", "void");
                visszater("a:Auto", "void");

                tesztLezaras("Sikertelen: Nem történt megcsúszás.");
            }
        } else {
            visszater("uJeges:Utegyseg", "false");
            visszater("a:Auto", "void");
            tesztLezaras("Sikertelen: A jármű nem tudott rálépni a következő útegységre.");
        }
    }

    private void npcUtvonaltervezes() {
        // TODO: A teszteset implementációja később kerül ide.
        tesztInditas("NPC útvonaltervezés");

        // Inicializálás (kommunikációs diagram szerint)
        Terkep t = new Terkep();
        Csomopont kezdopont = new Csomopont();
        Csomopont celpont = new Csomopont();
        Utegyseg elsoUE = new Utegyseg();
        Utegyseg kovetkezoUE = new Utegyseg();
        Sav sav = new Sav(elsoUE);
        Ut ut = new Ut();
        Auto npc = new Auto();

        hivas("sav:Sav", "setElsoUtegyseg(elsoUE)");
        sav.setElsoUtegyseg(elsoUE);
        visszater("sav:Sav", "void");

        hivas("elsoUE:Utegyseg", "setKovetkezoUtegyseg(kovetkezoUE)");
        elsoUE.setKovetkezoUtegyseg(kovetkezoUE);
        visszater("elsoUE:Utegyseg", "void");

        hivas("elsoUE:Utegyseg", "setJarmu(npc)");
        elsoUE.setJarmu(npc);
        visszater("elsoUE:Utegyseg", "void");

        hivas("npc:Auto", "setUtegyseg(elsoUE)");
        npc.setUtegyseg(elsoUE);
        visszater("npc:Auto", "void");

        hivas("t:Terkep", "addUt(ut)");
        //t.addUt(ut);
        visszater("t:Terkep", "void");

        hivas("t:Terkep", "addCsomopont(kezdopont)");
        //t.addCsomopont(kezdopont);
        visszater("t:Terkep", "void");

        hivas("t:Terkep", "addCsomopont(celpont)");
        //t.addCsomopont(celpont);
        visszater("t:Terkep", "void");

        // Szekvenciadiagram szerinti főhívás
        hivas("npc:Auto", "utvonalKereses(t)");
        hivas("t:Terkep", "utvonalTervezes(kezdopont, celpont)");
        t.utvonalTervezes(kezdopont, celpont);
        visszater("t:Terkep", "utvonal");
        visszater("npc:Auto", "void");

        tesztLezaras("Sikeres: Az NPC útvonaltervezése lefutott.");
    }

    private void penzHozzaadasaAKasszahoz() {
        // TODO: A teszteset implementációja később kerül ide.
        tesztInditas("Pénz hozzáadása a kasszához");

        // Inicializálás (kommunikációs diagram szerint)
        Nyilvantarto n = new Nyilvantarto(0, 0, 0, 0);

        hivas("n:Nyilvantarto", "penzNovel(osszeg)");
        int osszeg = egeszBeker("Mennyi pénzt adsz hozzá a kasszához?");
        n.penzNovel(osszeg);
        visszater("n:Nyilvantarto", "void");

        tesztLezaras("Sikeres: A kassza egyenlege növelve lett.");
    }

    private void autoSikeresSavvaltasa() {
        tesztInditas("Autó sikeres sávváltása");

        /// Inicializálás
        Auto a = new Auto();
        Ut ut = new Ut();
        Sav s1 = new Sav();
        Sav s2 = new Sav();
        Utegyseg ueAkt = new Utegyseg();
        Utegyseg ueJobb = new Utegyseg();
        Utegyseg ueKov = new Utegyseg();

        // Egységek összekötése
        ut.addSav(s1);
        ut.addSav(s2);
        s1.setElsoUtegyseg(ueAkt);
        ueAkt.setKovetkezoUtegyseg(ueKov);
        ueAkt.setJobbUtegyseg(ueJobb);
        ueJobb.setBalUtegyseg(ueAkt);
        ueAkt.setJarmu(a);
        a.setUtegyseg(ueAkt);

        // Az ueAkt-ról lépne előre az ueKov-re, de nem tud.
        // Így sávot akar váltani, ha true a válasz típusa, az ueJobb útegységre megy át,
        // ha false, akkor elakad (mert nincs több szomszédos útegység, ami szabad lenne).

        ueKov.setJarmu(new Auto());
        hivas("a:Auto", "lep()");
        a.lep();
        visszater("a:Auto", "void"); // lep vége

        hivas("ueKov:Utegyseg", "ralep(a)");
        ueKov.ralep(a);
        visszater("ueKov:Utegyseg", "false");

        if (igenNemBeker("Sikeres legyen a sávváltás? (Szabad a jobb oldali sáv?)")) {
            hivas("a:Auto", "savValtas()");
            a.savValtas();

            // A savValtas() belülről hívja a getJobbUtegyseg()-et
            hivas("ueAkt:Utegyseg", "getJobbUtegyseg()");
            visszater("ueAkt:Utegyseg", "ueJobb");

            hivas("a:Auto", "sikeresLepes(ueJobb)");

            // A sikeresLepes() frissíti a referenciákat
            hivas("ueAkt:Utegyseg", "setJarmu(null)");
            visszater("ueAkt:Utegyseg", "void");

            hivas("ueJobb:Utegyseg", "setJarmu(a)");
            visszater("ueJobb:Utegyseg", "void");

            visszater("a:Auto", "void"); // sikeresLepes vége
            visszater("a:Auto", "void"); // savValtas vége

            tesztLezaras("Sikeres, a sávváltás megtörtént, így az autó a jobb oldali sávba ment át.");

        } else {
            // Ha a felhasználó szerint nem sikeres (pl. foglalt a jobb sáv is)
            ueJobb.setBlokkolt(true);
            hivas("a:Auto", "savValtas()");
            a.savValtas();

            hivas("a:Auto", "elakad()");
            a.elakad();
            visszater("a:Auto", "void");

            visszater("a:Auto", "void"); // savValtas vége

            tesztLezaras("Sikertelen, a sávváltás nem sikerült, így az autó20 elakadt.");
        }
    }

    private void melyHobanElakadas() {
        tesztInditas("Mély hóban elakadás");

        /// Inicializálás
        Busz b = new Busz();
        Ut ut = new Ut();
        Sav s1 = new Sav();
        Sav s2 = new Sav();
        Sav s3 = new Sav();
        Utegyseg ueAkt = new Utegyseg();
        Utegyseg ueKov = new Utegyseg();
        Utegyseg ueJobb = new Utegyseg();
        Utegyseg ueBal = new Utegyseg();

        // Egységek összekötése
        ut.addSav(s1);
        ut.addSav(s2);
        ut.addSav(s3);
        s1.setElsoUtegyseg(ueAkt);
        s2.setElsoUtegyseg(ueJobb);
        s3.setElsoUtegyseg(ueBal);
        ueAkt.setKovetkezoUtegyseg(ueKov);
        ueKov.setJobbUtegyseg(ueJobb);
        ueKov.setBalUtegyseg(ueBal);
        ueAkt.setJarmu(b);
        b.setUtegyseg(ueAkt);

        hivas("b:Busz", "lep()");
        b.lep();
        visszater("b:Busz", "void");

        System.out.println("A hó elakadás küszöbe: " + ueKov.getHoElakadasKuszob());

        if(igenNemBeker("Elérte a hómagasság a küszöböt a következő útegységen?")){
            ueKov.setHoMagassag(20);
            System.out.println("A hómagasság most: " + ueKov.getHoMagassag());

            hivas("ueKov:Utegyseg", "ralep(b)");
            ueKov.ralep(b);
            visszater("ueKov:Utegyseg", "true");

            if(igenNemBeker("Tud a busz sávot váltani? (Szabad valamelyik szomszédos sáv?)")){
                ueJobb.setBlokkolt(false);
                ueJobb.setJarmu(null);

                hivas("b:Busz", "savValtas()");
                b.savValtas();
                visszater("b:Busz", "void");

                tesztLezaras("Sikertelen, a busz elakadt az eredeti sávban, de sávot váltott a jobboldalira.");

            }else{
                ueJobb.setBlokkolt(true);
                ueBal.setBlokkolt(true);

                hivas("b:Busz", "savValtas()");
                b.savValtas();
                visszater("b:Busz", "void");

                tesztLezaras("Sikeres, a busz mély hóba futott és nem tudott sávot váltani, mert a szomszédok is blokkoltak.");
            }

        }else{
            ueKov.setHoMagassag(5);
            System.out.println("A hómagasság most: " + ueKov.getHoMagassag());

            hivas("ueKov:Utegyseg", "ralep(b)");
            ueKov.ralep(b);
            visszater("ueKov:Utegyseg", "true");

            hivas("b:Busz", "sikeresLepes(ueKov)");
            b.sikeresLepes(ueKov);
            visszater("b:Busz", "void");

            tesztLezaras("Sikertelen, a hó nem volt elég mély az elakadáshoz, a busz továbbhaladt.");
        }
    }

    private void jatekVege() {
        tesztInditas("Játék vége");

        /// Inicializálás
        Nyilvantarto ny = new Nyilvantarto(0, 0, 0, 0);

        hivas("ny:Nyilvantarto", "ellenorizJatekVege()");
        if(igenNemBeker("Teljesült a vereségi feltétel?")){
            ny.setNemBeertAutokSzama(20);
            ny.ellenorizJatekVege();
            visszater("ny:Nyilvantarto", "true");
            tesztLezaras("Sikeres, a játék véget ért");
        }else{
            ny.setNemBeertAutokSzama(5);
            ny.ellenorizJatekVege();
            visszater("ny:Nyilvantarto", "false");
            tesztLezaras("Sikertelen, a játék nem ért véget");
        }

    }

    private void buszmegalloErintese() {
        tesztInditas("Buszmegálló érintése");

        /// Inicializálás
        Busz b = new Busz();
        Ut ut = new Ut();
        Sav s = new Sav();
        Utegyseg ueAkt = new Utegyseg();
        Utegyseg ueUtolso = new Utegyseg();
        Csomopont bm = new Csomopont();

        // Egységek összekötése
        ut.addSav(s);
        ut.setVegpont2(bm);
        s.setElsoUtegyseg(ueAkt);
        ueAkt.setKovetkezoUtegyseg(ueUtolso);
        bm.addUt(ut);
        ueAkt.setJarmu(b);
        b.setUtegyseg(ueAkt);

        hivas("b:Busz", "lep()");
        b.lep();
        visszater("b:Busz","void");

        hivas("ueKov:Utegyseg", "ralep(b)");
        ueUtolso.ralep(b);
        visszater("ueKov:Utegyseg","true");

        if(igenNemBeker("A csomópont egy megálló?")){
            bm.setBuszmegallo(true);

            hivas("bm:Csomopont", "jarmuErkezik(b)");
            bm.jarmuErkezik(b);

            hivas("b:Busz", "megalloErintese(bm)");
            b.megalloErintese(bm);
            visszater("b:Busz", "void");

            visszater("bm:Csomopont", "void");

            tesztLezaras("Sikeres, a busz megállót érintett");
        }else{
            hivas("bm:Csomopont", "jarmuErkezik(b)");
            bm.jarmuErkezik(b);

            hivas("b:Busz", "megalloErintese(null)");
            b.megalloErintese(null);
            visszater("b:Busz", "void");

            visszater("bm:Csomopont", "void");

            tesztLezaras("Sikertelen, a busz nem érintett megállót");
        }

    }

    private void jegkepzodesTaposasMiatt() {
        tesztInditas("Jégképződés taposás miatt");

        /// Inicializálás
        Busz b = new Busz();
        Ut ut = new Ut();
        Sav s = new Sav();
        Utegyseg ueAkt = new Utegyseg();
        Utegyseg ueKov = new Utegyseg();

        // Egységek összekötése
        ut.addSav(s);
        s.setElsoUtegyseg(ueAkt);
        ueAkt.setKovetkezoUtegyseg(ueKov);
        ueAkt.setJarmu(b);
        b.setUtegyseg(ueAkt);
        ueKov.setHoMagassag(10);

        hivas("b:Busz", "lep()");
        b.lep();
        visszater("b:Busz","void");

        hivas("ueKov:Utegyseg", "ralep(b)");
        ueKov.ralep(b);

        System.out.println("A letaposottság küszöbe: " + ueKov.getLetaposottsagKuszob());
        System.out.println("A letaposottság most: " + ueKov.getLetaposottsag());

        if(igenNemBeker("Elérte a letaposottság a küszöböt?")){
            hivas("ueKov:Utegyseg","taposodas(10)");

            ueKov.taposodas(10);
            visszater("ueKov:Utegyseg","void");
            visszater("ueKov:Utegyseg","true");

            System.out.println("A jég magassága: " + ueKov.getJegMagassag());

            tesztLezaras("Sikeres, jeges lett az útegység");
        }else{
            hivas("ueKov:Utegyseg","taposodas(1)");
            ueKov.taposodas(1);
            visszater("ueKov:Utegyseg","void");
            visszater("ueKov:Utegyseg","true");

            System.out.println("A jég magassága " + ueKov.getJegMagassag());

            tesztLezaras("Sikertelen, nem lett jeges az útegység");
        }
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
