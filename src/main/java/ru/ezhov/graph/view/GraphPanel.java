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

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
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
    private ScriptListModel scriptListModel;
    private JTabbedPane tabbedPane = new JTabbedPane();

    public GraphPanel(Scripts scripts) {
        this.scripts = scripts;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        List<ScriptViewDetail> all = new ArrayList<>();
        final Graph<String, String> graph = new DirectedSparseMultigraph<>();
        for (Script script : scripts.all()) {
            Set<ScriptView> scriptViewParent = new HashSet<>();
            Set<ScriptView> scriptViewChildren = new HashSet<>();
            String id = script.id();
            Set<Script> parents = scripts.parents(id);
            Set<Script> children = scripts.children(id);
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

        JPanel panelSearchScript = new JPanel(new BorderLayout());
        final JTextField textField = new JTextField();
        panelSearchScript.add(textField, BorderLayout.CENTER);
        JButton buttonClearSearch = new JButton(new ImageIcon(this.getClass().getResource("/clear_16x16.png")));
        buttonClearSearch.setToolTipText("Очистить");
        Dimension dimension = new Dimension(20, buttonClearSearch.getHeight());
        buttonClearSearch.setSize(dimension);
        buttonClearSearch.setMaximumSize(dimension);
        buttonClearSearch.setMinimumSize(dimension);
        buttonClearSearch.setPreferredSize(dimension);
        panelSearchScript.add(buttonClearSearch, BorderLayout.EAST);

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = textField.getText();
                if (text.length() >= 3 || "".equals(text.trim())) {
                    scriptListModel.find(text);
                }
            }
        });

        buttonClearSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
                scriptListModel.find("");
            }
        });

//        Collections.sort(all);

        final Selected selected = new Selected();

        final JList list = new JList();
        scriptListModel = new ScriptListModel(all);
        list.setModel(scriptListModel);
        list.setCellRenderer(new ScriptListRender());
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ScriptViewDetail s = (ScriptViewDetail) list.getSelectedValue();
                if (s != null) {
                    selected.setSelected(s.id());
                    GraphPanel.this.repaint();
                }
            }
        });

        list.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    ScriptViewDetail s = (ScriptViewDetail) list.getSelectedValue();
                                    GraphDetailPanel graphPanel = new GraphDetailPanel(s);
                                    int tabCount = tabbedPane.getTabCount();

                                    int index = tabbedPane.indexOfTab(s.id());
                                    if (index != -1) {
                                        tabbedPane.setSelectedIndex(index);
                                    } else {
                                        tabbedPane.addTab(s.id(), graphPanel);
                                        tabbedPane.setSelectedIndex(tabCount);
                                    }
                                }
                            });
                        }
                    }
                }
        );

        Layout<Integer, String> layout = new FRLayout(graph);
        layout.setSize(new

                Dimension(2000, 2000)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        VisualizationViewer<Integer, String> vv =
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

        JPanel panelWithList = new JPanel(new BorderLayout());
        panelWithList.add(panelSearchScript, BorderLayout.NORTH);
        panelWithList.add(new JScrollPane(list), BorderLayout.CENTER);

        splitPane.setLeftComponent(panelWithList);

        JPanel panelGraph = new JPanel();
        JLabel label = new JLabel("<html><center>Нажмите 'p' для возможности перетаскивания вершин <p>Нажмите 't' для перетаскивания всего графа");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelGraph.add(label, BorderLayout.NORTH);
        panelGraph.add(panelVV, BorderLayout.CENTER);

        tabbedPane.addTab("ГРАФ", panelGraph);

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
}
