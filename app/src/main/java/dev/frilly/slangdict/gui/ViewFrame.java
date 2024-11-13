package dev.frilly.slangdict.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.interfaces.Translatable;
import dev.frilly.slangdict.listener.DocumentChangeListener;
import dev.frilly.slangdict.model.DictionaryCellRenderer;

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

    private volatile int queryCount = 0;
    private volatile double queryTime = 0;

    private Future future;

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

    // Dictionary display. We're using a variation of scroll pane to make sure that not all data is
    // rendered at all time to improve performance.
    private final JPanel dictionaryViewport = new JPanel(new GridLayout(5, 1, 0, 16));
    private final List<DictionaryCellRenderer> viewportCells =
            IntStream.range(0, 5).mapToObj(i -> new DictionaryCellRenderer()).toList();
    private final JScrollPane viewportScroll = new JScrollPane(dictionaryViewport);
    private final List<String> displayedWords = new CopyOnWriteArrayList<>();

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

    private void query(final String txt) {
        // Cancel the current querying if there's another query coming.
        try {
            if (future != null)
                future.cancel(true);
        } catch (Exception e) {
        }

        future = CompletableFuture.runAsync(() -> {
            displayedWords.clear();
            final var time = System.currentTimeMillis();
            Dictionary.getInstance().getWords().entrySet().parallelStream()
                    .filter(e -> e.getKey().toLowerCase().contains(txt.toLowerCase())
                            || e.getValue().definition.stream()
                                    .anyMatch(s -> s.toLowerCase().contains(txt.toLowerCase())))
                    .map(e -> e.getKey()).sorted().forEach(displayedWords::add);
            queryCount = displayedWords.size();
            queryTime = (System.currentTimeMillis() - time) / 1000.0;
        }).thenRun(() -> {
            updateTranslations();
            repaint(0);

            final var paneHeight = viewportCells.get(0).getHeight();
            viewportScroll.getVerticalScrollBar().getModel().setRangeProperties(0, paneHeight, 0,
                    paneHeight * displayedWords.size(), false);;
        });
    }

    private void repaint(final int i) {
        for (var idx = i; idx < displayedWords.size() && idx < idx + 5; idx++) {
            final var word = Dictionary.getInstance().getWord(displayedWords.get(idx));
            viewportCells.get(idx - i).setWord(word);
        }
        viewportScroll.revalidate();
        viewportScroll.repaint();
    }

    private void setup() {
        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);
        pane.setBorder(BorderFactory.createEmptyBorder(32, 16, 32, 16));

        searchBox.setPreferredSize(new Dimension(600, 24));
        viewportCells.forEach(dictionaryViewport::add);
        viewportScroll.getVerticalScrollBar().getModel().addChangeListener(e -> {
            final var i = viewportScroll.getVerticalScrollBar().getValue()
                    / viewportScroll.getVerticalScrollBar().getUnitIncrement();
            repaint(i);
        });
        query("");

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
                .addGap(6, 8, 10).addComponent(viewportScroll));

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
                        .addComponent(viewportScroll));

        layout.linkSize(addButton, deleteButton, favoriteButton, lockButton, reloadButton,
                saveButton);
        outerPane.add(pane);
    }

    private void setupActions() {
        searchBox.getDocument().addDocumentListener((DocumentChangeListener) e -> {
            searchResult.setText(I18n.tl("view.querying"));
            query(searchBox.getText());
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
