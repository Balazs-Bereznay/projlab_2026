package view;

import controller.JatekController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * A játék térképét megjelenítő panel. Rajzolja a csomópontokat, útszakaszokat,
 * útegységeket és járműveket. Egérkattintással lehet útegységet kijelölni.
 */
public class TerkeploPanel extends JPanel {

    private final JatekController controller;

    // Útegység mérete pixelben
    private static final int UE_MERET = 14;
    private static final int CS_MERET = 22;

    public TerkeploPanel(JatekController controller) {
        this.controller = controller;
        setBackground(new Color(40, 40, 50));
        setPreferredSize(new Dimension(650, 530));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    handleClick(e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rajzolUtszakaszok(g2);
        rajzolUtegysegek(g2);
        rajzolCsomopontok(g2);
        rajzolJarmuvek(g2);
        rajzolJelmagyarazat(g2);
    }

    private void rajzolUtszakaszok(Graphics2D g2) {
        Map<Csomopont, int[]> poziciok = controller.getCsomopontPoziciok();
        g2.setStroke(new BasicStroke(2));
        for (Ut ut : controller.getTerkep().getElLista()) {
            Csomopont vp1 = ut.getVegpont1();
            Csomopont vp2 = ut.getVegpont2();
            if (vp1 == null || vp2 == null) continue;
            int[] p1 = poziciok.get(vp1);
            int[] p2 = poziciok.get(vp2);
            if (p1 == null || p2 == null) continue;
            g2.setColor(ut.getAlagut() ? new Color(80, 60, 40) : new Color(90, 90, 110));
            g2.drawLine(p1[0], p1[1], p2[0], p2[1]);
            if (ut.getAlagut()) {
                g2.setColor(new Color(120, 90, 60));
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                int mx = (p1[0] + p2[0]) / 2;
                int my = (p1[1] + p2[1]) / 2;
                g2.drawString("ALAGÚT", mx - 20, my - 6);
            }
        }
        g2.setStroke(new BasicStroke(1));
    }

    private void rajzolUtegysegek(Graphics2D g2) {
        Map<Utegyseg, int[]> poziciok = controller.getUtegysegPoziciok();
        Map<Iranyithato, List<Utegyseg>> tervek = controller.getTervekMap();
        Iranyithato kivalasztott = controller.getKivalasztottJarmu();

        Set<Utegyseg> terveltUeSet = new HashSet<>();
        if (kivalasztott != null) {
            List<Utegyseg> terv = tervek.get(kivalasztott);
            if (terv != null) terveltUeSet.addAll(terv);
        }

        for (Map.Entry<Utegyseg, int[]> entry : poziciok.entrySet()) {
            Utegyseg ue = entry.getKey();
            int[] pos = entry.getValue();
            int x = pos[0] - UE_MERET / 2;
            int y = pos[1] - UE_MERET / 2;

            Color fill = utegysegSzin(ue);
            g2.setColor(fill);
            g2.fillRect(x, y, UE_MERET, UE_MERET);

            // Keret
            if (terveltUeSet.contains(ue)) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRect(x, y, UE_MERET, UE_MERET);
                g2.setStroke(new BasicStroke(1));
            } else if (ue.getBlokkolt()) {
                g2.setColor(Color.RED);
                g2.drawRect(x, y, UE_MERET, UE_MERET);
            } else {
                g2.setColor(new Color(60, 60, 80));
                g2.drawRect(x, y, UE_MERET, UE_MERET);
            }
        }
    }

    private Color utegysegSzin(Utegyseg ue) {
        int ho = ue.getHoMagassag();
        int jeg = ue.getJegMagassag();
        int so = ue.getSoMennyiseg();

        if (ue.getJeges() && jeg > 0) {
            // Jeges felület: cián árnyalat
            int intenzitas = Math.min(255, 150 + jeg * 15);
            return new Color(80, intenzitas, intenzitas);
        } else if (ue.getJeges()) {
            return new Color(100, 200, 220);
        } else if (ho > 0) {
            // Havazás: fehér felé tolva
            int r = Math.min(255, 130 + ho * 10);
            int g = Math.min(255, 150 + ho * 8);
            int b = Math.min(255, 200 + ho * 5);
            if (ue.getBlokkolt()) return new Color(100, 100, 200);
            return new Color(r, g, b);
        } else if (so > 0) {
            // Sózott út: halványsárga
            return new Color(230, 230, 160);
        } else {
            // Tiszta aszfalt
            return new Color(160, 170, 160);
        }
    }

    private void rajzolCsomopontok(Graphics2D g2) {
        Map<Csomopont, int[]> poziciok = controller.getCsomopontPoziciok();
        for (Map.Entry<Csomopont, int[]> entry : poziciok.entrySet()) {
            Csomopont cs = entry.getKey();
            int[] pos = entry.getValue();
            int x = pos[0] - CS_MERET / 2;
            int y = pos[1] - CS_MERET / 2;

            // Háttér szín a csomópont típusa alapján
            if (cs.getCelpont()) {
                g2.setColor(new Color(200, 80, 80));
            } else if (cs.getBuszmegallo()) {
                g2.setColor(new Color(60, 120, 200));
            } else {
                g2.setColor(new Color(130, 130, 150));
            }
            g2.fillOval(x, y, CS_MERET, CS_MERET);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, CS_MERET, CS_MERET);
            g2.setStroke(new BasicStroke(1));

            // Csomópont azonosítója
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            String label = cs.getAzonosito();
            int lx = pos[0] - fm.stringWidth(label) / 2;
            int ly = pos[1] + fm.getAscent() / 2 - 1;
            g2.drawString(label, lx, ly);

            // Típus felirat
            String tipusLabel = cs.getCelpont() ? "célpont" : cs.getBuszmegallo() ? "megálló" : "";
            if (!tipusLabel.isEmpty()) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                g2.setColor(new Color(220, 220, 150));
                g2.drawString(tipusLabel, pos[0] - fm.stringWidth(tipusLabel) / 2 + 2, pos[1] + CS_MERET);
            }
        }
    }

    private void rajzolJarmuvek(Graphics2D g2) {
        Map<Utegyseg, int[]> poziciok = controller.getUtegysegPoziciok();

        // NPC autók
        for (Auto a : controller.getAutok()) {
            rajzolJarmu(g2, a, poziciok, new Color(240, 200, 60), "A");
        }
        // Buszok
        for (Busz b : controller.getBuszok()) {
            rajzolJarmu(g2, b, poziciok, new Color(60, 200, 80), "B");
        }
        // Hókotrók
        for (Hokotro hk : controller.getHokotrók()) {
            rajzolJarmu(g2, hk, poziciok, new Color(240, 140, 40), "H");
        }
    }

    private void rajzolJarmu(Graphics2D g2, Jarmu j, Map<Utegyseg, int[]> poziciok,
                              Color szin, String cimke) {
        Utegyseg ue = j.getUtegyseg();
        if (ue == null) return;
        int[] pos = poziciok.get(ue);
        if (pos == null) return;

        int r = 9;
        g2.setColor(j.getMegcsuszott() ? Color.RED : szin);
        g2.fillOval(pos[0] - r, pos[1] - r, r * 2, r * 2);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(pos[0] - r, pos[1] - r, r * 2, r * 2);
        g2.setStroke(new BasicStroke(1));
        g2.setFont(new Font("SansSerif", Font.BOLD, 9));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(cimke, pos[0] - fm.stringWidth(cimke) / 2, pos[1] + fm.getAscent() / 2 - 1);
    }

    private void rajzolJelmagyarazat(Graphics2D g2) {
        int bx = 10, by = getHeight() - 120;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(bx - 4, by - 4, 170, 115, 8, 8);
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2.setColor(Color.WHITE);
        g2.drawString("Jelmagyarázat:", bx, by + 10);

        Object[][] jelmagyarazat = {
            {new Color(160, 170, 160), "Tiszta út"},
            {new Color(200, 210, 240), "Havas út"},
            {new Color(100, 100, 200), "Blokkolt (mély hó)"},
            {new Color(100, 200, 220), "Jeges út"},
            {Color.YELLOW, "Kijelölt útegység"},
            {new Color(240, 140, 40), "H - Hókotró"},
            {new Color(60, 200, 80),  "B - Busz"},
            {new Color(240, 200, 60), "A - Autó (NPC)"},
        };

        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int i = 0; i < jelmagyarazat.length; i++) {
            int ry = by + 22 + i * 12;
            g2.setColor((Color) jelmagyarazat[i][0]);
            g2.fillRect(bx, ry - 8, 12, 10);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(bx, ry - 8, 12, 10);
            g2.setColor(Color.WHITE);
            g2.drawString((String) jelmagyarazat[i][1], bx + 16, ry);
        }
    }

    private void handleClick(int mouseX, int mouseY) {
        Map<Utegyseg, int[]> poziciok = controller.getUtegysegPoziciok();
        Utegyseg legkozelebbi = null;
        double minTavolsag = UE_MERET * 1.5;

        for (Map.Entry<Utegyseg, int[]> entry : poziciok.entrySet()) {
            int[] pos = entry.getValue();
            double d = Math.hypot(mouseX - pos[0], mouseY - pos[1]);
            if (d < minTavolsag) {
                minTavolsag = d;
                legkozelebbi = entry.getKey();
            }
        }

        if (legkozelebbi != null) {
            controller.utegysegKijelol(legkozelebbi);
        }
    }

}
