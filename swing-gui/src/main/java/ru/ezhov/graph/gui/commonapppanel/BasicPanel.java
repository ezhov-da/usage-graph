package ru.ezhov.graph.gui.commonapppanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ezhov_da on 25.04.2018.
 */
public class BasicPanel extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(BasicPanel.class);

    private JButton buttonLoad;

    private JPanel panelStub;
    private JPanel panelCenter;

    public BasicPanel() {
        setLayout(new BorderLayout());
        panelStub = new JPanel(new BorderLayout());
        JLabel labelStub = new JLabel("Построитель графов");
        labelStub.setHorizontalAlignment(SwingConstants.CENTER);
        panelStub.add(labelStub, BorderLayout.CENTER);
//        add(directoryPanel, BorderLayout.NORTH);
        panelCenter = panelStub;

        buttonLoad = new JButton("Загрузить скрипты");

        add(panelCenter, BorderLayout.CENTER);
    }
}
