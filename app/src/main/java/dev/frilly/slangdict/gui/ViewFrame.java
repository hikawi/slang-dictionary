package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.features.file.BombFeature;
import dev.frilly.slangdict.features.file.CloseDatabaseFeature;
import dev.frilly.slangdict.features.file.ReloadFeature;
import dev.frilly.slangdict.features.file.SaveFeature;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.interfaces.Translatable;
import dev.frilly.slangdict.listener.DocumentChangeListener;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Implementation of the probably most used frame of this application.
 *
 * This is where the dictionary is shown,along with options and tools to manage said dictionary.
 */
public final class ViewFrame implements Overrideable, Translatable {

    private static ViewFrame instance;

    private Future<?> future;

    // -------------
    // UI Components.
    // -------------

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

    // Database-wide buttons
    private final JButton addButton = new JButton();
    private final JButton bombButton = new JButton();
    private final JButton saveButton = new JButton();
    private final JButton reloadButton = new JButton();

    // Dictionary display. We're only showing 5 words to make sure that not all data is
    // rendered at all time to improve performance. We're gonna use a form of pagination.
    private final JPanel[] viewport = new JPanel[5];
    private final JPanel emptyViewport = new JPanel();

    // Pagination data
    private volatile int page = 0;
    private volatile int pageMax = 0;
    private final List<Word> currentWords = Collections.synchronizedList(new ArrayList<>());

    // private final JPanel dictionaryViewport = new JPanel(
    //     new GridLayout(5, 1, 0, 16)
    // );
    // private final List<DictionaryCellRenderer> viewportCells = IntStream.range(
    //     0,
    //     5
    // )
    //     .mapToObj(i -> new DictionaryCellRenderer())
    //     .toList();
    // private final List<String> displayedWords = new CopyOnWriteArrayList<>();

    // Pagination data.
    // private volatile int page = 0;
    // private final JButton previousButton = new JButton();
    // private final JButton nextButton = new JButton();
    // private final JLabel pageText = new JLabel();

    private ViewFrame() {
        this.pane = new JPanel();

        databaseName.putClientProperty("FlatLaf.styleClass", "h3");
        searchLabel.putClientProperty("FlatLaf.styleClass", "medium");
        searchResult.putClientProperty("FlatLaf.styleClass", "semibold");

        backButton.setIcon(Application.getIcon(getClass().getResource("/icons/back.png"), 12, 12));
        addButton.setIcon(Application.getIcon(getClass().getResource("/icons/add.png"), 24, 24));
        bombButton.setIcon(Application.getIcon(getClass().getResource("/icons/bomb.png"), 24, 24));
        saveButton.setIcon(Application.getIcon(getClass().getResource("/icons/save.png"), 24, 24));
        reloadButton.setIcon(Application.getIcon(getClass().getResource("/icons/refresh.png"), 24, 24));
        // nextButton.setIcon(
        //     Application.getIcon(
        //         getClass().getResource("/icons/next.png"),
        //         24,
        //         24
        //     )
        // );
        // previousButton.setIcon(
        //     Application.getIcon(
        //         getClass().getResource("/icons/previous.png"),
        //         24,
        //         24
        //     )
        // );

        this.setup();
        this.setupActions();
        I18n.register(this);
    }

    private void query(final String txt) {
        // Cancel the current querying if there's another query coming.
        try {
            if (future != null) future.cancel(true);
        } catch (Exception e) {}

        future = CompletableFuture.runAsync(() -> {}).thenRun(() -> {
            page = 0;
            repaint();
            updateTranslations();
        });
    }

    private void repaint() {}

    private void setup() {
        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);
        pane.setBorder(BorderFactory.createEmptyBorder(32, 16, 32, 16));

        searchBox.setPreferredSize(new Dimension(600, 24));
        query("");

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
                        .createParallelGroup(Alignment.TRAILING)
                        .addComponent(addButton)
                        .addComponent(bombButton)
                        .addComponent(saveButton)
                        .addComponent(reloadButton)
                )
                .addGap(12)
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
                        .addComponent(bombButton)
                        .addComponent(saveButton)
                        .addComponent(reloadButton)
                )
        );

        layout.linkSize(addButton, bombButton, bombButton, reloadButton, saveButton);
    }

    private void setupActions() {
        searchBox
            .getDocument()
            .addDocumentListener(
                (DocumentChangeListener) e -> {
                    searchResult.setText(I18n.tl("view.querying"));
                    query(searchBox.getText());
                }
            );

        // Rename button replaces the "database name field" with a textbox, and the rename
        // button with a confirm button.
        rename.addActionListener(e -> {
            final var layout = (GroupLayout) pane.getLayout();
            layout.replace(rename, renameConfirm);
            layout.replace(databaseName, databaseNameField);
            databaseNameField.setText(databaseName.getText());
            databaseNameField.requestFocus();
        });
        databaseNameField
            .getDocument()
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
            updateTranslations();
        });

        bombButton.addActionListener(e -> {
            final var res = Dialogs.confirm("file.bomb.confirm", Dictionary.getInstance().getName());
            if (res != JOptionPane.YES_OPTION) return;

            new BombFeature().run();
            query("");
        });
        saveButton.addActionListener(e -> {
            new SaveFeature().run();
            if (Dictionary.getInstance().getFile() != null) Dialogs.info(
                "file.save",
                Dictionary.getInstance().getName()
            );
        });
        reloadButton.addActionListener(e -> {
            new ReloadFeature().run();
            if (Dictionary.getInstance().getFile() != null) Dialogs.info(
                "file.reload",
                Dictionary.getInstance().getName()
            );
            repaint();
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
        updateTranslations();
        repaint();
        return pane;
    }

    @Override
    public void updateTranslations() {
        backButton.setText(I18n.tl("view.close"));
        usingLabel.setText(I18n.tl("view.using"));
        databaseName.setText(Dictionary.getInstance().getName());
        rename.setText(I18n.tl("view.rename"));
        renameConfirm.setText(I18n.tl("view.rename.confirm"));

        searchLabel.setText(I18n.tl("view.search"));
        // searchResult.setText(
        //     I18n.tl("view.searchResult", queryCount, queryTime)
        // );
        // pageText.setText(
        //     I18n.tl(
        //         "view.page",
        //         page + 1,
        //         Math.round(Math.ceil(displayedWords.size() / 5.0))
        //     )
        // );
    }
}
