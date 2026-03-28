package model;

/**
 * A pálya legkisebb, hálószerűen összekapcsolt felépítési egysége.
 * Felelős az időjárás hatására rajta keletkező és eltűnő természeti hatások
 * feljegyzésére (hó, jég), illetve a rajta álló járművek, és a környező egységek nyilvántartására.
 */
public class Utegyseg {
    private static final int HO_ELAKADAS_KUSZOB = 15;
    private static final int LETAPOSOTTSAG_KUSZOB = 10;

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

    ///Konstruktorok
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

    ///Getterek és setterek
    public int getLetaposottsag() { return letaposottsag; }
    public void setLetaposottsag(int letaposottsag) { this.letaposottsag = letaposottsag; }

    public double getMegcsuszasEsely() { return megcsuszasEsely; }
    public void setMegcsuszasEsely(double megcsuszasEsely) { this.megcsuszasEsely = megcsuszasEsely; }

    public Jarmu getJarmu() { return jarmu; }
    public void setJarmu(Jarmu jarmu) { this.jarmu = jarmu; }

    public Utegyseg getKovetkezoUtegyseg() { return kovetkezoUtegyseg; }
    public void setKovetkezoUtegyseg(Utegyseg kovetkezoUtegyseg) { this.kovetkezoUtegyseg = kovetkezoUtegyseg; }

    public Utegyseg getBalUtegyseg() { return balUtegyseg; }
    public void setBalUtegyseg(Utegyseg balUtegyseg) { this.balUtegyseg = balUtegyseg; }

    public Utegyseg getJobbUtegyseg() { return jobbUtegyseg; }
    public void setJobbUtegyseg(Utegyseg jobbUtegyseg) { this.jobbUtegyseg = jobbUtegyseg; }

    public int getHoMagassag() { return hoMagassag; }
    public void setHoMagassag(int hoMagassag) { this.hoMagassag = hoMagassag; }

    public int getJegMagassag() { return jegMagassag; }
    public void setJegMagassag(int jegMagassag) { this.jegMagassag = jegMagassag; }

    public boolean getBlokkolt() { return blokkolt; }   ///ez a getFoglalt egyes diagramokon
    public void setBlokkolt(boolean blokkolt) { this.blokkolt = blokkolt; }

    public int getSoMennyiseg() { return soMennyiseg; }
    public void setSoMennyiseg(int soMennyiseg) { this.soMennyiseg = soMennyiseg; }

    ///További metódusok
    /**
     * Növeli a hóréteg vastagságát a mezőn a szimulált időjárás hatására.
     * @param mennyiseg Az a hómennyiség, amivel nő az útegységen lévő hóréteg.
     */
    public void havazas(int mennyiseg){
        hoMagassag += mennyiseg;

        if(hoMagassag >= HO_ELAKADAS_KUSZOB)
            blokkolt = true;
        //TODO
    }

    /**
     * Növeli az útegységen található só vastagságát.
     * @param mennyiseg Az a sómennyiség, amivel nő az útegységen lévő sóréteg.
     */
    public void sozas(int mennyiseg){
        soMennyiseg += mennyiseg;
        //TODO
    }

    /**
     * Növeli a jégréteg vastagságát ha  a letaposottság  értéke elér
     * egy adott értéket, illetve ha további hó esik a jégre.
     * @param mennyiseg Ennyivel nő meg a jégréteg az útegységen.
     */
    public void jegesedes(int mennyiseg){
        //TODO
        jegMagassag += mennyiseg;
    }

    /**
     * A járművek áthaladása tömöríti a havat, ami később jégréteg kialakulásához vezethet.
     */
    public void taposodas(){
        //TODO
        letaposottsag += 2;
        if(letaposottsag >= LETAPOSOTTSAG_KUSZOB)
            jegesedes();
    }

    /**
     * A jégtörő fej hatására lenullázza a jégvastagságot az adott
     * útegységen és a modell szerint "hó" ként fog viselkedni.
     */
    public void jegtores(){
        hoMagassag = jegMagassag;
        jegMagassag = 0;
        System.out.println("A jégből hó lett.");
    }

    /**
     * A só hatására csökkenti a hó vagy jégvastagság méretét.
     */
    public void soOlvasztas(){
        //TODO
    }

    /**
     * A hókotró munkájának eredményeként eltávolítja a csapadékot a mezőről.
     */
    public void tisztulas(){
        hoMagassag = 0;
        jegMagassag = 0;
        System.out.println("Az útegység tiszta lett.");
    }

    /**
     * Visszaadja, hogy jeges útegység esetén a jármű megcsúszott-e vagy sem.
     * @return Visszaadja, hogy megcsúszott-e az útegységen lévő jármű.
     */
    public bool megcsuszas(){
        //TODO
    }

    /**
     * Regisztrálja, hogy egy jarmű megérkezett egy adott útegységre, kiválasztva
     * az esetleges interakciókat.
     * @param jarmu Az a jármű, ami rálép az útegységre.
     */
    public void ralep(Jarmu jarmu){
        //TODO
    }
}

