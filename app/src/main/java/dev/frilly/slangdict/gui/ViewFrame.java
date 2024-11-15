package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.*;
import dev.frilly.slangdict.features.edit.DeleteWordFeature;
import dev.frilly.slangdict.features.file.BombFeature;
import dev.frilly.slangdict.features.file.CloseDatabaseFeature;
import dev.frilly.slangdict.features.file.ReloadFeature;
import dev.frilly.slangdict.features.file.SaveFeature;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.interfaces.Translatable;
import dev.frilly.slangdict.listener.DocumentChangeListener;
import dev.frilly.slangdict.model.DictionaryViewModel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

/**
 * Implementation of the probably most used frame of this application.
 * <p>
 * This is where the dictionary is shown,along with options and tools to manage said dictionary.
 */
public final class ViewFrame implements Overrideable {

    private static ViewFrame instance;

    private Future<?> future;

    // -------------
    // UI Components.
    // -------------

    private final JPanel pane;

    // Database close button at top
    private final JButton backButton = new JButton("Go Back");

    // Database name display & renaming options
    private final JLabel usingLabel = new JLabel("Using Database");
    private final JLabel databaseName = new JLabel();
    private final JTextField databaseNameField = new JTextField();
    private final JButton rename = new JButton("Rename");
    private final JButton renameConfirm = new JButton("COnfirm Rename");

    // Search display.
    private final JLabel searchLabel = new JLabel("Search");
    private final JTextField searchBox = new JTextField();
    private final JLabel searchResult = new JLabel();

    // Button-controls. Entry-specific buttons are on the left, database-wide buttons are on the right.
    private final JButton addButton = new JButton();
    private final JButton editButton = new JButton();
    private final JButton removeButton = new JButton();
    private final JButton starButton = new JButton();
    private final JButton unstarButton = new JButton();
    private final JButton lockButton = new JButton();
    private final JButton unlockButton = new JButton();

    private final JButton bombButton = new JButton();
    private final JButton saveButton = new JButton();
    private final JButton reloadButton = new JButton();

    // Ok fuck it, what if I just put a JTable
    private final DictionaryViewModel model = new DictionaryViewModel();
    private final JTable table = new JTable(model);
    private final JScrollPane scrollPane = new JScrollPane(table);

    private ViewFrame() {
        this.pane = new JPanel();

        databaseName.putClientProperty("FlatLaf.styleClass", "h3");
        searchLabel.putClientProperty("FlatLaf.styleClass", "medium");
        searchResult.putClientProperty("FlatLaf.styleClass", "semibold");

        backButton.setIcon(Application.getIcon(getClass().getResource("/icons/back.png"), 12, 12));

        addButton.setIcon(Application.getIcon(getClass().getResource("/icons/add.png"), 24, 24));
        editButton.setIcon(Application.getIcon(getClass().getResource("/icons/edit.png"), 24, 24));
        removeButton.setIcon(Application.getIcon(getClass().getResource("/icons/remove.png"), 24, 24));
        starButton.setIcon(Application.getIcon(getClass().getResource("/icons/star-filled.png"), 24, 24));
        unstarButton.setIcon(Application.getIcon(getClass().getResource("/icons/star.png"), 24, 24));
        lockButton.setIcon(Application.getIcon(getClass().getResource("/icons/lock.png"), 24, 24));
        unlockButton.setIcon(Application.getIcon(getClass().getResource("/icons/lock-open.png"), 24, 24));

        bombButton.setIcon(Application.getIcon(getClass().getResource("/icons/bomb.png"), 24, 24));
        saveButton.setIcon(Application.getIcon(getClass().getResource("/icons/save.png"), 24, 24));
        reloadButton.setIcon(Application.getIcon(getClass().getResource("/icons/refresh.png"), 24, 24));

        this.setup();
        this.setupActions();
    }

    /**
     * Initialize another query.
     */
    public void query() {
        // Cancel the current querying if there's another query coming.
        try {
            if (future != null) future.cancel(true);
        } catch (Exception ignored) {
        }

        // Create another query.
        future = model.query(searchBox.getText()).thenAccept(res ->
            searchResult.setText("Queried %d result(s) in %.3fs".formatted(res.count(), res.time())));
    }

    /**
     * Retrieves the stream of selected words in the table.
     *
     * @return The selected words.
     */
    public Stream<Word> getSelectedWords() {
        return Arrays.stream(table.getSelectedRows()).mapToObj(i -> (String) table.getValueAt(i, 0))
            .map(Dictionary.getInstance()::getWord);
    }

    /**
     * Retrieves the instance of the currently selected word only.
     *
     * @return The word.
     */
    public Word getSelectedWord() {
        return Dictionary.getInstance().getWord((String) table.getValueAt(table.getSelectedRow(), 0));
    }

    private void repaint() {
        databaseName.setText(Dictionary.getInstance().getName());
        model.fireTableDataChanged();
    }

    private void setup() {
        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);
        pane.setBorder(BorderFactory.createEmptyBorder(32, 16, 32, 16));

        searchBox.setPreferredSize(new Dimension(600, 24));
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(600);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        searchBox.setText("");
        query();

        layout.setVerticalGroup(
            layout
                .createSequentialGroup()
                .addComponent(backButton)
                .addGap(24)
                .addGroup(layout.createBaselineGroup(true, false).addComponent(usingLabel).addComponent(databaseName))
                .addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(rename))
                .addGap(16, 20, 24)
                .addGroup(
                    layout.createParallelGroup(Alignment.BASELINE).addComponent(searchLabel).addComponent(searchBox)
                )
                .addComponent(searchResult)
                .addGap(16, 20, 24)
                .addGroup(
                    layout
                        .createParallelGroup(Alignment.BASELINE)
                        .addComponent(addButton)
                        .addComponent(editButton)
                        .addComponent(removeButton)
                        .addComponent(starButton)
                        .addComponent(unstarButton)
                        .addComponent(lockButton)
                        .addComponent(unlockButton)
                        .addComponent(bombButton)
                        .addComponent(saveButton)
                        .addComponent(reloadButton)
                )
                .addGap(12)
                .addComponent(scrollPane)
        );

        layout.setHorizontalGroup(
            layout
                .createParallelGroup()
                .addComponent(backButton)
                .addGroup(layout.createSequentialGroup().addComponent(usingLabel).addGap(10).addComponent(databaseName))
                .addComponent(rename, Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup().addComponent(searchLabel).addGap(10).addComponent(searchBox))
                .addComponent(searchResult)
                .addGroup(
                    Alignment.TRAILING,
                    layout
                        .createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(editButton)
                        .addComponent(removeButton)
                        .addComponent(starButton)
                        .addComponent(unstarButton)
                        .addComponent(lockButton)
                        .addComponent(unlockButton)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bombButton)
                        .addComponent(saveButton)
                        .addComponent(reloadButton)
                )
                .addComponent(scrollPane)
        );

        layout.linkSize(addButton, bombButton, bombButton, reloadButton, saveButton);
    }

    private void setupActions() {
        searchBox
            .getDocument()
            .addDocumentListener(
                (DocumentChangeListener) e -> {
                    searchResult.setText("Querying...");
                    query();
                }
            );

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
                return;

            // Can only be enabled if no favorite words are selected.
            removeButton.setEnabled(getSelectedWords().noneMatch(w -> w.favorite));

            // Can only edit if the selected word is not locked.
            editButton.setEnabled(!getSelectedWord().locked);
        });

        // Rename button replaces the "database name field" with a textbox, and the rename
        // button with a confirm button.
        rename.addActionListener(e -> {
            final var layout = (GroupLayout) pane.getLayout();
            layout.replace(rename, renameConfirm);
            layout.replace(databaseName, databaseNameField);
            databaseNameField.setText(databaseName.getText());
            databaseNameField.requestFocus();
        });
        databaseNameField.getDocument()
            .addDocumentListener(
                (DocumentChangeListener) e -> {
                    renameConfirm.setEnabled(!databaseNameField.getText().isBlank());
                }
            );
        renameConfirm.addActionListener(e -> {
            final var layout = (GroupLayout) pane.getLayout();
            layout.replace(renameConfirm, rename);
            layout.replace(databaseNameField, databaseName);
            Dictionary.getInstance().rename(databaseNameField.getText());
            databaseName.setText(Dictionary.getInstance().getName());
        });

        removeButton.addActionListener(e -> new DeleteWordFeature().run());
        
        bombButton.addActionListener(e -> {
            final var res = Dialogs.confirm("Are you sure you want to delete all entries of \"%s\"?", Dictionary.getInstance().getName());
            if (res != JOptionPane.YES_OPTION) return;
            new BombFeature().run();
            searchBox.setText("");
            query();
        });
        saveButton.addActionListener(e -> {
            new SaveFeature().run();
            if (Dictionary.getInstance().getFile() != null)
                Dialogs.info("Saved dictionary \"%s\"", Dictionary.getInstance().getName());
        });
        reloadButton.addActionListener(e -> {
            new ReloadFeature().run();
            if (Dictionary.getInstance().getFile() != null)
                Dialogs.info("Reloaded dictionary \"%s\"", Dictionary.getInstance().getName());
        });
        backButton.addActionListener(e -> new CloseDatabaseFeature().run());
    }

    public static ViewFrame getInstance() {
        return switch (instance) {
            case null -> instance = new ViewFrame();
            default -> instance;
        };
    }

    @Override
    public JPanel getOverridingPane() {
        searchBox.setText("");
        repaint();
        return pane;
    }

}
