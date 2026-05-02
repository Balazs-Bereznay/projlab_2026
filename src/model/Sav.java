package model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Az út egy sávját leíró osztály. Felelős a sávhoz tartozó útegységek kezdőpontjának nyilvántartásáért.
 * A Sav az út felépítésében segédobjektumként jelenik meg, amely az első útegységen keresztül kapcsolódik
 * a sávot alkotó útegységek láncolatához.
 */
public class Sav implements ProtoEntitas {
    ///Minden sáv legalább egy Utegysegből áll, melyhez a sáv további útegységei kapcsolódhatnak egymás után. Ez a sáv első útegysége.
    private Utegyseg elsoUtegyseg;

    ///Konstruktorok
    public Sav(Utegyseg utegyseg){

        this.elsoUtegyseg = utegyseg;
    }

    public Sav(){

        this(null);
    }

    /**
     * Feldolgozza a sávra érkező, egyszerű prototípus-parancsokat.
     *
     * @param parancs a feldolgozandó parancs neve
     * @param args a parancs további paraméterei
     */
    @Override
    public void parancsFeldolgoz(String parancs, List<String> args) {
        if (parancs == null) {
            return;
        }

        switch (parancs) {
            case "info":
                String currentId = args.get(0);
                String euStr = (this.elsoUtegyseg != null) ? this.elsoUtegyseg.toString() : "null";

                String infoKimenet = """
                    %s:
                    elsoUtegyseg: %s
                    """.formatted(
                        currentId,
                        euStr
                );

                System.out.print(infoKimenet);
                System.out.println("Info displayed");
                break;

            default:
                break;
        }
    }

    @Override
    public void parancsFeldolgoz(String parancs, Utegyseg utegyseg, List<String> args) {
        if (parancs.equals("assign")) {
            this.elsoUtegyseg = utegyseg;// A sáv listájába felvesszük az útegységet
            System.out.println("Útegység sikeresen a sávhoz rendelve.");
        }
    }


    ///Getter és setter
    /**
     * Visszatér az elsoUtegyseg tagváltozó értékével.
     * @return A sávban lévő első útegység.
     */
    public Utegyseg getElsoUtegyseg(){
        return elsoUtegyseg;
    }

    /**
     * Az elsoUtegyseg nevű tagváltozónak az értékét beállítja a paraméterül kapott értékre.
     * @param elsoUtegyseg A sáv első útegységének beállított útegység.
     */
    public void setElsoUtegyseg(Utegyseg elsoUtegyseg){
        this.elsoUtegyseg = elsoUtegyseg;
    }

}
