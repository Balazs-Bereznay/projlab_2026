package controller;

import model.*;
import java.util.*;

/**
 * A játék fő vezérlője. Kezeli a játékállapotot (térképet, járműveket, erőforrásokat),
 * a körök menetét (tervezési és szimulációs fázis), és értesíti a view-t az állapotváltozásokról.
 */
public class JatekController {

    public enum Fazis { TERVEZES, SZIMULACIO, JATEK_VEGE }

    private Terkep terkep;
    private Nyilvantarto nyilvantarto;
    private Bolt bolt;
    private List<Jatekos> jatekosok = new ArrayList<>();

    private Fazis aktualisFazis = Fazis.TERVEZES;
    private int aktualisKor = 1;
    public static final int TICKS_PER_KOR = 6;
    public static final int HAVAZAS_PER_TICK = 1;

    private Iranyithato kivalasztottJarmu = null;

    // Tervezési fázisban összegyűjtött útvonalak
    private final Map<Iranyithato, List<Utegyseg>> tervekMap = new LinkedHashMap<>();
    // Szimulációs fázisban aktív útvonalak
    private final Map<Iranyithato, List<Utegyseg>> aktivTervek = new LinkedHashMap<>();
    // Hol tart az adott jármű az aktív útvonalán
    private final Map<Iranyithato, Integer> szimulacioIndex = new LinkedHashMap<>();

    // Csomópontok megjelenítési pozíciói (koordinátáktól független modell!)
    private final Map<Csomopont, int[]> csomopontPoziciok = new LinkedHashMap<>();
    // Útegységek megjelenítési pozíciói (az utak mentén számítva)
    private final Map<Utegyseg, int[]> utegysegPoziciok = new LinkedHashMap<>();
    // Útegységek menetirányai (egységvektor * 1000)
    private final Map<Utegyseg, int[]> utegysegIranyok = new LinkedHashMap<>();

    private final List<Auto> autok = new ArrayList<>();
    private final List<Hokotro> hokotrók = new ArrayList<>();
    private final List<Busz> buszok = new ArrayList<>();

    private int szimulacioTickSzamlalo = 0;
    private final List<Runnable> listeners = new ArrayList<>();
    private final List<String> jatekosNevek = new ArrayList<>();

    public JatekController() {
    }

    public void ujJatek(int jatekosokSzama, String[] nevek) {
        jatekosNevek.clear();
        for (int i = 0; i < jatekosokSzama; i++) {
            String nev = (nevek != null && i < nevek.length && !nevek[i].isBlank())
                ? nevek[i] : "Játékos " + (i + 1);
            jatekosNevek.add(nev);
        }
        ujJatek(jatekosokSzama);
    }

    public void ujJatek(int jatekosokSzama) {
        terkep = new Terkep();
        nyilvantarto = new Nyilvantarto(500, 100, 50, 0);
        bolt = new Bolt();
        bolt.setNyilvantarto(nyilvantarto);
        bolt.setSoAr(50);
        bolt.setBiokerozinAr(100);
        bolt.setSeproAr(50);
        bolt.setHanyoAr(80);
        bolt.setJegtoroAr(100);
        bolt.setSoszoroAr(150);
        bolt.setSarkanyAr(300);
        bolt.setSebessegfejlesztesAr(150);
        bolt.setTapadasfejlesztesAr(120);
        bolt.setHozamfejlesztesAr(200);

        jatekosok.clear(); autok.clear(); hokotrók.clear(); buszok.clear();
        tervekMap.clear(); aktivTervek.clear(); szimulacioIndex.clear();
        csomopontPoziciok.clear(); utegysegPoziciok.clear();
        szimulacioTickSzamlalo = 0;
        aktualisFazis = Fazis.TERVEZES;
        aktualisKor = 1;
        kivalasztottJarmu = null;

        // Térkép felépítése:
        // cs1(A) --ut1-- cs2(B)
        //   |               |
        //  ut4             ut2
        //   |               |
        // cs4(D) --ut3-- cs3(C)
        Csomopont cs1 = makeCsomopont("A", false, true);   // busz megálló
        Csomopont cs2 = makeCsomopont("B", true, false);   // célpont
        Csomopont cs3 = makeCsomopont("C", false, true);   // busz megálló
        Csomopont cs4 = makeCsomopont("D", true, false);   // célpont

        Ut ut1 = makeUt(cs1, cs2, 2, 5, false);
        Ut ut2 = makeUt(cs2, cs3, 2, 4, false);
        Ut ut3 = makeUt(cs3, cs4, 2, 5, false);
        Ut ut4 = makeUt(cs4, cs1, 2, 4, false);

        terkep.addCsomopont(cs1); terkep.addCsomopont(cs2);
        terkep.addCsomopont(cs3); terkep.addCsomopont(cs4);
        terkep.addUt(ut1); terkep.addUt(ut2);
        terkep.addUt(ut3); terkep.addUt(ut4);

        csomopontPoziciok.put(cs1, new int[]{100, 140});
        csomopontPoziciok.put(cs2, new int[]{500, 140});
        csomopontPoziciok.put(cs3, new int[]{500, 380});
        csomopontPoziciok.put(cs4, new int[]{100, 380});
        szamolUtegysegPoziciok();

        for (int i = 0; i < jatekosokSzama; i++) {
            Jatekos j = new Jatekos(nyilvantarto);
            j.setBolt(bolt);
            jatekosok.add(j);
        }

        // Hókotró (1. játékos)
        Hokotro hk = new Hokotro(new Sopro());
        hk.setSebesseg(1);
        hk.setTapadas(50);
        hk.setNyilvantarto(nyilvantarto);
        placeJarmu(hk, ut1.getSavok().get(0).getElsoUtegyseg());
        hokotrók.add(hk);
        jatekosok.get(0).getFlotta().add(hk);
        tervekMap.put(hk, new ArrayList<>());

        // Busz (1. játékos)
        Busz busz = new Busz();
        busz.setSebesseg(1);
        busz.setTapadas(40);
        busz.setNyilvantarto(nyilvantarto);
        busz.setBevetel(20);
        busz.setVegallomas1(cs1);
        busz.setVegallomas2(cs3);
        placeJarmu(busz, ut3.getSavok().get(0).getElsoUtegyseg());
        buszok.add(busz);
        jatekosok.get(0).getFlotta().add(busz);
        tervekMap.put(busz, new ArrayList<>());

        // NPC autó
        Auto a1 = new Auto();
        a1.setSebesseg(1);
        a1.setTapadas(30);
        a1.setNyilvantarto(nyilvantarto);
        setupNpcAuto(a1, cs4, cs2, ut4);
        autok.add(a1);

        ertesitListeners();
    }

    private Csomopont makeCsomopont(String id, boolean celpont, boolean buszmegallo) {
        Csomopont cs = new Csomopont();
        cs.setAzonosito(id);
        cs.setCelpont(celpont);
        cs.setBuszmegallo(buszmegallo);
        return cs;
    }

    private Ut makeUt(Csomopont vp1, Csomopont vp2, int savokSzama, int uePerSav, boolean alagut) {
        Ut ut = new Ut();
        ut.setVegpont1(vp1);
        ut.setVegpont2(vp2);
        ut.setAlagut(alagut);
        vp1.addUt(ut);
        vp2.addUt(ut);

        for (int i = 0; i < savokSzama; i++) {
            Sav sav = new Sav();
            // Páros sávok: vp1→vp2, páratlan sávok: vp2→vp1
            sav.setVegCsomopont(i % 2 == 0 ? vp2 : vp1);
            Utegyseg prev = null;
            for (int j = 0; j < uePerSav; j++) {
                Utegyseg ue = new Utegyseg();
                ue.setSav(sav);
                if (prev == null) sav.setElsoUtegyseg(ue);
                else prev.setKovetkezoUtegyseg(ue);
                prev = ue;
            }
            ut.addSav(sav);
        }

        // Bal/jobb szomszéd beállítása a sávok között
        for (int i = 0; i < savokSzama - 1; i++) {
            Utegyseg ue1 = ut.getSavok().get(i).getElsoUtegyseg();
            Utegyseg ue2 = ut.getSavok().get(i + 1).getElsoUtegyseg();
            while (ue1 != null && ue2 != null) {
                ue1.setJobbUtegyseg(ue2);
                ue2.setBalUtegyseg(ue1);
                ue1 = ue1.getKovetkezoUtegyseg();
                ue2 = ue2.getKovetkezoUtegyseg();
            }
        }
        return ut;
    }

    private void placeJarmu(Jarmu j, Utegyseg ue) {
        if (ue != null && ue.getJarmu() == null) {
            ue.setJarmu(j);
            j.setUtegyseg(ue);
        }
    }

    private void setupNpcAuto(Auto auto, Csomopont start, Csomopont end, Ut startUt) {
        auto.setKezdopont(start);
        auto.setCelpont(end);
        List<Ut> path = terkep.utvonalTervezes(start, end);
        for (Ut ut : path) auto.addKijeloltUt(ut);
        // Azt a sávot keressük, amelyik start csomópontból indul (vegCsomopont != start)
        for (Sav sav : startUt.getSavok()) {
            if (sav.getVegCsomopont() != start) {
                Utegyseg ue = sav.getElsoUtegyseg();
                while (ue != null) {
                    if (ue.getJarmu() == null) {
                        placeJarmu(auto, ue);
                        return;
                    }
                    ue = ue.getKovetkezoUtegyseg();
                }
            }
        }
    }

    private void szamolUtegysegPoziciok() {
        utegysegPoziciok.clear();
        utegysegIranyok.clear();
        final int LANE_OFFSET = 22;
        for (Ut ut : terkep.getElLista()) {
            Csomopont vp1 = ut.getVegpont1();
            Csomopont vp2 = ut.getVegpont2();
            if (vp1 == null || vp2 == null) continue;
            int[] p1 = csomopontPoziciok.get(vp1);
            int[] p2 = csomopontPoziciok.get(vp2);
            if (p1 == null || p2 == null) continue;

            double dx = p2[0] - p1[0];
            double dy = p2[1] - p1[1];
            double len = Math.sqrt(dx * dx + dy * dy);
            double nx = dx / len, ny = dy / len;
            double px = -ny, py = nx;

            List<Sav> savok = ut.getSavok();
            int n = savok.size();
            for (int si = 0; si < n; si++) {
                Sav sav = savok.get(si);
                double offset = (si - (n - 1) / 2.0) * LANE_OFFSET;
                boolean forward = (sav.getVegCsomopont() == vp2);

                int dirX = (int)((forward ? nx : -nx) * 1000);
                int dirY = (int)((forward ? ny : -ny) * 1000);

                List<Utegyseg> ueList = new ArrayList<>();
                Utegyseg cur = sav.getElsoUtegyseg();
                while (cur != null) {
                    ueList.add(cur);
                    cur = cur.getKovetkezoUtegyseg();
                }
                int m = ueList.size();
                for (int ui = 0; ui < m; ui++) {
                    double t = (ui + 0.5) / m;
                    double progress = forward ? t : (1.0 - t);
                    double adjustedProgress = 0.05 + progress * 0.9;
                    int x = (int) (p1[0] + dx * adjustedProgress + px * offset);
                    int y = (int) (p1[1] + dy * adjustedProgress + py * offset);
                    utegysegPoziciok.put(ueList.get(ui), new int[]{x, y});
                    utegysegIranyok.put(ueList.get(ui), new int[]{dirX, dirY});
                }
            }
        }
    }

    /** Tervezési fázisban kijelöl egy útegységet a kiválasztott jármű útvonalán, ha érvényes szomszédos lépés. */
    public void utegysegKijelol(Utegyseg ue) {
        if (aktualisFazis != Fazis.TERVEZES || kivalasztottJarmu == null) return;
        List<Utegyseg> terv = tervekMap.get(kivalasztottJarmu);
        if (terv == null) return;
        if (terv.contains(ue)) {
            terv.remove(ue);
        } else {
            Utegyseg ref = terv.isEmpty()
                ? ((Jarmu) kivalasztottJarmu).getUtegyseg()
                : terv.get(terv.size() - 1);
            if (ref != null && validKovetkezok(ref).contains(ue)) {
                terv.add(ue);
            }
        }
        ertesitListeners();
    }

    private Set<Utegyseg> validKovetkezok(Utegyseg ref) {
        Set<Utegyseg> eredmeny = new HashSet<>();
        Utegyseg kovetkezo = ref.getKovetkezoUtegyseg();
        if (kovetkezo != null) {
            eredmeny.add(kovetkezo);
        } else {
            // Sáv végén: a vegCsomóponttól INDULÓ (nem oda érkező) sávok első útegységei érvényesek
            Sav sav = ref.getSav();
            if (sav != null && sav.getVegCsomopont() != null) {
                Csomopont vegCsp = sav.getVegCsomopont();
                for (Ut ut : vegCsp.getUtLista()) {
                    for (Sav s : ut.getSavok()) {
                        if (s.getVegCsomopont() != vegCsp && s.getElsoUtegyseg() != null) {
                            eredmeny.add(s.getElsoUtegyseg());
                        }
                    }
                }
            }
        }
        return eredmeny;
    }

    /** Kiválaszt egy járművet az útvonal-tervezéshez. */
    public void jarmuvKivalaszt(Iranyithato j) {
        kivalasztottJarmu = j;
        ertesitListeners();
    }

    /** Lezárja a tervezési fázist és elindítja a szimulációt. */
    public void korBefejezes() {
        if (aktualisFazis != Fazis.TERVEZES) return;
        aktivTervek.clear();
        for (Map.Entry<Iranyithato, List<Utegyseg>> e : tervekMap.entrySet()) {
            aktivTervek.put(e.getKey(), new ArrayList<>(e.getValue()));
            szimulacioIndex.put(e.getKey(), 0);
        }
        for (List<Utegyseg> list : tervekMap.values()) list.clear();
        szimulacioTickSzamlalo = 0;
        aktualisFazis = Fazis.SZIMULACIO;
        kivalasztottJarmu = null;
        ertesitListeners();
    }

    /** Lefuttat egy szimulációs ticket. */
    public void szimulacioTick() {
        if (aktualisFazis != Fazis.SZIMULACIO) return;

        // 1. Havazás és sóolvasztás
        for (Ut ut : terkep.getElLista()) {
            if (!ut.getAlagut()) {
                for (Sav sav : ut.getSavok()) {
                    Utegyseg ue = sav.getElsoUtegyseg();
                    while (ue != null) {
                        ue.soOlvasztas();
                        ue.havazas(HAVAZAS_PER_TICK);
                        ue = ue.getKovetkezoUtegyseg();
                    }
                }
            }
        }

        // 2. Irányítható járművek mozgatása a tervezett útvonalon
        for (Hokotro hk : hokotrók) {
            moveAlongTerv(hk);
            if (hk.getUtegyseg() != null) hk.takarit();
        }
        for (Busz b : buszok) {
            moveAlongTerv(b);
        }

        // 3. NPC autók mozgatása
        for (Auto a : autok) {
            a.lep();
        }

        // 4. Balesetek keresése
        for (Ut ut : terkep.getElLista()) {
            ut.balesetetKeres();
        }

        // 5. Játék vége ellenőrzés
        if (nyilvantarto.ellenorizJatekVege()) {
            aktualisFazis = Fazis.JATEK_VEGE;
            ertesitListeners();
            return;
        }

        szimulacioTickSzamlalo++;
        if (szimulacioTickSzamlalo >= TICKS_PER_KOR) {
            aktualisKor++;
            aktualisFazis = Fazis.TERVEZES;
        }
        ertesitListeners();
    }

    private void moveAlongTerv(Iranyithato vehicle) {
        List<Utegyseg> terv = aktivTervek.get(vehicle);
        if (terv == null || terv.isEmpty()) return;
        int idx = szimulacioIndex.getOrDefault(vehicle, 0);
        if (idx < terv.size()) {
            Utegyseg target = terv.get(idx);
            if (target.ralep((Jarmu) vehicle)) {
                szimulacioIndex.put(vehicle, idx + 1);
            }
        }
    }

    /** Törli a megadott jármű tervezett útvonalát. */
    public void tervTorol(Iranyithato j) {
        List<Utegyseg> terv = tervekMap.get(j);
        if (terv != null) terv.clear();
        ertesitListeners();
    }

    public void addAllapotValtozoListener(Runnable r) {
        listeners.add(r);
    }

    public void clearListeners() {
        listeners.clear();
    }

    private void ertesitListeners() {
        for (Runnable r : listeners) r.run();
    }

    // --- Getterek ---
    public Terkep getTerkep() { return terkep; }
    public Nyilvantarto getNyilvantarto() { return nyilvantarto; }
    public Bolt getBolt() { return bolt; }
    public List<Jatekos> getJatekosok() { return jatekosok; }
    public Fazis getAktualisFazis() { return aktualisFazis; }
    public int getAktualisKor() { return aktualisKor; }
    public int getSzimulacioTickSzamlalo() { return szimulacioTickSzamlalo; }
    public Iranyithato getKivalasztottJarmu() { return kivalasztottJarmu; }
    public Map<Iranyithato, List<Utegyseg>> getTervekMap() { return tervekMap; }
    public Map<Csomopont, int[]> getCsomopontPoziciok() { return csomopontPoziciok; }
    public Map<Utegyseg, int[]> getUtegysegPoziciok() { return utegysegPoziciok; }
    public Map<Utegyseg, int[]> getUtegysegIranyok() { return utegysegIranyok; }
    public List<Auto> getAutok() { return autok; }
    public List<Hokotro> getHokotrók() { return hokotrók; }
    public List<Busz> getBuszok() { return buszok; }

    public String getAktivJatekosNev() {
        if (jatekosNevek.isEmpty()) return "Játékos 1";
        return jatekosNevek.get(0);
    }

    public int getJarmuekOsszesenSzama() {
        return hokotrók.size() + buszok.size();
    }

    public String getAktivHokotroFejNev() {
        Iranyithato kiv = kivalasztottJarmu;
        if (kiv instanceof Hokotro && ((Hokotro) kiv).getFej() != null) {
            return ((Hokotro) kiv).getFej().getClass().getSimpleName();
        }
        if (!hokotrók.isEmpty() && hokotrók.get(0).getFej() != null) {
            return hokotrók.get(0).getFej().getClass().getSimpleName();
        }
        return "-";
    }
}
