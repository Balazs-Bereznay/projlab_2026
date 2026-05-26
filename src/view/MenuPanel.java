package view;

import controller.JatekController;
import controller.JatekosKonfig;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuPanel extends JPanel implements ActionListener {

    private static class PlayerCard extends JPanel {
        final JTextField nevMezo;
        final JComboBox<String> szerepCombo;

        PlayerCard(int idx) {
            setBackground(new Color(245, 246, 252));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(150, 160, 200)),
                    (idx + 1) + ". játékos",
                    TitledBorder.LEFT, TitledBorder.TOP,
                    new Font("SansSerif", Font.PLAIN, 11),
                    new Color(80, 80, 120)
                ),
                new EmptyBorder(6, 8, 8, 8)
            ));

            nevMezo = new JTextField("Játékos " + (idx + 1), 14);
            nevMezo.setFont(new Font("SansSerif", Font.PLAIN, 12));
            nevMezo.setAlignmentX(Component.LEFT_ALIGNMENT);
            nevMezo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

            JLabel szerepLabel = new JLabel("Szerep:");
            szerepLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            szerepLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            szerepCombo = new JComboBox<>(new String[]{"Hókotró", "Buszvezető"});
            szerepCombo.setFont(new Font("SansSerif", Font.PLAIN, 12));
            szerepCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
            szerepCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

            add(nevMezo);
            add(Box.createRigidArea(new Dimension(0, 4)));
            add(szerepLabel);
            add(szerepCombo);
        }
    }

    private final JatekController controller;
    private final JComboBox<Integer> jatekosSzamCombo;
    private final JPanel kartyakPanel;
    private final List<PlayerCard> kartyak = new ArrayList<>();
    private final JButton ujJatekGomb;
    private final JButton kilepesGomb;

    public MenuPanel(JatekController controller) {
        this.controller = controller;

        setBackground(new Color(235, 238, 248));
        setLayout(new BorderLayout());

        JLabel cim = new JLabel("Hókotrók", SwingConstants.CENTER);
        cim.setFont(new Font("Serif", Font.BOLD, 48));
        cim.setForeground(new Color(40, 60, 120));
        cim.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(cim, BorderLayout.NORTH);

        JPanel kozep = new JPanel();
        kozep.setLayout(new BoxLayout(kozep, BoxLayout.Y_AXIS));
        kozep.setOpaque(false);
        kozep.setBorder(new EmptyBorder(0, 40, 0, 40));

        JPanel jatszamSor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jatszamSor.setOpaque(false);
        JLabel jatszamLabel = new JLabel("Játékosok száma: ");
        jatszamLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        jatszamSor.add(jatszamLabel);
        jatekosSzamCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        jatekosSzamCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        jatekosSzamCombo.addActionListener(e -> frissitKartyak((Integer) jatekosSzamCombo.getSelectedItem()));
        jatszamSor.add(jatekosSzamCombo);
        kozep.add(jatszamSor);
        kozep.add(Box.createRigidArea(new Dimension(0, 12)));

        kartyakPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        kartyakPanel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(kartyakPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(900, 170));
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        kozep.add(scroll);
        kozep.add(Box.createRigidArea(new Dimension(0, 24)));

        ujJatekGomb = zoldGomb("Új játék");
        kilepesGomb = zoldGomb("Kilépés");
        ujJatekGomb.addActionListener(this);
        kilepesGomb.addActionListener(this);

        JPanel gombPanel = new JPanel();
        gombPanel.setLayout(new BoxLayout(gombPanel, BoxLayout.Y_AXIS));
        gombPanel.setOpaque(false);
        ujJatekGomb.setAlignmentX(Component.CENTER_ALIGNMENT);
        kilepesGomb.setAlignmentX(Component.CENTER_ALIGNMENT);
        gombPanel.add(ujJatekGomb);
        gombPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gombPanel.add(kilepesGomb);
        kozep.add(gombPanel);

        add(kozep, BorderLayout.CENTER);

        frissitKartyak(1);
    }

    public void frissitKartyak(int n) {
        kartyakPanel.removeAll();
        kartyak.clear();
        for (int i = 0; i < n; i++) {
            PlayerCard k = new PlayerCard(i);
            kartyak.add(k);
            kartyakPanel.add(k);
        }
        kartyakPanel.revalidate();
        kartyakPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == kilepesGomb) {
            System.exit(0);
        } else if (e.getSource() == ujJatekGomb) {
            List<JatekosKonfig> konfig = new ArrayList<>();
            for (PlayerCard k : kartyak) {
                String nev = k.nevMezo.getText().trim();
                if (nev.isEmpty()) nev = "Játékos";
                String szerep = (String) k.szerepCombo.getSelectedItem();
                konfig.add(new JatekosKonfig(nev, szerep));
            }
            controller.ujJatek(konfig);
        }
    }

    private JButton zoldGomb(String szoveg) {
        JButton btn = new JButton(szoveg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(new Color(150, 230, 150));
        btn.setForeground(new Color(20, 60, 20));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }
}
