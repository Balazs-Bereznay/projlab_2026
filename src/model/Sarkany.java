package model;

/**
 * Tisztítófej, amely képes egy útegységet szinte azonnal megtisztítani
 * biokerozin erőforrás felhasználásával.
 */
public class Sarkany extends Fej implements Szorofej {
    private Nyilvantarto nyilvantarto;
    /// Biokerozin mennyiség, ami egy útegység megtakarításához szükséges.
    private static final int BIOKEROZIN_ADAG = 5;

    public Sarkany(Nyilvantarto ny) {
        super(300); // Meghívja a Fej konstruktorát
        this.nyilvantarto = ny;
    }

    /**
     * Biokerozin felhasználásával azonnali hatással tisztítja mind a jeget
     * és a havat egy adott útegységről.
     * A tisztítás csak akkor megy végbe, ha az aktiválás (erőforrás-levonás) sikeres.
     * @param utegyseg Az aktuális útegység, amelyet meg kell tisztítani.
     * @return Igaz, ha az útegység megtisztul a metódus meghívása után, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        System.out.println("A hókotró használja a sárkány fejet.");
        if (this.aktival(utegyseg)) {
            utegyseg.jegtores();    /// Jég megszüntetése
            utegyseg.tisztulas();   /// Hó eltávolítása
            return true;
        }
        return false;
    }

    /**
     * Elindítja a biokerozin felhasználásának folyamatát.
     * Kapcsolatba lép a Nyilvántartóval a szükséges üzemanyagmennyiség levonása érdekében.
     * @param utegyseg Az útegység, amelyen a fej aktiválásra kerül.
     * @return Igaz, ha a nyilvántartóban rendelkezésre állt a biokerozin és a levonás sikeres volt, egyébként hamis.
     */
    @Override
    public boolean aktival (Utegyseg utegyseg){
        System.out.println("A hókotró megpróbálja aktiválni a sárkány fejet.");
        return nyilvantarto.biokerozinLevon(BIOKEROZIN_ADAG);
    }

    public Nyilvantarto getNyilvantarto() {
        return nyilvantarto;
    }

    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        this.nyilvantarto = nyilvantarto;
    }
}
