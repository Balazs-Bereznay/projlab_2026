package model;

import java.util.ArrayList;

/**
 * A játékban szereplő járművek absztrakt ősosztálya.
 * Tartalmazza a közös tulajdonságokat és alapvető működéseket.
 */
public abstract class Jarmu {

    protected Nyilvantarto nyilvantarto; /// DAVID rakta hozzá mert a kritikus vereseghez kellene hogy ha az auto balesetezik akkor a nyivlantartonak tudja noveni a nembeertatuos erteket.
    protected int sebesseg;
    protected Utegyseg utegyseg;
    protected ArrayList<Ut> kijeloltUtvonal;
    protected int tapadas;
    protected boolean elakadt;
    protected boolean baleset;
    protected boolean megcsuszott;

    public Jarmu() {
        this.sebesseg = 1;
        this.utegyseg = null;
        this.kijeloltUtvonal = new ArrayList<>();
        this.tapadas = 1;
        this.elakadt = false;
        this.baleset = false;
        this.megcsuszott = false;
    }

    public Jarmu(int sebesseg, Utegyseg utegyseg, int tapadas) {
        this.sebesseg = sebesseg;
        this.utegyseg = utegyseg;
        this.kijeloltUtvonal = new ArrayList<>();
        this.tapadas = tapadas;
        this.elakadt = false;
        this.baleset = false;
        this.megcsuszott = false;
    }

    /**
     * A jármű megpróbál egyet lépni a kijelölt útvonalon.
     */
    public void lep() {
        System.out.println(getClass().getSimpleName() + " lep() meghivva.");

        if (baleset) {
            System.out.println("A jarmu balesetet szenvedett, nem tud lepni.");
            return;
        }

        if (elakadt) {
            System.out.println("A jarmu elakadt, nem tud lepni.");
            return;
        }

        if (utegyseg == null) {
            System.out.println("A jarmu nincs utegysegen.");
            return;
        }

        System.out.println("A jarmu megprobal tovabblepni a kijelolt utvonalon.");
    }

    /**
     * A jármű megcsúszását modellezi.
     */
    public void csuszik() {
        this.megcsuszott = true;
        System.out.println(getClass().getSimpleName() + " megcsuszott.");
    }

    /**
     * A jármű balesetét modellezi.
     */
    public void baleset() {
        this.baleset = true;
        if (utegyseg != null) {
            utegyseg.setBlokkolt(true);
        }
        System.out.println(getClass().getSimpleName() + " balesetet szenvedett.");
    }

    /**
     * A jármű elakadását modellezi.
     */
    public void elakad() {
        this.elakadt = true;
        if (utegyseg != null) {
            utegyseg.setBlokkolt(true);
        }
        System.out.println(getClass().getSimpleName() + " elakadt.");
    }

    /**
     * Frissíti a jármű helyzetét egy sikeres lépés után.
     * @param ujUtegyseg Az útegység, amelyre a jármű sikeresen rálépett.
     */
    public void sikeresLepes(Utegyseg ujUtegyseg) {
        if (ujUtegyseg == null) {
            System.out.println("Nincs megadott uj utegyseg.");
            return;
        }

        if (this.utegyseg != null) {
            this.utegyseg.setJarmu(null);
        }

        this.utegyseg = ujUtegyseg;
        this.utegyseg.setJarmu(this);

        System.out.println(getClass().getSimpleName() + " sikeresen atlepett egy uj utegysegre.");
    }

    /**
     * A jármű megpróbál sávot váltani egy szomszédos útegységre.
     * Először a jobb, majd a bal oldali szomszédot vizsgálja.
     */
    public void savValtas() {
        if (utegyseg == null) {
            System.out.println("A jarmu nincs utegysegen, nem tud savot valtani.");
            return;
        }

        Utegyseg cel = null;

        if (utegyseg.getJobbUtegyseg() != null
                && utegyseg.getJobbUtegyseg().getJarmu() == null
                && !utegyseg.getJobbUtegyseg().getBlokkolt()) {
            cel = utegyseg.getJobbUtegyseg();
        } else if (utegyseg.getBalUtegyseg() != null
                && utegyseg.getBalUtegyseg().getJarmu() == null
                && !utegyseg.getBalUtegyseg().getBlokkolt()) {
            cel = utegyseg.getBalUtegyseg();
        }

        if (cel == null) {
            System.out.println("Nincs szabad szomszedos sav, a savvaltas sikertelen.");
            return;
        }

        sikeresLepes(cel);
        System.out.println(getClass().getSimpleName() + " sikeresen savot valtott.");
    }

    /**
     * Megpróbál karambolpartnert keresni a jármű közvetlen környezetében.
     * Skeleton szinten a következő, jobb és bal szomszédos útegységeken álló járművet vizsgálja.
     */
    public void KeresPartner() {
        if (utegyseg == null) {
            System.out.println("A jarmu nincs utegysegen, nem tud karambolpartnert keresni.");
            return;
        }

        Jarmu partner = null;

        if (utegyseg.getKovetkezoUtegyseg() != null && utegyseg.getKovetkezoUtegyseg().getJarmu() != null) {
            partner = utegyseg.getKovetkezoUtegyseg().getJarmu();
        } else if (utegyseg.getJobbUtegyseg() != null && utegyseg.getJobbUtegyseg().getJarmu() != null) {
            partner = utegyseg.getJobbUtegyseg().getJarmu();
        } else if (utegyseg.getBalUtegyseg() != null && utegyseg.getBalUtegyseg().getJarmu() != null) {
            partner = utegyseg.getBalUtegyseg().getJarmu();
        }

        if (partner == null) {
            System.out.println("Nem talalhato karambolpartner a kozelben.");
            return;
        }

        System.out.println(getClass().getSimpleName() + " karambolpartnert talalt: "
                + partner.getClass().getSimpleName());

        this.baleset();
        partner.baleset();
    }

    public void setKijeloltUtvonal(ArrayList<Ut> kijeloltUtvonal) {
        this.kijeloltUtvonal = kijeloltUtvonal;
    }

    public ArrayList<Ut> getKijeloltUtvonal() {
        return kijeloltUtvonal;
    }

    public int getSebesseg() {
        return sebesseg;
    }

    public void setSebesseg(int sebesseg) {
        this.sebesseg = sebesseg;
    }

    public Utegyseg getUtegyseg() {
        return utegyseg;
    }

    public void setUtegyseg(Utegyseg utegyseg) {
        this.utegyseg = utegyseg;
    }

    public int getTapadas() {
        return tapadas;
    }

    public void setTapadas(int tapadas) {
        this.tapadas = tapadas;
    }

    public boolean isElakadt() {
        return elakadt;
    }

    public boolean isBaleset() {
        return baleset;
    }

    public boolean isMegcsuszott() {
        return megcsuszott;
    }

    public void setNyilvantarto(Nyilvantarto nyilvantarto) { /// Ezi is DAVID irta hozza
        this.nyilvantarto = nyilvantarto;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{sebesseg=" + sebesseg
                + ", tapadas=" + tapadas
                + ", elakadt=" + elakadt
                + ", baleset=" + baleset
                + ", megcsuszott=" + megcsuszott
                + "}";
    }
}