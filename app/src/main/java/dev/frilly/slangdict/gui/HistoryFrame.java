package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.gui.component.SearchHistory;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import java.awt.*;

/**
 * The frame that shows your search history.
 */
public final class HistoryFrame implements Overrideable {

    private static HistoryFrame instance;

    private final JPanel panel = new JPanel();
    private final SearchHistory history = new SearchHistory();

    private final JLabel searchHistory = new JLabel("Your Search History");
    private final JTable searchList = new JTable(history);
    private final JScrollPane scrollPane = new JScrollPane(searchList);

    private final JButton clearHistory = new JButton("Clear History");
    private final JButton back = new JButton("Back");

    private HistoryFrame() {
        setup();
        setupActions();
    }

    public static HistoryFrame getInstance() {
        return instance == null ? instance = new HistoryFrame() : instance;
    }

    /**
     * Pushes an entry to the search history.
     *
     * @param q The entry.
     */
    public void push(final String q, final int queries, final double timeTaken) {
        history.push(q, queries, timeTaken);
    }

    private void setup() {
        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        searchList.getColumnModel().getColumn(3).setMinWidth(300);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        l.setHorizontalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(searchHistory)
            .addComponent(scrollPane)
            .addGroup(GroupLayout.Alignment.TRAILING, l.createSequentialGroup()
                .addComponent(back)
                .addComponent(clearHistory)));

        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(searchHistory)
            .addGap(8, 10, 12)
            .addComponent(scrollPane)
            .addGroup(l.createParallelGroup()
                .addComponent(back)
                .addComponent(clearHistory)));

        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
    }

    private void setupActions() {
        back.addActionListener(e -> MainFrame.getInstance().back());
        clearHistory.addActionListener(e -> history.clear());
    }

    @Override
    public JPanel getOverridingPane() {
        return panel;
    }

}
