package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy játékos által irányított jármű, amelynek elsődleges feladata,
 * hogy az utakat letisztítsa a hótól és jégtől.
 * A takarítással bevételt termel a közös kasszába,
 * valamint csökkenti a balesetek számát az utakon.
 */
public class Hokotro extends Jarmu implements Iranyithato {
    private static int BEVETEL = 10;
    private Fej fej;
    private int zuzalekMennyiseg;
    private int zuzalekLimit;
    List<Utegyseg> tervezettUtvonal;

    public Hokotro(Fej fej) {
        super();
        this.fej = fej;
    }

    /**
     * A hókotróra felszerelt fej használatával takarítja a jármű alatti útegységet.
     * A művelet sikeres elvégzése után a jármű bevételt generál a rendszer számára.
     */
    public void takarit() {
        // Ellenőrizzük, hogy a 'fej' tagváltozó nem null (létezik-e objektum)
        if (this.fej == null) {
            return; // Ha hiányzik, kilépünk a metódusból
        }

        // Ellenőrizzük, hogy az 'utegyseg' tagváltozó nem null (van-e hol takarítani)
        if (this.utegyseg == null) {
            return; // Ha hiányzik, kilépünk a metódusból
        }

        // Ha mindkettő rendelkezésre áll, meghívjuk a fej hasznal metódusát
        // az aktuális útegység átadásával.
        if (this.fej.hasznal(this.utegyseg)) {
            nyilvantarto.penzNovel(BEVETEL);
        }
    }

    /**
     * Iranyithato interfész függvénye.
     * Elmenti a kapott útvonalat a tervezettUtvonal tagváltozóba.
     */
    @Override
    public void setKijeloltUtegysegek(List<Utegyseg> utegysegLista) {
        this.tervezettUtvonal = utegysegLista;
    }

    /**
     * A hókotró zúzalékkészletének növelésére szolgál.
     * @param mennyiseg A hozzáadni kívánt mennyiség.
     * @return Igaz, ha történt módosítás, hamis egyébként.
     */
    public boolean zuzalekNovel(int mennyiseg) {
        // Ha a paraméterként kapott mennyiseg nem pozitív
        if (mennyiseg <= 0) {
            return false;
        }

        int szabadKapacitas = zuzalekLimit - zuzalekMennyiseg;

        // Ha a tartály már elérte a zuzalekLimit értékét (nincs szabad hely)
        if (szabadKapacitas <= 0) {
            return false;
        }

        int hozzaadando = mennyiseg;

        // Ha a mennyiség több, mint amennyi belefér, csak a limitig töltjük
        if (mennyiseg > szabadKapacitas) {
            hozzaadando = szabadKapacitas;
        }

        this.zuzalekMennyiseg += hozzaadando;
        return true;
    }

    /**
     * A hókotró zúzalékkészletének csökkentésére szolgál.
     * @param mennyiseg A levonni kívánt mennyiség.
     * @return Igaz, ha sikeres a levonás, hamis ha nincs elég készlet vagy érvénytelen a paraméter.
     */
    public boolean zuzalekLevon(int mennyiseg) {
        // Ha a paraméterként kapott mennyiseg nem pozitív
        if (mennyiseg <= 0) {
            return false;
        }

        // Ha van elegendő zúzalék a levonáshoz
        if (this.zuzalekMennyiseg >= mennyiseg) {
            this.zuzalekMennyiseg -= mennyiseg;
            return true;
        } else {
            // Ha nincs elegendő zúzalék
            return false;
        }
    }

    public Fej getFej() {
        return fej;
    }

    public void setFej(Fej fej) {
        this.fej = fej;
    }

    /**
     * Visszatér a bevetel tagváltozó értékével.
     */
    public static int getBevetel() {
        return BEVETEL;
    }

    /**
     * A bevetel nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public static void setBevetel(int bevetel) {
        Hokotro.BEVETEL = bevetel;
    }

    /**
     * Visszatér a zuzalekMennyiseg tagváltozó értékével.
     */
    public int getZuzalekMennyiseg() {
        return zuzalekMennyiseg;
    }

    /**
     * A zuzalekMennyiseg nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public void setZuzalekMennyiseg(int zuzalekMennyiseg) {
        this.zuzalekMennyiseg = zuzalekMennyiseg;
    }

    /**
     * Visszatér a zuzalekLimit tagváltozó értékével.
     */
    public int getZuzalekLimit() {
        return zuzalekLimit;
    }

    /**
     * A zuzalekLimit nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     */
    public void setZuzalekLimit(int zuzalekLimit) {
        this.zuzalekLimit = zuzalekLimit;
    }
}