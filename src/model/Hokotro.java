package model;

import java.util.ArrayList;

/**
 * Egy játékos által irányított jármű, amelynek elsődleges feladata,
 * hogy az utakat letisztítsa a hótól és jégtől.
 * A takarítással bevételt termel a közös kasszába,
 * valamint csökkenti a balesetek számát az utakon.
 */
public class Hokotro extends Jarmu implements Iranyithato {
    private Fej fej;
    private static final double BEVETEL = 10;

    public Hokotro(Fej fej) {
        super();
        this.fej = fej;
    }

    /**
     * A hókotróra felszerelt fej használatával takarítja a jármű alatti útegységet.
     * A művelet sikeres elvégzése után a jármű bevételt generál a rendszer számára.
     */
    public void takarit() {
        fej.hasznal(utegyseg);
        System.out.println("A hókotró elkezdi takarítani az útegységet.");
    }

    /**
     * Lehetővé teszi az útvonal kiválasztását a rendelkezésre álló úthálózatban.
     * A játékos ezen keresztül határozhatja meg a jármű haladási irányát.
     * @param utegysegLista A választható útegységek listája.
     */
    @Override
    public void utvonalatValaszt(ArrayList<Utegyseg> utegysegLista){
        System.out.println("Hókotró utvonalatValaszt() meghívva.");
        System.out.println("Kiválasztott útegysegek száma: " +
                (utegysegLista == null ? 0 : utegysegLista.size()));
    }

    public Fej getFej() {
        return fej;
    }

    public void setFej(Fej fej) {
        this.fej = fej;
    }
}