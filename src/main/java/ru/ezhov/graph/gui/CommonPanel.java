package ru.ezhov.graph.gui;

import ru.ezhov.graph.script.Script;
import ru.ezhov.graph.script.Scripts;
import ru.ezhov.graph.util.PercentScreenDimension;
import ru.ezhov.graph.gui.tablepanel.EventType;
import ru.ezhov.graph.gui.tablepanel.TableScriptEvent;
import ru.ezhov.graph.gui.tablepanel.TableScriptPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by ezhov_da on 20.04.2018.
 */
public class CommonPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(CommonPanel.class.getName());

    private Scripts scripts;
    private TableScriptPanel tableScriptPanel;
    private JTabbedPane tabbedPane = new JTabbedPane();

    public CommonPanel(Scripts scripts) {
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
                        CommonPanel.this.repaint();
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


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.4);
        splitPane.setResizeWeight(0.4);

        splitPane.setLeftComponent(tableScriptPanel);

        tabbedPane.addTab("ГРАФ", new GraphPanel(scripts, selected));
        tabbedPane.setTabComponentAt(0, new TabHeader("ГРАФ", tabbedPane));

        splitPane.setRightComponent(tabbedPane);

        add(splitPane, BorderLayout.CENTER);

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
