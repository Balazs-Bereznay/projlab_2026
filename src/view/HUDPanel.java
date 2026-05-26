package view;

import common.Megfigyelo;
import controller.JatekController;
import controller.Fazis;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class HUDPanel extends JPanel implements Megfigyelo {

    private final JatekController controller;
    private final Runnable boltNyitCallback;

    private final JLabel kasszaLabel   = new JLabel();
    private final JLabel soLabel       = new JLabel();
    private final JLabel bioLabel      = new JLabel();
    private final JLabel zuzalekLabel  = new JLabel();
    private final JLabel nemBeertLabel = new JLabel();
    private final JLabel aktivFejLabel = new JLabel();
    private final JLabel tervLabel     = new JLabel();
    private final JButton tervTorleBtn;
    private final JPanel jarmuListaPanel = new JPanel();

    public HUDPanel(JatekController controller, Runnable boltNyitCallback) {
        this.controller = controller;
        this.boltNyitCallback = boltNyitCallback;

        setBackground(new Color(240, 242, 248));
        setPreferredSize(new Dimension(190, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(180, 180, 200)),
            new EmptyBorder(10, 8, 8, 8)
        ));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
        Color labelSzin = new Color(40, 40, 60);

        add(szekcioCim("Erőforrások"));
        add(Box.createRigidArea(new Dimension(0, 4)));

        for (JLabel l : new JLabel[]{kasszaLabel, soLabel, bioLabel, zuzalekLabel, nemBeertLabel}) {
            l.setFont(labelFont); l.setForeground(labelSzin);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(l);
            add(Box.createRigidArea(new Dimension(0, 3)));
        }

        add(Box.createRigidArea(new Dimension(0, 8)));
        add(szekcioCim("Aktív fej"));
        aktivFejLabel.setFont(labelFont); aktivFejLabel.setForeground(labelSzin);
        aktivFejLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(aktivFejLabel);

        add(Box.createRigidArea(new Dimension(0, 10)));
        JButton boltBtn = zoldGomb("Bolt");
        boltBtn.addActionListener(e -> boltNyitCallback.run());
        add(boltBtn);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(szekcioCim("Jelmagyarázat"));
        add(Box.createRigidArea(new Dimension(0, 4)));
        add(jelsor(new Color(148, 152, 148), "Tiszta út"));
        add(jelsor(new Color(195, 210, 238), "Havas (hX)"));
        add(jelsor(new Color(55,  65,  190), "Mély hó – blokkolt"));
        add(jelsor(new Color(75,  198, 215), "Jeges (jX)"));
        add(jelsor(new Color(218, 214, 135), "Sózott (sX)"));
        add(jelsor(new Color(185, 165, 135), "Zúzalékos (Z)"));

        add(Box.createRigidArea(new Dimension(0, 12)));
        add(szekcioCim("Járművek"));
        add(Box.createRigidArea(new Dimension(0, 4)));

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
        tervTorleBtn.setVisible(false);
        add(tervTorleBtn);

        add(Box.createVerticalGlue());
        frissit();
    }

    @Override
    public void frissit() {
        Nyilvantarto ny = controller.getNyilvantarto();
        if (ny == null) return;

        kasszaLabel.setText("Kassza: " + ny.getPenz() + " T");
        soLabel.setText("Só: " + ny.getSo());
        bioLabel.setText("Biokerozin: " + ny.getBiokerozin());

        Iranyithato aktualisJarmu = controller.getKivalasztottJarmu();
        if (aktualisJarmu instanceof Hokotro) {
            Hokotro hk = (Hokotro) aktualisJarmu;
            zuzalekLabel.setText("Zúzalék (aktív): " + hk.getZuzalekMennyiseg() + "/" + hk.getZuzalekLimit());
        } else {
            zuzalekLabel.setText("Zúzalék (aktív): -");
        }
        nemBeertLabel.setText("Nem beért: " + ny.getNemBeertAutokSzama() + "/" + ny.getNemBeertAutokLimit() + " db");
        aktivFejLabel.setText("Aktív fej: " + controller.getAktivHokotroFejNev());

        boolean tervezes = controller.getAktualisFazis() == Fazis.TERVEZES;
        frissitJarmuLista(tervezes);

        Iranyithato kiv = controller.getKivalasztottJarmu();
        if (kiv != null && tervezes) {
            List<Utegyseg> terv = controller.getKijeloltUtegysegek();
            int db = (terv != null) ? terv.size() : 0;
            String buszInfo = (kiv instanceof Busz)
                ? "<br>" + controller.getBuszUtvonalLeiras((Busz) kiv)
                : "";
            tervLabel.setText("<html><b>aktuális:</b> " + controller.getJarmuTulajdonosNev(kiv)
                + " / " + controller.getJarmuNev(kiv)
                + "<br><b>útvonal:</b> " + db + "/" + controller.getAktivJarmuHatotav() + " egység"
                + "<br><b>hátralévő:</b> " + controller.getHatralevoHatotav() + buszInfo + "</html>");
            tervTorleBtn.setVisible(db > 0);
            for (var l : tervTorleBtn.getActionListeners()) tervTorleBtn.removeActionListener(l);
            tervTorleBtn.addActionListener(e -> controller.tervTorol(kiv));
        } else {
            tervLabel.setText("kijelölt jármű: –");
            tervTorleBtn.setVisible(false);
        }

        revalidate(); repaint();
    }

    public void setAktivFej(String fejNev) { aktivFejLabel.setText("Aktív fej: " + fejNev); }

    private void frissitJarmuLista(boolean tervezes) {
        jarmuListaPanel.removeAll();
        Iranyithato kiv = controller.getKivalasztottJarmu();
        List<Jatekos> jatekosok = controller.getJatekosok();
        for (int pi = 0; pi < jatekosok.size(); pi++) {
            JLabel tulaj = new JLabel(controller.getJatekosNev(pi));
            tulaj.setFont(new Font("SansSerif", Font.BOLD, 10));
            tulaj.setForeground(pi == controller.getAktualisJatekosIndex()
                ? new Color(35, 95, 55)
                : new Color(95, 95, 115));
            tulaj.setAlignmentX(Component.LEFT_ALIGNMENT);
            jarmuListaPanel.add(tulaj);

            for (Iranyithato jarmu : jatekosok.get(pi).getFlotta()) {
                boolean aktualis = controller.isAktualisJarmu(jarmu);
                boolean aktiv = tervezes && aktualis;
                jarmuListaPanel.add(jarmuSor(controller.getJarmuNev(jarmu), jarmu, kiv == jarmu, aktiv));
            }
            jarmuListaPanel.add(Box.createRigidArea(new Dimension(0, 4)));
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
        if (aktiv) btn.addActionListener(e -> controller.kijeloltJarmuValt(jarmu));
        sor.add(btn, BorderLayout.CENTER);
        return sor;
    }

    private String jarmuNev(Iranyithato j) {
        List<Hokotro> hk = controller.getHokotrók();
        for (int i = 0; i < hk.size(); i++) if (hk.get(i) == j) return "Hókotró " + (i + 1);
        List<Busz> b = controller.getBuszok();
        for (int i = 0; i < b.size(); i++) if (b.get(i) == j) return "Busz " + (i + 1);
        return "Jármű";
    }

    private JPanel jelsor(Color szin, String szoveg) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        JLabel negyzet = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(szin);
                g.fillRoundRect(0, 1, 12, 12, 3, 3);
                g.setColor(new Color(0, 0, 0, 80));
                g.drawRoundRect(0, 1, 12, 12, 3, 3);
            }
        };
        negyzet.setPreferredSize(new Dimension(14, 14));
        JLabel szovegL = new JLabel(szoveg);
        szovegL.setFont(new Font("SansSerif", Font.PLAIN, 10));
        szovegL.setForeground(new Color(40, 40, 60));
        p.add(negyzet);
        p.add(szovegL);
        return p;
    }

    private JLabel szekcioCim(String szoveg) {
        JLabel l = new JLabel(szoveg);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(new Color(80, 80, 120));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton zoldGomb(String szoveg) {
        JButton btn = new JButton(szoveg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(new Color(150, 230, 150));
        btn.setForeground(new Color(20, 60, 20));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
}
