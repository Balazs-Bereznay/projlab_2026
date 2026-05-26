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

    private Fazis aktualisFazis = Fazis.TERVEZES;
    private int aktualisKor = 1;

    private Iranyithato kivalasztottJarmu = null;

    private final Map<Iranyithato, List<Utegyseg>> tervekMap   = new LinkedHashMap<>();
    private final Map<Iranyithato, List<Utegyseg>> aktivTervek = new LinkedHashMap<>();
    private final Map<Iranyithato, Integer>        szimulacioIndex = new LinkedHashMap<>();

    private final Map<Csomopont, int[]> csomopontPoziciok = new LinkedHashMap<>();

    private final List<Auto>    autok    = new ArrayList<>();
    private final List<Hokotro> hokotrók = new ArrayList<>();
    private final List<Busz>    buszok   = new ArrayList<>();

    private PalyaLayout layout;
    private Map<Jarmu, JarmuView> jarmuViewk = new LinkedHashMap<>();

    private final List<Runnable> listeners    = new ArrayList<>();
    private final List<String>   jatekosNevek = new ArrayList<>();

    public static final int HAVAZAS_PER_TICK = 1;

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
        csomopontPoziciok.clear();
        aktualisFazis = Fazis.TERVEZES;
        aktualisKor = 1;
        aktualisJatekosIndex = 0;
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
    }

    private Hokotro ujHokotro() {
        Hokotro hk = new Hokotro(new Sopro());
        hk.setSebesseg(1);
        hk.setTapadas(50);
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
        }
        placeOnFreeUtegyseg(b);
        return b;
    }

    private void npcAutoLetrehozas() {
        List<Csomopont> csList = terkep.getCsomopontLista();
        List<Ut> utLista = terkep.getElLista();
        if (csList.size() < 4 || utLista.isEmpty()) return;

        Auto a1 = new Auto();
        a1.setSebesseg(1);
        a1.setTapadas(30);
        a1.setNyilvantarto(nyilvantarto);
        a1.setKezdopont(csList.get(3));
        a1.setCelpont(csList.get(1));
        List<Ut> path = terkep.utvonalTervezes(csList.get(3), csList.get(1));
        for (Ut ut : path) a1.addKijeloltUt(ut);
        for (Sav sav : utLista.get(3).getSavok()) {
            if (sav.getVegCsomopont() != csList.get(3)) {
                Utegyseg ue = sav.getElsoUtegyseg();
                while (ue != null) {
                    if (ue.getJarmu() == null) { placeJarmu(a1, ue); break; }
                    ue = ue.getKovetkezoUtegyseg();
                }
                if (a1.getUtegyseg() != null) break;
            }
        }
        autok.add(a1);
    }

    private void inditas() {
        if (ablak != null) {
            clearListeners();
            addAllapotValtozoListener(() -> ablak.frissitJatek());
            osszekot();
            if (bolt != null) bolt.addObserver(ablak.getBoltPanel());
            ablak.mutatJatek();
            ablak.regisztraljBemenetKezelo();
        }
        ertesitListeners();
    }

    // -------------------------------------------------------------------------
    // Observer kapcsolás
    // -------------------------------------------------------------------------

    public void osszekot() {
        if (ablak == null || terkep == null) return;
        PalyaPanel palyaPanel = ablak.getPalyaPanel();
        if (palyaPanel == null) return;

        layout = new PalyaLayout();
        layout.setCsomopontPoziciokFromIntArray(csomopontPoziciok);
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
            UtView uv = new UtView(ut, csomopontPoziciok);
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

    // -------------------------------------------------------------------------
    // Játékfázis-vezérlés
    // -------------------------------------------------------------------------

    public void kijeloltJarmuValt(Iranyithato j) {
        kivalasztottJarmu = j;
        tervekMap.putIfAbsent(j, new ArrayList<>());
        if (ablak != null)
            ablak.getPalyaPanel().setKijeloltUtegysegek(tervekMap.get(j));
        ertesitListeners();
    }

    public void utegysegValasztva(Utegyseg ue) {
        if (aktualisFazis != Fazis.TERVEZES || kivalasztottJarmu == null) return;
        utegysegKijelol(ue);
        if (ablak != null)
            ablak.getPalyaPanel().setKijeloltUtegysegek(getKijeloltUtegysegek());
    }

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

    public void utvonalVeglegesit() {
        if (kivalasztottJarmu == null) return;
        List<Utegyseg> terv = tervekMap.get(kivalasztottJarmu);
        if (terv != null && !terv.isEmpty()) {
            kivalasztottJarmu.setKijeloltUtegysegek(new ArrayList<>(terv));
        }
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
        aktualisJatekosIndex++;
        if (aktualisJatekosIndex >= jatekosok.size()) {
            aktualisJatekosIndex = 0;
            szimulacioLepes();
        } else {
            kivalasztottJarmu = null;
            if (ablak != null) ablak.getPalyaPanel().setKijeloltUtegysegek(null);
            ertesitListeners();
        }
    }

    public void kovetkezoJatekos() {
        aktualisJatekosIndex = (aktualisJatekosIndex + 1) % Math.max(1, jatekosok.size());
        kivalasztottJarmu = null;
        if (ablak != null) ablak.getPalyaPanel().setKijeloltUtegysegek(null);
        ertesitListeners();
    }

    public void szimulacioLepes() {
        aktualisFazis = Fazis.SZIMULACIO;
        ertesitListeners();

        aktivTervek.clear();
        for (Map.Entry<Iranyithato, List<Utegyseg>> e : tervekMap.entrySet()) {
            aktivTervek.put(e.getKey(), new ArrayList<>(e.getValue()));
            szimulacioIndex.put(e.getKey(), 0);
        }

        runTick();

        for (List<Utegyseg> list : tervekMap.values()) list.clear();
        aktivTervek.clear();
        szimulacioIndex.clear();

        aktualisKor++;
        kivalasztottJarmu = null;

        if (nyilvantarto != null && nyilvantarto.isJatekVege()) {
            aktualisFazis = Fazis.TERVEZES;
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
        if (ablak != null) ablak.getPalyaPanel().setKijeloltUtegysegek(null);
        ertesitListeners();
    }

    private void runTick() {
        if (terkep == null) return;

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
            moveAlongTerv(hk);
            if (hk.getUtegyseg() != null) hk.takarit();
        }
        for (Busz b : buszok) {
            moveAlongTerv(b);
        }
        for (Auto a : new ArrayList<>(autok)) {
            a.lep();
        }
        for (Ut ut : terkep.getElLista()) {
            ut.balesetetKeres();
        }

        if (nyilvantarto != null) {
            nyilvantarto.ellenorizJatekVege();
        }
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
                            ujHk.setNyilvantarto(nyilvantarto);
                            bolt.hokotroVasarol(j, ujHk);
                            hokotrók.add(ujHk);
                            tervekMap.put(ujHk, new ArrayList<>());
                            j.getFlotta().add(ujHk);
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
        if (ablak != null) ablak.getPalyaPanel().setKijeloltUtegysegek(new ArrayList<>());
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

    // -------------------------------------------------------------------------
    // Getterek
    // -------------------------------------------------------------------------

    public Terkep getTerkep()                              { return terkep; }
    public Nyilvantarto getNyilvantarto()                  { return nyilvantarto; }
    public Bolt getBolt()                                  { return bolt; }
    public List<Jatekos> getJatekosok()                   { return jatekosok; }
    public Fazis getAktualisFazis()                        { return aktualisFazis; }
    public int getAktualisKor()                            { return aktualisKor; }
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
        return hokotrók.size() + buszok.size();
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
