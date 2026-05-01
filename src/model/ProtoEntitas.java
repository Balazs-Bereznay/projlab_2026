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
}
