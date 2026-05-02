package model;

import java.util.*;

/**
 * A prototípus ezt az osztályt fogja használni a játék során létrehozott objektumok struktúrált kezelésére.
 * A rendszerben található összes entitás központi nyilvántartója.
 * Segítségével típus és egyedi azonosító alapján érhetők el az objektumok.
 */
class ObjektumKatalogus {
    // Típusonként csoportosított belső térképek: Class -> (ID -> ProtoEntitas)
    private final Map<Class<?>, Map<String, ProtoEntitas>> kontener = new HashMap<>();

    // A típus-fordító Map
    private final Map<String, Class<? extends ProtoEntitas>> tipusTerkepe = Map.ofEntries(
            Map.entry("Utegyseg", Utegyseg.class),
            Map.entry("Csomopont", Csomopont.class),
            Map.entry("Ut", Ut.class),
            Map.entry("Sav", Sav.class),
            Map.entry("Hokotro", Hokotro.class),
            Map.entry("Auto", Auto.class),
            Map.entry("Busz", Busz.class),
            Map.entry("Zuzalekszoro", Zuzalekszoro.class),
            Map.entry("Sopro", Sopro.class),
            Map.entry("Jegtoro", Jegtoro.class),
            Map.entry("Sarkany", Sarkany.class),
            Map.entry("Soszoro", Soszoro.class),
            Map.entry("Hanyo", Hanyo.class),
            Map.entry("Nyilvantarto", Nyilvantarto.class),
            Map.entry("Jatekos", Jatekos.class),
            Map.entry("Bolt", Bolt.class)
    );

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
     * Visszaadja egy katalógusban szereplő entitásnak az azonosítóját (id)
     * @param entitas Az az entitás, aminek az azonosítóját megkeresi és visszaadja
     * @return A keresett entitás azonosítója
     */
    public String getId(ProtoEntitas entitas) {
        if (entitas == null) return "null";
        for (Map<String, ProtoEntitas> map : kontener.values()) {
            for (Map.Entry<String, ProtoEntitas> entry : map.entrySet()) {
                if (entry.getValue() == entitas) {
                    return entry.getKey();
                }
            }
        }
        return "null";
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

    /**
     * Ha a tipusNev null, az összes létező objektum ID-ját visszaadja.
     * Ha van típus megadva, csak az adott típushoz tartozókat.
     */
    public List<String> osszesIdLeker(String tipusNev) {
        List<String> eredmeny = new ArrayList<>();

        if (tipusNev == null) {
            // Végigmegyünk az összes típuson (összes belső Map-en)
            for (Map<String, ProtoEntitas> alcsoport : kontener.values()) {
                eredmeny.addAll(alcsoport.keySet());
            }
        } else {
            Class<? extends ProtoEntitas> osztaly = tipusTerkepe.get(tipusNev);

            if (osztaly != null && kontener.containsKey(osztaly)) {
                eredmeny.addAll(kontener.get(osztaly).keySet());
            }
        }

        Collections.sort(eredmeny); // Mindig legyen rendezett a lista
        return eredmeny;
    }
}