package dev.frilly.slangdict.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.interfaces.Translatable;
import dev.frilly.slangdict.listener.DocumentChangeListener;
import dev.frilly.slangdict.model.DictionaryViewModel;

/**
 * Implementation of the probably most used frame of this application.
 * 
 * This is where the dictionary is shown,along with options and tools to manage said dictionary.
 */
public final class ViewFrame implements Overrideable, Translatable {

    private static ViewFrame instance;

    // -------------
    // DATA MODELS
    // -------------

    private int queryCount = 2000;
    private double queryTime = 0.1;

    private DictionaryViewModel tableModel = new DictionaryViewModel();

    // -------------
    // UI Components.
    // -------------

    private final JPanel outerPane;
    private final JPanel pane;

    // Database close button at top
    private final JButton backButton = new JButton();

    // Database name display & renaming options
    private final JLabel usingLabel = new JLabel();
    private final JLabel databaseName = new JLabel();
    private final JTextField databaseNameField = new JTextField();
    private final JButton rename = new JButton();
    private final JButton renameConfirm = new JButton();

    // Search display.
    private final JLabel searchLabel = new JLabel();
    private final JTextField searchBox = new JTextField();
    private final JLabel searchResult = new JLabel();

    // Buttons, first set are editorial buttons on the left, second set are database
    // buttons on the right
    private final JButton addButton = new JButton();
    private final JButton deleteButton = new JButton();
    private final JButton lockButton = new JButton();
    private final JButton favoriteButton = new JButton();

    private final JButton saveButton = new JButton();
    private final JButton reloadButton = new JButton();

    // Dictionary display.
    private final JTable table = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(table);

    private ViewFrame() {
        this.outerPane = new JPanel();
        this.outerPane.setLayout(new GridBagLayout());
        this.pane = new JPanel();

        databaseName.putClientProperty("FlatLaf.styleClass", "h3");
        searchLabel.putClientProperty("FlatLaf.styleClass", "medium");
        searchResult.putClientProperty("FlatLaf.styleClass", "semibold");

        backButton.setIcon(Application.getIcon(getClass().getResource("/icons/back.png"), 12, 12));
        addButton.setIcon(Application.getIcon(getClass().getResource("/icons/add.png"), 24, 24));
        deleteButton
                .setIcon(Application.getIcon(getClass().getResource("/icons/remove.png"), 24, 24));
        favoriteButton
                .setIcon(Application.getIcon(getClass().getResource("/icons/star.png"), 24, 24));
        lockButton.setIcon(Application.getIcon(getClass().getResource("/icons/lock.png"), 24, 24));
        saveButton.setIcon(Application.getIcon(getClass().getResource("/icons/save.png"), 24, 24));
        reloadButton
                .setIcon(Application.getIcon(getClass().getResource("/icons/refresh.png"), 24, 24));

        this.setup();
        this.setupActions();
        I18n.register(this);
    }

    private void setup() {
        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);
        pane.setBorder(BorderFactory.createEmptyBorder(32, 16, 32, 16));

        searchBox.setPreferredSize(new Dimension(600, 24));
        tableModel.query("").thenAccept(q -> {
            queryCount = q.queryCount();
            queryTime = q.time();
            updateTranslations();
        });
        scrollPane.setPreferredSize(new Dimension(1200, 600));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(backButton).addGap(24)
                .addGroup(layout.createBaselineGroup(true, false).addComponent(usingLabel)
                        .addComponent(databaseName))
                .addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(rename))
                .addGap(16, 20, 24)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(searchLabel)
                        .addComponent(searchBox))
                .addComponent(searchResult).addGap(16, 20, 24)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(addButton).addComponent(deleteButton)
                                .addComponent(favoriteButton).addComponent(lockButton))
                        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                .addComponent(saveButton).addComponent(reloadButton)))
                .addGap(6, 8, 10).addComponent(scrollPane));

        layout.setHorizontalGroup(
                layout.createParallelGroup().addComponent(backButton)
                        .addGroup(layout.createSequentialGroup().addComponent(usingLabel).addGap(10)
                                .addComponent(databaseName))
                        .addComponent(rename, Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup().addComponent(searchLabel)
                                .addGap(10).addComponent(searchBox))
                        .addComponent(searchResult)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createSequentialGroup().addComponent(addButton)
                                        .addComponent(deleteButton).addComponent(favoriteButton)
                                        .addComponent(lockButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup().addComponent(saveButton)
                                        .addComponent(reloadButton)))
                        .addComponent(scrollPane));

        layout.linkSize(addButton, deleteButton, favoriteButton, lockButton, reloadButton,
                saveButton);
        outerPane.add(pane);
    }

    private void setupActions() {
        searchBox.getDocument().addDocumentListener((DocumentChangeListener) e -> {
            searchResult.setText(I18n.tl("view.querying"));
            tableModel.query(searchBox.getText()).thenAccept(q -> {
                queryCount = q.queryCount();
                queryTime = q.time();
                updateTranslations();
            });
        });

        backButton.addActionListener(e -> {
            MainFrame.getInstance().back();
        });
    }

    public static ViewFrame getInstance() {
        return switch (instance) {
            case null -> instance = new ViewFrame();
            default -> instance;
        };
    }

    @Override
    public JPanel getOverridingPane() {
        updateTranslations();
        return outerPane;
    }

    @Override
    public void updateTranslations() {
        backButton.setText(I18n.tl("view.close"));
        usingLabel.setText(I18n.tl("view.using"));
        databaseName.setText(Dictionary.getInstance().getName());
        rename.setText(I18n.tl("view.rename"));

        searchLabel.setText(I18n.tl("view.search"));
        searchResult.setText(I18n.tl("view.searchResult", queryCount, queryTime));
    }

}
