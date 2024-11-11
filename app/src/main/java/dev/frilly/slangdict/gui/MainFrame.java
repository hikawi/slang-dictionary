package dev.frilly.slangdict.gui;

import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;

import dev.frilly.slangdict.gui.menu.MainBar;
import dev.frilly.slangdict.interfaces.Overrideable;

/**
 * The application's frame. This is the one and only frame, no more frames
 * shall be created.
 */
public final class MainFrame extends JFrame {

    private static MainFrame instance;

    private final Stack<Overrideable> stack;
    private Overrideable current;

    private MainFrame() {
        super("Slang Dictionary");
        this.stack = new Stack<>();
        this.getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);
        this.setup();
    }

    private void setup() {
        MainBar.getInstance().init(this);
        this.add(new JLabel("Test Component"));
    }

    /**
     * Create the frame.
     */
    public final void start() {
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Push the current frame to the stack, then
     * override the current pane with the new instance.
     */
    public final void override(final Overrideable instance) {
        if (current != null)
            stack.push(current);
        current = instance;

        this.setContentPane(instance.getOverridingPane());
        rerender();
    }

    /**
     * Override the pane with the new instance, without
     * pushing the current frame to the stack.
     * 
     * @param instance The instance.
     */
    public final void replace(final Overrideable instance) {
        current = instance;
        this.setContentPane(instance.getOverridingPane());
        rerender();
    }

    /**
     * Go back to last frame.
     */
    public final void back() {
        if (stack.isEmpty())
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); // Close if back to nothing.

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
        return switch (instance) {
            case null -> instance = new MainFrame();
            default -> instance;
        };
    }

}
