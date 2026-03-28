package model;

import model.Iranyithato;
import model.Nyilvantarto;

import java.util.ArrayList;
import java.util.List;

/**
 * A felhasználót jelképező osztály, aki a hókotrók és buszok flottáját kezeli,
 * dönthet a jármű útvonalának kijelölésében, illetve, hogy a boltban milyen
 * tranzakciókat hajt végre.
 */
public class Jatekos {

    /**
     * A játékoshoz tartozó irányítható járművek (hókotrók, buszok) listája.
     */
    private List<Iranyithato> flotta;

    /**
     * Kapcsolat a nyilvántartó rendszerrel, amely a globális adatokat kezeli.
     */
    private Nyilvantarto nyilvantarto;

    public Jatekos() {
        this.flotta = new ArrayList<>();
    }

    public Jatekos(Nyilvantarto nyilvantarto) {
        this.flotta = new ArrayList<>();
        this.nyilvantarto = nyilvantarto;
    }

    public List<Iranyithato> getFlotta() {
        return flotta;
    }

    public void setFlotta(List<Iranyithato> flotta) {
        this.flotta = flotta;
    }

    public Nyilvantarto getNyilvantarto() {
        return nyilvantarto;
    }

    public void setNyilvantarto(Nyilvantarto nyilvantarto) {
        this.nyilvantarto = nyilvantarto;
    }
}