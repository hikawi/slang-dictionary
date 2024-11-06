package dev.frilly.slangdict;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class MainClass {

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        final var frame = new MainFrame();
        frame.pack();
        frame.setVisible(true);
    }

}
