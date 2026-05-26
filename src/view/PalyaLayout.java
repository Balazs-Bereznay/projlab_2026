package view;

import model.Csomopont;
import model.Sav;
import model.Terkep;
import model.Ut;
import model.Utegyseg;

import java.awt.Rectangle;
import java.io.*;
import java.util.*;

public class PalyaLayout {
    private final Map<Csomopont, Pont> csomopontPoziciok = new LinkedHashMap<>();
    private int mertekSzelesseg = 30;

    private final Map<Utegyseg, int[]> utegysegPoziciok = new LinkedHashMap<>();
    private final Map<Utegyseg, int[]> utegysegIranyok  = new LinkedHashMap<>();

    private static final int LANE_OFFSET = 34;
    private static final double MAX_NODE_MARGIN_RATIO = 0.28;

    public Pont getPozicio(Csomopont cs) { return csomopontPoziciok.get(cs); }
    public void setPozicio(Csomopont cs, Pont p) { csomopontPoziciok.put(cs, p); }
    public Map<Csomopont, Pont> getCsomopontPoziciok() { return csomopontPoziciok; }
    public Map<Utegyseg, int[]> getUtegysegPoziciok() { return utegysegPoziciok; }
    public Map<Utegyseg, int[]> getUtegysegIranyok()  { return utegysegIranyok; }
    public int getMertekSzelesseg() { return mertekSzelesseg; }

    public void betolt(String fajl, Terkep terkep) {
        try (BufferedReader br = new BufferedReader(new FileReader(fajl))) {
            String sor;
            while ((sor = br.readLine()) != null) {
                sor = sor.trim();
                if (sor.isEmpty() || sor.startsWith("#")) continue;
                String[] t = sor.split("\\s+");
                if (t.length >= 4 && t[0].equals("NODE")) {
                    String azonosito = t[1];
                    int x = Integer.parseInt(t[2]);
                    int y = Integer.parseInt(t[3]);
                    for (Csomopont cs : terkep.getCsomopontLista()) {
                        if (azonosito.equals(cs.getAzonosito())) {
                            csomopontPoziciok.put(cs, new Pont(x, y));
                            break;
                        }
                    }
                } else if (t.length >= 2 && t[0].equals("SCALE")) {
                    mertekSzelesseg = Integer.parseInt(t[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("PalyaLayout betöltési hiba: " + e.getMessage());
        }
        szamolUtegysegPoziciok(terkep);
    }

    public void szamolUtegysegPoziciok(Terkep terkep) {
        utegysegPoziciok.clear();
        utegysegIranyok.clear();

        for (Ut ut : terkep.getElLista()) {
            Csomopont vp1 = ut.getVegpont1();
            Csomopont vp2 = ut.getVegpont2();
            if (vp1 == null || vp2 == null) continue;
            Pont p1 = csomopontPoziciok.get(vp1);
            Pont p2 = csomopontPoziciok.get(vp2);
            if (p1 == null || p2 == null) continue;

            double dx = p2.x - p1.x;
            double dy = p2.y - p1.y;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len < 1) continue;
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
                double nodeMarginPx = Math.max(mertekSzelesseg * 2.0, LANE_OFFSET * 1.35);
                double nodeMarginRatio = Math.min(MAX_NODE_MARGIN_RATIO, nodeMarginPx / len);
                double usableRatio = Math.max(0.1, 1.0 - 2.0 * nodeMarginRatio);
                for (int ui = 0; ui < m; ui++) {
                    double t = (ui + 0.5) / m;
                    double progress = forward ? t : (1.0 - t);
                    double adjustedProgress = nodeMarginRatio + progress * usableRatio;
                    int x = (int)(p1.x + dx * adjustedProgress + px * offset);
                    int y = (int)(p1.y + dy * adjustedProgress + py * offset);
                    utegysegPoziciok.put(ueList.get(ui), new int[]{x, y});
                    utegysegIranyok.put(ueList.get(ui), new int[]{dirX, dirY});
                }
            }
        }
    }

    public Rectangle getUtegysegTeglalap(Utegyseg ue) {
        int[] pos = utegysegPoziciok.get(ue);
        if (pos == null) return null;
        return new Rectangle(pos[0] - mertekSzelesseg / 2, pos[1] - mertekSzelesseg / 2,
                             mertekSzelesseg, mertekSzelesseg);
    }

    public void setCsomopontPoziciokFromIntArray(Map<Csomopont, int[]> src) {
        for (Map.Entry<Csomopont, int[]> e : src.entrySet()) {
            csomopontPoziciok.put(e.getKey(), new Pont(e.getValue()[0], e.getValue()[1]));
        }
    }
}
