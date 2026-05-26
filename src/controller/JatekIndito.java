package controller;

import model.Prototipus;
import view.JatekAblak;

import javax.swing.*;

public class JatekIndito {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Prototipus proto = new Prototipus();
            JatekController controller = new JatekController(proto);
            JatekAblak ablak = new JatekAblak(controller);
            controller.setAblak(ablak);
            ablak.mutatMenu();
            ablak.setVisible(true);
        });
    }
}
