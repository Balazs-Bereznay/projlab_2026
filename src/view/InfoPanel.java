package view;

import controller.JatekController;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * Jobb oldali információs sáv: erőforrások, járműlista, útvonal-info, Bolt gomb.
 */
public class InfoPanel extends JPanel {

    private final JatekController controller;
    private final Runnable boltMegnyitCallback;

    private final JLabel kasszaLabel  = new JLabel();
    private final JLabel soLabel      = new JLabel();
    private final JLabel bioLabel     = new JLabel();
    private final JLabel zuzalekLabel = new JLabel();
    private final JLabel utazikLabel  = new JLabel();
    private final JLabel nemBeertLabel= new JLabel();
    private final JLabel aktivFejLabel= new JLabel();

    private final JPanel jarmuListaPanel = new JPanel();
    private final JLabel tervLabel = new JLabel();
    private final JButton tervTorleBtn;

    public InfoPanel(JatekController controller, Runnable boltMegnyitCallback) {
        this.controller = controller;
        this.boltMegnyitCallback = boltMegnyitCallback;
        setBackground(new Color(240, 242, 248));
        setPreferredSize(new Dimension(190, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(180, 180, 200)),
            new EmptyBorder(10, 8, 8, 8)
        ));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
        Color labelSzin = new Color(40, 40, 60);

        // --- Kassza / erőforrások ---
        add(szekcioCim("Erőforrások"));
        add(Box.createRigidArea(new Dimension(0, 4)));

        kasszaLabel.setFont(labelFont);  kasszaLabel.setForeground(labelSzin);
        soLabel.setFont(labelFont);      soLabel.setForeground(labelSzin);
        bioLabel.setFont(labelFont);     bioLabel.setForeground(labelSzin);
        zuzalekLabel.setFont(labelFont); zuzalekLabel.setForeground(labelSzin);
        utazikLabel.setFont(labelFont);  utazikLabel.setForeground(labelSzin);
        nemBeertLabel.setFont(labelFont);nemBeertLabel.setForeground(labelSzin);
        aktivFejLabel.setFont(labelFont);aktivFejLabel.setForeground(labelSzin);

        for (JLabel l : new JLabel[]{kasszaLabel, soLabel, bioLabel, zuzalekLabel,
                                      utazikLabel, nemBeertLabel}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(l);
            add(Box.createRigidArea(new Dimension(0, 3)));
        }

        add(Box.createRigidArea(new Dimension(0, 8)));
        add(szekcioCim("Aktív fej"));
        aktivFejLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(aktivFejLabel);
        add(Box.createRigidArea(new Dimension(0, 3)));

        // --- Bolt gomb ---
        add(Box.createRigidArea(new Dimension(0, 10)));
        JButton boltBtn = new JButton("Bolt");
        boltBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        boltBtn.setBackground(new Color(150, 230, 150));
        boltBtn.setForeground(new Color(20, 60, 20));
        boltBtn.setBorderPainted(true);
        boltBtn.setFocusPainted(false);
        boltBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        boltBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        boltBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        boltBtn.addActionListener(e -> boltMegnyitCallback.run());
        add(boltBtn);

        // --- Útvonal szekció ---
        add(Box.createRigidArea(new Dimension(0, 12)));
        add(szekcioCim("Útvonal"));
        add(Box.createRigidArea(new Dimension(0, 4)));

        // Járműlista
        jarmuListaPanel.setLayout(new BoxLayout(jarmuListaPanel, BoxLayout.Y_AXIS));
        jarmuListaPanel.setOpaque(false);
        jarmuListaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(jarmuListaPanel);

        add(Box.createRigidArea(new Dimension(0, 4)));
        tervLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tervLabel.setForeground(new Color(80, 80, 100));
        tervLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(tervLabel);

        tervTorleBtn = new JButton("Terv törlése");
        tervTorleBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
        tervTorleBtn.setBackground(new Color(220, 100, 100));
        tervTorleBtn.setForeground(Color.WHITE);
        tervTorleBtn.setBorderPainted(false);
        tervTorleBtn.setFocusPainted(false);
        tervTorleBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        tervTorleBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(tervTorleBtn);

        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(szekcioCim("Jelmagyarázat"));
        add(Box.createRigidArea(new Dimension(0, 4)));
        add(buildJelmagyarazat());
        frissit();
    }

    public void frissit() {
        Nyilvantarto ny = controller.getNyilvantarto();
        JatekController.Fazis fazis = controller.getAktualisFazis();

        kasszaLabel.setText("Kassza: " + ny.getPenz() + " T");
        soLabel.setText("Só: " + ny.getSo());
        bioLabel.setText("Biokerozin: " + ny.getBiokerozin());
        int osszesenZuzalek = 0;
        for (model.Hokotro hk : controller.getHokotrók())
            osszesenZuzalek += hk.getZuzalekMennyiseg();
        zuzalekLabel.setText("Zúzalék: " + osszesenZuzalek);
        utazikLabel.setText("Utazik: " + controller.getAutok().size() + " NPC");
        nemBeertLabel.setText("Nem beért: " + ny.getNemBeertAutokSzama()
                + "/" + ny.getNemBeertAutokLimit() + " db");
        aktivFejLabel.setText("Aktív fej: " + controller.getAktivHokotroFejNev());

        boolean tervezes = fazis == JatekController.Fazis.TERVEZES;
        frissitJarmuLista(tervezes);

        Iranyithato kiv = controller.getKivalasztottJarmu();
        if (kiv != null && tervezes) {
            List<Utegyseg> terv = controller.getTervekMap().get(kiv);
            int db = (terv != null) ? terv.size() : 0;
            tervLabel.setText("<html><b>kijelölt:</b> " + getJarmuNev(kiv)
                + "<br><b>útvonal:</b> " + db + " egység</html>");
            tervTorleBtn.setVisible(db > 0);
            final Iranyithato torolando = kiv;
            for (var l : tervTorleBtn.getActionListeners()) tervTorleBtn.removeActionListener(l);
            tervTorleBtn.addActionListener(e -> controller.tervTorol(torolando));
        } else if (kiv != null) {
            tervLabel.setText("<html><b>kijelölt:</b> " + getJarmuNev(kiv) + "</html>");
            tervTorleBtn.setVisible(false);
        } else {
            tervLabel.setText("kijelölt jármű: –");
            tervTorleBtn.setVisible(false);
        }

        revalidate();
        repaint();
    }

    private void frissitJarmuLista(boolean tervezes) {
        jarmuListaPanel.removeAll();

        Iranyithato kivalasztott = controller.getKivalasztottJarmu();

        int i = 1;
        for (Hokotro hk : controller.getHokotrók()) {
            jarmuListaPanel.add(jarmuSor("Hókotró " + i, hk, kivalasztott == hk, tervezes));
            i++;
        }
        i = 1;
        for (Busz b : controller.getBuszok()) {
            jarmuListaPanel.add(jarmuSor("Busz " + i, b, kivalasztott == b, tervezes));
            i++;
        }

        jarmuListaPanel.revalidate();
        jarmuListaPanel.repaint();
    }

    private JPanel jarmuSor(String nev, Iranyithato jarmu, boolean kivalasztott, boolean aktiv) {
        JPanel sor = new JPanel(new BorderLayout());
        sor.setOpaque(false);
        sor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        sor.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btn = new JButton(nev);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setEnabled(aktiv);
        if (kivalasztott) {
            btn.setBackground(new Color(255, 240, 160));
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 0), 1));
        } else {
            btn.setBackground(new Color(200, 210, 230));
            btn.setBorder(BorderFactory.createLineBorder(new Color(150, 160, 190), 1));
        }
        if (aktiv) {
            btn.addActionListener(e -> controller.jarmuvKivalaszt(jarmu));
        }
        sor.add(btn, BorderLayout.CENTER);
        return sor;
    }

    private String getJarmuNev(Iranyithato j) {
        List<model.Hokotro> hk = controller.getHokotrók();
        for (int i = 0; i < hk.size(); i++) if (hk.get(i) == j) return "Hókotró " + (i + 1);
        List<model.Busz> b = controller.getBuszok();
        for (int i = 0; i < b.size(); i++) if (b.get(i) == j) return "Busz " + (i + 1);
        return "Jármű";
    }

    private JPanel buildJelmagyarazat() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Object[][] jm = {
            {new Color(148, 152, 148), "Tiszta út"},
            {new Color(195, 210, 238), "Havas (h=magasság)"},
            {new Color(55,   65, 190), "Mély hó – blokkolt"},
            {new Color(75,  198, 215), "Jeges (j=vastagság)"},
            {new Color(218, 214, 135), "Sózott út"},
            {new Color(255, 215,   0), "Tervezett útvonal"},
            {new Color(235, 128,  28), "H – Hókotró"},
            {new Color(52,  185,  68), "B – Busz"},
            {new Color(235, 192,  48), "A – NPC autó"},
        };

        for (Object[] sor : jm) {
            Color szin = (Color) sor[0];
            String szoveg = (String) sor[1];

            JPanel sorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 1));
            sorPanel.setOpaque(false);
            sorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            sorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));

            JPanel negyzet = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(szin);
                    g.fillRect(0, 0, 12, 11);
                    g.setColor(new Color(20, 22, 32));
                    g.drawRect(0, 0, 11, 10);
                }
            };
            negyzet.setOpaque(false);
            negyzet.setPreferredSize(new Dimension(12, 12));

            JLabel label = new JLabel(szoveg);
            label.setFont(new Font("SansSerif", Font.PLAIN, 9));
            label.setForeground(new Color(40, 40, 60));

            sorPanel.add(negyzet);
            sorPanel.add(label);
            panel.add(sorPanel);
        }
        return panel;
    }

    private JLabel szekcioCim(String szoveg) {
        JLabel l = new JLabel(szoveg);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(new Color(80, 80, 120));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
