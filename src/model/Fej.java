package model;

import java.util.List;

/**
 * A hókotróra felszerelhető kotrófejek absztrakt ősosztálya.
 * Meghatározza, hogy az adott fej milyen tulajdonságokkal rendelkezik,
 * és milyen módon befolyásolja az útviszonyokat.
 */
public abstract class Fej implements ProtoEntitas {

    /**
     * Végrehajtja a hókotróra szerelt fejhez tartozó takarítási logikát
     * a megadott útegységen.
     * @param utegyseg Az aktuális útszakasz egysége, amelyen a takarítás történik.
     */
    public abstract boolean hasznal(Utegyseg utegyseg);

    @Override
    public void parancsFeldolgozHokotroval(String parancs, Hokotro hokotro, List<String> args) {
        if ("assign".equals(parancs)) {
            hokotro.setFej(this);
            System.out.println("Fej sikeresen a hókotróra szerelve.");
        } else if ("remove".equals(parancs)) {
            if (hokotro.getFej() == this) {
                hokotro.setFej(null);
                System.out.println("Fej eltávolítva a hókotróról.");
            }
        }
    }
}