package dev.frilly.slangdict.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.concurrent.CompletableFuture;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import dev.frilly.slangdict.interfaces.Overrideable;

/**
 * An implementation for showing the progress bar.
 */
public final class ProgressFrame implements Overrideable {

    public static Runnable BACK = () -> {
        MainFrame.getInstance().back();
    };

    private static ProgressFrame instance;

    private int targetProgress = 0;

    // UI Components
    private final JPanel outerPane;
    private final JPanel pane;

    private final JLabel heading;
    private final JProgressBar progressBar;
    private final JLabel message;

    private final Timer timer;

    private ProgressFrame() {
        outerPane = new JPanel();
        outerPane.setLayout(new GridBagLayout());
        outerPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        pane = new JPanel();
        heading = new JLabel();
        progressBar = new JProgressBar(0, 100);
        message = new JLabel();

        // For a smooth progress bar.
        timer = new Timer(50, null);
        timer.addActionListener(e -> {
            if (progressBar.getValue() > targetProgress)
                progressBar.setValue(progressBar.getValue() - 1);
            else if (progressBar.getValue() < targetProgress)
                progressBar.setValue(progressBar.getValue() + 1);
        });

        this.setup();
    }

    private void setup() {
        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(heading)
                .addGap(20, 28, 36)
                .addComponent(progressBar)
                .addGap(4, 6, 8)
                .addComponent(message));
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(heading)
                .addComponent(progressBar)
                .addComponent(message));

        progressBar.setStringPainted(true);
        message.setPreferredSize(new Dimension(600, 200));

        heading.putClientProperty("FlatLaf.styleClass", "h2");
        message.putClientProperty("FlatLaf.styleClass", "large");
        outerPane.add(pane);
    }

    /**
     * Initializes the progress bar with values.
     * 
     * @param heading The heading.
     * @param message The message.
     */
    public void setValues(final String heading, final String message) {
        this.heading.setText(heading);
        this.message.setText(message);
        this.targetProgress = 0;
    }

    /**
     * Sets the heading.
     * 
     * @param heading The heading.
     */
    public void setHeading(final String heading) {
        this.heading.setText(heading);
    }

    /**
     * Sets the message.
     * 
     * @param message The message.
     */
    public void setMessage(final String message) {
        this.message.setText(message);
    }

    /**
     * Sets the progress bar value.
     * 
     * @param progress The progress value.
     */
    public void setProgress(final int progress) {
        targetProgress = progress;
    }

    /**
     * Sets whether the progress bar is indeterminate.
     * 
     * @param state The state
     */
    public void setIndeterminate(final boolean state) {
        progressBar.setIndeterminate(state);
    }

    /**
     * Starts a task and displays the progress bar.
     * 
     * @param task    The task to start.
     * @param resolve The task to run after it resolves.
     * @param reject  The task to run after it gets cancelled or failed.
     */
    public void startTask(final Runnable task, final Runnable resolve, final Runnable reject) {
        MainFrame.getInstance().override(this);
        timer.start();
        CompletableFuture.runAsync(task)
                .whenComplete((ret, error) -> {
                    if (error != null && reject != null)
                        reject.run();
                    else if (resolve != null)
                        resolve.run();
                    timer.stop();
                });
    }

    @Override
    public JPanel getOverridingPane() {
        return outerPane;
    }

    public static ProgressFrame getInstance() {
        return switch (instance) {
            case null -> instance = new ProgressFrame();
            default -> instance;
        };
    }

}
