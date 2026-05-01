package model;

import java.util.List;

public interface ProtoEntitas {
    // Sima parancsok (set, move, clean, stb.)
    void parancsFeldolgoz(String parancs, List<String> args);

    // Kapcsolati parancsok (assign, remove) - alapértelmezett hibaüzenettel
    default void parancsFeldolgoz(String parancs, ProtoEntitas masik, List<String> args) {
        System.out.println("Hiba: " + this.getClass().getSimpleName() +
                " nem tud mit kezdeni a(z) '" + masik.getClass().getSimpleName() +
                "' típussal a(z) " + parancs + " parancs során.");
    }

    default void parancsFeldolgoz(String parancs, Jarmu jarmu, List<String> args){
        throw new ClassCastException("Hiba: " + this.getClass() + "nem tamogatja ezt a hivast.");
    }
    default void parancsFeldolgoz(String parancs, Fej fej, List<String> args){
        throw new ClassCastException("Hiba: " + this.getClass() + "nem tamogatja ezt a hivast.");
    }
    default void parancsFeldolgoz(String parancs, Nyilvantarto nyilvantarto, List<String> args){
        throw new ClassCastException("Hiba: " + this.getClass() + "nem tamogatja ezt a hivast.");
    }
    default void parancsFeldolgoz(String parancs, Utegyseg utegyseg, List<String> args){
        throw new ClassCastException("Hiba: " + this.getClass() + "nem tamogatja ezt a hivast.");
    }
    default void parancsFeldolgoz(String parancs, Sav sav, List<String> args){
        throw new ClassCastException("Hiba: " + this.getClass() + "nem tamogatja ezt a hivast.");
    }
    default void parancsFeldolgoz(String parancs, Ut ut,  List<String> args){
        throw new ClassCastException("Hiba: " + this.getClass() + "nem tamogatja ezt a hivast.");
    }
    default void parancsFeldolgoz(String parancs, Csomopont csomopont, List<String> args){
        throw new ClassCastException("Hiba: " + this.getClass() + "nem tamogatja ezt a hivast.");
    }
}
