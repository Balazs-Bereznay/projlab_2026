package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Főmenü képernyő: játékosok számának és nevének megadása, szerepkör kiválasztása.
 */
public class FomenuPanel extends JPanel {

    private static final String[] SZEREPEK = {"Buszvezető", "Hókotró"};
    private static final Color ROSA = new Color(255, 200, 200);
    private static final Color KAT_BG = new Color(245, 245, 245);

    private final JComboBox<String> szamCB;
    private final JPanel jatekosokTartaly;
    private final List<JTextField> nevMezok = new ArrayList<>();
    private final List<JComboBox<String>> szerepCBk = new ArrayList<>();

    /** Hívódik, amikor a játékos a "Új játék" gombot nyomja: (szám, nevek, szerepek). */
    private final BiConsumer<String[], String[]> ujJatekCallback;

    public FomenuPanel(BiConsumer<String[], String[]> ujJatekCallback, Runnable kilepes) {
        this.ujJatekCallback = ujJatekCallback;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // --- Cím ---
        JLabel cim = new JLabel("Hókotrók", SwingConstants.CENTER);
        cim.setFont(new Font("Serif", Font.PLAIN, 52));
        cim.setForeground(new Color(50, 50, 80));
        cim.setBorder(new EmptyBorder(30, 0, 20, 0));
        add(cim, BorderLayout.NORTH);

        // --- Középső tartalom ---
        JPanel kozep = new JPanel(new BorderLayout(20, 0));
        kozep.setOpaque(false);
        kozep.setBorder(new EmptyBorder(10, 40, 10, 40));

        // Bal: játékosok száma
        JPanel balPanel = new JPanel();
        balPanel.setLayout(new BoxLayout(balPanel, BoxLayout.Y_AXIS));
        balPanel.setOpaque(false);

        JLabel szamCim = new JLabel("játékosok száma");
        szamCim.setFont(new Font("SansSerif", Font.PLAIN, 12));
        szamCim.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] options = {"1 játékos", "2 játékos", "3 játékos", "4 játékos"};
        szamCB = new JComboBox<>(options);
        szamCB.setBackground(ROSA);
        szamCB.setMaximumSize(new Dimension(150, 30));
        szamCB.setAlignmentX(Component.CENTER_ALIGNMENT);
        szamCB.addActionListener(e -> frissitJatekosPanel());

        balPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        balPanel.add(szamCim);
        balPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        balPanel.add(szamCB);

        kozep.add(balPanel, BorderLayout.WEST);

        // Jobb: játékos panelek
        jatekosokTartaly = new JPanel();
        jatekosokTartaly.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        jatekosokTartaly.setOpaque(false);

        JScrollPane scroll = new JScrollPane(jatekosokTartaly);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        kozep.add(scroll, BorderLayout.CENTER);

        add(kozep, BorderLayout.CENTER);

        // --- Gombok ---
        JPanel gombokPanel = new JPanel();
        gombokPanel.setLayout(new BoxLayout(gombokPanel, BoxLayout.Y_AXIS));
        gombokPanel.setOpaque(false);
        gombokPanel.setBorder(new EmptyBorder(10, 0, 40, 0));

        JButton ujJatekBtn = menuGomb("Új játék");
        ujJatekBtn.addActionListener(e -> inditJatek());

        JButton kilepesBtN = menuGomb("Kilépés");
        kilepesBtN.addActionListener(e -> kilepes.run());

        gombokPanel.add(ujJatekBtn);
        gombokPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gombokPanel.add(kilepesBtN);

        add(gombokPanel, BorderLayout.SOUTH);

        frissitJatekosPanel();
    }

    private void frissitJatekosPanel() {
        int szam = szamCB.getSelectedIndex() + 1;
        jatekosokTartaly.removeAll();
        nevMezok.clear();
        szerepCBk.clear();

        for (int i = 0; i < szam; i++) {
            jatekosokTartaly.add(jatekosKartya(i + 1));
        }

        jatekosokTartaly.revalidate();
        jatekosokTartaly.repaint();
    }

    private JPanel jatekosKartya(int sorsz) {
        JPanel kartya = new JPanel();
        kartya.setLayout(new BoxLayout(kartya, BoxLayout.Y_AXIS));
        kartya.setBackground(Color.WHITE);
        kartya.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 200), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        kartya.setPreferredSize(new Dimension(170, 160));

        JLabel cim = new JLabel(sorsz + ". játékos", SwingConstants.CENTER);
        cim.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cim.setAlignmentX(Component.CENTER_ALIGNMENT);
        kartya.add(cim);
        kartya.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField nevMezo = new JTextField("Játékos " + sorsz);
        nevMezo.setBackground(new Color(180, 220, 255));
        nevMezo.setBorder(BorderFactory.createLineBorder(new Color(140, 180, 220)));
        nevMezo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        nevMezok.add(nevMezo);
        kartya.add(nevMezo);
        kartya.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel szerepLabel = new JLabel("szerep");
        szerepLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        szerepLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        kartya.add(szerepLabel);
        kartya.add(Box.createRigidArea(new Dimension(0, 4)));

        JComboBox<String> szerepCB = new JComboBox<>(SZEREPEK);
        szerepCB.setBackground(ROSA);
        szerepCB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        szerepCBk.add(szerepCB);
        kartya.add(szerepCB);

        return kartya;
    }

    private void inditJatek() {
        int szam = szamCB.getSelectedIndex() + 1;
        String[] nevek = new String[szam];
        String[] szerepek = new String[szam];
        for (int i = 0; i < szam; i++) {
            nevek[i] = nevMezok.get(i).getText().trim();
            szerepek[i] = (String) szerepCBk.get(i).getSelectedItem();
        }
        ujJatekCallback.accept(nevek, szerepek);
    }

    private JButton menuGomb(String szoveg) {
        JButton btn = new JButton(szoveg);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setBackground(new Color(150, 230, 150));
        btn.setForeground(new Color(30, 30, 30));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true),
            new EmptyBorder(5, 20, 5, 20)
        ));
        return btn;
    }
}
