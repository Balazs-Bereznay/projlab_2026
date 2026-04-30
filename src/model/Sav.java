package model;

/**
 * Az út egy sávját leíró osztály. Felelős a sávhoz tartozó útegységek kezdőpontjának nyilvántartásáért.
 * A Sav az út felépítésében segédobjektumként jelenik meg, amely az első útegységen keresztül kapcsolódik
 * a sávot alkotó útegységek láncolatához.
 */
public class Sav {
    ///Minden sáv legalább egy Utegysegből áll, melyhez a sáv további útegységei kapcsolódhatnak egymás után. Ez a sáv első útegysége.
    private Utegyseg elsoUtegyseg;

    ///Konstruktorok
    public Sav(Utegyseg utegyseg){

        this.elsoUtegyseg = utegyseg;
    }

    public Sav(){

        this(null);
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
