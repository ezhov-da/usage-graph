package ru.ezhov.graph.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabHeader extends JPanel {
    private JLabel label;
    private JButton buttonFly;
    private JButton buttonClose;
    private JTabbedPane tabbedPane;
    private String text;

    public TabHeader(final String text, final JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        this.text = text;
        setOpaque(false);
        label = new JLabel(text);

        buttonFly = new JButton(new ImageIcon(this.getClass().getResource("/not_fly_16x16.png")));
        buttonFly.setToolTipText("Открепить");
        setDefaultButtonProperties(buttonFly);

        buttonClose = new JButton("x");
        setDefaultButtonProperties(buttonClose);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.setOpaque(false);
        panelButtons.add(buttonFly);
        if (!"ГРАФ".equals(text)) {
            panelButtons.add(buttonClose);
        }
        add(panelButtons, BorderLayout.EAST);
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.remove(tabbedPane.indexOfTab(text));
            }
        });

        buttonFly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = tabbedPane.indexOfTab(text);
                Component component = tabbedPane.getComponentAt(index);
                tabbedPane.remove(index);
                new FlyFrame(text, tabbedPane, component);
            }
        });

    }

    private void setDefaultButtonProperties(JButton button) {
        Dimension dimension = new Dimension(20, 20);
        button.setMaximumSize(dimension);
        button.setMinimumSize(dimension);
        button.setBorder(null);
        button.setPreferredSize(dimension);
        button.setSize(dimension);
        button.setFont(new Font(new JLabel().getFont().getName(), Font.PLAIN, 8));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabHeader tabHeader = (TabHeader) o;
        return text != null ? text.equals(tabHeader.text) : tabHeader.text == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
