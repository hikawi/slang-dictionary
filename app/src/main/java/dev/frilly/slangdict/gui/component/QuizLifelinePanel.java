package dev.frilly.slangdict.gui.component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A component that puts lifeline choices.
 */
public final class QuizLifelinePanel extends JPanel {

    private final JPanel contentPanel = new JPanel();

    // Label and explanation
    private final JLabel title = new JLabel("Lifelines");
    private final JTextArea description = new JTextArea("For each quiz you may pick up to 4 partners to play with. Their abilities may help with gaining a high score before your HP runs out. Hover over each partner to see their ability. Note that each partner may only be activated once.", 3, 40);

    // Choices
    private final Map<Partner, JCheckBox> partners = Stream.of(Partner.NOAH, Partner.MIO, Partner.EUNIE, Partner.TAION, Partner.LANZ, Partner.SENA, Partner.MATTHEW, Partner.A, Partner.NIKOL, Partner.GLIMMER, Partner.SHULK, Partner.REX).map(s -> Map.entry(s, new JCheckBox(s.name))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    // Queue to make sure only 4 are selected at one time.
    private final Deque<Partner> queue = new ArrayDeque<>(4);

    public QuizLifelinePanel() {
        setup();
        setupStyles();
        setupActions();
    }

    /**
     * Retrieves the selected partners.
     *
     * @return The partners.
     */
    public List<Partner> getQueue() {
        return queue.stream().toList();
    }

    private void setup() {
        final var l = new GroupLayout(contentPanel);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);
        contentPanel.setLayout(l);

        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setLayout(new BorderLayout());

        final var gridLayout = new GridLayout(6, 2, 16, 4);
        final var gridPane = new JPanel(gridLayout);

        Stream.of(Partner.NOAH, Partner.MATTHEW, Partner.MIO, Partner.A, Partner.EUNIE, Partner.NIKOL, Partner.TAION, Partner.GLIMMER, Partner.LANZ, Partner.SHULK, Partner.SENA, Partner.REX)
            .map(partners::get).forEach(gridPane::add);

        l.setHorizontalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(title)
            .addComponent(description)
            .addComponent(gridPane));
        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(true, title)
            .addComponent(description)
            .addComponent(gridPane));

        add(contentPanel, BorderLayout.CENTER);
    }

    private void setupStyles() {
        title.putClientProperty("FlatLaf.styleClass", "h3");

        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setOpaque(false);

        partners.get(Partner.NOAH).setToolTipText("Your HP and Combo Stack is invulnerable for the next 10 seconds.");
        partners.get(Partner.MIO).setToolTipText("Convert 30% of damage taken into points.");
        partners.get(Partner.EUNIE).setToolTipText("Heal 20% of your HP.");
        partners.get(Partner.TAION).setToolTipText("Reduce damage taken for the next 3 incorrect answers.");
        partners.get(Partner.SENA).setToolTipText("Multiply the next score gain by 4x.");
        partners.get(Partner.LANZ).setToolTipText("Reduce damage taken slightly by clock ticking and incorrect answers for the entire game, but you gain 0.5x score.");

        partners.get(Partner.MATTHEW).setToolTipText("Increase combo multiplier by 1.5x.");
        partners.get(Partner.A).setToolTipText("Heal 5% of your HP and block all damage taken for the next 10 seconds.");
        partners.get(Partner.NIKOL).setToolTipText("Reduce damage taken greatly by clock ticking and incorrect answers for the entire game, but you don't have a Combo Stack.");
        partners.get(Partner.GLIMMER).setToolTipText("Heal 10% of your HP and increase score gain for the next 3 answers.");
        partners.get(Partner.REX).setToolTipText("Deal damage to self and multiply the next score gain by 10x.");
        partners.get(Partner.SHULK).setToolTipText("Once you take fatal damage, regain 10% of max HP.");
    }

    private void setupActions() {
        partners.forEach((key, value) -> value.addActionListener(ev -> {
            if(queue.contains(key)) {
                if(value.isSelected())
                    return;
                queue.remove(key);
                value.setSelected(false);
                return;
            }

            if (queue.size() >= 4) {
                final var last = queue.pollLast();
                partners.get(last).setSelected(false);
            }

            queue.addLast(key);
            value.setSelected(true);
        }));
    }

    public enum Partner {

        NOAH("Noah (Brave Assault)"),
        MIO("Mio (Lightning Quick)"),
        EUNIE("Eunie (Pinion Primed)"),
        TAION("Taion (Art of Subjugation)"),
        LANZ("Lanz (Tyrant Wave)"),
        SENA("Sena (Bombshell Blitz)"),

        MATTHEW("Matthew (Avalanche Onslaught)"),
        A("A (Lightspeed)"),
        NIKOL("Nikol (Cyclone Form)"),
        GLIMMER("Glimmer (Red-Hot Paean)"),
        SHULK("Shulk (Liberation Edge)"),
        REX("Rex (Deliverance Hew)");

        public final String name;

        Partner(final String name) {
            this.name = name;
        }

    }

}
