package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.interfaces.Overrideable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Stack;

/**
 * The application's frame. This is the one and only frame, no more frames shall be created.
 */
public final class MainFrame extends JFrame {

    private static MainFrame instance;

    private final Stack<Overrideable> stack;
    private Overrideable current;

    private MainFrame() {
        super("Slang Dictionary");
        this.stack = new Stack<>();
        this.setup();
    }

    private void setup() {
        // Load the icon from the JAR
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/book.png"));

        // Set taskbar icon (Java 9+)
        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            try {
                taskbar.setIconImage(icon);
            } catch (UnsupportedOperationException e) {
                System.err.println("The OS does not support setting taskbar icons.");
            } catch (SecurityException e) {
                System.err.println("Insufficient permissions to set the taskbar icon.");
            }
        }

        try {
            final var img = ImageIO.read(getClass().getResourceAsStream("/images/book.png"));
            this.setIconImage(img);
        } catch (final IOException ignored) {
        }
    }

    /**
     * Create the frame.
     */
    public void start() {
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Push the current frame to the stack, then override the current pane with the new instance.
     */
    public void override(final Overrideable instance) {
        if (current != null) stack.push(current);
        current = instance;

        this.setContentPane(instance.getOverridingPane());
        rerender();
    }

    /**
     * Retrieves the current instance of overrideable.
     *
     * @return The current frame
     */
    public Overrideable getCurrentFrame() {
        return current;
    }

    /**
     * Override the pane with the new instance, without pushing the current frame to the stack.
     *
     * @param instance The instance.
     */
    public void replace(final Overrideable instance) {
        current = instance;
        this.setContentPane(instance.getOverridingPane());
        rerender();
    }

    /**
     * Go back to last frame.
     */
    public void back() {
        // Close if back to nothing.
        if (stack.isEmpty()) dispatchEvent(
            new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
        );

        current = stack.pop();
        this.setContentPane(current.getOverridingPane());
        rerender();
    }

    private void rerender() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    /**
     * Retrieves the instance of the main frame.
     *
     * @return The frame.
     */
    public static MainFrame getInstance() {
        if (instance == null) instance = new MainFrame();
        return instance;
    }

}
