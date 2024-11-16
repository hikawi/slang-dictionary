package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.features.file.CloseDatabaseFeature;
import dev.frilly.slangdict.gui.component.DictionaryModel;
import dev.frilly.slangdict.gui.component.RandomWordPanel;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * The main frame of the application. You spend the most about of time here.
 */
public final class ViewFrame implements Overrideable {

    private static ViewFrame instance;

    // The main panel.
    private final JPanel panel = new JPanel();

    // The top left button, to "close the database" and go back one window.
    private final JButton backButton = new JButton("Back");

    // The uppermost view, shows which database we're using.
    private final JLabel usingLabel = new JLabel("Using Database");
    private final JLabel dbName = new JLabel();
    private final JTextField dbNameField = new JTextField();
    private final JButton renameButton = new JButton("Rename");
    private final Component rigidBox = Box.createHorizontalStrut(16); // To replace with the cancel rename button
    private final JButton cancelRenameButton = new JButton("Cancel Rename");
    private final JButton renameConfirmButton = new JButton("Confirm Rename");

    // The second section, that shows a random word of the day.
    private final RandomWordPanel randomWordPanel = new RandomWordPanel();

    // The third section, that shows a search box and some query text.
    private final JLabel search = new JLabel("Search: ");
    private final JTextField searchField = new JTextField();
    private final JLabel queryResult = new JLabel("Queried 0 results in 0s.");
    private final JButton historyButton = new JButton("History");

    // The fourth section, that shows editorial buttons.
    private final JButton addButton = new JButton();
    private final JButton editButton = new JButton();
    private final JButton removeButton = new JButton();
    private final JButton starButton = new JButton();
    private final JButton unstarButton = new JButton();
    private final JButton lockButton = new JButton();
    private final JButton unlockButton = new JButton();

    private final JButton resetButton = new JButton();
    private final JButton reloadButton = new JButton();
    private final JButton bombButton = new JButton();

    // The fifth section, that shows some dictionary entries.
    private final DictionaryModel model = new DictionaryModel(queryResult);
    private final JTable table = new JTable(model);
    private final JScrollPane tableScrollPane = new JScrollPane(table);

    // The sixth section, that shows pagination data.

    private ViewFrame() {
        setup();
        setupIcons();
        setupActions();
    }

    public static ViewFrame getInstance() {
        return instance == null ? instance = new ViewFrame() : instance;
    }

    private void setup() {
        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        l.setHorizontalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(backButton)
            .addGroup(l.createSequentialGroup()
                .addComponent(usingLabel)
                .addComponent(dbName))
            .addGroup(GroupLayout.Alignment.TRAILING, l.createSequentialGroup()
                .addComponent(rigidBox)
                .addComponent(renameButton))
            .addGroup(l.createSequentialGroup()
                .addComponent(search)
                .addComponent(searchField)
                .addComponent(historyButton))
            .addComponent(queryResult)
            .addComponent(randomWordPanel)
            .addGroup(GroupLayout.Alignment.CENTER, l.createSequentialGroup()
                .addComponent(addButton)
                .addComponent(editButton)
                .addComponent(removeButton)
                .addComponent(starButton)
                .addComponent(unstarButton)
                .addComponent(lockButton)
                .addComponent(unlockButton)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(resetButton)
                .addComponent(reloadButton)
                .addComponent(bombButton))
            .addComponent(tableScrollPane));
        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(backButton)
            .addGap(16, 20, 24)
            .addGroup(l.createBaselineGroup(true, false)
                .addComponent(usingLabel)
                .addComponent(dbName))
            .addGroup(l.createBaselineGroup(true, false)
                .addComponent(rigidBox)
                .addComponent(renameButton))
            .addGap(24, 28, 32)
            .addGroup(l.createBaselineGroup(true, false)
                .addComponent(search)
                .addComponent(searchField)
                .addComponent(historyButton))
            .addGap(4, 6, 8)
            .addComponent(queryResult)
            .addGap(12, 14, 16)
            .addComponent(randomWordPanel)
            .addGap(24, 28, 32)
            .addGroup(l.createBaselineGroup(true, false)
                .addComponent(addButton)
                .addComponent(editButton)
                .addComponent(removeButton)
                .addComponent(starButton)
                .addComponent(unstarButton)
                .addComponent(lockButton)
                .addComponent(unlockButton)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(resetButton)
                .addComponent(reloadButton)
                .addComponent(bombButton))
            .addGap(4, 6, 8)
            .addComponent(tableScrollPane));

        dbName.putClientProperty("FlatLaf.styleClass", "h3");
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(500);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        l.linkSize(addButton, editButton, removeButton, starButton, unstarButton, lockButton, unlockButton, reloadButton, resetButton, bombButton);
    }

    private void setupIcons() {
        backButton.setIcon(Application.getIcon("/icons/back.png", 14, 14));
        historyButton.setIcon(Application.getIcon("/icons/history.png", 14, 14));

        addButton.setIcon(Application.getIcon("/icons/add.png", 24, 24));
        editButton.setIcon(Application.getIcon("/icons/edit.png", 24, 24));
        removeButton.setIcon(Application.getIcon("/icons/remove.png", 24, 24));
        starButton.setIcon(Application.getIcon("/icons/star.png", 24, 24));
        unstarButton.setIcon(Application.getIcon("/icons/star-filled.png", 24, 24));
        lockButton.setIcon(Application.getIcon("/icons/lock.png", 24, 24));
        unlockButton.setIcon(Application.getIcon("/icons/lock-open.png", 24, 24));

        reloadButton.setIcon(Application.getIcon("/icons/sync.png", 24, 24));
        resetButton.setIcon(Application.getIcon("/icons/reset.png", 24, 24));
        bombButton.setIcon(Application.getIcon("/icons/bomb.png", 24, 24));
    }

    private void setupActions() {
        // Setup back button.
        backButton.addActionListener(e -> new CloseDatabaseFeature().run());

        // Search function.
        searchField.addActionListener(e -> {
            searchField.setEnabled(false);
            model.query(searchField.getText(), () -> searchField.setEnabled(true));
        });

        // Setup rename feature. When rename is clicked, set the text field.
        // Then there's a Confirm and a Cancel button.
        renameButton.addActionListener(e -> {
            final var l = (GroupLayout) panel.getLayout();
            l.replace(dbName, dbNameField);
            l.replace(renameButton, renameConfirmButton);
            l.replace(rigidBox, cancelRenameButton);

            dbNameField.setText(dbName.getText());
            dbNameField.requestFocus();
        });
        renameConfirmButton.addActionListener(e -> {
            Dictionary.getInstance().rename(dbNameField.getText());

            final var l = (GroupLayout) panel.getLayout();
            l.replace(dbNameField, dbName);
            l.replace(renameConfirmButton, renameButton);
            l.replace(cancelRenameButton, rigidBox);
            update();
        });
        cancelRenameButton.addActionListener(e -> {
            final var l = (GroupLayout) panel.getLayout();
            l.replace(dbNameField, dbName);
            l.replace(renameConfirmButton, renameButton);
            l.replace(cancelRenameButton, rigidBox);
        });

        // Setup table selection.
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            final var s = Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> (String) model.getValueAt(i, 0))
                .map(Dictionary.getInstance()::getWord)
                .filter(Objects::nonNull);
        });
    }

    /**
     * Updates all text data to be up to date.
     */
    public void update() {
        dbName.setText(Dictionary.getInstance().getName());
    }

    @Override
    public JPanel getOverridingPane() {
        if (!searchField.getText().isEmpty())
            searchField.setText("");
        model.query("", () -> {
        });
        randomWordPanel.randomize();
        update();
        return panel;
    }

}
