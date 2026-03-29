package model;

/**
 * A pálya legkisebb, hálószerűen összekapcsolt felépítési egysége.
 * Felelős az időjárás hatására rajta keletkező és eltűnő természeti hatások
 * feljegyzésére (hó, jég), illetve a rajta álló járművek, és a környező egységek nyilvántartására.
 */
public class Utegyseg {
    private static final int HO_ELAKADAS_KUSZOB = 15;
    private static final int LETAPOSOTTSAG_KUSZOB = 5;

    private int letaposottsag;
    private double megcsuszasEsely;
    private Jarmu jarmu;
    private Utegyseg kovetkezoUtegyseg;
    private Utegyseg balUtegyseg;
    private Utegyseg jobbUtegyseg;
    private int hoMagassag;
    private int jegMagassag;
    private boolean blokkolt;
    private int soMennyiseg;

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

    /// Getterek és setterek
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

    ///További metódusok
    /**
     * Növeli a hóréteg vastagságát a mezőn a szimulált időjárás hatására.
     *
     * @param mennyiseg Az a hómennyiség, amivel nő az útegységen lévő hóréteg.
     */
    public void havazas(int mennyiseg) {
        System.out.println("Hómagasság növelése az útegységen " + mennyiseg + " mennyiségű hóval.");
        ///Ha nincs só az úton, ami olvasztaná a havat
        if (soMennyiseg == 0) {
            if (jegMagassag > 0)
                jegesedes(mennyiseg);
            else {
                hoMagassag += mennyiseg;
            }
        }
    }

    /**
     * Növeli az útegységen található só vastagságát.
     *
     * @param mennyiseg Az a sómennyiség, amivel nő az útegységen lévő sóréteg.
     */
    public void sozas(int mennyiseg) {
        System.out.println("Sómennyiség növelése az útegységen " + mennyiseg + " mennyiségű sóval.");
        soMennyiseg += mennyiseg;
    }

    /**
     * Növeli a jégréteg vastagságát ha  a letaposottság értéke elér
     * egy adott értéket, illetve ha további hó esik a jégre.
     *
     * @param mennyiseg Ennyivel nő meg a jégréteg az útegységen.
     */
    public void jegesedes(int mennyiseg) {
        if (soMennyiseg == 0) {
            jegMagassag += mennyiseg;
            ///Ha már nincs hó, akkor nem kell újra nullává állítani a szintjét
            if (hoMagassag != 0)
                hoMagassag = 0;
            System.out.println("Jegesedés: " + mennyiseg + " mennyiségű jéggel nőtt meg a jégmagasság.");
            System.out.println("A hómagasság 0.");
        }
    }

    /**
     * A járművek áthaladása tömöríti a havat, ami később jégréteg kialakulásához vezethet.
     */
    public void taposodas(int mertek) {
        if (hoMagassag > 0) {
            letaposottsag += mertek;
            System.out.println("Taposás: " + mertek + " mértékkel lett letaposva az út.");
            if (letaposottsag >= LETAPOSOTTSAG_KUSZOB) {
                ///Ha taposás miatt lesz jég, akkor olyan nagy lesz, mint az ott lévő letaposott hó
                System.out.println("Elérte a küszöböt a letaposottság mértéke.");
                jegesedes(hoMagassag);
                letaposottsag = 0;
            }
        }
    }

    /**
     * A jégtörő fej hatására lenullázza a jégvastagságot az adott
     * útegységen és a modell szerint "hó" ként fog viselkedni.
     */
    public void jegtores() {
        hoMagassag = jegMagassag;
        jegMagassag = 0;
        if (hoMagassag < HO_ELAKADAS_KUSZOB) {
            System.out.println("A jégből kevés hó lett.");
        } else {
            System.out.println("A jégből mély hó lett.");
            blokkolt = true;
        }

    }

    /**
     * A só hatására csökkenti a hó vagy jégvastagság méretét.
     */
    public void soOlvasztas() {
        if (soMennyiseg > 0) {
            soMennyiseg--;
            System.out.println("Sómennyiség csökken.");
            if (hoMagassag > 0) {
                hoMagassag--;
                System.out.println("Hómagasság csökken.");
            } else if (jegMagassag > 0) {
                jegMagassag--;
                System.out.println("Jégmagasság csökken.");
            }
        }
        if (hoMagassag < HO_ELAKADAS_KUSZOB && jegMagassag < HO_ELAKADAS_KUSZOB) {
            blokkolt = false;
            System.out.println("Nincs blokkolva az úttest.");
        }
    }

    /**
     * A hókotró munkájának eredményeként eltávolítja a csapadékot a mezőről.
     */
    public void tisztulas() {
        hoMagassag = 0;
        jegMagassag = 0;
        blokkolt = false;
        System.out.println("Az útegység tiszta lett.");
    }

    /**
     * Visszaadja, hogy jeges útegység esetén a jármű megcsúszott-e vagy sem.
     *
     * @return Visszaadja, hogy megcsúszott-e az útegységen lévő jármű.
     */
    public boolean megcsuszas() {
        if (jegMagassag > 0 && (Math.random() < megcsuszasEsely)) {
            System.out.println("A jármű megcsúszott.");
            return true;
        }
        System.out.println("A jármű nem csúszott meg.");
        return false;
    }

    /**
     * Regisztrálja, hogy egy jármű megérkezett egy adott útegységre, kiválasztva
     * az esetleges interakciókat (megcsúszás, elakadás).
     *
     * @param jarmu Az a jármű, ami rálép az útegységre.
     * @return true, ha a jármű sikeresen rálépett az útegységre,
     * false, ha a rálépés meghiúsult (mert a sáv foglalt vagy blokkolva van).
     */
    public boolean ralep(Jarmu jarmu) {
        System.out.println("-> Utegyseg.ralep(" + jarmu.getClass().getSimpleName() + ") meghívva");

        /// Ha a sáv már blokkolva van, vagy fizikailag áll rajta egy másik jármű,
        /// senki nem léphet rá (még a hókotró sem mehet "át" rajta).
        if (this.blokkolt || this.jarmu != null) {
            System.out.println("A rálépés sikertelen: az útegység foglalt vagy blokkolva van.");
            return false;
        }

        /// A jármű fizikailag rálép az útegységre (lefoglalja a referenciát)
        jarmu.sikeresLepes(this);
        System.out.println("Az útegység referenciája frissítve, a járművet fogadta.");

        /// A Hókotrókra nem vonatkoznak az időjárás negatív hatásai
        if (!jarmu.getClass().getSimpleName().equals("Hokotro")) {

            /// Elakadás vizsgálata: Ha a hóréteg eléri a kritikus szintet
            if (this.hoMagassag >= HO_ELAKADAS_KUSZOB || this.jegMagassag >= HO_ELAKADAS_KUSZOB) {

                /// A jármű ráhajtott a mély hóra, ezért elakad, és a sáv blokkolt lesz
                this.blokkolt = true;
                //jarmu.elakad();
                System.out.println("A jármű mély hóra/jégre futott és elakadt. A sáv blokkolttá vált.");
                return true;
            }

            /// Megcsúszás vizsgálata jeges úton (Ha nem akadt el)
            if (this.jegMagassag > 0 && megcsuszas()) {
                System.out.println("A jármű megcsúszott a jégen!");
                jarmu.csuszik();
            }
        }
        // Itt majd meghívja a taposodast
        return true;
    }
}

