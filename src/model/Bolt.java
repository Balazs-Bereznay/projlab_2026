package model;


import java.util.List;

/**
 * A játék gazdasági központja, ahol a játékosok a megszerzett pénzből új eszközöket, nyersanyagokat vásárolhatnak,
 * van lehetőség a hókotrófejek cseréjére, valamint a buszok fejlesztésére is. Az áruk listázása is az ő felelőssége.
 */

public class Bolt implements ProtoEntitas {

    /// a megvasarolható sónak az egységára
    private  int soAr;
    /// a megvasarolható biokerozin egységára
    private  int biokerozinAr;
    private int hokotroAr;
    private int soproAr;
    private int hanyoAr;
    private int jegtoroAr;
    private int soszoroAr;
    private int sarkanyAr;
    private int zuzalekAr;
    private int zuzalekszoroAr;
    private int sebessegfejlesztesAr;
    private int tapadasfejlesztesAr;
    private int hozamfejlesztesAr;
    /// a nyilvantarto amiben majd eltaroljuk hogy az egyes nyersanyagokbol mennyi all rednelkezesre
    private Nyilvantarto nyilvantarto;

    public Bolt(int soAr, int biokerozinAr) {
        this.soAr = soAr;
        this.biokerozinAr = biokerozinAr;
    }

    /**
     * A játékos megvásárol egy új hokotrót
     */
    public void hokotroVasarol(Jatekos jatekos){
        Fej sopro = new Sopro();
        jatekos.getFlotta().add(new Hokotro(sopro));
        nyilvantarto.penzLevon(hokotroAr);
        System.out.println("Egy új alap hókotró sikeresen megvásárolva!");

    }

    /**
     * Egy játékos által vásárolt adott mennyiségű sót hozzáadja a nyilvántartó rendszerhez.
     * @param mennyiseg annak a mennyisége, hogy mennyi sót  akarunk vásárolni.
     */
    public void soVasarol(int mennyiseg){

        nyilvantarto.soNovel(mennyiseg);
        nyilvantarto.penzLevon(soAr);
        System.out.println("Sikeres tranzakció: " + mennyiseg + " egység só megvásárolva.");
    }

    /**
     * "Egy játékos által vásárolt adott mennyiségű biokerozint hozzáadja a Nyilvántartó rendszerhez.
     * @param mennyiseg annak a mennyisége, hogy mennyi biokerozint  akarunk vásárolni.
     */
    public void setBiokerozinVasarol(int mennyiseg){
        nyilvantarto.biokerozinNovel(mennyiseg);
        nyilvantarto.penzLevon(biokerozinAr);
        System.out.println("Sikeres tranzakció: " + mennyiseg + " liter biokerozin megvásárolva.");
    }

    public void sebessegFejlesztes(Busz busz, int novelesMerteke){
       // Sebessegfejlesztes sebesseg = new Sebessegfejlesztes(fejlesztAr,1);
       // sebesseg.fejleszt(busz);
        System.out.println("Sebességfejlesztés alkalmazva.");
        if(busz == null){
            return;
        }
        busz.setSebesseg(busz.getSebesseg() + novelesMerteke);
        busz.nyilvantarto.penzLevon(sebessegfejlesztesAr);

       System.out.println("A busz sebessége fejlesztve lett!");
    }
    public void tapadasFejlesztes(Busz busz,  int novelesMerteke){
      //  Tapadasfejlesztes tapadasfejlesztes = new Tapadasfejlesztes(fejlesztAr,1);
      //  tapadasfejlesztes.fejleszt(busz);
        System.out.println("Tapadásfejlesztés alkalmazva.");
        if(busz == null){
            return;
        }
        busz.setTapadas(busz.getTapadas() + novelesMerteke);
        busz.nyilvantarto.penzLevon(tapadasfejlesztesAr);

        System.out.println("A busz tapadasa fejlesztve lett!");
    }
    public void hozamFejlesztes(Busz busz, int  novelesMerteke){
        //Hozamfejlesztes hozamfejlesztes = new Hozamfejlesztes(fejlesztAr,100);
        //  hozamfejlesztes.fejleszt(busz);
        System.out.println("Hozamfejlesztés alkalmazva.");
        if(busz == null){
            return;
        }
        busz.setBevetel(busz.getBevetel() + novelesMerteke);
        busz.nyilvantarto.penzLevon(sebessegfejlesztesAr);

        System.out.println("A busz hozama fejlesztve lett!");
    }

    /**
     * A jatékos megvásárol egy seprőfejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     * @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void seproVasarol(Hokotro hokotro){
        Fej sepro = new Sopro();
        hokotro.setFej(sepro);
        nyilvantarto.penzLevon(soproAr);
        System.out.println("Seprőfej megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     * A jatékos megvásárol egy hányófejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void hanyoVasarol(Hokotro hokotro){
        Fej hanyo = new Hanyo();
        hokotro.setFej(hanyo);
        nyilvantarto.penzLevon(hanyoAr);
        System.out.println("Hóhányófej megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     *A jatékos megvásárol egy jégtörő fejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void jegtoroVasarol(Hokotro hokotro){
        Fej jegt = new Jegtoro();
        hokotro.setFej(jegt);
        nyilvantarto.penzLevon(jegtoroAr);
        System.out.println("Jégtörő fej megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     * A jatékos megvásárol egy sószoró, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public void soszoroVasarol(Hokotro hokotro){
        Fej sSz = new Soszoro(nyilvantarto);
        hokotro.setFej(sSz);
        nyilvantarto.penzLevon(soszoroAr);
        System.out.println("Sószóró megvásárolva és sikeresen felszerelve a kiválasztott hókotróra.");
    }

    /**
     * A jatékos megvásárol egy sárkány fejet, amit egyből fel is szerelünk a hókotróra, és ezzel együtt a korábbi hókotró eltűnik.
     *  @param hokotro az a hókotró amelyikre fel akarjuk szerelni az új tisztító fejet.
     */
    public  void sarkanyVasarol(Hokotro hokotro){
        Fej srkny = new Sarkany(nyilvantarto);
        hokotro.setFej(srkny);
        nyilvantarto.penzLevon(sarkanyAr);
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

    /**
     * Egy uj nyilvantarto beallitasa amit a bolt tud kezelni.
     * @param ny amit be akarunk allitani uj nyilvantartonak
     */
    public void setNyilvantarto(Nyilvantarto ny) {
        nyilvantarto = ny;
        System.out.println("uj nyilvantarto lett beallitva");

    }

    public int gethokotroAr(){return hokotroAr;}
     public void sethokotroAr(int mennyiseg)
     {
        hokotroAr = mennyiseg;
     }

    public int getSeproAr(){
        return soproAr;
    }
    public void setSeproAr(int mennyiseg){
        soproAr = mennyiseg;
    }

    public int getHanyoAr(){
        return hanyoAr;
    }
    public void setHanyoAr(int mennyiseg){
        hanyoAr = mennyiseg;
    }

    public int getJegtoroAr(){
        return jegtoroAr;
    }
    public void setJegtoroAr(int mennyiseg){
        jegtoroAr = mennyiseg;
    }

    public int getSoszoroAr(){
        return soszoroAr;
    }
    public void setSoszoroAr(int mennyiseg){
        soszoroAr = mennyiseg;
    }

    public int getSarkanyAr(){
        return sarkanyAr;
    }
    public void setSarkanyAr(int mennyiseg){
        sarkanyAr = mennyiseg;
    }

    public int getZuzalekAr(){
        return zuzalekAr;
    }
    public void setZuzalekAr(int mennyiseg){
        zuzalekAr = mennyiseg;
    }

    public int getZuzalekszoroAr(){ return zuzalekszoroAr; }
    public void setZuzalekszoroAr(int mennyiseg){   zuzalekszoroAr = mennyiseg; }

    public int getSebessegfejlesztesAr(){return sebessegfejlesztesAr;}
    public void setSebessegfejlesztesAr(int mennyiseg){sebessegfejlesztesAr = mennyiseg; }

    public int getTapadasfejlesztesAr(){ return tapadasfejlesztesAr; }
    public void setTapadasfejlesztesAr(int mennyiseg){tapadasfejlesztesAr = mennyiseg; }

    public int getHozamfejlesztesAr(){ return hozamfejlesztesAr; }
    public void setHozamfejlesztesAr(int mennyiseg){hozamfejlesztesAr = mennyiseg; }

    /**
     * Feldolgozza a boltra érkező, egyszerű prototípus-parancsokat.
     *
     * @param parancs a feldolgozandó parancs neve
     * @param args a parancs további paraméterei
     */
    @Override
    public void parancsFeldolgoz(String parancs, List<String> args) {
        if (parancs == null || args == null) {
            return;
        }
        if (nyilvantarto == null) {
            System.out.println("Hiba: A rendszer nincs inicializálva (hiányzik a nyilvántartó)!");
            return;
        }

        switch (parancs) {
            case "list_shop":
                String kinalat = """
                    Bolt kínálata:
        
                    Vásárolható erőforrások:
                    - Só ára: %d
                    - Biokerozin ára: %d
                    - Zúzalék ára: %d
        
                    Vásárolható fejek:
                    - Söprő fej ára: %d
                    - Hányó fej ára: %d
                    - Jégtörő fej ára: %d
                    - Sószóró fej ára: %d
                    - Sárkány fej ára: %d
                    - Zúzalékszóró fej ára: %d
        
                    Egyéb eszközök:
                    - Hókotró ára: %d
        
                    Fejlesztések buszokra:
                    - Sebességfejlesztés ára: %d
                    - Tapadásfejlesztés ára: %d
                    - Hozamfejlesztés ára: %d
                    
                    Jelenleg a nyilvántartóban rendelkezésre álló erőforrások mennyisége:
                    - pénz:
                    - só:
                    - biokerozin:
                    """.formatted(
                        soAr, biokerozinAr, zuzalekAr,
                        soproAr, hanyoAr, jegtoroAr, soszoroAr, sarkanyAr, zuzalekszoroAr,
                        hokotroAr,
                        sebessegfejlesztesAr, tapadasfejlesztesAr, hozamfejlesztesAr,
                        nyilvantarto.getPenz(), nyilvantarto.getSo(), nyilvantarto.getBiokerozin()
                );
                break;
            case "load":
            case "save":
            case "set_random":
            case "tick":
            case "create":
            case "delete":
            case "assign":
            case "remove":
            case "add_condition":
            case "set":
            case "move":
            case "clean":
            case "purchase":
            case "list":
            case "info":
            case "help":
            case "add":
                break;
            default:
                break;
        }
    }
}
