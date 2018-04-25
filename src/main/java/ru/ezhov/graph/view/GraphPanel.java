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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphPanel extends JPanel {
    private Scripts scripts;
    private Selected selected;

    public GraphPanel(Scripts scripts, Selected selected) {
        setLayout(new BorderLayout());
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
        }

        Layout<Integer, String> layout = new FRLayout(graph);
        layout.setSize(new Dimension(2000, 2000)); // sets the initial size of the space
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

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(label, BorderLayout.CENTER);
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(buttonSaveToJpg);
        panelTop.add(panelButton, BorderLayout.EAST);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(panelTop, BorderLayout.NORTH);
        add(panelVV, BorderLayout.CENTER);
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
}