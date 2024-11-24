package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.features.edit.LockWordFeature;
import dev.frilly.slangdict.features.edit.StarWordFeature;
import dev.frilly.slangdict.features.file.BombFeature;
import dev.frilly.slangdict.features.file.CloseDatabaseFeature;
import dev.frilly.slangdict.features.file.ReloadFeature;
import dev.frilly.slangdict.features.file.ResetDatabaseFeature;
import dev.frilly.slangdict.features.quiz.QuizFeature;
import dev.frilly.slangdict.gui.component.DictionaryModel;
import dev.frilly.slangdict.gui.component.RandomWordPanel;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.listener.DocumentChangeListener;

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
    private final JLabel     usingLabel          = new JLabel("Using Database");
    private final JLabel     dbName              = new JLabel();
    private final JTextField dbNameField         = new JTextField();
    private final JButton    renameButton        = new JButton("Rename");
    private final Component  rigidBox            = Box.createHorizontalStrut(
        16);// To replace with the cancel rename button
    private final JButton    cancelRenameButton  = new JButton("Cancel Rename");
    private final JButton    renameConfirmButton = new JButton(
        "Confirm Rename");

    // The second section, that shows a random word of the day.
    private final RandomWordPanel randomWordPanel = new RandomWordPanel();

    // The third section, that shows a search box and some query text.
    private final JLabel     search        = new JLabel("Search: ");
    private final JTextField searchField   = new JTextField();
    private final JLabel     queryResult   = new JLabel(
        "Queried 0 results in 0s.");
    private final JButton    historyButton = new JButton("History");

    private final JCheckBox instantSearch   = new JCheckBox("Instant Search");
    private final JCheckBox matchWord       = new JCheckBox("Match Word", true);
    private final JCheckBox matchDefinition = new JCheckBox("Match Definition",
        true);
    private final JCheckBox matchCase       = new JCheckBox("Match Case");
    private final JCheckBox matchRegex      = new JCheckBox("Match Regex");

    private final JComboBox<String> sortingOptions = new JComboBox<>();

    // The fourth section, that shows editorial buttons.
    private final JButton addButton    = new JButton();
    private final JButton editButton   = new JButton();
    private final JButton removeButton = new JButton();
    private final JButton starButton   = new JButton();
    private final JButton unstarButton = new JButton();
    private final JButton lockButton   = new JButton();
    private final JButton unlockButton = new JButton();

    private final JButton resetButton  = new JButton();
    private final JButton reloadButton = new JButton();
    private final JButton bombButton   = new JButton();

    // The fifth section, that shows some dictionary entries.
    private final DictionaryModel model           = new DictionaryModel(
        queryResult, matchWord, matchDefinition, matchCase, matchRegex,
        sortingOptions);
    private final JTable          table           = new JTable(model);
    private final JScrollPane     tableScrollPane = new JScrollPane(table);

    // The final section, the button to go to quiz.
    private final JButton quizButton = new JButton("Quiz");

    private ViewFrame() {
        setup();
        setupIcons();
        setupActions();
    }

    private void setup() {
        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        // Lol this is getting big and ridiculous.
        l.setHorizontalGroup(
            l.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(backButton)
                .addGroup(l.createSequentialGroup()
                    .addComponent(usingLabel)
                    .addComponent(dbName))
                .addGroup(GroupLayout.Alignment.TRAILING,
                    l.createSequentialGroup()
                        .addComponent(rigidBox)
                        .addComponent(renameButton))
                .addGroup(l.createSequentialGroup()
                    .addComponent(search)
                    .addGroup(
                        l.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(searchField)
                            .addGroup(l.createSequentialGroup()
                                .addGroup(l.createParallelGroup()
                                    .addComponent(matchWord)
                                    .addComponent(matchCase))
                                .addGroup(l.createParallelGroup()
                                    .addComponent(matchDefinition)
                                    .addComponent(matchRegex))
                                .addComponent(instantSearch)))
                    .addGroup(
                        l.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(historyButton)))
                .addComponent(queryResult)
                .addComponent(randomWordPanel)
                .addGroup(l.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(sortingOptions))
                .addGroup(GroupLayout.Alignment.CENTER,
                    l.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(editButton)
                        .addComponent(removeButton)
                        .addComponent(unstarButton)
                        .addComponent(starButton)
                        .addComponent(lockButton)
                        .addComponent(unlockButton)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(resetButton)
                        .addComponent(reloadButton)
                        .addComponent(bombButton))
                .addComponent(tableScrollPane)
                .addComponent(quizButton, GroupLayout.Alignment.CENTER));

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
            .addGroup(l.createBaselineGroup(true, true)
                .addGroup(l.createSequentialGroup()
                    .addComponent(matchWord)
                    .addComponent(matchCase))
                .addGroup(l.createSequentialGroup()
                    .addComponent(matchDefinition)
                    .addComponent(matchRegex))
                .addComponent(instantSearch))
            .addGap(4, 6, 8)
            .addGroup(l.createBaselineGroup(true, false)
                .addComponent(queryResult)
                .addComponent(instantSearch))
            .addGap(12, 14, 16)
            .addComponent(randomWordPanel)
            .addGap(24, 28, 32)
            .addGroup(l.createBaselineGroup(true, false)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(sortingOptions))
            .addGroup(l.createBaselineGroup(true, false)
                .addComponent(addButton)
                .addComponent(editButton)
                .addComponent(removeButton)
                .addComponent(unstarButton)
                .addComponent(starButton)
                .addComponent(lockButton)
                .addComponent(unlockButton)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(resetButton)
                .addComponent(reloadButton)
                .addComponent(bombButton))
            .addGap(4, 6, 8)
            .addComponent(tableScrollPane)
            .addGap(12, 14, 16)
            .addComponent(quizButton));

        dbName.putClientProperty("FlatLaf.styleClass", "h3");
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(500);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        final var comboModel = new DefaultComboBoxModel<String>();
        comboModel.addElement("Favorites up top");
        comboModel.addElement("Favorites down bottom");
        comboModel.addElement("Favorites hidden");
        comboModel.addElement("Favorites only");
        comboModel.addElement("Favorites unsorted");
        sortingOptions.setModel(comboModel);
        sortingOptions.setPreferredSize(new Dimension(200, 20));

        l.linkSize(addButton, editButton, removeButton, starButton,
            unstarButton, lockButton, unlockButton, reloadButton, resetButton,
            bombButton);
    }

    private void setupIcons() {
        backButton.setIcon(Application.getIcon("/icons/back.png", 14, 14));
        historyButton.setIcon(
            Application.getIcon("/icons/history.png", 14, 14));

        addButton.setIcon(Application.getIcon("/icons/add.png", 24, 24));
        editButton.setIcon(Application.getIcon("/icons/edit.png", 24, 24));
        removeButton.setIcon(Application.getIcon("/icons/remove.png", 24, 24));
        starButton.setIcon(
            Application.getIcon("/icons/star-filled.png", 24, 24));
        unstarButton.setIcon(Application.getIcon("/icons/star.png", 24, 24));
        lockButton.setIcon(Application.getIcon("/icons/lock.png", 24, 24));
        unlockButton.setIcon(
            Application.getIcon("/icons/lock-open.png", 24, 24));

        reloadButton.setIcon(
            Application.getIcon("/icons/load-file.png", 24, 24));
        resetButton.setIcon(Application.getIcon("/icons/sync.png", 24, 24));
        bombButton.setIcon(Application.getIcon("/icons/bomb.png", 24, 24));
    }

    private void setupActions() {
        setupBackButton();
        setupSearchFunction();
        setupRenameFunction();
        setupWordEditFunctions();
        setupDatabaseFunctions();
        setupTableView();
    }

    private void setupBackButton() {
        backButton.addActionListener(e -> new CloseDatabaseFeature().run());
    }

    private void setupSearchFunction() {
        searchField.getDocument()
            .addDocumentListener((DocumentChangeListener) e -> {
                if (!instantSearch.isSelected()) {
                    return;
                }
                model.query(searchField.getText());
            });
        searchField.addActionListener(e -> {
            if (instantSearch.isSelected()) {
                return;
            }
            searchField.setEnabled(false);
            model.query(searchField.getText(),
                () -> searchField.setEnabled(true));
        });
        historyButton.addActionListener(
            e -> MainFrame.getInstance().override(HistoryFrame.getInstance()));
    }

    /**
     * Setups the rename feature.
     * <p>
     * When the rename button is clicked, the text field replaces the name
     * label.
     * Then the confirm replaces the rename button, then the cancel button
     * appears.
     */
    private void setupRenameFunction() {
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
    }

    private void setupWordEditFunctions() {
        addButton.addActionListener(
            e -> MainFrame.getInstance().override(AddingFrame.getInstance()));
        editButton.addActionListener(e -> {
            final var v = (String) table.getValueAt(table.getSelectedRow(), 0);
            final var w = Dictionary.getInstance().getWord(v);

            if (w.locked) {
                Dialogs.error("You can't edit a locked word.");
                return;
            }

            EditingFrame.getInstance().setWord(w);
            MainFrame.getInstance().override(EditingFrame.getInstance());
        });
        removeButton.addActionListener(e -> {
            final var choices = Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> (String) table.getValueAt(i, 0))
                .toList();

            if (choices.isEmpty()) {
                Dialogs.error("You have to select a row first to delete!");
                return;
            }

            DeletingFrame.getInstance().setSelectedWords(choices);
            MainFrame.getInstance().override(DeletingFrame.getInstance());
        });
        lockButton.addActionListener(e -> {
            final var choices = Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> (String) table.getValueAt(i, 0))
                .toList();
            new LockWordFeature(choices, true).run();
            model.fireTableDataChanged();
            table.clearSelection();
        });
        unlockButton.addActionListener(e -> {
            final var choices = Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> (String) table.getValueAt(i, 0))
                .toList();
            new LockWordFeature(choices, false).run();
            model.fireTableDataChanged();
            table.clearSelection();
        });
        starButton.addActionListener(e -> {
            final var choices = Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> (String) table.getValueAt(i, 0))
                .toList();
            new StarWordFeature(choices, true).run();
            model.query(searchField.getText());
            table.clearSelection();
        });
        unstarButton.addActionListener(e -> {
            final var choices = Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> (String) table.getValueAt(i, 0))
                .toList();
            new StarWordFeature(choices, false).run();
            model.query(searchField.getText());
            table.clearSelection();
        });
    }

    private void setupDatabaseFunctions() {
        reloadButton.addActionListener(e -> {
            new ReloadFeature().run();
            model.query(searchField.getText());
        });
        resetButton.addActionListener(e -> new ResetDatabaseFeature().run());
        bombButton.addActionListener(e -> {
            new BombFeature().run();
            model.query(searchField.getText());
        });

        quizButton.addActionListener(e -> {
            if (Dictionary.getInstance()
                    .getWords()
                    .size() < QuizFeature.MINIMUM_ENTRIES) {
                Dialogs.error("Dictionary needs at least 4 entries.");
                return;
            }

            MainFrame.getInstance().override(QuizFrame.getInstance());
        });
    }

    private void setupTableView() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }

            final var s = Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> (String) model.getValueAt(i, 0))
                .map(Dictionary.getInstance()::getWord)
                .filter(Objects::nonNull)
                .toList();

            editButton.setEnabled(s.stream().noneMatch(w -> w.locked));
            removeButton.setEnabled(s.stream().noneMatch(w -> w.favorite));
        });

        // Combo box should update immediately.
        sortingOptions.addActionListener(e -> {
            model.query(searchField.getText());
        });
    }

    /**
     * Updates all text data to be up to date.
     */
    public void update() {
        dbName.setText(Dictionary.getInstance().getName());
    }

    public static ViewFrame getInstance() {
        return instance == null ? instance = new ViewFrame() : instance;
    }

    @Override
    public JPanel getOverridingPane() {
        if (!searchField.getText().isEmpty()) {
            searchField.setText("");
        }
        model.query("");
        randomWordPanel.randomize();
        update();
        return panel;
    }

}
