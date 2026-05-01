package model;

import java.util.HashMap;
import java.util.Map;

/**
 * A prototípus ezt az osztályt fogja használni a játék során létrehozott objektumok struktúrált kezelésére.
 * A rendszerben található összes entitás központi nyilvántartója.
 * Segítségével típus és egyedi azonosító alapján érhetők el az objektumok.
 */
class ObjektumKatalogus {
    // Típusonként csoportosított belső térképek: Class -> (ID -> ProtoEntitas)
    private final Map<Class<?>, Map<String, ProtoEntitas>> kontener = new HashMap<>();

    /**
     * Entitás hozzáadása. Csak olyan objektumot fogad el, ami ProtoEntitas.
     */
    public void hozzaad(String id, ProtoEntitas entitas) {
        kontener.computeIfAbsent(entitas.getClass(), k -> new HashMap<>())
                .put(id, entitas);
    }

    /**
     * Globális keresés ID alapján.
     * Mivel az ID-k egyediek, végigfésüljük a típus-csoportokat.
     */
    public ProtoEntitas keres(String id) {
        for (Map<String, ProtoEntitas> alcsoport : kontener.values()) {
            if (alcsoport.containsKey(id)) {
                return alcsoport.get(id);
            }
        }
        return null;
    }

}