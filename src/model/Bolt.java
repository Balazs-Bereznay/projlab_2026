package model;


/**
 * A játék gazdasági központja, ahol a játékosok a megszerzett pénzből új eszközöket, nyersanyagokat vásárolhatnak,
 * van lehetőség a hókotrófejek cseréjére, valamint a buszok fejlesztésére is. Az áruk listázása is az ő felelőssége.
 */

public class Bolt {

    /// a megvasarolható sónak az egységára
    private  int soAr;
    /// a megvasarolható biokerozin egységára
    private  int biokerozinAr;

    public Bolt(int soAr, int biokerozinAr) {
        this.soAr = soAr;
        this.biokerozinAr = biokerozinAr;
    }

    /**
     * A játékos megvásárol egy új hokotrót
     */
    public void hokotroVasarol(){
        System.out.println("Egy új alap hókotró sikeresen megvásárolva!");
    }

    /**
     * Egy játékos által vásárolt adott mennyiségű sót hozzáadja a nyilvántartó rendszerhez.
     * @param mennyiseg annak a mennyisége, hogy mennyi sót  akarunk vásárolni.
     */
    public void soVasarol(int mennyiseg){
        System.out.println("Sikeres tranzakció: " + mennyiseg + " egység só megvásárolva.");
    }

    /**
     * "Egy játékos által vásárolt adott mennyiségű biokerozint hozzáadja a Nyilvántartó rendszerhez.
     * @param mennyiseg annak a mennyisége, hogy mennyi biokerozint  akarunk vásárolni.
     */
    public void setBiokerozinVasarol(int mennyiseg){
        System.out.println("Sikeres tranzakció: " + mennyiseg + " liter biokerozin megvásárolva.");
    }

    /**
     * A jatékos megvásárol egy seprőfejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     * @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void seproVasarol(Hokotro hokotro){
        System.out.println("Seprőfej megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     * A jatékos megvásárol egy hányófejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void hanyoVasarol(Hokotro hokotro){
        System.out.println("Hóhányófej megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     *A jatékos megvásárol egy jégtörő fejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void jegtoroVasarol(Hokotro hokotro){
        System.out.println("Jégtörő fej megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     * A jatékos megvásárol egy sószoró, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void soszoroVasarol(Hokotro hokotro){
        System.out.println("Sószóró megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     * A jatékos megvásárol egy sárkány fejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public  void sarkanyVasarol(Hokotro hokotro){
        System.out.println("Sárkány fej megvásárolva! A lángszóró sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     * a só egységárának a gettere
     * @return A só aktuális ára
     */
    public int getSoAr() {
        System.out.println("Lekérdezték a só árát, ami jelenleg: " + this.soAr);
        return soAr;
    }
    /**
     * a só egységárának a settere
     * @return soAr Az új egységár, amit be akarunk állítani.
     */
    public void setSoAr(int soAr) {
        this.soAr = soAr;
        System.out.println("Árváltozás! A só egységára " + this.soAr + " értékre lett beállítva.");
    }
    /**
     * a biokerozin egységárának a gettere
     * @return A biokerozin aktuális literenkénti ára.
     */
    public int getBiokerozinAr() {
        System.out.println("Lekérdezték a biokerozin árát, ami jelenleg: " + this.biokerozinAr);
        return biokerozinAr;
    }
    /**
     * a biokerozin egységárának a settere
     * @return biokerozinAr Az új ár, amennyiért a kerozin vásárolható.
     */
    public void setBiokerozinAr(int biokerozinAr) {
        this.biokerozinAr = biokerozinAr;
        System.out.println("Árváltozás! A biokerozin egységára " + this.biokerozinAr + " értékre lett beállítva.");
    }
}
