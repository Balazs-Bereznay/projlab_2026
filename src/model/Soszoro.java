package model;

/**
 * A kiszórt sóval a jegesedés, vagy a hó megszüntetésére, illetve a havazás megelőzésére
 * használható tisztító fej, ami só erőforrás használatával működik.
 * Megvalósítja a Szorofej interfészt az erőforrás-kezelés és aktiválás érdekében.
 */
class Soszoro extends Fej implements Szorofej {
    private Nyilvantarto nyilvantarto;
    /// Egy használattal egyszerre ekkora adag sót tud kiszórni az adott útegységre.
    private static final int SO_ADAG = 5;

    public Soszoro(Nyilvantarto ny) {
        super(250); // Meghívja a Fej konstruktorát
        this.nyilvantarto = ny;
    }

    /**
     * Só kiszórásával megkezdi a jég és hó olvasztását, illetve megelőzi
     * újabb hó lerakódását az adott útegységen.
     * A sózás csak akkor hajtódik végre, ha az aktiválás (sólevonás) sikeres volt.
     * @param utegyseg Az aktuális útegység, amelyre a sót ki kell szórni.
     * @return Igaz, ha az útegység megtisztul a metódus meghívása után, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        System.out.println("A hókotró használja a sószóró fejet.");
        return false;
    }

    /**
     * Elindítja a só felhasználásának folyamatát.
     * Megkísérli levonni a szükséges sómennyiséget a Nyilvántartóból.
     * @param utegyseg Az útegység, amelyen a fej aktiválásra kerül.
     * @return Igaz, ha a készletben volt elegendő só és a levonás sikeres, egyébként hamis.
     */
    @Override
    public boolean aktival(Utegyseg utegyseg){
        System.out.println("A hókotró megpróbálja aktiválni a sószóró fejet.");
        return false;
    }

    public Nyilvantarto getNyilvantarto() {
        return nyilvantarto;
    }

    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        this.nyilvantarto = nyilvantarto;
    }
}