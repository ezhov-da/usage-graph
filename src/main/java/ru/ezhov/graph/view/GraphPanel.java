package ru.ezhov.graph.view;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import org.apache.commons.collections15.Transformer;
import ru.ezhov.graph.script.Script;
import ru.ezhov.graph.script.Scripts;
import ru.ezhov.graph.util.PercentScreenDimension;
import ru.ezhov.graph.view.table.EventType;
import ru.ezhov.graph.view.table.TableScriptEvent;
import ru.ezhov.graph.view.table.TableScriptPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 20.04.2018.
 */
public class GraphPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(GraphPanel.class.getName());

    private Scripts scripts;
    private TableScriptPanel tableScriptPanel;
    private JTabbedPane tabbedPane = new JTabbedPane();

    public GraphPanel(Scripts scripts) {
        this.scripts = scripts;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        final Selected selected = new Selected();

        tableScriptPanel = new TableScriptPanel(scripts, new TableScriptEvent() {
            @Override
            public void event(EventType eventType, Object event, final Script script) {
                switch (eventType) {
                    case MOUSE_RELEASED:
                        selected.setSelected(script.id());
                        GraphPanel.this.repaint();
                        break;
                    case MOUSE_CLICKED:
                        MouseEvent mouseEvent = (MouseEvent) event;
                        if (mouseEvent.getClickCount() == 2) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Set<ScriptView> children = new HashSet<>();
                                    Set<ScriptView> parent = new HashSet<>();

                                    List<Script> childrenS = scripts.children(script.id());
                                    for (Script s : childrenS) {
                                        children.add(new DefaultScriptView(s));
                                    }

                                    List<Script> parentS = scripts.parents(script.id());
                                    for (Script s : parentS) {
                                        parent.add(new DefaultScriptView(s));
                                    }

                                    GraphDetailPanel graphPanel = new GraphDetailPanel(new ScriptViewDetail(script, parent, children));
                                    int tabCount = tabbedPane.getTabCount();

                                    int index = tabbedPane.indexOfTab(script.id());
                                    if (index != -1) {
                                        tabbedPane.setSelectedIndex(index);
                                    } else {
                                        tabbedPane.addTab(script.id(), graphPanel);
                                        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, new TabHeader(script.id(), tabbedPane));
                                        tabbedPane.setSelectedIndex(tabCount);
                                    }
                                }
                            });
                        }
                        break;
                }
            }
        });


        List<ScriptViewDetail> all = new ArrayList<>();
        final Graph<String, String> graph = new DirectedSparseMultigraph<>();
        for (Script script : scripts.all()) {
            Set<ScriptView> scriptViewParent = new HashSet<>();
            Set<ScriptView> scriptViewChildren = new HashSet<>();
            String id = script.id();
            List<Script> parents = scripts.parents(id);
            List<Script> children = scripts.children(id);
            for (Script child : children) {
                graph.addEdge(script.id() + "Использует " + child.id(), script.id(), child.id(), EdgeType.DIRECTED);
                scriptViewChildren.add(new DefaultScriptView(child));
            }
            for (Script parent : parents) {
                graph.addEdge(parent.id() + "Использует " + script.id(), parent.id(), script.id(), EdgeType.DIRECTED);
                scriptViewParent.add(new DefaultScriptView(parent));
            }
            all.add(new ScriptViewDetail(script, scriptViewParent, scriptViewChildren));
        }

        Layout<Integer, String> layout = new FRLayout(graph);
        layout.setSize(new

                Dimension(2000, 2000)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        final VisualizationViewer<Integer, String> vv =
                new VisualizationViewer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(2000, 2000)); //Sets the viewing area size
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.setEdgeToolTipTransformer(
                new Transformer<String, String>() {
                    @Override
                    public String transform(String edge) {
                        return graph.getEndpoints(edge).toString();
                    }
                }
        );
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexFillPaintTransformer(new MyVertexFillPaintFunction(selected));
        vv.getRenderer().getVertexLabelRenderer().setPositioner(new BasicVertexLabelRenderer.InsidePositioner());

        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);

        GraphZoomScrollPane panelVV = new GraphZoomScrollPane(vv);
        AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);

        vv.addKeyListener(graphMouse.getModeKeyListener());
        vv.setToolTipText("<html><center>Нажмите 'p' для возможности перетаскивания вершин <p>Нажмите 't' для перетаскивания всего графа");

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.4);
        splitPane.setResizeWeight(0.4);

        splitPane.setLeftComponent(tableScriptPanel);

        JPanel panelGraph = new JPanel();

        JPanel panelTop = new JPanel(new BorderLayout());

        JLabel label = new JLabel("<html><center>Нажмите 'p' для возможности перетаскивания вершин <p>Нажмите 't' для перетаскивания всего графа");
        JButton buttonSaveToJpg = new JButton("Сохранить в JPG");
        buttonSaveToJpg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = new BufferedImage(vv.getWidth(), vv.getHeight(), BufferedImage.TYPE_INT_RGB);
                vv.print(img.getGraphics()); // or: panel.printAll(...);
                try {
                    ImageIO.write(img, "jpg", new File("panel.jpg"));
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });
        panelTop.add(label, BorderLayout.CENTER);
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(buttonSaveToJpg);
        panelTop.add(panelButton, BorderLayout.EAST);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelGraph.add(panelTop, BorderLayout.NORTH);
        panelGraph.add(panelVV, BorderLayout.CENTER);


        tabbedPane.addTab("ГРАФ", panelGraph);
        tabbedPane.setTabComponentAt(0, new TabHeader("ГРАФ", tabbedPane));

        splitPane.setRightComponent(tabbedPane);

        add(splitPane, BorderLayout.CENTER);

    }

    private class MyVertexFillPaintFunction<V> implements Transformer<V, Paint> {
        private Selected selected;

        public MyVertexFillPaintFunction(Selected selected) {
            this.selected = selected;
        }

        public Paint transform(V v) {
            if (v.equals(selected.getSelected())) {
                return Color.BLUE;
            } else {
                return Color.RED;
            }
        }
    }

    private class Selected {
        private String selected;

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }
    }

    private class TabHeader extends JPanel {
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

    private class FlyFrame {
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
}
