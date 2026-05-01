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
