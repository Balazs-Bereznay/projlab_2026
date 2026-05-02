package model;

import java.util.List;
import java.util.Random;

/**
 * A pálya legkisebb, hálószerűen összekapcsolt felépítési egysége.
 * Felelős az időjárás hatására rajta keletkező és eltűnő természeti hatások
 * feljegyzésére (hó, jég), illetve a rajta álló járművek, és a környező egységek nyilvántartására.
 */
public class Utegyseg implements ProtoEntitas{
    private static final int HO_ELAKADAS_KUSZOB = 15;
    private static final int LETAPOSOTTSAG_KUSZOB = 5;
    private static final int BEFEDES_KUSZOB = 5;

    private int letaposottsag;
    private int befedettseg;
    private double megcsuszasEsely;
    private Jarmu jarmu;
    private Utegyseg kovetkezoUtegyseg;
    private Utegyseg balUtegyseg;
    private Utegyseg jobbUtegyseg;
    private int hoMagassag;
    private int jegMagassag;
    private boolean blokkolt;
    private int soMennyiseg;
    private boolean zuzalek;
    private boolean jeges;

    /// Konstruktorok
    public Utegyseg(int letaposottsag, double megcsuszasEsely, Jarmu jarmu,
                    Utegyseg kovetkezoUtegyseg, Utegyseg balUtegyseg, Utegyseg jobbUtegyseg,
                    int hoMagassag, int jegMagassag, boolean blokkolt, int soMennyiseg) {
        this.letaposottsag = letaposottsag;
        this.megcsuszasEsely = megcsuszasEsely;
        this.jarmu = jarmu;
        this.kovetkezoUtegyseg = kovetkezoUtegyseg;
        this.balUtegyseg = balUtegyseg;
        this.jobbUtegyseg = jobbUtegyseg;
        this.hoMagassag = hoMagassag;
        this.jegMagassag = jegMagassag;
        this.blokkolt = blokkolt;
        this.soMennyiseg = soMennyiseg;
    }

    public Utegyseg() {
        this(0, 0.0, null, null, null, null, 0, 0, false, 0);
    }

    @Override
    public void parancsFeldolgoz(String parancs, ProtoEntitas masik, List<String> args) {
        masik.parancsFeldolgozUtegyseggel(parancs, this, args);
    }


    /// Getterek és setterek
    /**
     * Visszatér a hoElakadasKuszob statikus tagváltozó értékével.
     */
    public static int getHoElakadasKuszob() {
        return HO_ELAKADAS_KUSZOB;
    }

    /**
     * Visszatér a letaposottsagKuszob statikus tagváltozó értékével.
     */
    public static int getLetaposottsagKuszob() {
        return LETAPOSOTTSAG_KUSZOB;
    }

    /**
     * Visszatér a befedesKuszob statikus tagváltozó értékével.
     */
    public static int getBefedesKuszob() {
        return BEFEDES_KUSZOB;
    }

    public int getLetaposottsag() {
        return letaposottsag;
    }

    public void setLetaposottsag(int letaposottsag) {
        this.letaposottsag = letaposottsag;
    }

    public double getMegcsuszasEsely() {
        return megcsuszasEsely;
    }

    public void setMegcsuszasEsely(double megcsuszasEsely) {
        this.megcsuszasEsely = megcsuszasEsely;
    }

    public Jarmu getJarmu() {
        return jarmu;
    }

    public void setJarmu(Jarmu jarmu) {
        this.jarmu = jarmu;
    }

    public Utegyseg getKovetkezoUtegyseg() {
        return kovetkezoUtegyseg;
    }

    public void setKovetkezoUtegyseg(Utegyseg kovetkezoUtegyseg) {
        this.kovetkezoUtegyseg = kovetkezoUtegyseg;
    }

    public Utegyseg getBalUtegyseg() {
        return balUtegyseg;
    }

    public void setBalUtegyseg(Utegyseg balUtegyseg) {
        this.balUtegyseg = balUtegyseg;
    }

    public Utegyseg getJobbUtegyseg() {
        return jobbUtegyseg;
    }

    public void setJobbUtegyseg(Utegyseg jobbUtegyseg) {
        this.jobbUtegyseg = jobbUtegyseg;
    }

    public int getHoMagassag() {
        return hoMagassag;
    }

    public void setHoMagassag(int hoMagassag) {
        this.hoMagassag = hoMagassag;
    }

    public int getJegMagassag() {
        return jegMagassag;
    }

    public void setJegMagassag(int jegMagassag) {
        this.jegMagassag = jegMagassag;
    }

    public boolean getBlokkolt() {
        return blokkolt;
    }

    /// ez a getFoglalt egyes diagramokon
    public void setBlokkolt(boolean blokkolt) {
        this.blokkolt = blokkolt;
    }

    public int getSoMennyiseg() {
        return soMennyiseg;
    }

    public void setSoMennyiseg(int soMennyiseg) {
        this.soMennyiseg = soMennyiseg;
    }

    /**
     * Visszatér a zuzalek tagváltozó értékével.
     */
    public boolean getZuzalek() {
        return zuzalek;
    }

    /**
     * A zuzalek nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public void setZuzalek(boolean zuzalek) {
        this.zuzalek = zuzalek;
    }

    /**
     * Visszatér a jeges tagváltozó értékével.
     */
    public boolean getJeges() {
        return jeges;
    }

    /**
     * A jeges nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public void setJeges(boolean jeges) {
        this.jeges = jeges;
    }

    @Override
    public void parancsFeldolgozJarmuvel(String parancs, Jarmu jarmu, List<String> args) {
        if ("assign".equals(parancs)) {
            this.jarmu = jarmu;
            jarmu.utegyseg = this;
            System.out.println("Jármű sikeresen az útegységre helyezve.");
        } else if ("remove".equals(parancs)) {
            if (this.jarmu == jarmu) {
                jarmu.utegyseg = null;
                this.jarmu = null;
                System.out.println("Jármű eltávolítva az útegységről.");
            }
        }
    }

    @Override
    public void parancsFeldolgozSavval(String parancs, Sav sav, List<String> args) {
        if ("assign".equals(parancs)) {
            sav.setElsoUtegyseg(this);
            System.out.println("Útegység sikeresen a sávhoz rendelve.");
        } else if ("remove".equals(parancs)) {
            if (sav.getElsoUtegyseg() == this) {
                sav.setElsoUtegyseg(null);
                System.out.println("Útegység eltávolítva a sávból.");
            }
        }
    }

    ///További metódusok
    /**
     * Növeli a hóréteg vastagságát a mezőn a szimulált időjárás hatására.
     *
     * @param mennyiseg Az a hómennyiség, amivel nő az útegységen lévő hóréteg.
     */
    public void havazas(int mennyiseg) {
        //Ha nincs só az úton, ami olvasztaná a havat
        if (soMennyiseg == 0) {
            if (this.jeges) {
                // Ha jeges az út, a jégmagasság nő
                this.jegMagassag += mennyiseg;
            } else {
                // Ha nem jeges, a hómagasság nő
                this.hoMagassag += mennyiseg;

                if (hoMagassag >= HO_ELAKADAS_KUSZOB) {
                    blokkolt = true;
                }

                // Ha van kint zúzalék, vizsgálni kell a befedettséget
                if (this.zuzalek) {
                    this.befedettseg += mennyiseg;

                    // Ha a befedettség eléri vagy meghaladja a statikus küszöbértéket
                    if (this.befedettseg >= BEFEDES_KUSZOB) {
                        this.zuzalek = false;
                        this.befedettseg = 0;
                    }
                }
            }
        }
    }

    /**
     * Növeli az útegységen található só vastagságát.
     *
     * @param mennyiseg Az a sómennyiség, amivel nő az útegységen lévő sóréteg.
     */
    public void sozas(int mennyiseg) {
        soMennyiseg += mennyiseg;
    }

    /**
     * A hó jéggé alakulását szimuláló metódus.
     * A meglévő havat jéggé alakítja, és ha nincs zúzalék, jegesnek jelöli az utat.
     */
    public void jegesedes() {
        // A meglévő hómagasságot hozzáadjuk a jégmagassághoz
        this.jegMagassag += this.hoMagassag;

        // A hómagasságot és a letaposottságot nullázzuk
        this.hoMagassag = 0;
        this.letaposottsag = 0;

        // Ha nincs zúzalék az úton, akkor az útfelület jeges állapotba kerül
        if (!this.zuzalek) {
            this.jeges = true;
        }
    }

    /**
     * A járművek áthaladása tömöríti a havat, ami később jégréteg kialakulásához vezethet.
     */
    public void taposodas() {
        if (this.hoMagassag > 0) {
            this.letaposottsag++;

            if (this.letaposottsag == LETAPOSOTTSAG_KUSZOB) {
                jegesedes();
            }
        } else {
            // Ha nincs hó a számláló nullázódik
            this.letaposottsag = 0;
        }
    }

    /**
     * A jégtörő fej hatására lenullázza a jégvastagságot az adott
     * útegységen és a modell szerint "hó" ként fog viselkedni.
     */
    public void jegtores() {
        this.hoMagassag += this.jegMagassag;

        if (hoMagassag > HO_ELAKADAS_KUSZOB) {
            blokkolt = true;
        }

        this.jegMagassag = 0;

        this.jeges = false;
    }

    /**
     * A só hatására csökkenti a hó vagy jégvastagság méretét.
     */
    public void soOlvasztas() {
        if (this.hoMagassag > 0) {
            this.hoMagassag--;
        } else if (this.jegMagassag > 0) {
            this.jegMagassag--;
        }

        this.soMennyiseg--;

        if (hoMagassag < HO_ELAKADAS_KUSZOB) {
            blokkolt = false;
        }
    }

    /**
     * A hókotró munkájának eredményeként eltávolítja a csapadékot a mezőről.
     */
    public void tisztulas() {
        this.hoMagassag = 0;
        this.zuzalek = false;
        blokkolt = false;

        // Ha maradt jég az úton, az útfelületet jegesnek jelöljük
        if (this.jegMagassag > 0) {
            this.jeges = true;
        }
    }

    /**
     * Kiszámítja és eldönti, hogy a jármű megcsúszik-e az útegységen.
     * A döntés a jegesedéstől, az út alap esélyétől és a jármű tapadásától függ.
     * @return igaz, ha bekövetkezik a megcsúszás, egyébként hamis.
     */
    public boolean megcsuszas() {
        if (!this.jeges) {
            return false;
        }

        // Feltételezzük, hogy a 'jarmu' változó létezik és nem null
        int t = this.jarmu.getTapadas();

        // A (100 - t) / 100.0 biztosítja, hogy lebegőpontos osztást végezzünk
        double aktualisEsely = this.megcsuszasEsely * (100 - t) / 100.0;

        Random rand = new Random();
        int veletlenSzam = rand.nextInt(101);

        return veletlenSzam < aktualisEsely;
    }

    /**
     * Kezeli a jármű útegységre történő rálépését.
     * @param j A belépni kívánó jármű.
     * @return Igaz, ha a jármű sikeresen rálépett az egységre, hamis ha az egység blokkolt.
     */
    public boolean ralep(Jarmu j) {
        if (j == null) {return false;}

        if (this.blokkolt) {
            j.elakad(); // Jelezzük a járműnek, hogy nem tud továbbhaladni
            return false;
        }

        this.jarmu = j;

        // A járműnek átadjuk a 'this' referenciát, hogy tudja, melyik útegységen áll
        j.sikeresLepes(this);

        if (this.megcsuszas()) {
            j.csuszik();
        }

        // A rálépés mindenképpen sikeres (igaz), ha nem volt blokkolt az út
        return true;
    }

    /**
     * Feldolgozza az útegységre érkező, egyszerű prototípus-parancsokat.
     *
     * @param parancs a feldolgozandó parancs neve
     * @param args a parancs további paraméterei
     */
    @Override
    public void parancsFeldolgoz(String parancs, List<String> args) {
        if (parancs == null || args == null) {
            return;
        }

        switch (parancs) {
            case "add_condition":
                if (args.size() < 2) {
                    return;
                }

                if(!args.get(0).equals("hó") && !args.get(0).equals("jég")) {
                    return;
                }

                String type = args.get(0);
                String amount = args.get(1);

                switch (type){
                    case "hó":
                        try {
                            setHoMagassag(Integer.parseInt(amount));
                        } catch (NumberFormatException ignored) {
                            return;
                        }
                        break;
                    case "jég":
                        try {
                            setHoMagassag(Integer.parseInt(amount));
                            jegesedes();
                        } catch (NumberFormatException ignored) {
                            return;
                        }
                        break;
                    default:
                        break;
                }
            case "info":
                String currentId = args.get(0);

                String jarmuStr = (this.jarmu != null) ? this.jarmu.toString() : "null";
                String kovetkezoStr = (this.kovetkezoUtegyseg != null) ? this.kovetkezoUtegyseg.toString() : "null";
                String balStr = (this.balUtegyseg != null) ? this.balUtegyseg.toString() : "null";
                String jobbStr = (this.jobbUtegyseg != null) ? this.jobbUtegyseg.toString() : "null";

                String infoKimenet = """
                    %s:
                    hoMagassag: %d
                    jegMagassag: %d
                    soMennyiseg: %d
                    letaposottsag: %d
                    megcsuszasEsely: %.1f
                    blokkolt: %b
                    zuzalek: %b
                    jeges: %b
                    befedesLimit: %d
                    befedesSzamlalo: %d
                    jarmu: %s
                    kovetkezoUtegyseg: %s
                    balUtegyseg: %s
                    jobbUtegyseg: %s
                    """.formatted(
                        currentId,
                        this.hoMagassag,
                        this.jegMagassag,
                        this.soMennyiseg,
                        this.letaposottsag,
                        this.megcsuszasEsely,
                        this.blokkolt,
                        this.zuzalek,
                        this.jeges,
                        getBefedesKuszob(),
                        this.befedettseg,
                        jarmuStr,
                        kovetkezoStr,
                        balStr,
                        jobbStr
                );

                System.out.print(infoKimenet);
                System.out.println("Info displayed");
                break;
            case "list_shop":
            case "load":
            case "save":
            case "set_random":
            case "tick":
            case "create":
            case "delete":
            case "assign":
            case "remove":
            case "set":
            case "move":
            case "clean":
            case "purchase":
            case "list":
            case "help":
            case "add":
                break;
            default:
                break;
        }
    }
}

