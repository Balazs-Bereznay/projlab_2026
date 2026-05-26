package view;

import controller.Fazis;
import controller.JatekController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AkcioSor extends JPanel implements ActionListener {

    private final JatekController controller;
    private final JButton utvonalKijelolesGomb;
    private final JButton korVegeGomb;

    public AkcioSor(JatekController controller) {
        this.controller = controller;

        setBackground(new Color(235, 238, 248));
        setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(180, 185, 210)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0));

        utvonalKijelolesGomb = zoldGomb("Útvonal véglegesítése");
        korVegeGomb          = zoldGomb("Kör vége");

        utvonalKijelolesGomb.addActionListener(this);
        korVegeGomb.addActionListener(this);

        add(utvonalKijelolesGomb);
        add(korVegeGomb);

        fazisFrissites(Fazis.TERVEZES);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == utvonalKijelolesGomb) {
            controller.utvonalVeglegesit();
        } else if (src == korVegeGomb) {
            controller.korVegeKattintas();
        }
    }

    public void fazisFrissites(Fazis f) {
        boolean tervezes = f == Fazis.TERVEZES;
        utvonalKijelolesGomb.setEnabled(tervezes);
        korVegeGomb.setEnabled(tervezes);
    }

    private JButton zoldGomb(String szoveg) {
        JButton btn = new JButton(szoveg);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBackground(new Color(150, 230, 150));
        btn.setForeground(new Color(20, 60, 20));
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 100), 1, true));
        btn.setPreferredSize(new Dimension(190, 34));
        return btn;
    }
}
