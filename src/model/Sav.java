package model;

/**
 * Egy útnak egy adott sávját határozza meg, ami egymás után következő egységekből épül fel.
 */
public class Sav {
    ///Minden sáv legalább egy Utegysegből áll, melyhez a sáv további útegységei kapcsolódhatnak egymás után
    private Utegyseg elsoUtegyseg;

    ///Konstruktorok
    public Sav(Utegyseg utegyseg){
        this.elsoUtegyseg = utegyseg;
    }

    public Sav(){
        this(null);
    }

    ///Getter és setter
    public Utegyseg getElsoUtegyseg(){ return elsoUtegyseg; }
    public void setElsoUtegyseg(Utegyseg elsoUtegyseg){ this.elsoUtegyseg = elsoUtegyseg; }

}
