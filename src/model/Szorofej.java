package model;

/**
 * Interfész az erőforrással (só, biokerozin) dolgozó tisztítófejek számára.
 * Definiálja az aktiválási folyamatot az adott útegységen.
 */
public interface Szorofej {
    /**
     * Elindítja az erőforrás használatának folyamatát a megadott útegységen.
     *  @param utegyseg Az útegység, ahol az erőforrás felhasználásra kerül.
     * @return Igaz, ha rendelkezésre áll a szükséges mennyiségű erőforrás, egyébként hamis.
     */
    boolean aktival(Utegyseg utegyseg);
}
