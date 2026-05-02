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

    public boolean torol(String id) {
        for(Map<String, ProtoEntitas> map : kontener.values()){

            if(map.containsKey(id)){

                map.remove(id);
                return true;
            }

        }
        return false;
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

    /**
     * Visszaadja az adott típusú összes entitást.
     */
    @SuppressWarnings("unchecked")
    public <T extends ProtoEntitas> java.util.List<T> osszesOfType(Class<T> type) {
        Map<String, ProtoEntitas> alcsoport = kontener.get(type);
        if (alcsoport == null) return new java.util.ArrayList<>();
        return new java.util.ArrayList<>((java.util.Collection<T>) (java.util.Collection<?>) alcsoport.values());
    }

    /**
     * Kiüríti a külső Map-et, így minden referencia megszűnik
     */
    public void alaphelyzet() {
        kontener.clear();

        //System.out.println("Az objektum katalógus tartalmát töröltem.");
    }

}