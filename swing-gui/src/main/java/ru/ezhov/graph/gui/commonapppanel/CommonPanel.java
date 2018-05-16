package ru.ezhov.graph.gui.commonapppanel;

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

    private GraphObjectsGui graphObjectsGui;
    private GraphTablePanel graphTablePanel;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private GraphPanel graphPanel;

    public CommonPanel(GraphObjectsGui graphObjectsGui) {
        this.graphObjectsGui = graphObjectsGui;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        graphPanel = new GraphPanel(graphObjectsGui);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        graphTablePanel = new GraphTablePanel(splitPane, graphObjectsGui, new GraphTableListener() {
            @Override
            public void event(EventType eventType, Object event, final GraphObjectGui graphObjectGui) {
                switch (eventType) {
                    case MOUSE_RELEASED:
                    case LIST_SELECTION_EVENT:
                        graphPanel.setSelectedGraphObject(graphObjectGui);
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

                                    Set<GraphObjectGui> childrenS = graphObjectGui.children();
                                    for (GraphObjectGui s : childrenS) {
                                        children.add(s);
                                    }

                                    Set<GraphObjectGui> parentS = graphObjectGui.parents();
                                    for (GraphObjectGui s : parentS) {
                                        parent.add(s);
                                    }

                                    DetailInfoPanel graphPanel = new DetailInfoPanel(graphObjectGui);
                                    int tabCount = tabbedPane.getTabCount();

                                    int index = tabbedPane.indexOfTab(graphObjectGui.id());
                                    if (index != -1) {
                                        tabbedPane.setSelectedIndex(index);
                                    } else {
                                        tabbedPane.addTab(graphObjectGui.id(), graphPanel);
                                        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new TabHeader(graphObjectGui.id(), tabbedPane, true));
                                        tabbedPane.setSelectedIndex(tabCount);
                                    }
                                }
                            });
                        }
                        break;
                    case HIDE_SHOW_TABLE_PANEL:
                        splitPane.resetToPreferredSizes();
                        break;

                    case SHOW_SELECTED_GRAPH_OBJECTS:
                        //TODO: Глаза обливаются кровью, нужно сделать адекватную обработку событий
                        //TODO: Нужно до конца реализовать загрузку по выделенным
                        GraphObjectsGui graphObjectsGui = (GraphObjectsGui) event;
                        tabbedPane.removeTabAt(tabbedPane.indexOfTab("ГРАФ"));
                        graphPanel = new GraphPanel(graphObjectsGui);
                        tabbedPane.addTab("ГРАФ", graphPanel);
                        tabbedPane.setTabComponentAt(0, new TabHeader("ГРАФ", tabbedPane, false));
                        break;
                }
            }
        });

        splitPane.setLeftComponent(graphTablePanel);
        tabbedPane.addTab("ГРАФ", graphPanel);
        tabbedPane.setTabComponentAt(0, new TabHeader("ГРАФ", tabbedPane, false));
        splitPane.setRightComponent(tabbedPane);
        add(splitPane, BorderLayout.CENTER);
    }
}