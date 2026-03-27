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
    public void hokotroVasarol(){}

    /**
     * Egy játékos által vásárolt adott mennyiségű sót hozzáadja a nyilvántartó rendszerhez.
     * @param mennyiseg annak a mennyisége, hogy mennyi sót  akarunk vásárolni.
     */
    public void soVasarol(int mennyiseg){}

    /**
     * "Egy játékos által vásárolt adott mennyiségű biokerozint hozzáadja a Nyilvántartó rendszerhez.
     * @param mennyiseg annak a mennyisége, hogy mennyi biokerozint  akarunk vásárolni.
     */
    public void setBiokerozinVasarol(int mennyiseg){}

    /**
     * A jatékos megvásárol egy seprőfejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     * @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void seproVasarol(Hokotro hokotro){

    }

    /**
     * A jatékos megvásárol egy hányófejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void hanyoVasarol(Hokotro hokotro){

    }

    /**
     *A jatékos megvásárol egy jégtörő fejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void jegtoroVasarol(Hokotro hokotro){

    }

    /**
     * A jatékos megvásárol egy sószoró, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void soszoroVasarol(Hokotro hokotro){

    }

    /**
     * A jatékos megvásárol egy sárkány fejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public  void sarkanyVasarol(Hokotro hokotro){

    }

    /**
     * a só egységárának a gettere
     * @return A só aktuális ára
     */
    public int getSoAr() {
        return soAr;
    }
    /**
     * a só egységárának a settere
     * @return soAr Az új egységár, amit be akarunk állítani.
     */
    public void setSoAr(int soAr) {
        this.soAr = soAr;
    }
    /**
     * a biokerozin egységárának a gettere
     * @return A biokerozin aktuális literenkénti ára.
     */
    public int getBiokerozinAr() {
        return biokerozinAr;
    }
    /**
     * a biokerozin egységárának a settere
     * @return biokerozinAr Az új ár, amennyiért a kerozin vásárolható.
     */
    public void setBiokerozinAr(int biokerozinAr) {
        this.biokerozinAr = biokerozinAr;
    }
}
