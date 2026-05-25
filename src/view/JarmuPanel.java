package view;

import controller.JatekController;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * A játékos flottáját megjelenítő oldalsáv. Lehetővé teszi a jármű kiválasztását
 * az útvonal-tervezéshez.
 */
public class JarmuPanel extends JPanel {

    private final JatekController controller;
    private final JPanel listaPanel;

    public JarmuPanel(JatekController controller) {
        this.controller = controller;
        setBackground(new Color(35, 35, 48));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(170, 0));
        setBorder(new EmptyBorder(8, 6, 8, 6));

        JLabel cim = new JLabel("Flotta");
        cim.setFont(new Font("SansSerif", Font.BOLD, 14));
        cim.setForeground(new Color(200, 200, 220));
        cim.setBorder(new EmptyBorder(0, 0, 8, 0));
        add(cim, BorderLayout.NORTH);

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listaPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        frissit();
    }

    public void frissit() {
        listaPanel.removeAll();

        if (controller.getJatekosok().isEmpty()) {
            listaPanel.revalidate();
            listaPanel.repaint();
            return;
        }

        Iranyithato kivalasztott = controller.getKivalasztottJarmu();
        JatekController.Fazis fazis = controller.getAktualisFazis();
        boolean tervezesFazis = fazis == JatekController.Fazis.TERVEZES;

        // Hókotrók
        for (Hokotro hk : controller.getHokotrók()) {
            JButton btn = jarmuGomb(hk, "Hókotró", new Color(240, 140, 40),
                    kivalasztott == hk, tervezesFazis);
            btn.addActionListener(e -> {
                if (tervezesFazis) controller.jarmuvKivalaszt(hk);
            });
            listaPanel.add(btn);
            listaPanel.add(Box.createRigidArea(new Dimension(0, 4)));

            // Fejinfo
            String fejNev = hk.getFej() != null ? hk.getFej().getClass().getSimpleName() : "-";
            JLabel fejLabel = new JLabel("Fej: " + fejNev);
            fejLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
            fejLabel.setForeground(new Color(180, 180, 160));
            fejLabel.setBorder(new EmptyBorder(0, 4, 0, 0));
            listaPanel.add(fejLabel);
            listaPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        // Buszok
        for (Busz b : controller.getBuszok()) {
            JButton btn = jarmuGomb(b, "Busz", new Color(60, 180, 80),
                    kivalasztott == b, tervezesFazis);
            btn.addActionListener(e -> {
                if (tervezesFazis) controller.jarmuvKivalaszt(b);
            });
            listaPanel.add(btn);
            listaPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        }

        // NPC autók (csak info, nem irányítható)
        if (!controller.getAutok().isEmpty()) {
            JSeparator sep = new JSeparator();
            sep.setForeground(new Color(80, 80, 100));
            listaPanel.add(sep);
            listaPanel.add(Box.createRigidArea(new Dimension(0, 4)));

            JLabel npcCim = new JLabel("NPC autók: " + controller.getAutok().size());
            npcCim.setFont(new Font("SansSerif", Font.ITALIC, 10));
            npcCim.setForeground(new Color(160, 160, 140));
            listaPanel.add(npcCim);
        }

        // Terv információ a kiválasztott járműhöz
        if (kivalasztott != null && tervezesFazis) {
            List<Utegyseg> terv = controller.getTervekMap().get(kivalasztott);
            if (terv != null) {
                listaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                JLabel tervCim = new JLabel("Kijelölt útegységek:");
                tervCim.setFont(new Font("SansSerif", Font.BOLD, 10));
                tervCim.setForeground(new Color(220, 220, 100));
                listaPanel.add(tervCim);

                JLabel tervSzam = new JLabel("  " + terv.size() + " db");
                tervSzam.setFont(new Font("SansSerif", Font.PLAIN, 10));
                tervSzam.setForeground(new Color(220, 220, 100));
                listaPanel.add(tervSzam);

                JButton torleBtn = new JButton("Terv törlése");
                torleBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
                torleBtn.setForeground(Color.WHITE);
                torleBtn.setBackground(new Color(160, 60, 60));
                torleBtn.setBorderPainted(false);
                torleBtn.setFocusPainted(false);
                Iranyithato torolando = kivalasztott;
                torleBtn.addActionListener(e -> controller.tervTorol(torolando));
                listaPanel.add(Box.createRigidArea(new Dimension(0, 4)));
                listaPanel.add(torleBtn);
            }
        }

        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private JButton jarmuGomb(Object jarmu, String nev, Color szin,
                               boolean kivalasztott, boolean aktiv) {
        JButton btn = new JButton(nev);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);

        if (kivalasztott) {
            btn.setBackground(szin.darker());
            btn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        } else {
            btn.setBackground(aktiv ? szin.darker().darker() : new Color(60, 60, 80));
            btn.setBorder(BorderFactory.createLineBorder(szin, 1));
        }

        if (!aktiv) btn.setEnabled(false);
        return btn;
    }
}
