package view;

import model.Ut;
import model.Csomopont;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class UtView {
    private final Ut ut;
    private final List<SavView> savViewk = new ArrayList<>();
    private final Map<Csomopont, int[]> csomopontPoziciok;

    public UtView(Ut ut, Map<Csomopont, int[]> csomopontPoziciok) {
        this.ut = ut;
        this.csomopontPoziciok = csomopontPoziciok;
    }

    public void addSavView(SavView sv) { savViewk.add(sv); }
    public List<SavView> getSavViewk() { return savViewk; }

    public void kirajzol(Graphics2D g) {
        Csomopont vp1 = ut.getVegpont1();
        Csomopont vp2 = ut.getVegpont2();
        if (vp1 == null || vp2 == null) return;
        int[] p1 = csomopontPoziciok.get(vp1);
        int[] p2 = csomopontPoziciok.get(vp2);
        if (p1 == null || p2 == null) return;

        if (ut.getAlagut()) {
            float[] dash = {6f, 4f};
            g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
            g.setColor(new Color(75, 52, 32));
        } else {
            g.setStroke(new BasicStroke(5));
            g.setColor(new Color(75, 78, 92));
        }
        g.drawLine(p1[0], p1[1], p2[0], p2[1]);
        g.setStroke(new BasicStroke(1));

        for (SavView sv : savViewk) {
            sv.kirajzol(g);
        }
    }

    public Ut getUt() { return ut; }
}
