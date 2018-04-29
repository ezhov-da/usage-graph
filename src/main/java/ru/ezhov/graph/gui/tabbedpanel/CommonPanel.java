package ru.ezhov.graph.gui.tabbedpanel;

import ru.ezhov.graph.gui.Selected;
import ru.ezhov.graph.gui.components.TabHeader;
import ru.ezhov.graph.gui.detailinfopanel.DetailInfoPanel;
import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.domain.GraphObjectsGui;
import ru.ezhov.graph.gui.graphpanel.GraphPanel;
import ru.ezhov.graph.gui.graphtablepanel.EventType;
import ru.ezhov.graph.gui.graphtablepanel.GraphTableListener;
import ru.ezhov.graph.gui.graphtablepanel.GraphTablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 20.04.2018.
 */
public class CommonPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(CommonPanel.class.getName());

    private GraphObjectsGui scripts;
    private GraphTablePanel graphTablePanel;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private GraphPanel graphPanel;

    public CommonPanel(GraphObjectsGui scripts) {
        this.scripts = scripts;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        final Selected selected = new Selected();

        graphPanel = new GraphPanel(scripts, selected);

        graphTablePanel = new GraphTablePanel(scripts, new GraphTableListener() {
            @Override
            public void event(EventType eventType, Object event, final GraphObjectGui script) {
                switch (eventType) {
                    case MOUSE_RELEASED:
                    case LIST_SELECTION_EVENT:
                        selected.setSelected(script.id());
                        CommonPanel.this.repaint();
                        graphPanel.repaint();
                        break;
                    case MOUSE_CLICKED:
                        MouseEvent mouseEvent = (MouseEvent) event;
                        if (mouseEvent.getClickCount() == 2) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Set<GraphObjectGui> children = new HashSet<>();
                                    Set<GraphObjectGui> parent = new HashSet<>();

                                    Set<GraphObjectGui> childrenS = script.children();
                                    for (GraphObjectGui s : childrenS) {
                                        children.add(s);
                                    }

                                    Set<GraphObjectGui> parentS = script.parents();
                                    for (GraphObjectGui s : parentS) {
                                        parent.add(s);
                                    }

                                    DetailInfoPanel graphPanel = new DetailInfoPanel(script);
                                    int tabCount = tabbedPane.getTabCount();

                                    int index = tabbedPane.indexOfTab(script.id());
                                    if (index != -1) {
                                        tabbedPane.setSelectedIndex(index);
                                    } else {
                                        tabbedPane.addTab(script.id(), graphPanel);
                                        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new TabHeader(script.id(), tabbedPane, true));
                                        tabbedPane.setSelectedIndex(tabCount);
                                    }
                                }
                            });
                        }
                        break;
                }
            }
        });


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.4);
        splitPane.setResizeWeight(0.4);

        splitPane.setLeftComponent(graphTablePanel);

        tabbedPane.addTab("ГРАФ", graphPanel);
        tabbedPane.setTabComponentAt(0, new TabHeader("ГРАФ", tabbedPane, false));

        splitPane.setRightComponent(tabbedPane);

        add(splitPane, BorderLayout.CENTER);

    }
}
