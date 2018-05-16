package ru.ezhov.graph.gui.graphpanel;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.VoltageScorer;
import edu.uci.ics.jung.algorithms.scoring.util.VertexScoreTransformer;
import edu.uci.ics.jung.algorithms.util.SelfLoopEdgePredicate;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.*;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeArrowRenderingSupport;
import edu.uci.ics.jung.visualization.renderers.CenterEdgeArrowRenderingSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.domain.GraphObjectsGui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class GraphPanel extends JPanel implements ActionListener {

    private static final Logger LOG = LoggerFactory.getLogger(GraphPanel.class);

    private static final int GRADIENT_NONE = 0;
    private static final int GRADIENT_RELATIVE = 1;
    private static int gradient_level = GRADIENT_NONE;
    private GraphObjectsGui scripts;
    private JCheckBox v_color;
    private JCheckBox e_color;
    private JCheckBox v_stroke;
    private JCheckBox e_uarrow_pred;
    private JCheckBox e_darrow_pred;
    private JCheckBox e_arrow_centered;
    private JCheckBox v_shape;
    private JCheckBox v_size;
    private JCheckBox v_aspect;
    private JCheckBox v_labels;
    private JRadioButton e_line;
    private JRadioButton e_bent;
    private JRadioButton e_wedge;
    private JRadioButton e_quad;
    private JRadioButton e_ortho;
    private JRadioButton e_cubic;
    private JCheckBox e_labels;
    private JCheckBox font;
    private JCheckBox e_show_d;
    private JCheckBox e_show_u;
    private JCheckBox v_small;
    private JCheckBox zoom_at_mouse;
    private JCheckBox fill_edges;

    private JSlider slider;

    private Layout<String, String> graphLayout;
    private JRadioButton no_gradient;
    private JRadioButton gradient_relative;
    private SeedFillColor<String> seedFillColor;
    private SeedDrawColor<String> seedDrawColor;
    private EdgeWeightStrokeFunction<String> ewcs;
    private VertexStrokeHighlight<String, String> vsh;
    private Transformer<String, String> vs_none;
    private Transformer<String, String> es_none;
    private VertexFontTransformer<String> vff;
    private EdgeFontTransformer<String> eff;
//    private VertexShapeSizeAspect<String, String> vssa;
    private DirectionDisplayPredicate<String, String> show_edge;
    private DirectionDisplayPredicate<String, String> show_arrow;
    private VertexDisplayPredicate<String, String> show_vertex;
    private Predicate<Context<Graph<String, String>, String>> self_loop;
    private GradientPickedEdgePaintFunction<String, String> edgeDrawPaint;
    private GradientPickedEdgePaintFunction<String, String> edgeFillPaint;
    private Map<String, Number> edge_weight = new HashMap<String, Number>();
//    private Transformer<String, Double> voltages;
    private Map<String, Double> transparency = new HashMap<String, Double>();
    private VisualizationViewer<String, String> vv;
    private DefaultModalGraphMouse<String, String> gm;
    private Set<String> seedVertices = new HashSet<String>();
    private Graph<String, String> g;


    public GraphPanel(GraphObjectsGui scripts) {
        setLayout(new BorderLayout());
        this.scripts = scripts;
        start();
    }

    public void start() {
        add(startFunction());
    }

    public JPanel startFunction() {
        g = getGraph();
        graphLayout = new FRLayout<String, String>(g);

        vv = new VisualizationViewer<String, String>(graphLayout);
        PickedState<String> picked_state = vv.getPickedVertexState();
        self_loop = new SelfLoopEdgePredicate<String, String>();
        // create decorators
        seedFillColor = new SeedFillColor<String>(picked_state);
        seedDrawColor = new SeedDrawColor<String>(picked_state);
        ewcs = new EdgeWeightStrokeFunction<String>(edge_weight);
        vsh = new VertexStrokeHighlight<String, String>(g, picked_state);
        vff = new VertexFontTransformer<String>();
        eff = new EdgeFontTransformer<String>();
        vs_none = new ConstantTransformer(null);
        es_none = new ConstantTransformer(null);
//        vssa = new VertexShapeSizeAspect<String, String>(g, voltages);
        show_edge = new DirectionDisplayPredicate<String, String>(true, true);
        show_arrow = new DirectionDisplayPredicate<String, String>(true, false);
        show_vertex = new VertexDisplayPredicate<String, String>(false);

        // использует край градиента, если он не установлен, в противном случае используется выбранный выбор
        edgeDrawPaint =
                new GradientPickedEdgePaintFunction<String, String>(
                        new PickableEdgePaintTransformer<String>(
                                vv.getPickedEdgeState(), Color.black, Color.cyan), vv);
        edgeFillPaint =
                new GradientPickedEdgePaintFunction<String, String>(
                        new PickableEdgePaintTransformer<String>(
                                vv.getPickedEdgeState(), Color.black, Color.cyan), vv);

        vv.getRenderContext().setVertexFillPaintTransformer(seedFillColor);
        vv.getRenderContext().setVertexDrawPaintTransformer(seedDrawColor);
        vv.getRenderContext().setVertexStrokeTransformer(vsh);
//        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
        vv.getRenderContext().setVertexFontTransformer(vff);
//        vv.getRenderContext().setVertexShapeTransformer(vssa);
        vv.getRenderContext().setVertexIncludePredicate(show_vertex);

        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPaint);
//        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());
        vv.getRenderContext().setEdgeFontTransformer(eff);
        vv.getRenderContext().setEdgeStrokeTransformer(ewcs);
        vv.getRenderContext().setEdgeIncludePredicate(show_edge);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<String, String>());
        vv.getRenderContext().setEdgeArrowPredicate(show_arrow);

        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        vv.getRenderContext().setArrowDrawPaintTransformer(new ConstantTransformer(Color.black));

        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());

        vv.setBackground(Color.white);
        GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
        jp.add(scrollPane);
        gm = new DefaultModalGraphMouse<String, String>();
        vv.setGraphMouse(gm);
        gm.add(new PopupGraphMousePlugin());

        addBottomControls(jp);
//        vssa.setScaling(true);

//        vv.setVertexToolTipTransformer(new VoltageTips<String>());
        vv.setToolTipText("<html><center>Используйте колесико мыши для увеличения<p>Нажмите и перетащите мышь для панорамирования<p>Shift-click и Drag для вращения</center></html>");

        return jp;
    }

    public Graph<String, String> getGraph() {
        Graph<String, String> g = new DirectedSparseMultigraph<>();

        Set<String> sources = new HashSet<String>();
        Set<String> sinks = new HashSet<String>();

        List<GraphObjectGui> scriptsGuis = scripts.all();
        for (GraphObjectGui script : scriptsGuis) {
            String id = script.id();
            Set<GraphObjectGui> parents = script.parents();
            Set<GraphObjectGui> children = script.children();
            double childrenCount = parents.size();
            double parentCount = children.size();

            for (GraphObjectGui child : children) {
                g.addEdge(script.id() + "Использует " + child.id(), script.id(), child.id(), EdgeType.DIRECTED);
                edge_weight.put(script.id() + "Использует " + child.id(), (childrenCount + parentCount) != 0 ? ((Double) (childrenCount / (childrenCount + parentCount))) : 0);
            }

            for (GraphObjectGui parent : parents) {
                g.addEdge(parent.id() + "Использует " + script.id(), parent.id(), script.id(), EdgeType.DIRECTED);
            }

//            if (!script.children().isEmpty()) {
//                seedVertices.add(script.id());
//                sources.add(script.id());
//            } else if (!script.parents().isEmpty()) {
//                sinks.add(script.id());
//            }
        }

//        if (seedVertices.size() < 2) {
//            LOG.error("need at least 2 seeds (one source, one sink)");
//        }
//
//        VoltageScorer<String, String> voltage_scores =
//                new VoltageScorer<String, String>(g, MapTransformer.getInstance(edge_weight), sources, sinks);
//        voltage_scores.evaluate();
//        voltages = new VertexScoreTransformer<String, Double>(voltage_scores);

        Collection<String> verts = g.getVertices();

        // присваиваем значение прозрачности 1 для всех вершин
        for (String v : verts) {
            transparency.put(v, 1D);
        }

        return g;
    }

    /**
     * @param jp panel to which controls will be added
     */
    protected void addBottomControls(final JPanel jp) {
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new BoxLayout(control_panel, BoxLayout.Y_AXIS));
        jp.add(control_panel, BorderLayout.EAST);

        final Component slidePanel = createSliderPanel();
        JPanel panelSlide = new JPanel(new BorderLayout());
        panelSlide.add(slidePanel, BorderLayout.CENTER);

        final Component vertex_panel = createVertexPanel();
        JPanel panelVertex = new JPanel(new BorderLayout());
        panelVertex.add(vertex_panel, BorderLayout.CENTER);
        final Component edge_panel = createEdgePanel();
        JPanel panelEdge = new JPanel(new BorderLayout());
        panelEdge.add(edge_panel, BorderLayout.CENTER);
        final Component both_panel = createBothPanel();
        JPanel panelBoth = new JPanel(new BorderLayout());
        panelBoth.add(both_panel, BorderLayout.CENTER);

        control_panel.add(panelSlide);
        control_panel.add(panelVertex);
        control_panel.add(panelEdge);
        control_panel.add(panelBoth);
        control_panel.add(Box.createVerticalGlue());
    }

    private Component createSliderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Размер полотна (перерисовка графа)"));
        slider = new JSlider(JSlider.HORIZONTAL, (int) graphLayout.getSize().getWidth(), 5000, (int) graphLayout.getSize().getWidth());
        JButton button = new JButton("Применить");
        final JTextField textField = new JTextField();
        textField.setEnabled(false);
        textField.setText(String.valueOf(slider.getValue()));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textField.setText(String.valueOf(slider.getValue()));
            }
        });
        panel.add(slider, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        panel.add(textField, BorderLayout.SOUTH);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphLayout = new FRLayout<String, String>(g);
                GraphPanel.this.graphLayout.setSize(new Dimension(slider.getValue(), slider.getValue()));
                vv.setGraphLayout(graphLayout);
                vv.repaint();
            }
        });
        return panel;
    }

    private Component createVertexPanel() {
        final Box vertex_panel = Box.createVerticalBox();
        vertex_panel.setBorder(BorderFactory.createTitledBorder("Вершины"));
        // set up vertex controls
        v_color = new JCheckBox("подсветить родителей");
        v_color.addActionListener(this);
        v_stroke = new JCheckBox("подсветить зависимости выделенной вершины");
        v_stroke.addActionListener(this);
        v_labels = new JCheckBox("отобразить названия");
        v_labels.addActionListener(this);
        v_shape = new JCheckBox("форма в зависимости от степени");
        v_shape.addActionListener(this);
        v_size = new JCheckBox("размер в зависимости от устойчивости");
        v_size.addActionListener(this);
        v_aspect = new JCheckBox("коэффициент растяжения по степени");
        v_aspect.addActionListener(this);
        v_small = new JCheckBox("фильтровать, когда степень < " + VertexDisplayPredicate.MIN_DEGREE);
        v_small.addActionListener(this);

        vertex_panel.add(v_color);
        vertex_panel.add(v_stroke);
        vertex_panel.add(v_labels);
//        vertex_panel.add(v_shape);
        //TODO: реализовать
        //vertex_panel.add(v_size);
        //TODO: реализовать
        //vertex_panel.add(v_aspect);
        //TODO: реализовать
        //vertex_panel.add(v_small);
        return vertex_panel;
    }

    private Component createEdgePanel() {
        final Box edge_panel = Box.createVerticalBox();
        edge_panel.setBorder(BorderFactory.createTitledBorder("Грани"));

        JPanel gradient_panel = new JPanel(new GridLayout(1, 0));
        gradient_panel.setBorder(BorderFactory.createTitledBorder("Отрисовка грани"));
        no_gradient = new JRadioButton("Сплошная");
        no_gradient.addActionListener(this);
        no_gradient.setSelected(true);
        gradient_relative = new JRadioButton("Градиент");
        gradient_relative.addActionListener(this);
        ButtonGroup bg_grad = new ButtonGroup();
        bg_grad.add(no_gradient);
        bg_grad.add(gradient_relative);
        gradient_panel.add(no_gradient);
        gradient_panel.add(gradient_relative);

        JPanel shape_panel = new JPanel(new GridLayout(3, 2));
        shape_panel.setBorder(BorderFactory.createTitledBorder("Фигура грани"));
        e_line = new JRadioButton("линия");
        e_line.addActionListener(this);
        e_line.setSelected(true);
        e_wedge = new JRadioButton("клин");
        e_wedge.addActionListener(this);
        e_quad = new JRadioButton("квадратная кривая");
        e_quad.addActionListener(this);
        e_cubic = new JRadioButton("кривая");
        e_cubic.addActionListener(this);
        e_ortho = new JRadioButton("ортогональная");
        e_ortho.addActionListener(this);
        ButtonGroup bg_shape = new ButtonGroup();
        bg_shape.add(e_line);
        bg_shape.add(e_wedge);
        bg_shape.add(e_quad);
        bg_shape.add(e_ortho);
        bg_shape.add(e_cubic);
        shape_panel.add(e_line);
        shape_panel.add(e_wedge);
        shape_panel.add(e_quad);
        shape_panel.add(e_cubic);
        shape_panel.add(e_ortho);
        fill_edges = new JCheckBox("заполнять грань");
        fill_edges.setSelected(false);
        fill_edges.addActionListener(this);
        shape_panel.add(fill_edges);
        shape_panel.setOpaque(true);
        e_color = new JCheckBox("подсветить грань по весу");
        e_color.addActionListener(this);
        e_labels = new JCheckBox("показать вес грани");
        e_labels.addActionListener(this);
        e_uarrow_pred = new JCheckBox("ненаправленная");
        e_uarrow_pred.addActionListener(this);
        e_darrow_pred = new JCheckBox("направленная");
        e_darrow_pred.addActionListener(this);
        e_darrow_pred.setSelected(true);
        e_arrow_centered = new JCheckBox("центрировать");
        e_arrow_centered.addActionListener(this);
        JPanel arrow_panel = new JPanel(new GridLayout(1, 0));
        arrow_panel.setBorder(BorderFactory.createTitledBorder("Отображать стрелки"));
        arrow_panel.add(e_uarrow_pred);
        arrow_panel.add(e_darrow_pred);
        arrow_panel.add(e_arrow_centered);

        e_show_d = new JCheckBox("направленная");
        e_show_d.addActionListener(this);
        e_show_d.setSelected(true);
        e_show_u = new JCheckBox("ненаправленная");
        e_show_u.addActionListener(this);
        e_show_u.setSelected(true);

        shape_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        edge_panel.add(shape_panel);
        gradient_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        edge_panel.add(gradient_panel);
        arrow_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        edge_panel.add(arrow_panel);

        //TODO: реализовать
        //e_color.setAlignmentX(Component.LEFT_ALIGNMENT);
        //edge_panel.add(e_color);
        //e_labels.setAlignmentX(Component.LEFT_ALIGNMENT);
        //edge_panel.add(e_labels);
        return edge_panel;
    }

    private Component createBothPanel() {
        final Box both_panel = Box.createVerticalBox();

        // set up zoom controls
        zoom_at_mouse = new JCheckBox("<html><center>масштабировать около мыши<p>(только колесо)</center></html>");
        zoom_at_mouse.addActionListener(this);
        zoom_at_mouse.setSelected(true);

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });

        JPanel zoomPanel = new JPanel(new BorderLayout());
        JPanel panelZoomButton = new JPanel(new GridLayout(1, 2));
        zoomPanel.setBorder(BorderFactory.createTitledBorder("Масштаб"));
        panelZoomButton.add(plus);
        panelZoomButton.add(minus);
        zoomPanel.add(panelZoomButton, BorderLayout.CENTER);
        zoom_at_mouse.setAlignmentX(Component.CENTER_ALIGNMENT);
        zoomPanel.add(zoom_at_mouse, BorderLayout.EAST);

        JPanel fontPanel = new JPanel();
        // add font and zoom controls to center panel
        font = new JCheckBox("жирный текст");
        font.addActionListener(this);
        font.setAlignmentX(Component.LEFT_ALIGNMENT);
        fontPanel.add(font);

        both_panel.add(zoomPanel);
        both_panel.add(fontPanel);

        JComboBox modeBox = gm.getModeComboBox();
        modeBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel modePanel = new JPanel(new BorderLayout()) {
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        modePanel.setBorder(BorderFactory.createTitledBorder("Режим мыши"));
        modePanel.add(modeBox);
        JPanel comboGrid = new JPanel(new GridLayout(0, 1));
        comboGrid.add(modePanel);

        both_panel.add(comboGrid);

        JComboBox cb = new JComboBox();
        cb.addItem(Renderer.VertexLabel.Position.N);
        cb.addItem(Renderer.VertexLabel.Position.NE);
        cb.addItem(Renderer.VertexLabel.Position.E);
        cb.addItem(Renderer.VertexLabel.Position.SE);
        cb.addItem(Renderer.VertexLabel.Position.S);
        cb.addItem(Renderer.VertexLabel.Position.SW);
        cb.addItem(Renderer.VertexLabel.Position.W);
        cb.addItem(Renderer.VertexLabel.Position.NW);
        cb.addItem(Renderer.VertexLabel.Position.N);
        cb.addItem(Renderer.VertexLabel.Position.CNTR);
        cb.addItem(Renderer.VertexLabel.Position.AUTO);
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Renderer.VertexLabel.Position position =
                        (Renderer.VertexLabel.Position) e.getItem();
                vv.getRenderer().getVertexLabelRenderer().setPosition(position);
                vv.repaint();
            }
        });
        cb.setSelectedItem(Renderer.VertexLabel.Position.SE);
        JPanel positionPanel = new JPanel(new BorderLayout());
        positionPanel.setBorder(BorderFactory.createTitledBorder("Расположение названия"));
        positionPanel.add(cb, BorderLayout.CENTER);

        comboGrid.add(positionPanel);
        return both_panel;
    }

    private GraphObjectGui graphObjectGuiSelected;

    public void setSelectedGraphObject(GraphObjectGui graphObject) {
        this.graphObjectGuiSelected = graphObject;
    }

    public void actionPerformed(ActionEvent e) {
        AbstractButton source = (AbstractButton) e.getSource();
        if (source == v_color) {
            seedDrawColor.setSeedColoring(source.isSelected());
            seedFillColor.setSeedColoring(source.isSelected());
        } else if (source == e_color) {
            ewcs.setWeighted(source.isSelected());
        } else if (source == v_stroke) {
            vsh.setHighlight(source.isSelected());
        } else if (source == v_labels) {
            if (source.isSelected())
                vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
            else
                vv.getRenderContext().setVertexLabelTransformer(vs_none);
        } else if (source == e_labels) {
            if (source.isSelected()) {
//                vv.getRenderContext().setEdgeLabelTransformer(es);
            } else
                vv.getRenderContext().setEdgeLabelTransformer(es_none);
        } else if (source == e_uarrow_pred) {
            show_arrow.showUndirected(source.isSelected());
        } else if (source == e_darrow_pred) {
            show_arrow.showDirected(source.isSelected());
        } else if (source == e_arrow_centered) {
            if (source.isSelected()) {
                vv.getRenderer().getEdgeRenderer().setEdgeArrowRenderingSupport(new CenterEdgeArrowRenderingSupport());
            } else {
                vv.getRenderer().getEdgeRenderer().setEdgeArrowRenderingSupport(new BasicEdgeArrowRenderingSupport());
            }
        } else if (source == font) {
            vff.setBold(source.isSelected());
            eff.setBold(source.isSelected());
//        } else if (source == v_shape) {
//            vssa.useFunnyShapes(source.isSelected());
//        } else if (source == v_size) {
//            vssa.setScaling(source.isSelected());
//        } else if (source == v_aspect) {
//            vssa.setStretching(source.isSelected());
        } else if (source == e_line) {
            if (source.isSelected()) {
                vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<String, String>());
            }
        } else if (source == e_ortho) {
            if (source.isSelected())
                vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Orthogonal<String, String>());
        } else if (source == e_wedge) {
            if (source.isSelected())
                vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Wedge<String, String>(10));
        } else if (source == e_quad) {
            if (source.isSelected()) {
                vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<String, String>());
            }
        } else if (source == e_cubic) {
            if (source.isSelected()) {
                vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<String, String>());
            }
        } else if (source == e_show_d) {
            show_edge.showDirected(source.isSelected());
        } else if (source == e_show_u) {
            show_edge.showUndirected(source.isSelected());
        } else if (source == v_small) {
            show_vertex.filterSmall(source.isSelected());
        } else if (source == zoom_at_mouse) {
            gm.setZoomAtMouse(source.isSelected());
        } else if (source == no_gradient) {
            if (source.isSelected()) {
                gradient_level = GRADIENT_NONE;
            }
        } else if (source == gradient_relative) {
            if (source.isSelected()) {
                gradient_level = GRADIENT_RELATIVE;
            }
        } else if (source == fill_edges) {
            if (source.isSelected()) {
                vv.getRenderContext().setEdgeFillPaintTransformer(edgeFillPaint);
            } else {
                vv.getRenderContext().setEdgeFillPaintTransformer(new ConstantTransformer(null));
            }
        }
        vv.repaint();
    }

    private final static class EdgeWeightStrokeFunction<E>
            implements Transformer<E, Stroke> {
        protected static final Stroke basic = new BasicStroke(1);
        protected static final Stroke heavy = new BasicStroke(2);
        protected static final Stroke dotted = RenderContext.DOTTED;

        protected boolean weighted = false;
        protected Map<E, Number> edge_weight;

        public EdgeWeightStrokeFunction(Map<E, Number> edge_weight) {
            this.edge_weight = edge_weight;
        }

        public void setWeighted(boolean weighted) {
            this.weighted = weighted;
        }

        public Stroke transform(E e) {
            if (weighted) {
                if (drawHeavy(e))
                    return heavy;
                else
                    return dotted;
            } else
                return basic;
        }

        protected boolean drawHeavy(E e) {
            double value = edge_weight.get(e).doubleValue();
            if (value > 0.7)
                return true;
            else
                return false;
        }

    }

    private final static class VertexStrokeHighlight<V, E> implements
            Transformer<V, Stroke> {
        protected boolean highlight = false;
        protected Stroke heavy = new BasicStroke(5);
        protected Stroke medium = new BasicStroke(3);
        protected Stroke light = new BasicStroke(1);
        protected PickedInfo<V> pi;
        protected Graph<V, E> graph;

        public VertexStrokeHighlight(Graph<V, E> graph, PickedInfo<V> pi) {
            this.graph = graph;
            this.pi = pi;
        }

        public void setHighlight(boolean highlight) {
            this.highlight = highlight;
        }

        public Stroke transform(V v) {
            if (highlight) {
                if (pi.isPicked(v))
                    return heavy;
                else {
                    for (V w : graph.getNeighbors(v)) {
                        if (pi.isPicked(w))
                            return medium;
                    }
                    return light;
                }
            } else
                return light;
        }

    }

    private final static class VertexFontTransformer<V>
            implements Transformer<V, Font> {
        protected boolean bold = false;
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font b = new Font("Helvetica", Font.BOLD, 12);

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        public Font transform(V v) {
            if (bold)
                return b;
            else
                return f;
        }
    }

    private final static class EdgeFontTransformer<E>
            implements Transformer<E, Font> {
        protected boolean bold = false;
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font b = new Font("Helvetica", Font.BOLD, 12);

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        public Font transform(E e) {
            if (bold)
                return b;
            else
                return f;
        }
    }

    private final static class DirectionDisplayPredicate<V, E>
            implements Predicate<Context<Graph<V, E>, E>>
            //extends AbstractGraphPredicate<V,E>
    {
        protected boolean show_d;
        protected boolean show_u;

        public DirectionDisplayPredicate(boolean show_d, boolean show_u) {
            this.show_d = show_d;
            this.show_u = show_u;
        }

        public void showDirected(boolean b) {
            show_d = b;
        }

        public void showUndirected(boolean b) {
            show_u = b;
        }

        public boolean evaluate(Context<Graph<V, E>, E> context) {
            Graph<V, E> graph = context.graph;
            E e = context.element;
            if (graph.getEdgeType(e) == EdgeType.DIRECTED && show_d) {
                return true;
            }
            if (graph.getEdgeType(e) == EdgeType.UNDIRECTED && show_u) {
                return true;
            }
            return false;
        }
    }

    private final static class VertexDisplayPredicate<V, E>
            implements Predicate<Context<Graph<V, E>, V>>
//    	extends  AbstractGraphPredicate<V,E>
    {
        protected final static int MIN_DEGREE = 4;
        protected boolean filter_small;

        public VertexDisplayPredicate(boolean filter) {
            this.filter_small = filter;
        }

        public void filterSmall(boolean b) {
            filter_small = b;
        }

        public boolean evaluate(Context<Graph<V, E>, V> context) {
            Graph<V, E> graph = context.graph;
            V v = context.element;
//            Vertex v = (Vertex)arg0;
            if (filter_small)
                return (graph.degree(v) < MIN_DEGREE);
            else
                return true;
        }
    }

    /**
     * Controls the shape, size, and aspect ratio for each vertex.
     *
     * @author Joshua O'Madadhain
     */
    private final static class VertexShapeSizeAspect<V, E>
            extends AbstractVertexShapeTransformer<V>
            implements Transformer<V, Shape> {

        protected boolean stretch = false;
        protected boolean scale = false;
        protected boolean funny_shapes = false;
        protected Transformer<V, Double> voltages;
        protected Graph<V, E> graph;

        public VertexShapeSizeAspect(Graph<V, E> graphIn, Transformer<V, Double> voltagesIn) {
            this.graph = graphIn;
            this.voltages = voltagesIn;
            setSizeTransformer(new Transformer<V, Integer>() {

                public Integer transform(V v) {
                    if (scale)
                        return (int) (voltages.transform(v) * 30) + 20;
                    else
                        return 20;

                }
            });
            setAspectRatioTransformer(new Transformer<V, Float>() {

                public Float transform(V v) {
                    if (stretch) {
                        return (float) (graph.inDegree(v) + 1) /
                                (graph.outDegree(v) + 1);
                    } else {
                        return 1.0f;
                    }
                }
            });
        }

        public void setStretching(boolean stretch) {
            this.stretch = stretch;
        }

        public void setScaling(boolean scale) {
            this.scale = scale;
        }

        public void useFunnyShapes(boolean use) {
            this.funny_shapes = use;
        }

        public Shape transform(V v) {
            if (funny_shapes) {
                if (graph.degree(v) < 5) {
                    int sides = Math.max(graph.degree(v), 3);
                    return factory.getRegularPolygon(v, sides);
                } else
                    return factory.getRegularStar(v, graph.degree(v));
            } else
                return factory.getEllipse(v);
        }
    }

    private final class SeedDrawColor<V> implements Transformer<V, Paint> {
        protected final static float dark_value = 0.8f;
        protected final static float light_value = 0.2f;
        protected PickedInfo<V> pi;
        protected boolean seed_coloring;

        public SeedDrawColor(PickedInfo<V> pi) {
            this.pi = pi;
            seed_coloring = false;
        }

        public void setSeedColoring(boolean b) {
            this.seed_coloring = b;
        }

        public Paint transform(V v) {
            return Color.BLACK;
        }
    }

    private final class SeedFillColor<V> implements Transformer<V, Paint> {
        protected final static float dark_value = 0.8f;
        protected final static float light_value = 0.2f;
        protected PickedInfo<V> pi;
        protected boolean seed_coloring;

        public SeedFillColor(PickedInfo<V> pi) {
            this.pi = pi;
            seed_coloring = false;
        }

        public void setSeedColoring(boolean b) {
            this.seed_coloring = b;
        }

        public Paint transform(V v) {
            if (v_color.isSelected()) {
                float alpha = transparency.get(v).floatValue();
                if (pi.isPicked(v)) {
                    return new Color(1f, 1f, 0, alpha);
                } else {
                    if (seed_coloring && seedVertices.contains(v)) {
                        Color dark = new Color(0, 0, dark_value, alpha);
                        Color light = new Color(0, 0, light_value, alpha);
                        return new GradientPaint(0, 0, dark, 10, 0, light, true);
                    } else
                        return new Color(1f, 0, 0, alpha);
                }
            } else {
                if (graphObjectGuiSelected != null && v.equals(graphObjectGuiSelected.id())) {
                    return Color.BLUE;
                } else {
                    return Color.RED;
                }

            }
        }
    }

    /**
     * GraphMousePlugin, который предлагает всплывающее окно
     * поддержка меню
     */
    protected class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin
            implements MouseListener {

        public PopupGraphMousePlugin() {
            this(MouseEvent.BUTTON3_MASK);
        }

        public PopupGraphMousePlugin(int modifiers) {
            super(modifiers);
        }

        /**
         * Если это событие находится над вершиной, вы можете открыть меню
         * позволяет пользователю увеличивать/уменьшать напряжение
         * атрибут этой вершины
         *
         * @param e
         */
        @SuppressWarnings("unchecked")
        protected void handlePopup(MouseEvent e) {
            final VisualizationViewer<String, Number> vv =
                    (VisualizationViewer<String, Number>) e.getSource();
            Point2D p = e.getPoint();

            GraphElementAccessor<String, Number> pickSupport = vv.getPickSupport();
            if (pickSupport != null) {
                final String v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
                if (v != null) {
                    //TODO: реализовать
//                    JPopupMenu popup = new JPopupMenu();
//                    popup.add(new AbstractAction("Уменьшить прозрачность") {
//                        {
//                            putValue(AbstractAction.SMALL_ICON, new ImageIcon(this.getClass().getResource("/brightness_darken_16x16.png")));
//                        }
//
//                        public void actionPerformed(ActionEvent e) {
//                            Double value = Math.max(0, transparency.get(v).doubleValue() - 0.1);
//                            transparency.put(v, value);
//                            vv.repaint();
//                        }
//                    });
//                    popup.add(new AbstractAction("Повысить прозрачность") {
//                        {
//                            putValue(AbstractAction.SMALL_ICON, new ImageIcon(this.getClass().getResource("/brightness_16x16.png")));
//                        }
//
//                        public void actionPerformed(ActionEvent e) {
//                            Double value = Math.min(1, transparency.get(v).doubleValue() + 0.1);
//                            transparency.put(v, value);
//                            vv.repaint();
//                        }
//                    });
//                    popup.show(vv, e.getX(), e.getY());
//                } else {
//                    final Number edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
//                    if (edge != null) {
//                        JPopupMenu popup = new JPopupMenu();
//                        popup.add(new AbstractAction(edge.toString()) {
//                            public void actionPerformed(ActionEvent e) {
//                                System.err.println("got " + edge);
//                            }
//                        });
//                        popup.show(vv, e.getX(), e.getY());
//
//                    }
                }
            }
        }
    }

//    public class VoltageTips<E>
//            implements Transformer<String, String> {
//
//        public String transform(String vertex) {
//            return "Уровень устойчивости: " + voltages.transform(vertex);
//        }
//    }

    public class GradientPickedEdgePaintFunction<V, E> extends GradientEdgePaintTransformer<V, E> {
        protected boolean fill_edge = false;
        Predicate<Context<Graph<V, E>, E>> selfLoop = new SelfLoopEdgePredicate<V, E>();
        private Transformer<E, Paint> defaultFunc;

        public GradientPickedEdgePaintFunction(Transformer<E, Paint> defaultEdgePaintFunction,
                                               VisualizationViewer<V, E> vv) {
            super(Color.WHITE, Color.BLACK, vv);
            this.defaultFunc = defaultEdgePaintFunction;
        }

        public void useFill(boolean b) {
            fill_edge = b;
        }

        public Paint transform(E e) {
            if (gradient_level == GRADIENT_NONE) {
                return defaultFunc.transform(e);
            } else {
                return super.transform(e);
            }
        }

        protected Color getColor2(E e) {
            return vv.getPickedEdgeState().isPicked(e) ? Color.CYAN : c2;
        }

    }
}


