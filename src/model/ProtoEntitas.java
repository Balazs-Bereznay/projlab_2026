package model;

import java.util.List;

/**
 * A prototipus parancsai altal megszolithato objektumok kozos interfesze.
 */
public interface ProtoEntitas {

    // Sima parancsok (set, move, clean, stb.)
    default void parancsFeldolgoz(String parancs, List<String> args, ObjektumKatalogus katalogus) {}

    /**
     * Feldolgozza azokat a parancsokat, amelyek egy masik prototipus-entitast
     * is erintenek.
     *
     * <p>Double dispatch hasznalata eseten ezt a metodust a gazda objektum
     * irja felul, es innen hivja meg a cel objektum megfelelo
     * {@code parancsFeldolgoz...} metodusat.</p>
     *
     * @param parancs a feldolgozando parancs neve
     * @param cel a parancs masik erintett objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgoz(String parancs, ProtoEntitas cel, List<String> args) {
        kapcsolatNemTamogatott(parancs, cel);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy altalanos jarmu.
     *
     * @param parancs a feldolgozando parancs neve
     * @param jarmu a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozJarmuvel(String parancs, Jarmu jarmu, List<String> args) {
        kapcsolatNemTamogatott(parancs, jarmu);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy auto.
     *
     * @param parancs a feldolgozando parancs neve
     * @param auto a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozAutoval(String parancs, Auto auto, List<String> args) {
        parancsFeldolgozJarmuvel(parancs, auto, args);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy busz.
     *
     * @param parancs a feldolgozando parancs neve
     * @param busz a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozBusszal(String parancs, Busz busz, List<String> args) {
        parancsFeldolgozJarmuvel(parancs, busz, args);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy hokotro.
     *
     * @param parancs a feldolgozando parancs neve
     * @param hokotro a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozHokotroval(String parancs, Hokotro hokotro, List<String> args) {
        parancsFeldolgozJarmuvel(parancs, hokotro, args);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy fej.
     *
     * @param parancs a feldolgozando parancs neve
     * @param fej a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozFejjel(String parancs, Fej fej, List<String> args) {
        kapcsolatNemTamogatott(parancs, fej);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy nyilvantarto.
     *
     * @param parancs a feldolgozando parancs neve
     * @param nyilvantarto a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozNyilvantartoval(String parancs,
            Nyilvantarto nyilvantarto, List<String> args) {
        kapcsolatNemTamogatott(parancs, nyilvantarto);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy utegyseg.
     *
     * @param parancs a feldolgozando parancs neve
     * @param utegyseg a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozUtegyseggel(String parancs, Utegyseg utegyseg, List<String> args) {
        kapcsolatNemTamogatott(parancs, utegyseg);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy sav.
     *
     * @param parancs a feldolgozando parancs neve
     * @param sav a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozSavval(String parancs, Sav sav, List<String> args) {
        kapcsolatNemTamogatott(parancs, sav);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy ut.
     *
     * @param parancs a feldolgozando parancs neve
     * @param ut a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozUttal(String parancs, Ut ut, List<String> args) {
        kapcsolatNemTamogatott(parancs, ut);
    }

    /**
     * Celoldali feldolgozas akkor, amikor a gazda egy csomopont.
     *
     * @param parancs a feldolgozando parancs neve
     * @param csomopont a parancs gazda objektuma
     * @param args a parancs tovabbi parameterei
     */
    default void parancsFeldolgozCsomoponttal(String parancs, Csomopont csomopont,
            List<String> args) {
        kapcsolatNemTamogatott(parancs, csomopont);
    }

    default void parancsFeldolgozJatekossal(String parancs, Jatekos jatekos, List<String> args) {
        kapcsolatNemTamogatott(parancs, jatekos);
    }

    default void parancsFeldolgozTerkeppel(String parancs, Terkep terkep, List<String> args) {
        kapcsolatNemTamogatott(parancs, terkep);
    }



    /**
     * Alapertelmezett hibajelzes nem tamogatott kapcsolati parancsokhoz.
     *
     * @param parancs a feldolgozando parancs neve
     * @param masik a masik erintett objektum
     */
    private void kapcsolatNemTamogatott(String parancs, Object masik) {
        String masikTipus = masik == null ? "null" : masik.getClass().getSimpleName();
        System.out.println("Hiba: " + this.getClass().getSimpleName()
                + " nem tud mit kezdeni a(z) '" + masikTipus
                + "' tipussal a(z) " + parancs + " parancs soran.");
    }
}
