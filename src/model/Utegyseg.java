package model;

import java.util.List;
import java.util.Locale;
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

    @Override
    public void parancsFeldolgozUtegyseggel(String parancs, Utegyseg utegyseg, List<String> args) {
        if (parancs.equalsIgnoreCase("set"))
        {
            String arg = args.get(0).toLowerCase();
            switch(arg) {
                case "kovetkezoutegyseg":
                    utegyseg.setKovetkezoUtegyseg(this);
                    break;

                case "balutegyseg":
                    utegyseg.setBalUtegyseg(this);
                    break;

                case "jobbutegyseg":
                    utegyseg.setJobbUtegyseg(this);
                    break;

                default:
                    System.out.println("Ismeretlen tulajdonsag: " + arg);
                    break;
            }}
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
        if(jarmu != null){
            this.jarmu = jarmu;
        }

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

    @Override
    public void parancsFeldolgozHokotroval(String parancs, Hokotro hokotro, List<String> args) {
        if ("add".equals(parancs) && args.get(0).equals("tervezettUtvonal")) {
            hokotro.addTervezettUtegyseg(this);
        }
    }

    ///További metódusok
    /**
     * Növeli a hóréteg vastagságát a mezőn a szimulált időjárás hatására.
     *
     * @param mennyiseg Az a hómennyiség, amivel nő az útegységen lévő hóréteg.
     */
    public void havazas(int mennyiseg) {
        // Ha sós az út, a hó nem tud leesni rá.
        if (soMennyiseg > 0) {
            return;
        }

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
        if (soMennyiseg <= 0) {
            return;
        }

        if (this.hoMagassag > 0) {
            this.hoMagassag--;
        } else if (this.jegMagassag > 0) {
            this.jegMagassag--;
            if(jegMagassag <= 0) {
                this.jeges = false;
            }
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

        if(this.jarmu != null) {
            return false;
        }

        this.jarmu = j;

        // A járműnek átadjuk a 'this' referenciát, hogy tudja, melyik útegységen áll
        j.sikeresLepes(this);
        this.taposodas();

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

                if(!args.get(0).equals("ho") && !args.get(0).equals("jeg")) {
                    return;
                }

                String type = args.get(0);
                String amount = args.get(1);

                switch (type){
                    case "ho":
                        try {
                            havazas(Integer.parseInt(amount));
                        } catch (NumberFormatException ignored) {
                            return;
                        }
                        break;
                    case "jeg":
                        try {
                            havazas(Integer.parseInt(amount));
                            jegesedes();
                        } catch (NumberFormatException ignored) {
                            return;
                        }
                        break;
                    default:
                        break;
                }
                break;

            case "set":
                if (args.size() < 2) return;
                String tulajdonsag = args.get(0).toLowerCase();
                String ertek = args.get(1);

                try {
                    switch (tulajdonsag) {
                        case "letaposottsag":
                            this.letaposottsag = Integer.parseInt(ertek);
                            break;
                        case "befedettseg":
                            this.befedettseg = Integer.parseInt(ertek);
                            break;
                        case "homagassag":
                            this.hoMagassag = Integer.parseInt(ertek);
                            break;
                        case "jegmagassag":
                            this.jegMagassag = Integer.parseInt(ertek);
                            break;
                        case "somennyiseg":
                            this.soMennyiseg = Integer.parseInt(ertek);
                            break;

                        case "megcsuszasesely":
                            this.megcsuszasEsely = Double.parseDouble(ertek);
                            break;

                        case "blokkolt":
                            this.blokkolt = Boolean.parseBoolean(ertek);
                            break;
                        case "zuzalek":
                            this.zuzalek = Boolean.parseBoolean(ertek);
                            break;
                        case "jeges":
                            this.jeges = Boolean.parseBoolean(ertek);
                            break;

                        default:
                            break;
                    }
                } catch (NumberFormatException e) {
                // Itt kapjuk el, ha a parseInt vagy parseDouble elszállt
                System.out.println("Hiba: ÉrvÉnytelen számformátum!");
                }
                break;

            default:
                break;
        }
    }

    /**
     * Adatok kiírásához, naplózásához szükséges
     * @param id Az entitás azonosítója, amiről összegyűjti az adatot egy string-be
     * @param katalogus A nyilvántartó, amiben az objektumok vannak
     * @return Az entitás adatai egy stringben
     */
    public String info(String id, ObjektumKatalogus katalogus){
        String jarmuId = katalogus.getId(this.jarmu);
        String kovetkezoId = katalogus.getId(this.kovetkezoUtegyseg);
        String balId = katalogus.getId(this.balUtegyseg);
        String jobbId = katalogus.getId(this.jobbUtegyseg);

        String infoKimenet = String.format(
            Locale.ROOT,
            "%s:\n" +
                "hoMagassag: %d\n" +
                "jegMagassag: %d\n" +
                "soMennyiseg: %d\n" +
                "letaposottsag: %d\n" +
                "megcsuszasEsely: %.1f\n" +
                "blokkolt: %b\n" +
                "zuzalek: %b\n" +
                "jeges: %b\n" +
                "befedesSzamlalo: %d\n" +
                "jarmu: %s\n" +
                "kovetkezoUtegyseg: %s\n" +
                "balUtegyseg: %s\n" +
                "jobbUtegyseg: %s\n",
                id,
                this.hoMagassag,
                this.jegMagassag,
                this.soMennyiseg,
                this.letaposottsag,
                this.megcsuszasEsely,
                this.blokkolt,
                this.zuzalek,
                this.jeges,
                this.befedettseg,
                jarmuId,
                kovetkezoId,
                balId,
                jobbId
        );
        return infoKimenet;
    }
}

