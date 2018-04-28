package ru.ezhov.graph.gui.components;

import ru.ezhov.graph.util.PercentScreenDimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FlyFrame {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private String text;
    private Component component;

    public FlyFrame(final String text, final JTabbedPane tabbedPane, final Component component) {
        this.tabbedPane = tabbedPane;
        this.text = text;
        this.component = component;

        frame = new JFrame(text);
        frame.setIconImage(new ImageIcon(this.getClass().getResource("/graph_16x16.png")).getImage());
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                frame.remove(component);
                tabbedPane.addTab(text, component);
                tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new TabHeader(text, tabbedPane));
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(component);
        frame.setSize(new PercentScreenDimension(70).dimension());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
