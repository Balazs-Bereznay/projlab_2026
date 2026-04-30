package model;

/**
 * Tisztítófej, amely képes egy útegységet szinte azonnal megtisztítani
 * biokerozin erőforrás felhasználásával.
 */
public class Sarkany extends Fej {
    ///A játékban lévő közös nyilvántartó.
    private Nyilvantarto nyilvantarto;
    /// Biokerozin mennyiség, ami egy útegység megtakarításához szükséges.
    private static final int BIOKEROZIN_ADAG = 5;

    public Sarkany(Nyilvantarto ny) {
        this.nyilvantarto = ny;
    }

    /**
     * Biokerozin felhasználásával azonnali hatással tisztítja mind a jeget
     * és a havat egy adott útegységről.
     * A tisztítás csak akkor megy végbe, ha az erőforrás-levonás sikeres.
     * @param utegyseg Az aktuális útegység, amelyet meg kell tisztítani.
     * @return Igaz, ha volt elég biokerozin és céljának megfelelően, havas vagy jeges útegységen lett használva a fej, egyébként hamis.
     */
    @Override
    public boolean hasznal(Utegyseg utegyseg) {
        if(nyilvantarto.biokerozinLevon(BIOKEROZIN_ADAG)){
            if(utegyseg.getHoMagassag() > 0 || utegyseg.getJegMagassag() > 0){
                utegyseg.jegtores();
                utegyseg.tisztulas();   ///igaz, hogy a sárkány fej nem hat a zúzalékra, de ha már nincs se alatta se fölötte semmi, akkor nincs értelme a zúzaléknak, "egybeolvad" az úttal
                System.out.println("A hókotró sikeresen használta a sárkány fejet.");
                return true;
            }
        }
        System.out.println("Nem volt sikeres a sárkány fej használata.");
        return false;
    }

    /**
     * Visszatér a nyilvantarto tagváltozó értékével.
     * @return A közös nyilvántartó, amit a Bolt tud kezelni.
     */
    public Nyilvantarto getNyilvantarto() {
        return nyilvantarto;
    }

    /**
     * A nyilvantarto nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     * @param nyilvantarto Az új nyilvántartó, amit a Bolt tud kezelni.
     */
    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        this.nyilvantarto = nyilvantarto;
    }
}
