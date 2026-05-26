package controller;

import common.Megfigyelo;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.Rectangle;
import java.util.*;

public class JatekController implements Megfigyelo {

    private JatekAblak ablak;

    private Terkep terkep;
    private Nyilvantarto nyilvantarto;
    private Bolt bolt;
    private final List<Jatekos> jatekosok = new ArrayList<>();
    private int aktualisJatekosIndex = 0;
    private int aktualisJarmuIndex = 0;

    private Fazis aktualisFazis = Fazis.TERVEZES;
    private int aktualisKor = 1;

    private Iranyithato kivalasztottJarmu = null;

    private final Map<Iranyithato, List<Utegyseg>> tervekMap   = new LinkedHashMap<>();
    private final Map<Iranyithato, List<Utegyseg>> aktivTervek = new LinkedHashMap<>();
    private final Map<Iranyithato, Integer>        szimulacioIndex = new LinkedHashMap<>();
    private final Set<Utegyseg> kijelolhetoUtegysegek = new LinkedHashSet<>();

    private final Map<Csomopont, int[]> csomopontPoziciok = new LinkedHashMap<>();

    private final List<Auto>    autok    = new ArrayList<>();
    private final List<Hokotro> hokotrók = new ArrayList<>();
    private final List<Busz>    buszok   = new ArrayList<>();

    private PalyaLayout layout;
    private Map<Jarmu, JarmuView> jarmuViewk = new LinkedHashMap<>();

    private final List<Runnable> listeners    = new ArrayList<>();
    private final List<String>   jatekosNevek = new ArrayList<>();

    public static final int HAVAZAS_PER_TICK = 1;
    private static final int UTKOZES_BLOKK_KOROK = 2;
    private static final int SZIMULACIO_ANIMACIO_MS = 280;
    private static final double GUI_ALAP_MEGCSUSZAS_ESELY = 0.35;

    private final List<UtkozesAllapot> utkozesek = new ArrayList<>();
    private final Set<Iranyithato> szimulacioLezartTervek = new HashSet<>();
    private final Set<Auto> szimulacioLepettAutok = new HashSet<>();
    private javax.swing.Timer szimulacioTimer;
    private boolean szimulacioKornyezetFrissitve = false;

    private static class UtkozesAllapot {
        private final Jarmu elso;
        private final Jarmu masodik;
        private final Utegyseg elsoUtegyseg;
        private final Utegyseg masodikUtegyseg;
        private final boolean elsoEredetilegBlokkolt;
        private final boolean masodikEredetilegBlokkolt;
        private int hatralevoKor;

        private UtkozesAllapot(Jarmu elso, Jarmu masodik, int hatralevoKor) {
            this.elso = elso;
            this.masodik = masodik;
            this.elsoUtegyseg = elso != null ? elso.getUtegyseg() : null;
            this.masodikUtegyseg = masodik != null ? masodik.getUtegyseg() : null;
            this.elsoEredetilegBlokkolt = elsoUtegyseg != null && elsoUtegyseg.getBlokkolt();
            this.masodikEredetilegBlokkolt = masodikUtegyseg != null && masodikUtegyseg.getBlokkolt();
            this.hatralevoKor = hatralevoKor;
        }
    }

    public JatekController() {}
    public JatekController(Prototipus proto) {}

    public void setAblak(JatekAblak ablak) {
        this.ablak = ablak;
    }

    // -------------------------------------------------------------------------
    // Új játék
    // -------------------------------------------------------------------------

    public void ujJatek(List<JatekosKonfig> konfig) {
        resetAllapot();
        setupTerkep();

        jatekosNevek.clear();
        for (JatekosKonfig k : konfig) {
            String nev = (k.nev != null && !k.nev.isBlank()) ? k.nev : "Játékos";
            jatekosNevek.add(nev);
            Jatekos j = new Jatekos(nyilvantarto);
            j.setBolt(bolt);
            jatekosok.add(j);

            if ("Buszvezető".equals(k.szerep)) {
                Busz b = ujBusz();
                buszok.add(b);
                j.getFlotta().add(b);
                tervekMap.put(b, new ArrayList<>());
            } else {
                Hokotro hk = ujHokotro();
                hokotrók.add(hk);
                j.getFlotta().add(hk);
                tervekMap.put(hk, new ArrayList<>());
            }
        }

        if (hokotrók.isEmpty()) {
            Hokotro hk = ujHokotro();
            hokotrók.add(hk);
            if (!jatekosok.isEmpty()) jatekosok.get(0).getFlotta().add(hk);
            tervekMap.put(hk, new ArrayList<>());
        }

        npcAutoLetrehozas();
        inditas();
    }

    public void ujJatek(int jatekosokSzama, String[] nevek) {
        List<JatekosKonfig> konfig = new ArrayList<>();
        for (int i = 0; i < jatekosokSzama; i++) {
            String nev = (nevek != null && i < nevek.length && !nevek[i].isBlank())
                         ? nevek[i] : "Játékos " + (i + 1);
            konfig.add(new JatekosKonfig(nev, "Hókotró"));
        }
        ujJatek(konfig);
    }

    public void ujJatek(int jatekosokSzama) {
        ujJatek(jatekosokSzama, null);
    }

    private void resetAllapot() {
        jatekosok.clear(); jatekosNevek.clear();
        autok.clear(); hokotrók.clear(); buszok.clear();
        tervekMap.clear(); aktivTervek.clear(); szimulacioIndex.clear();
        utkozesek.clear();
        szimulacioLezartTervek.clear();
        szimulacioLepettAutok.clear();
        if (szimulacioTimer != null) {
            szimulacioTimer.stop();
            szimulacioTimer = null;
        }
        szimulacioKornyezetFrissitve = false;
        kijelolhetoUtegysegek.clear();
        csomopontPoziciok.clear();
        aktualisFazis = Fazis.TERVEZES;
        aktualisKor = 1;
        aktualisJatekosIndex = 0;
        aktualisJarmuIndex = 0;
        kivalasztottJarmu = null;
        listeners.clear();
    }

    private void setupTerkep() {
        terkep = new Terkep();
        nyilvantarto = new Nyilvantarto(500, 100, 50, 0);
        bolt = new Bolt();
        bolt.setNyilvantarto(nyilvantarto);
        bolt.setSoAr(5);
        bolt.setBiokerozinAr(10);
        bolt.sethokotroAr(200);
        bolt.setSeproAr(50);
        bolt.setHanyoAr(80);
        bolt.setJegtoroAr(100);
        bolt.setSoszoroAr(150);
        bolt.setSarkanyAr(300);
        bolt.setZuzalekAr(10);
        bolt.setZuzalekszoroAr(120);
        bolt.setSebessegfejlesztesAr(150);
        bolt.setTapadasfejlesztesAr(120);
        bolt.setHozamfejlesztesAr(200);

        //  cs1(A) --ut1-- cs2(B)
        //    |                |
        //   ut4              ut2
        //    |                |
        //  cs4(D) --ut3-- cs3(C)
        Csomopont cs1 = makeCsomopont("A", false, true);
        Csomopont cs2 = makeCsomopont("B", true,  false);
        Csomopont cs3 = makeCsomopont("C", false, true);
        Csomopont cs4 = makeCsomopont("D", true,  false);
        Csomopont cs5 = makeCsomopont("E", false, false);

        Ut ut1  = makeUt(cs1, cs2, 2, 5, false);
        Ut ut2  = makeUt(cs2, cs3, 2, 4, false);
        Ut ut3  = makeUt(cs3, cs4, 2, 5, false);
        Ut ut4  = makeUt(cs4, cs1, 2, 4, false);
        Ut utEA = makeUt(cs5, cs1, 2, 3, false);
        Ut utEB = makeUt(cs5, cs2, 2, 3, false);
        Ut utEC = makeUt(cs5, cs3, 2, 3, false);
        Ut utED = makeUtDupla(cs5, cs4, 3, false);

        terkep.addCsomopont(cs1); terkep.addCsomopont(cs2);
        terkep.addCsomopont(cs3); terkep.addCsomopont(cs4);
        terkep.addCsomopont(cs5);
        terkep.addUt(ut1);  terkep.addUt(ut2);
        terkep.addUt(ut3);  terkep.addUt(ut4);
        terkep.addUt(utEA); terkep.addUt(utEB);
        terkep.addUt(utEC); terkep.addUt(utED);

        csomopontPoziciok.put(cs1, new int[]{  0,   0});
        csomopontPoziciok.put(cs2, new int[]{600,   0});
        csomopontPoziciok.put(cs3, new int[]{600, 400});
        csomopontPoziciok.put(cs4, new int[]{  0, 400});
        csomopontPoziciok.put(cs5, new int[]{300, 200});
    }

    private Hokotro ujHokotro() {
        Hokotro hk = new Hokotro(new Sopro());
        hk.setSebesseg(8);
        hk.setTapadas(50);
        hk.setZuzalekLimit(10);
        hk.setNyilvantarto(nyilvantarto);
        placeOnFreeUtegyseg(hk);
        return hk;
    }

    private Busz ujBusz() {
        Busz b = new Busz();
        b.setSebesseg(1);
        b.setTapadas(40);
        b.setNyilvantarto(nyilvantarto);
        b.setBevetel(20);
        List<Csomopont> csList = terkep.getCsomopontLista();
        if (csList.size() >= 3) {
            b.setVegallomas1(csList.get(0));
            b.setVegallomas2(csList.get(2));
            if (csList.size() >= 4) {
                csList.get(1).setBuszmegallo(true);
                csList.get(3).setBuszmegallo(true);
                b.setMegallokLista(Arrays.asList(csList.get(1), csList.get(3)));
            }
        }
        placeOnFreeUtegyseg(b);
        return b;
    }

    private void npcAutoLetrehozas() {
        List<Csomopont> csList = terkep.getCsomopontLista();
        List<Ut> utLista = terkep.getElLista();
        if (csList.size() < 4 || utLista.size() < 4) return;

        Auto a1 = new Auto();
        a1.setSebesseg(1);
        a1.setTapadas(30);
        a1.setNyilvantarto(nyilvantarto);
        Csomopont startCs = csList.get(3); // D
        a1.setKezdopont(startCs);
        a1.setCelpont(startCs);

        // Circular route: ut4(D→A), ut1(A→B), ut2(B→C), ut3(C→D)
        Ut ut4 = utLista.get(3);
        Ut ut1 = utLista.get(0);
        Ut ut2 = utLista.get(1);
        Ut ut3 = utLista.get(2);
        for (Ut ut : new Ut[]{ut4, ut1, ut2, ut3}) a1.addKijeloltUt(ut);
        a1.setUtonTolthetoIdo(Integer.MAX_VALUE / 2);

        placeJarmuAzUtKezdoSavjara(a1, ut4, startCs);
        autok.add(a1);

        Auto a2 = new Auto();
        a2.setSebesseg(1);
        a2.setTapadas(30);
        a2.setNyilvantarto(nyilvantarto);
        a2.setKezdopont(startCs);
        a2.setCelpont(startCs);

        // Opposite circular route: ut3(D->C), ut2(C->B), ut1(B->A), ut4(A->D)
        for (Ut ut : new Ut[]{ut3, ut2, ut1, ut4}) a2.addKijeloltUt(ut);
        a2.setUtonTolthetoIdo(Integer.MAX_VALUE / 2);

        placeJarmuAzUtKezdoSavjara(a2, ut3, startCs);
        autok.add(a2);
    }

    private void inditas() {
        if (ablak != null) {
            clearListeners();
            addAllapotValtozoListener(() -> ablak.frissitJatek());
            if (bolt != null) bolt.addObserver(ablak.getBoltPanel());
            ablak.mutatJatek();
            ablak.regisztraljBemenetKezelo();
            // Run after layout is finalized so panel has its real size for centering
            SwingUtilities.invokeLater(() -> {
                osszekot();
                valasszAktualisJarmuvet();
                ertesitListeners();
            });
        } else {
            valasszAktualisJarmuvet();
            ertesitListeners();
        }
    }

    // -------------------------------------------------------------------------
    // Observer kapcsolás
    // -------------------------------------------------------------------------

    private Map<Csomopont, int[]> computeDisplayPoziciok(int panelW, int panelH) {
        if (csomopontPoziciok.isEmpty()) return csomopontPoziciok;
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (int[] p : csomopontPoziciok.values()) {
            minX = Math.min(minX, p[0]); maxX = Math.max(maxX, p[0]);
            minY = Math.min(minY, p[1]); maxY = Math.max(maxY, p[1]);
        }
        int logW = maxX - minX;
        int logH = maxY - minY;
        if (logW == 0 || logH == 0) return csomopontPoziciok;
        int pad = 80;
        double scale = Math.min((double)(panelW - 2*pad) / logW,
                                (double)(panelH - 2*pad) / logH);
        int scaledW = (int)(logW * scale);
        int scaledH = (int)(logH * scale);
        int offX = (panelW - scaledW) / 2 - (int)(minX * scale);
        int offY = (panelH - scaledH) / 2 - (int)(minY * scale);
        Map<Csomopont, int[]> result = new LinkedHashMap<>();
        for (Map.Entry<Csomopont, int[]> e : csomopontPoziciok.entrySet()) {
            result.put(e.getKey(), new int[]{
                (int)(e.getValue()[0] * scale) + offX,
                (int)(e.getValue()[1] * scale) + offY
            });
        }
        return result;
    }

    public void osszekot() {
        if (ablak == null || terkep == null) return;
        PalyaPanel palyaPanel = ablak.getPalyaPanel();
        if (palyaPanel == null) return;

        int pw = palyaPanel.getWidth();
        int ph = palyaPanel.getHeight();
        if (pw < 200) pw = 900;
        if (ph < 200) ph = 600;
        Map<Csomopont, int[]> displayPoziciok = computeDisplayPoziciok(pw, ph);

        layout = new PalyaLayout();
        layout.setCsomopontPoziciokFromIntArray(displayPoziciok);
        layout.szamolUtegysegPoziciok(terkep);
        palyaPanel.setLayout2(layout);
        palyaPanel.setController(this);

        Map<Csomopont, CsomopontView> csvMap = new LinkedHashMap<>();
        for (Csomopont cs : terkep.getCsomopontLista()) {
            Pont p = layout.getPozicio(cs);
            if (p == null) continue;
            CsomopontView csv = new CsomopontView(cs, p, palyaPanel);
            cs.addObserver(csv);
            csvMap.put(cs, csv);
        }

        Map<Utegyseg, UtegysegView> uevMap = new LinkedHashMap<>();
        for (Ut ut : terkep.getElLista()) {
            for (Sav sav : ut.getSavok()) {
                Utegyseg ue = sav.getElsoUtegyseg();
                while (ue != null) {
                    Rectangle r = layout.getUtegysegTeglalap(ue);
                    if (r != null) {
                        UtegysegView uev = new UtegysegView(ue, palyaPanel);
                        uev.setBefoglaloTeglalap(r);
                        ue.addObserver(uev);
                        uevMap.put(ue, uev);
                    }
                    ue = ue.getKovetkezoUtegyseg();
                }
            }
        }

        List<UtView> utViewList = new ArrayList<>();
        for (Ut ut : terkep.getElLista()) {
            UtView uv = new UtView(ut, displayPoziciok);
            for (Sav sav : ut.getSavok()) {
                SavView sv = new SavView(sav);
                Utegyseg ue = sav.getElsoUtegyseg();
                while (ue != null) {
                    UtegysegView uev = uevMap.get(ue);
                    if (uev != null) sv.addUtegysegView(uev);
                    ue = ue.getKovetkezoUtegyseg();
                }
                uv.addSavView(sv);
            }
            utViewList.add(uv);
        }

        jarmuViewk = new LinkedHashMap<>();
        Map<Utegyseg, int[]> uePos = layout.getUtegysegPoziciok();
        int hi = 1;
        for (Hokotro hk : hokotrók) {
            HokotroView hkv = new HokotroView(hk, palyaPanel, uePos);
            hk.addObserver(hkv);
            jarmuViewk.put(hk, hkv);
            hi++;
        }
        int bi = 1;
        for (Busz b : buszok) {
            BuszView bv = new BuszView(b, "B" + bi, palyaPanel, uePos);
            b.addObserver(bv);
            jarmuViewk.put(b, bv);
            bi++;
        }
        for (Auto a : autok) {
            AutoView av = new AutoView(a, palyaPanel, uePos);
            a.addObserver(av);
            jarmuViewk.put(a, av);
        }

        palyaPanel.setCsomopontViewk(csvMap);
        palyaPanel.setUtegysegViewk(uevMap);
        palyaPanel.setJarmuViewk(jarmuViewk);
        palyaPanel.setUtViewk(utViewList);

        HUDPanel hudPanel = ablak.getHudPanel();
        if (hudPanel != null && nyilvantarto != null) {
            nyilvantarto.addObserver(hudPanel);
        }

        palyaPanel.repaint();
    }

    private void valasszAktualisJarmuvet() {
        normalizalAktivIndexek();
        kivalasztottJarmu = getAktualisIranyithato();
        if (kivalasztottJarmu != null) {
            tervekMap.putIfAbsent(kivalasztottJarmu, new ArrayList<>());
        }
        frissitPalyaTervezesiAllapot();
    }

    private void normalizalAktivIndexek() {
        if (jatekosok.isEmpty()) {
            aktualisJatekosIndex = 0;
            aktualisJarmuIndex = 0;
            return;
        }

        aktualisJatekosIndex = Math.max(0, Math.min(aktualisJatekosIndex, jatekosok.size() - 1));
        int guard = 0;
        while (guard < jatekosok.size() && getFlotta(aktualisJatekosIndex).isEmpty()) {
            aktualisJatekosIndex = (aktualisJatekosIndex + 1) % jatekosok.size();
            aktualisJarmuIndex = 0;
            guard++;
        }

        List<Iranyithato> flotta = getFlotta(aktualisJatekosIndex);
        if (flotta.isEmpty()) {
            aktualisJarmuIndex = 0;
        } else {
            aktualisJarmuIndex = Math.max(0, Math.min(aktualisJarmuIndex, flotta.size() - 1));
        }
    }

    private List<Iranyithato> getFlotta(int jatekosIndex) {
        if (jatekosIndex < 0 || jatekosIndex >= jatekosok.size()) {
            return Collections.emptyList();
        }
        List<Iranyithato> flotta = jatekosok.get(jatekosIndex).getFlotta();
        return flotta != null ? flotta : Collections.emptyList();
    }

    private Iranyithato getAktualisIranyithato() {
        if (jatekosok.isEmpty()) return null;
        List<Iranyithato> flotta = getFlotta(aktualisJatekosIndex);
        if (flotta.isEmpty()) return null;
        int idx = Math.max(0, Math.min(aktualisJarmuIndex, flotta.size() - 1));
        return flotta.get(idx);
    }

    private void frissitPalyaTervezesiAllapot() {
        kijelolhetoUtegysegek.clear();
        kijelolhetoUtegysegek.addAll(szamolKijelolhetoUtegysegek(kivalasztottJarmu));

        if (jarmuViewk != null) {
            for (Map.Entry<Jarmu, JarmuView> e : jarmuViewk.entrySet()) {
                boolean aktiv = e.getKey() == kivalasztottJarmu;
                JarmuView view = e.getValue();
                if (view instanceof HokotroView) {
                    ((HokotroView) view).setKivalasztott(aktiv);
                } else if (view instanceof BuszView) {
                    ((BuszView) view).setKivalasztott(aktiv);
                }
            }
        }

        if (ablak != null) {
            PalyaPanel pp = ablak.getPalyaPanel();
            if (pp != null) {
                pp.setKijeloltUtegysegek(getKijeloltUtegysegek());
                pp.setKijelolhetoUtegysegek(kijelolhetoUtegysegek);
                pp.setKivalasztottJarmu(kivalasztottJarmu instanceof Jarmu ? (Jarmu) kivalasztottJarmu : null);
                pp.setKivalasztottBusz(kivalasztottJarmu instanceof Busz ? (Busz) kivalasztottJarmu : null);
                pp.setUtkozesJelolesek(getUtkozesParok());
            }
        }
    }

    private Set<Utegyseg> szamolKijelolhetoUtegysegek(Iranyithato jarmu) {
        LinkedHashSet<Utegyseg> eredmeny = new LinkedHashSet<>();
        if (aktualisFazis != Fazis.TERVEZES || !(jarmu instanceof Jarmu)) {
            return eredmeny;
        }

        int marKijelolt = tervekMap.getOrDefault(jarmu, Collections.emptyList()).size();
        int maradek = getJarmuHatotav(jarmu) - marKijelolt;
        if (maradek <= 0) {
            return eredmeny;
        }

        Utegyseg start = getTervezesiReferencia(jarmu);
        if (start == null) {
            return eredmeny;
        }

        Queue<Utegyseg> sor = new ArrayDeque<>();
        Map<Utegyseg, Integer> tav = new HashMap<>();
        sor.add(start);
        tav.put(start, 0);

        while (!sor.isEmpty()) {
            Utegyseg akt = sor.poll();
            int aktTav = tav.get(akt);
            if (aktTav >= maradek) continue;

            for (Utegyseg kov : validKovetkezok(akt)) {
                if (!tervezhetoUtegyseg(kov, jarmu)) continue;
                if (tav.containsKey(kov)) continue;
                tav.put(kov, aktTav + 1);
                eredmeny.add(kov);
                sor.add(kov);
            }
        }
        return eredmeny;
    }

    private Utegyseg getTervezesiReferencia(Iranyithato jarmu) {
        List<Utegyseg> terv = tervekMap.getOrDefault(jarmu, Collections.emptyList());
        if (!terv.isEmpty()) {
            return terv.get(terv.size() - 1);
        }
        return (jarmu instanceof Jarmu) ? ((Jarmu) jarmu).getUtegyseg() : null;
    }

    private boolean tervezhetoUtegyseg(Utegyseg ue, Iranyithato jarmu) {
        if (ue == null) return false;
        if (!ue.getBlokkolt()) return true;
        return jarmu instanceof Hokotro;
    }

    // -------------------------------------------------------------------------
    // Játékfázis-vezérlés
    // -------------------------------------------------------------------------

    public void kijeloltJarmuValt(Iranyithato j) {
        if (j != getAktualisIranyithato()) {
            return;
        }
        kivalasztottJarmu = j;
        tervekMap.putIfAbsent(j, new ArrayList<>());
        frissitPalyaTervezesiAllapot();
        ertesitListeners();
    }

    public void utegysegValasztva(Utegyseg ue) {
        if (aktualisFazis != Fazis.TERVEZES || kivalasztottJarmu == null) return;
        utegysegKijelol(ue);
        frissitPalyaTervezesiAllapot();
    }

    public void utegysegKijelol(Utegyseg ue) {
        if (aktualisFazis != Fazis.TERVEZES || kivalasztottJarmu == null) return;
        List<Utegyseg> terv = tervekMap.get(kivalasztottJarmu);
        if (terv == null) return;
        int benneIndex = terv.indexOf(ue);
        if (benneIndex >= 0) {
            terv.subList(benneIndex, terv.size()).clear();
        } else {
            int hatotav = getJarmuHatotav(kivalasztottJarmu);
            if (terv.size() >= hatotav) {
                frissitPalyaTervezesiAllapot();
                ertesitListeners();
                return;
            }
            Utegyseg ref = terv.isEmpty()
                ? ((Jarmu) kivalasztottJarmu).getUtegyseg()
                : terv.get(terv.size() - 1);
            if (ref != null && validKovetkezok(ref).contains(ue) && tervezhetoUtegyseg(ue, kivalasztottJarmu)) {
                terv.add(ue);
            }
        }
        frissitPalyaTervezesiAllapot();
        ertesitListeners();
    }

    private Set<Utegyseg> validKovetkezok(Utegyseg ref) {
        Set<Utegyseg> eredmeny = new LinkedHashSet<>();
        if (ref == null) return eredmeny;
        addSavvaltasCel(eredmeny, ref, ref.getBalUtegyseg());
        addSavvaltasCel(eredmeny, ref, ref.getJobbUtegyseg());

        Utegyseg kovetkezo = ref.getKovetkezoUtegyseg();
        if (kovetkezo != null) {
            eredmeny.add(kovetkezo);
        } else {
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

    private void addSavvaltasCel(Set<Utegyseg> eredmeny, Utegyseg ref, Utegyseg cel) {
        if (eredmeny == null || ref == null || cel == null) return;
        Sav refSav = ref.getSav();
        Sav celSav = cel.getSav();
        if (refSav == null || celSav == null) return;
        if (refSav.getVegCsomopont() == celSav.getVegCsomopont()) {
            eredmeny.add(cel);
        }
    }

    public void utvonalVeglegesit() {
        if (kivalasztottJarmu == null) return;
        List<Utegyseg> terv = tervekMap.get(kivalasztottJarmu);
        kivalasztottJarmu.setKijeloltUtegysegek(terv != null ? new ArrayList<>(terv) : new ArrayList<>());
        frissitPalyaTervezesiAllapot();
        ertesitListeners();
    }

    public void takaritKattintas() {
        if (aktualisFazis != Fazis.TERVEZES) return;
        if (kivalasztottJarmu instanceof Hokotro) {
            ((Hokotro) kivalasztottJarmu).takarit();
        }
        ertesitListeners();
    }

    public void boltNyit() {
        if (aktualisFazis != Fazis.TERVEZES) return;
        aktualisFazis = Fazis.BOLT;
        if (ablak != null) {
            BoltPanel bp = ablak.getBoltPanel();
            if (bp != null) bp.setAktualisJatekos(getAktualisJatekos());
            ablak.mutatBolt();
        }
        ertesitListeners();
    }

    public void boltBezar() {
        aktualisFazis = Fazis.TERVEZES;
        if (ablak != null) ablak.mutatJatek();
        ertesitListeners();
    }

    public void korVegeKattintas() {
        if (aktualisFazis != Fazis.TERVEZES) return;
        utvonalVeglegesit();
        leptetTervezesiSorrend();
    }

    public void kovetkezoJatekos() {
        aktualisJatekosIndex = (aktualisJatekosIndex + 1) % Math.max(1, jatekosok.size());
        aktualisJarmuIndex = 0;
        valasszAktualisJarmuvet();
        ertesitListeners();
    }

    private void leptetTervezesiSorrend() {
        if (jatekosok.isEmpty()) {
            szimulacioLepes();
            return;
        }

        int j = aktualisJatekosIndex;
        int v = aktualisJarmuIndex + 1;
        while (j < jatekosok.size()) {
            List<Iranyithato> flotta = getFlotta(j);
            if (v < flotta.size()) {
                aktualisJatekosIndex = j;
                aktualisJarmuIndex = v;
                valasszAktualisJarmuvet();
                ertesitListeners();
                return;
            }
            j++;
            v = 0;
        }

        szimulacioLepes();
    }

    public void szimulacioLepes() {
        aktualisFazis = Fazis.SZIMULACIO;
        frissitPalyaTervezesiAllapot();
        ertesitListeners();

        if (szimulacioTimer != null) {
            szimulacioTimer.stop();
            szimulacioTimer = null;
        }
        aktivTervek.clear();
        szimulacioIndex.clear();
        szimulacioLezartTervek.clear();
        szimulacioLepettAutok.clear();
        szimulacioKornyezetFrissitve = false;
        for (Map.Entry<Iranyithato, List<Utegyseg>> e : tervekMap.entrySet()) {
            aktivTervek.put(e.getKey(), new ArrayList<>(e.getValue()));
            szimulacioIndex.put(e.getKey(), 0);
        }

        if (ablak != null) {
            inditAnimaltSzimulaciot();
            return;
        }

        runTick();
        szimulacioLezarasa();
    }

    private void szimulacioLezarasa() {
        for (List<Utegyseg> list : tervekMap.values()) list.clear();
        aktivTervek.clear();
        szimulacioIndex.clear();
        szimulacioLezartTervek.clear();
        szimulacioLepettAutok.clear();
        szimulacioKornyezetFrissitve = false;

        aktualisKor++;

        if (nyilvantarto != null && nyilvantarto.isJatekVege()) {
            aktualisFazis = Fazis.TERVEZES;
            valasszAktualisJarmuvet();
            ertesitListeners();
            if (ablak != null) {
                JOptionPane.showMessageDialog(ablak,
                    "A játék végetért!\nTúl sok jármű nem ért el a célba.",
                    "Játék vége", JOptionPane.WARNING_MESSAGE);
                ablak.mutatMenu();
            }
            return;
        }

        aktualisFazis = Fazis.TERVEZES;
        aktualisJatekosIndex = 0;
        aktualisJarmuIndex = 0;
        valasszAktualisJarmuvet();
        ertesitListeners();
    }

    private void runTick() {
        if (terkep == null) return;

        leptetUtkozeseket();

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

        for (Hokotro hk : hokotrók) {
            if (utkozesbenVan(hk)) continue;
            moveAlongTerv(hk);
        }
        for (Busz b : buszok) {
            if (utkozesbenVan(b)) continue;
            moveAlongTerv(b);
        }
        for (Auto a : new ArrayList<>(autok)) {
            if (utkozesbenVan(a)) continue;
            Utegyseg nextUe = getAutoNextUtegyseg(a);
            if (nextUe != null) {
                a.setUtonToltottIdo(a.getUtonToltottIdo() + 1);
                nextUe.ralep(a);
            } else {
                a.lep();
            }
            if (a.nemErBe()) autok.remove(a);
        }
        rogzitsMegcsuszasUtkozeseket();

        if (nyilvantarto != null) {
            nyilvantarto.ellenorizJatekVege();
        }
    }

    private Utegyseg getAutoNextUtegyseg(Auto auto) {
        Utegyseg cur = auto.getUtegyseg();
        if (cur == null) return null;
        Utegyseg next = cur.getKovetkezoUtegyseg();
        if (next != null) return next;
        Sav curSav = cur.getSav();
        if (curSav == null) return null;
        Csomopont vegCsp = curSav.getVegCsomopont();
        if (vegCsp == null) return null;
        List<Ut> utvonal = auto.getKijeloltUtvonal();
        if (utvonal.isEmpty()) return null;

        boolean foundCurrent = false;
        for (Ut ut : utvonal) {
            if (!foundCurrent) {
                if (ut.getSavok().contains(curSav)) foundCurrent = true;
                continue;
            }
            Utegyseg first = elsoUtegysegFele(ut, vegCsp);
            if (first != null) return first;
        }
        // Circular wrap: restart from beginning of route
        if (foundCurrent) {
            for (Ut ut : utvonal) {
                if (ut.getSavok().contains(curSav)) break;
                Utegyseg first = elsoUtegysegFele(ut, vegCsp);
                if (first != null) return first;
            }
        }
        return null;
    }

    private Utegyseg elsoUtegysegFele(Ut ut, Csomopont fel) {
        if (ut.getVegpont1() != fel && ut.getVegpont2() != fel) return null;
        for (Sav sav : ut.getSavok()) {
            if (sav.getVegCsomopont() != fel && sav.getElsoUtegyseg() != null)
                return sav.getElsoUtegyseg();
        }
        return null;
    }

    private boolean utkozesbenVan(Jarmu jarmu) {
        if (jarmu == null) return false;
        for (UtkozesAllapot utkozes : utkozesek) {
            if (utkozes.elso == jarmu || utkozes.masodik == jarmu) {
                return true;
            }
        }
        return false;
    }

    private void rogzitsMegcsuszasUtkozeseket() {
        for (Ut ut : terkep.getElLista()) {
            for (Sav sav : ut.getSavok()) {
                Utegyseg aktualis = sav.getElsoUtegyseg();
                while (aktualis != null) {
                    Jarmu jarmu = aktualis.getJarmu();
                    if (jarmu != null && jarmu.getMegcsuszott() && !utkozesbenVan(jarmu)) {
                        Jarmu partner = jarmu.keresPartner();
                        if (partner != null) {
                            rogzitsUtkozest(jarmu, partner);
                        }
                    }
                    aktualis = aktualis.getKovetkezoUtegyseg();
                }
            }
        }
    }

    private void inditAnimaltSzimulaciot() {
        szimulacioTimer = new javax.swing.Timer(SZIMULACIO_ANIMACIO_MS, e -> animaciosSzimulacioFrame());
        szimulacioTimer.setInitialDelay(0);
        szimulacioTimer.start();
    }

    private void animaciosSzimulacioFrame() {
        if (terkep == null) {
            befejezAnimaltSzimulaciot();
            return;
        }

        if (!szimulacioKornyezetFrissitve) {
            leptetUtkozeseket();
            frissitKorElejiUtegysegeket();
            szimulacioKornyezetFrissitve = true;
        }

        boolean tortentMozgas = false;
        for (Hokotro hk : hokotrók) {
            tortentMozgas |= moveAlongTervEgyLepes(hk);
        }
        for (Busz b : buszok) {
            tortentMozgas |= moveAlongTervEgyLepes(b);
        }
        for (Auto a : new ArrayList<>(autok)) {
            if (!szimulacioLepettAutok.contains(a)) {
                autoEgyLepes(a);
                szimulacioLepettAutok.add(a);
                tortentMozgas = true;
            }
        }

        rogzitsMegcsuszasUtkozeseket();
        if (nyilvantarto != null) {
            nyilvantarto.ellenorizJatekVege();
        }
        frissitPalyaTervezesiAllapot();
        ertesitListeners();

        if (!tortentMozgas && !vanMegAnimaciosLepes()) {
            befejezAnimaltSzimulaciot();
        }
    }

    private void befejezAnimaltSzimulaciot() {
        if (szimulacioTimer != null) {
            szimulacioTimer.stop();
            szimulacioTimer = null;
        }
        szimulacioLezarasa();
    }

    private void frissitKorElejiUtegysegeket() {
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
    }

    private void autoEgyLepes(Auto a) {
        if (a == null || utkozesbenVan(a)) return;
        Utegyseg nextUe = getAutoNextUtegyseg(a);
        if (nextUe != null) {
            a.setUtonToltottIdo(a.getUtonToltottIdo() + 1);
            nextUe.ralep(a);
        } else {
            a.lep();
        }
        if (a.nemErBe()) autok.remove(a);
    }

    private void rogzitsUtkozest(Jarmu elso, Jarmu masodik) {
        if (elso == null || masodik == null || elso == masodik) return;
        for (UtkozesAllapot utkozes : utkozesek) {
            boolean ugyanazPar = (utkozes.elso == elso && utkozes.masodik == masodik)
                || (utkozes.elso == masodik && utkozes.masodik == elso);
            if (ugyanazPar) return;
        }

        UtkozesAllapot utkozes = new UtkozesAllapot(elso, masodik, UTKOZES_BLOKK_KOROK);
        elso.jelolUtkozest();
        masodik.jelolUtkozest();
        if (utkozes.elsoUtegyseg != null) utkozes.elsoUtegyseg.setBlokkolt(true);
        if (utkozes.masodikUtegyseg != null) utkozes.masodikUtegyseg.setBlokkolt(true);
        utkozesek.add(utkozes);
        frissitUtkozesJelolesek();
    }

    private void leptetUtkozeseket() {
        if (utkozesek.isEmpty()) return;
        Iterator<UtkozesAllapot> it = utkozesek.iterator();
        while (it.hasNext()) {
            UtkozesAllapot utkozes = it.next();
            utkozes.hatralevoKor--;
            if (utkozes.hatralevoKor <= 0) {
                feloldUtkozest(utkozes);
                it.remove();
            }
        }
        frissitUtkozesJelolesek();
    }

    private void feloldUtkozest(UtkozesAllapot utkozes) {
        if (utkozes == null) return;
        if (utkozes.elsoUtegyseg != null) {
            utkozes.elsoUtegyseg.setBlokkolt(utkozes.elsoEredetilegBlokkolt);
        }
        if (utkozes.masodikUtegyseg != null) {
            utkozes.masodikUtegyseg.setBlokkolt(utkozes.masodikEredetilegBlokkolt);
        }
        feloldUtkozottJarmu(utkozes.elso);
        feloldUtkozottJarmu(utkozes.masodik);
    }

    private void feloldUtkozottJarmu(Jarmu jarmu) {
        if (jarmu == null) return;
        if (jarmu instanceof Auto) {
            autoNemErtBeEsVisszaHazhoz((Auto) jarmu);
        } else {
            jarmu.feloldUtkozest();
        }
    }

    private void autoNemErtBeEsVisszaHazhoz(Auto auto) {
        if (auto == null) return;
        Utegyseg aktualis = auto.getUtegyseg();
        if (aktualis != null && aktualis.getJarmu() == auto) {
            aktualis.setJarmu(null);
        }
        auto.setUtegyseg(null);
        auto.feloldUtkozest();
        auto.setUtonToltottIdo(0);
        if (nyilvantarto != null) {
            nyilvantarto.nemBeertAutokNovel(1);
        }

        List<Ut> utvonal = auto.getKijeloltUtvonal();
        Ut induloUt = utvonal.isEmpty() ? null : utvonal.get(0);
        if (induloUt != null && auto.getKezdopont() != null) {
            placeJarmuAzUtKezdoSavjara(auto, induloUt, auto.getKezdopont());
        } else {
            placeOnFreeUtegyseg(auto);
        }
    }

    private List<Jarmu[]> getUtkozesParok() {
        List<Jarmu[]> parok = new ArrayList<>();
        for (UtkozesAllapot utkozes : utkozesek) {
            parok.add(new Jarmu[]{utkozes.elso, utkozes.masodik});
        }
        return parok;
    }

    private void frissitUtkozesJelolesek() {
        if (ablak == null) return;
        PalyaPanel pp = ablak.getPalyaPanel();
        if (pp != null) {
            pp.setUtkozesJelolesek(getUtkozesParok());
        }
    }

    private boolean vanMegAnimaciosLepes() {
        for (Hokotro hk : hokotrók) {
            if (vanMegTervLepes(hk)) return true;
        }
        for (Busz b : buszok) {
            if (vanMegTervLepes(b)) return true;
        }
        for (Auto a : autok) {
            if (!szimulacioLepettAutok.contains(a) && !utkozesbenVan(a)) return true;
        }
        return false;
    }

    private boolean vanMegTervLepes(Iranyithato vehicle) {
        if (vehicle == null || szimulacioLezartTervek.contains(vehicle) || !(vehicle instanceof Jarmu)) {
            return false;
        }
        if (utkozesbenVan((Jarmu) vehicle)) return false;
        List<Utegyseg> terv = aktivTervek.get(vehicle);
        if (terv == null || terv.isEmpty()) return false;
        int idx = szimulacioIndex.getOrDefault(vehicle, 0);
        return idx < Math.min(terv.size(), getJarmuHatotav(vehicle));
    }

    private boolean moveAlongTervEgyLepes(Iranyithato vehicle) {
        if (!vanMegTervLepes(vehicle)) return false;

        Jarmu jarmu = (Jarmu) vehicle;
        List<Utegyseg> terv = aktivTervek.get(vehicle);
        int idx = szimulacioIndex.getOrDefault(vehicle, 0);
        Utegyseg target = terv.get(idx);
        if (target == null) {
            szimulacioLezartTervek.add(vehicle);
            return false;
        }

        if (vehicle instanceof Hokotro && target.getBlokkolt()) {
            target.setBlokkolt(false);
        }

        if (target.ralep(jarmu)) {
            szimulacioIndex.put(vehicle, idx + 1);
            if (vehicle instanceof Hokotro) {
                ((Hokotro) vehicle).takarit();
            }
            return true;
        }

        szimulacioLezartTervek.add(vehicle);
        return false;
    }

    private void moveAlongTerv(Iranyithato vehicle) {
        List<Utegyseg> terv = aktivTervek.get(vehicle);
        if (terv == null || terv.isEmpty() || !(vehicle instanceof Jarmu)) return;
        Jarmu jarmu = (Jarmu) vehicle;
        int idx = szimulacioIndex.getOrDefault(vehicle, 0);
        int maxLepes = getJarmuHatotav(vehicle);
        int lepett = 0;
        while (idx < terv.size() && lepett < maxLepes) {
            if (utkozesbenVan(jarmu)) break;
            Utegyseg target = terv.get(idx);
            if (target == null) break;
            if (vehicle instanceof Hokotro && target.getBlokkolt()) {
                target.setBlokkolt(false);
            }
            if (target.ralep(jarmu)) {
                idx++;
                lepett++;
                szimulacioIndex.put(vehicle, idx);
                if (vehicle instanceof Hokotro) {
                    ((Hokotro) vehicle).takarit();
                }
            } else {
                break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Vásárlás
    // -------------------------------------------------------------------------

    public void vasarol(String kategoria, String aru, Object cel, int mennyiseg) {
        if (bolt == null) return;
        try {
            switch (kategoria) {
                case "Fejek":
                    if (cel instanceof Hokotro) {
                        Hokotro hk = (Hokotro) cel;
                        switch (aru) {
                            case "Söprő":        bolt.soproVasarol(hk);        break;
                            case "Hányó":        bolt.hanyoVasarol(hk);        break;
                            case "Jégtörő":      bolt.jegtoroVasarol(hk);      break;
                            case "Sószóró":      bolt.soszoroVasarol(hk);      break;
                            case "Sárkány":      bolt.sarkanyVasarol(hk);      break;
                            case "Zúzalékszóró": bolt.zuzalekszoroVasarol(hk); break;
                        }
                    }
                    break;
                case "Erőforrás":
                    switch (aru) {
                        case "Só":         bolt.soVasarol(mennyiseg);          break;
                        case "Biokerozin": bolt.biokerozinVasarol(mennyiseg);  break;
                        case "Zúzalék":
                            if (cel instanceof Hokotro)
                                bolt.zuzalekVasarol((Hokotro) cel, mennyiseg);
                            break;
                    }
                    break;
                case "Buszfejlesztés":
                    if (cel instanceof Busz) {
                        Busz b = (Busz) cel;
                        switch (aru) {
                            case "Sebesség": bolt.sebessegFejlesztes(b, mennyiseg); break;
                            case "Tapadás":  bolt.tapadasFejlesztes(b, mennyiseg);  break;
                            case "Hozam":    bolt.hozamFejlesztes(b, mennyiseg);    break;
                        }
                    }
                    break;
                case "Új jármű":
                    if ("Hókotró".equals(aru)) {
                        Jatekos j = getAktualisJatekos();
                        if (j != null) {
                            Hokotro ujHk = new Hokotro(new Sopro());
                            ujHk.setSebesseg(8);
                            ujHk.setTapadas(50);
                            ujHk.setZuzalekLimit(10);
                            ujHk.setNyilvantarto(nyilvantarto);
                            bolt.hokotroVasarol(j, ujHk);
                            if (j.getFlotta().contains(ujHk)) {
                                hokotrók.add(ujHk);
                                tervekMap.put(ujHk, new ArrayList<>());
                                placeOnFreeUtegyseg(ujHk);
                                if (ablak != null && layout != null) {
                                    PalyaPanel pp = ablak.getPalyaPanel();
                                    HokotroView hkv = new HokotroView(ujHk, pp, layout.getUtegysegPoziciok());
                                    ujHk.addObserver(hkv);
                                    jarmuViewk.put(ujHk, hkv);
                                    pp.setJarmuViewk(jarmuViewk);
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (Exception ignored) {}
        ertesitListeners();
    }

    // -------------------------------------------------------------------------
    // Terv törlése
    // -------------------------------------------------------------------------

    public void tervTorol(Iranyithato j) {
        if (j == null) return;
        List<Utegyseg> terv = tervekMap.get(j);
        if (terv != null) terv.clear();
        frissitPalyaTervezesiAllapot();
        ertesitListeners();
    }

    // -------------------------------------------------------------------------
    // Megfigyelo
    // -------------------------------------------------------------------------

    @Override
    public void frissit() {
        ertesitListeners();
    }

    // -------------------------------------------------------------------------
    // Listener (Runnable) mechanism
    // -------------------------------------------------------------------------

    public void addAllapotValtozoListener(Runnable r) { listeners.add(r); }
    public void clearListeners() { listeners.clear(); }
    private void ertesitListeners() {
        for (Runnable r : new ArrayList<>(listeners)) r.run();
    }

    // -------------------------------------------------------------------------
    // Segédmetódusok (térkép-építés)
    // -------------------------------------------------------------------------

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
            sav.setVegCsomopont(i % 2 == 0 ? vp2 : vp1);
            Utegyseg prev = null;
            for (int j = 0; j < uePerSav; j++) {
                Utegyseg ue = new Utegyseg();
                ue.setSav(sav);
                ue.setMegcsuszasEsely(GUI_ALAP_MEGCSUSZAS_ESELY);
                if (prev == null) sav.setElsoUtegyseg(ue);
                else prev.setKovetkezoUtegyseg(ue);
                prev = ue;
            }
            ut.addSav(sav);
        }

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

    private Ut makeUtDupla(Csomopont vp1, Csomopont vp2, int uePerSav, boolean alagut) {
        Ut ut = new Ut();
        ut.setVegpont1(vp1);
        ut.setVegpont2(vp2);
        ut.setAlagut(alagut);
        vp1.addUt(ut);
        vp2.addUt(ut);

        // 4 lanes grouped by direction: sav0+sav1 → vp2, sav2+sav3 → vp1
        for (Csomopont vc : new Csomopont[]{vp2, vp2, vp1, vp1}) {
            Sav sav = new Sav();
            sav.setVegCsomopont(vc);
            Utegyseg prev = null;
            for (int j = 0; j < uePerSav; j++) {
                Utegyseg ue = new Utegyseg();
                ue.setSav(sav);
                ue.setMegcsuszasEsely(GUI_ALAP_MEGCSUSZAS_ESELY);
                if (prev == null) sav.setElsoUtegyseg(ue);
                else prev.setKovetkezoUtegyseg(ue);
                prev = ue;
            }
            ut.addSav(sav);
        }

        // Connect only same-direction pairs
        List<Sav> savok = ut.getSavok();
        savokOsszekot(savok.get(0), savok.get(1)); // lane 1 ↔ lane 2 toward vp2
        savokOsszekot(savok.get(2), savok.get(3)); // lane 1 ↔ lane 2 toward vp1
        return ut;
    }

    private void savokOsszekot(Sav savA, Sav savB) {
        Utegyseg ue1 = savA.getElsoUtegyseg();
        Utegyseg ue2 = savB.getElsoUtegyseg();
        while (ue1 != null && ue2 != null) {
            ue1.setJobbUtegyseg(ue2);
            ue2.setBalUtegyseg(ue1);
            ue1 = ue1.getKovetkezoUtegyseg();
            ue2 = ue2.getKovetkezoUtegyseg();
        }
    }

    private void placeJarmu(Jarmu j, Utegyseg ue) {
        if (ue != null && ue.getJarmu() == null) {
            ue.setJarmu(j);
            j.setUtegyseg(ue);
        }
    }

    private void placeOnFreeUtegyseg(Jarmu j) {
        if (terkep == null) return;
        for (Ut ut : terkep.getElLista()) {
            for (Sav sav : ut.getSavok()) {
                Utegyseg ue = sav.getElsoUtegyseg();
                while (ue != null) {
                    if (ue.getJarmu() == null) {
                        placeJarmu(j, ue);
                        return;
                    }
                    ue = ue.getKovetkezoUtegyseg();
                }
            }
        }
    }

    private void placeJarmuAzUtKezdoSavjara(Jarmu j, Ut ut, Csomopont indulasiPont) {
        if (j == null || ut == null || indulasiPont == null) {
            placeOnFreeUtegyseg(j);
            return;
        }
        for (Sav sav : ut.getSavok()) {
            if (sav.getVegCsomopont() == indulasiPont) {
                continue;
            }
            Utegyseg ue = sav.getElsoUtegyseg();
            while (ue != null) {
                if (ue.getJarmu() == null) {
                    placeJarmu(j, ue);
                    return;
                }
                ue = ue.getKovetkezoUtegyseg();
            }
        }
        placeOnFreeUtegyseg(j);
    }

    // -------------------------------------------------------------------------
    // Getterek
    // -------------------------------------------------------------------------

    public Terkep getTerkep()                              { return terkep; }
    public Nyilvantarto getNyilvantarto()                  { return nyilvantarto; }
    public Bolt getBolt()                                  { return bolt; }
    public List<Jatekos> getJatekosok()                   { return jatekosok; }
    public Fazis getAktualisFazis()                        { return aktualisFazis; }
    public int getAktualisKor()                            { return aktualisKor; }
    public int getAktualisJatekosIndex()                   { return aktualisJatekosIndex; }
    public int getAktualisJarmuIndex()                     { return aktualisJarmuIndex; }
    public Iranyithato getKivalasztottJarmu()              { return kivalasztottJarmu; }
    public Map<Iranyithato, List<Utegyseg>> getTervekMap() { return tervekMap; }
    public Map<Csomopont, int[]> getCsomopontPoziciok()    { return csomopontPoziciok; }
    public List<Auto>    getAutok()                        { return autok; }
    public List<Hokotro> getHokotrók()                     { return hokotrók; }
    public List<Busz>    getBuszok()                       { return buszok; }

    public List<Utegyseg> getKijeloltUtegysegek() {
        if (kivalasztottJarmu == null) return new ArrayList<>();
        return tervekMap.getOrDefault(kivalasztottJarmu, new ArrayList<>());
    }

    public Set<Utegyseg> getKijelolhetoUtegysegek() {
        return new LinkedHashSet<>(kijelolhetoUtegysegek);
    }

    public int getJarmuHatotav(Iranyithato j) {
        if (!(j instanceof Jarmu)) return 0;
        return Math.max(1, ((Jarmu) j).getSebesseg());
    }

    public int getAktivJarmuHatotav() {
        return getJarmuHatotav(kivalasztottJarmu);
    }

    public int getHatralevoHatotav() {
        return Math.max(0, getAktivJarmuHatotav() - getKijeloltUtegysegek().size());
    }

    public Jatekos getAktualisJatekos() {
        if (jatekosok.isEmpty()) return null;
        int idx = Math.min(aktualisJatekosIndex, jatekosok.size() - 1);
        return jatekosok.get(idx);
    }

    public String getAktivJatekosNev() {
        if (!jatekosNevek.isEmpty()) {
            int idx = Math.min(aktualisJatekosIndex, jatekosNevek.size() - 1);
            return jatekosNevek.get(idx);
        }
        return "Játékos " + (aktualisJatekosIndex + 1);
    }

    public int getJarmuekOsszesenSzama() {
        int osszes = 0;
        for (int i = 0; i < jatekosok.size(); i++) {
            osszes += getFlotta(i).size();
        }
        return osszes;
    }

    public int getAktivTervezesiSorszam() {
        if (getJarmuekOsszesenSzama() == 0 || kivalasztottJarmu == null) return 0;
        int sorszam = 0;
        for (int i = 0; i < jatekosok.size(); i++) {
            List<Iranyithato> flotta = getFlotta(i);
            if (i == aktualisJatekosIndex) {
                return Math.min(getJarmuekOsszesenSzama(), sorszam + aktualisJarmuIndex + 1);
            }
            sorszam += flotta.size();
        }
        return 0;
    }

    public List<Iranyithato> getAktualisJatekosFlotta() {
        return new ArrayList<>(getFlotta(aktualisJatekosIndex));
    }

    public boolean isAktualisJarmu(Iranyithato j) {
        return j != null && j == getAktualisIranyithato();
    }

    public String getJatekosNev(int index) {
        if (index >= 0 && index < jatekosNevek.size()) {
            return jatekosNevek.get(index);
        }
        return "Játékos " + (index + 1);
    }

    public String getJarmuNev(Iranyithato j) {
        if (j == null) return "Jármű";
        for (int pi = 0; pi < jatekosok.size(); pi++) {
            List<Iranyithato> flotta = getFlotta(pi);
            int hkDb = 0;
            int buszDb = 0;
            for (Iranyithato elem : flotta) {
                if (elem instanceof Hokotro) hkDb++;
                if (elem instanceof Busz) buszDb++;
                if (elem == j) {
                    if (elem instanceof Hokotro) return "Hókotró " + hkDb;
                    if (elem instanceof Busz) return "Busz " + buszDb;
                    return "Jármű " + (flotta.indexOf(elem) + 1);
                }
            }
        }
        return "Jármű";
    }

    public String getJarmuTulajdonosNev(Iranyithato j) {
        for (int pi = 0; pi < jatekosok.size(); pi++) {
            if (getFlotta(pi).contains(j)) {
                return getJatekosNev(pi);
            }
        }
        return "-";
    }

    public String getBuszUtvonalLeiras(Busz busz) {
        if (busz == null) return "";
        String v1 = csomopontNev(busz.getVegallomas1());
        String v2 = csomopontNev(busz.getVegallomas2());
        String megallok = busz.getMegallokLista().isEmpty()
            ? "-"
            : String.join(", ", busz.getMegallokLista().stream().map(this::csomopontNev).toList());
        String erintett = busz.getErintettLista().isEmpty()
            ? "-"
            : String.join(", ", busz.getErintettLista().stream().map(this::csomopontNev).toList());
        return "<b>végállomások:</b> " + v1 + " - " + v2
            + "<br><b>megállók:</b> " + megallok
            + "<br><b>érintve:</b> " + erintett;
    }

    private String csomopontNev(Csomopont cs) {
        return cs != null ? cs.getAzonosito() : "-";
    }

    public String getAktivHokotroFejNev() {
        if (kivalasztottJarmu instanceof Hokotro) {
            Fej fej = ((Hokotro) kivalasztottJarmu).getFej();
            if (fej != null) return fej.getClass().getSimpleName();
        }
        if (!hokotrók.isEmpty() && hokotrók.get(0).getFej() != null) {
            return hokotrók.get(0).getFej().getClass().getSimpleName();
        }
        return "-";
    }

    public void jarmuvKivalaszt(Iranyithato j) { kijeloltJarmuValt(j); }

    public Map<Utegyseg, int[]> getUtegysegPoziciok() {
        return layout != null ? layout.getUtegysegPoziciok() : new LinkedHashMap<>();
    }

    public Map<Utegyseg, int[]> getUtegysegIranyok() {
        return layout != null ? layout.getUtegysegIranyok() : new LinkedHashMap<>();
    }
}
