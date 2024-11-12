package dev.frilly.slangdict.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.interfaces.Translatable;
import dev.frilly.slangdict.listener.DocumentChangeListener;
import dev.frilly.slangdict.model.DictionaryViewModel;
import dev.frilly.slangdict.model.DictionaryViewRenderer;

/**
 * Implementation of the probably most used frame of this application.
 * 
 * This is where the dictionary is shown,along with options and tools
 * to manage said dictionary.
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

    // Buttons
    private final JButton addButton = new JButton();
    private final JButton deleteButton = new JButton();
    private final JButton lockButton = new JButton();
    private final JButton favoriteButton = new JButton();

    // Dictionary display.
    private final JTable table = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(table);

    private ViewFrame() {
        this.outerPane = new JPanel();
        this.outerPane.setLayout(new GridBagLayout());
        this.pane = new JPanel();

        this.setup();
        this.setupActions();
        I18n.register(this);
    }

    private void setup() {
        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);
        pane.setBorder(BorderFactory.createEmptyBorder(32, 16, 32, 16));

        table.setDefaultRenderer(List.class, new DictionaryViewRenderer());
        searchBox.setPreferredSize(new Dimension(600, 24));
        tableModel.query("").thenAccept(q -> {
            queryCount = q.queryCount();
            queryTime = q.time();
            updateTranslations();
        });
        scrollPane.setPreferredSize(new Dimension(800, 600));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(usingLabel)
                        .addComponent(databaseName))
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(rename))
                .addGap(16, 20, 24)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(searchLabel)
                        .addComponent(searchBox))
                .addComponent(searchResult)
                .addGap(16, 20, 24)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(addButton)
                        .addComponent(deleteButton)
                        .addComponent(lockButton)
                        .addComponent(favoriteButton))
                .addGap(6, 8, 10)
                .addComponent(scrollPane));

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(usingLabel)
                        .addGap(10)
                        .addComponent(databaseName))
                .addComponent(rename, Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(searchLabel)
                        .addGap(10)
                        .addComponent(searchBox))
                .addComponent(searchResult)
                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(deleteButton)
                        .addComponent(lockButton)
                        .addComponent(favoriteButton))
                .addComponent(scrollPane));

        outerPane.add(pane);
    }

    private void setupActions() {
        searchBox.getDocument().addDocumentListener((DocumentChangeListener) e -> {
            tableModel.query(searchBox.getText()).thenAccept(q -> {
                queryCount = q.queryCount();
                queryTime = q.time();
                updateTranslations();
            });
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
        usingLabel.setText(I18n.tl("view.using"));
        databaseName.setText(Dictionary.getInstance().getName());
        rename.setText(I18n.tl("view.rename"));

        searchLabel.setText(I18n.tl("view.search"));
        searchResult.setText(I18n.tl("view.searchResult", queryCount, queryTime));

        final var iconAdd = Application.getIcon(getClass().getResource("/icons/add.png"), 24, 24);
        addButton.setIcon(iconAdd);
        final var iconRemove = Application.getIcon(getClass().getResource("/icons/remove.png"), 24, 24);
        deleteButton.setIcon(iconRemove);
        final var iconStar = Application.getIcon(getClass().getResource("/icons/star.png"), 24, 24);
        favoriteButton.setIcon(iconStar);
        final var iconLock = Application.getIcon(getClass().getResource("/icons/lock.png"), 24, 24);
        lockButton.setIcon(iconLock);
    }

}
