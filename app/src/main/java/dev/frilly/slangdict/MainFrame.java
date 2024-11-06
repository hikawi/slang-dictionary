package dev.frilly.slangdict;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * The main frame of the program.
 */
public class MainFrame extends JFrame {

    private final DictionaryTableModel model;

    public MainFrame() {
        super("Slang Dictionary");
        this.model = new DictionaryTableModel();

        final var mainPane = new JPanel();
        final var layout = new BoxLayout(mainPane, BoxLayout.Y_AXIS);

        mainPane.setLayout(layout);
        mainPane.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        this.setContentPane(mainPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addSearchBox();
        addListView();
        addButtons();
    }

    /**
     * Add a search box with a search listener, along with a rigid area for spacing.
     */
    private void addSearchBox() {
        final var searchBox = new JPanel();
        final var layout = new BoxLayout(searchBox, BoxLayout.X_AXIS);
        searchBox.setLayout(layout);

        final var label = new JLabel("Search: ");
        final var input = new JTextField();
        input.setPreferredSize(new Dimension(400, 20));
        input.getDocument().addDocumentListener(new InputSearchListener(model, input));

        searchBox.add(label);
        searchBox.add(Box.createRigidArea(new Dimension(32, 0)));
        searchBox.add(input);

        this.getContentPane().add(searchBox);
        this.getContentPane().add(Box.createRigidArea(new Dimension(1, 32)));
    }

    /**
     * Add a view to list out slang words.
     */
    private void addListView() {
        final var viewBox = new JTable(model);
        final var scrollPane = new JScrollPane(viewBox);
        scrollPane.setPreferredSize(new Dimension(1200, 400));

        this.getContentPane().add(Box.createRigidArea(new Dimension(1, 32)));
        this.getContentPane().add(scrollPane);
    }

    /**
     * Add control buttons to the frame.
     */
    private void addButtons() {
        final var layout = new FlowLayout(FlowLayout.CENTER, 24, 24);
        final var buttonsMenu = new JPanel(layout);

        final var setDefaults = new JButton("Reset Defaults");
        setDefaults.addActionListener(e -> {
            model.loadDefaults();
            showMessage("All words have been reset to defaults.");
        });

        final var save = new JButton("Save");
        save.addActionListener(e -> {
            model.save();
            showMessage("Attempted to save all to ./data.bin");
        });

        buttonsMenu.add(setDefaults);
        buttonsMenu.add(save);

        this.getContentPane().add(Box.createRigidArea(new Dimension(1, 32)));
        this.getContentPane().add(buttonsMenu);
    }

    private void showMessage(final String message) {
        JOptionPane.showMessageDialog(this.getContentPane(), message, "", JOptionPane.INFORMATION_MESSAGE);
    }

}
