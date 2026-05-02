package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A kozlekedo jarmuvek kozos absztrakt ososztalya.
 *
 * <p>A Jarmu felel a jarmu aktualis helyzetenek, kijelolt utvonalanak,
 * mozgasi tulajdonsagainak es kozlekedesi allapotainak nyilvantartasaert.
 * A konkret jarmutipusok, peldaul a Hokotro, a Busz es az Auto, ebbol az
 * osztalybol szarmaztatva hasznaljak a kozos mozgasi es allapotkezelesi
 * muveleteket.</p>
 */
public abstract class Jarmu implements ProtoEntitas {

    /**
     * A jatek kozos nyilvantartoja. Elsosorban az NPC autok elakadasanak
     * jelentesehez hasznalhato, ha a jarmuhoz hozza van rendelve.
     */
    protected Nyilvantarto nyilvantarto;

    /**
     * A jarmu mozgasi sebessege.
     */
    protected int sebesseg;

    /**
     * Az az utegyseg, amelyen a jarmu aktualisan all.
     */
    protected Utegyseg utegyseg;

    /**
     * A jarmu altal kovetendo, utakbol allo kijelolt utvonal.
     */
    protected List<Ut> kijeloltUtvonal = new ArrayList<>();

    /**
     * A jarmu tapadasi erteke, amely a jeges uton torteno megcsuszas eselyet
     * befolyasolja.
     */
    protected int tapadas;

    /**
     * Igaz, ha a jarmu mely ho vagy mas akadaly miatt nem kepes tovabbhaladni.
     */
    protected boolean elakadt;

    /**
     * Igaz, ha a jarmu balesetben erintett.
     */
    protected boolean baleset;

    /**
     * Igaz, ha a jarmu megcsuszott egy jeges utegysegen.
     */
    protected boolean megcsuszott;

    /**
     * A jarmu alapveto elorehaladasi muvelete.
     *
     * <p>Ha a jarmu elakadt vagy balesetben erintett, akkor a metodus nem
     * hajt vegre mozgast. Egyebkent az aktualis utegyseg es a kijelolt utvonal
     * alapjan meghatarozza a kovetkezo utegyseget, majd a tenyleges belepest a
     * cel {@link Utegyseg#ralep(Jarmu)} metodusara bizza. Sikertelen ralepes
     * eseten a jarmu helyben marad.</p>
     *
     */
    @Override
    public void parancsFeldolgoz(String parancs, ProtoEntitas masik, List<String> args) {
        masik.parancsFeldolgozJarmuvel(parancs, this, args);
    }

    @Override
    public void parancsFeldolgozUtegyseggel(String parancs, Utegyseg u, List<String> args) {
        if (parancs.equals("assign")) {
            this.utegyseg = u;
            u.setJarmu(this);
            System.out.println("Jármű sikeresen az útegységre helyezve.");
        }
    }

    @Override
    public void parancsFeldolgozNyilvantartoval(String parancs, Nyilvantarto ny1, List<String> args) {
        if (parancs.equals("assign")) {
            this.setNyilvantarto(ny1);
            System.out.println("Jármű sikeresen beallitva a nyilvantarto.");
        }
    }

    public void lep() {
        if (elakadt || baleset) {
            return;
        }

        Utegyseg kovetkezo = null;
        if (utegyseg != null) {
            kovetkezo = utegyseg.getKovetkezoUtegyseg();
        }

        if (kovetkezo == null) {
            return;
        }

        kovetkezo.ralep(this);
    }

    /**
     * Rogziti a jarmu megcsuszasat.
     *
     * <p>A metodus nem donti el, hogy a csuszas bekovetkezik-e, nem keres
     * baleseti partnert, es nem modosit mas jarmuvet. Ezek kulon folyamatok
     * felelossegei.</p>
     */
    public void csuszik() {
        this.megcsuszott = true;
    }

    /**
     * Rogziti, hogy a jarmu balesetben erintett.
     *
     * <p>A metodus csak a jarmu sajat baleseti allapotat allitja be. Nem keresi
     * meg a baleset masik resztvevojet, es nem dont arrol, hogy a baleset
     * bekovetkezik-e.</p>
     */
    public void baleset() {
        this.baleset = true;
    }

    /**
     * Rogziti a jarmu elakadasat.
     *
     * <p>A metodus csak a jarmu sajat elakadt allapotat allitja be. A legfrissebb
     * reszletes terv szerint az utegyseg blokkolasanak felelossege nem a Jarmu
     * osztalye, hanem az elakadast kivalto palyaelem vagy ralepesi folyamat
     * dolga.</p>
     */
    public void elakad() {
        this.elakadt = true;
    }

    /**
     * Frissiti a jarmu aktualis helyzetet egy sikeres ralepes utan.
     *
     * <p>A metodus nem donti el, hogy a lepes sikeres-e, nem ellenorzi az
     * utegyseg foglaltsagat, es nem allitja be az {@link Utegyseg} jarmu
     * referenciajat. Ezek az {@link Utegyseg#ralep(Jarmu)} felelossegei.</p>
     *
     * @param ujUtegyseg az utegyseg, amelyre a jarmu sikeresen ralepett
     */
    public void sikeresLepes(Utegyseg ujUtegyseg) {
        if (ujUtegyseg == null) {
            return;
        }

        this.utegyseg = ujUtegyseg;
    }

    /**
     * Konkret iranyba torteno savvaltasi kiserletet hajt vegre.
     *
     * <p>Az irany parameter megengedett ertekei: {@code "bal"}, {@code "jobb"},
     * {@code "-l"} es {@code "-r"}. A metodus nem valaszt automatikusan masik
     * iranyt, nem modositja a kijelolt utvonalat, es nem hivodik meg
     * automatikusan a {@link #lep()} metodusbol. Sikeres savvaltas eseten
     * {@code true}, sikertelen vagy ervenytelen irany eseten {@code false}
     * ertekkel ter vissza.</p>
     *
     * @param irany a savvaltas iranya
     * @return igaz, ha a savvaltas sikeresen megtortent
     */
    public boolean savValtas(String irany) {
        if (elakadt || baleset || utegyseg == null || irany == null) {
            return false;
        }

        Utegyseg cel;
        if ("jobb".equalsIgnoreCase(irany) || "-r".equalsIgnoreCase(irany)) {
            cel = utegyseg.getJobbUtegyseg();
        } else if ("bal".equalsIgnoreCase(irany) || "-l".equalsIgnoreCase(irany)) {
            cel = utegyseg.getBalUtegyseg();
        } else {
            return false;
        }

        return cel != null && cel.ralep(this);
    }

    /**
     * Megcsuszas utan megkeresi a kozelben allo baleseti partnert.
     *
     * <p>A kereses determinisztikus sorrendben tortenik: eloszor a kovetkezo,
     * majd a jobb, vegul a bal szomszedos utegyseg kerul ellenorzesre. A metodus
     * nem modositja sem a sajat, sem a partner allapotat, nem hivja meg a
     * {@link #baleset()} metodust, es nem blokkol utegyseget.</p>
     *
     * @return a megtalalt partnerjarmu, vagy {@code null}, ha nincs ilyen
     */
    public Jarmu keresPartner() {
        if (!megcsuszott || utegyseg == null) {
            return null;
        }

        Utegyseg[] szomszedok = {
                utegyseg.getKovetkezoUtegyseg(),
                utegyseg.getJobbUtegyseg(),
                utegyseg.getBalUtegyseg()
        };

        for (Utegyseg szomszed : szomszedok) {
            if (szomszed != null && szomszed.getJarmu() != null) {
                return szomszed.getJarmu();
            }
        }

        return null;
    }

    /**
     * Visszaadja a jarmu kijelolt utvonalat.
     *
     * @return az utakbol allo kijelolt utvonal
     */
    public List<Ut> getKijeloltUtvonal() {
        return kijeloltUtvonal;
    }

    /**
     * Visszaadja a jarmu sebesseget.
     *
     * @return a jarmu sebessege
     */
    public int getSebesseg() {
        return sebesseg;
    }

    /**
     * Beallitja a jarmu sebesseget.
     *
     * @param sebesseg az uj sebesseg
     */
    public void setSebesseg(int sebesseg) {
        this.sebesseg = sebesseg;
    }

    /**
     * Visszaadja a jarmu tapadasi erteket.
     *
     * @return a tapadasi ertek
     */
    public int getTapadas() {
        return tapadas;
    }

    /**
     * Beallitja a jarmu tapadasi erteket.
     *
     * @param tapadas az uj tapadasi ertek
     */
    public void setTapadas(int tapadas) {
        this.tapadas = tapadas;
    }

    public void setUtegyseg(Utegyseg u){
        this.utegyseg = u;
    }

    public Utegyseg getUtegyseg(){
        return utegyseg;
    }

    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        this.nyilvantarto = nyilvantarto;
    }

    public Nyilvantarto getNyilvantarto() {
        return nyilvantarto;
    }
}
