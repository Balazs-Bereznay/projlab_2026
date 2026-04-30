package model;

import java.util.ArrayList;

/**
 * A játékos által irányított jármű, amely megállókat érintve közlekedik.
 */
public class Busz extends Jarmu implements Iranyithato {
    private Csomopont vegallomas1;
    private Csomopont vegallomas2;
    private ArrayList<Csomopont> megallokLista;
    private ArrayList<Csomopont> erintettLista;
    private int bevetel;

    public Busz() {
        super();
        this.vegallomas1 = null;
        this.vegallomas2 = null;
        this.megallokLista = new ArrayList<>();
        this.erintettLista = new ArrayList<>();
        this.bevetel = 0;
    }

    public Busz(Csomopont vegallomas1, Csomopont vegallomas2, int sebesseg, Utegyseg utegyseg, int tapadas) {
        super(sebesseg, utegyseg, tapadas);
        this.vegallomas1 = vegallomas1;
        this.vegallomas2 = vegallomas2;
        this.megallokLista = new ArrayList<>();
        this.erintettLista = new ArrayList<>();
        this.bevetel = 0;
    }

    /**
     * Beállítja a busz által követett útvonalat.
     * @param utegysegLista A kiválasztott útvonal útegységei.
     */
    @Override
    public void utvonalatValaszt(ArrayList<Utegyseg> utegysegLista) {
        System.out.println("Busz utvonalatValaszt() meghivva.");
        System.out.println("Kivalasztott utegysegek szama: " +
                (utegysegLista == null ? 0 : utegysegLista.size()));
    }

    /**
     * Rögzíti, hogy a busz érintett egy megállót.
     * @param megallo Az érintett megálló.
     */
    public void megalloErintese(Csomopont megallo) {
        if (megallo == null) {
            System.out.println("Nincs megadott megallo.");
            return;
        }

        if (!erintettLista.contains(megallo)) {
            erintettLista.add(megallo);
            System.out.println("Busz megerintette a megallot: " + megallo);
        } else {
            System.out.println("A megallo mar korabban erintve lett: " + megallo);
        }
    }

    /**
     * Kiszámítja a busz által termelt bevételt.
     * @return A kiszámított bevétel.
     */
    public int jutalomKiszamitasa() {
        this.bevetel = erintettLista.size() * 100;

        boolean ketVegallomasMegvan =
                erintettLista.contains(vegallomas1) && erintettLista.contains(vegallomas2);

        if (ketVegallomasMegvan) {
            this.bevetel += 500;
        }

        System.out.println("Busz jutalomKiszamitasa() meghivva, bevetel: " + bevetel);
        return bevetel;
    }

    /**
     * A busz egy lépését modellezi.
     */
    @Override
    public void lep() {
        super.lep();
        if (!baleset && !elakadt) {
            System.out.println("A busz tovabbhaladt a kijelolt utvonalon.");
        }
    }

    public Csomopont getVegallomas1() {
        return vegallomas1;
    }

    public void setVegallomas1(Csomopont vegallomas1) {
        this.vegallomas1 = vegallomas1;
    }

    public Csomopont getVegallomas2() {
        return vegallomas2;
    }

    public void setVegallomas2(Csomopont vegallomas2) {
        this.vegallomas2 = vegallomas2;
    }

    public ArrayList<Csomopont> getMegallokLista() {
        return megallokLista;
    }

    public void setMegallokLista(ArrayList<Csomopont> megallokLista) {
        this.megallokLista = megallokLista;
    }

    public ArrayList<Csomopont> getErintettLista() {
        return erintettLista;
    }

    public int getBevetel() {
        return bevetel;
    }

    public void setBevetel(int bevetel) {this.bevetel = bevetel;}

    @Override
    public String toString() {
        return super.toString()
                + " Busz{vegallomas1=" + vegallomas1
                + ", vegallomas2=" + vegallomas2
                + ", megallokSzama=" + megallokLista.size()
                + ", erintettSzama=" + erintettLista.size()
                + ", bevetel=" + bevetel
                + "}";
    }
}