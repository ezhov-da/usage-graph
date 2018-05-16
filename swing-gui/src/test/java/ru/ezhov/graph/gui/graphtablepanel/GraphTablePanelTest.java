package ru.ezhov.graph.gui.graphtablepanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.domain.GraphObjectsGui;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphTablePanelTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Throwable ex) {
                    //
                }


                JFrame frame = new JFrame("_________");
                GraphTablePanel graphTablePanel = new GraphTablePanel(frame, new GraphObjectsGui() {
                    @Override
                    public List<GraphObjectGui> all() {
                        final GraphObjectGui graphObjectGui1 = new GraphObjectGui() {
                            @Override
                            public String id() {
                                return "parent";
                            }

                            @Override
                            public String text() throws Exception {
                                return "привет";
                            }

                            @Override
                            public double stability() {
                                return 0;
                            }

                            @Override
                            public int rows() {
                                return 12;
                            }

                            @Override
                            public Set<GraphObjectGui> parents() {
                                return new HashSet<>();
                            }

                            @Override
                            public Set<GraphObjectGui> children() {
                                return new HashSet<>();
                            }
                        };

                        GraphObjectGui graphObjectGui2 = new GraphObjectGui() {
                            @Override
                            public String id() {
                                return "child";
                            }

                            @Override
                            public String text() throws Exception {
                                return "привет";
                            }

                            @Override
                            public double stability() {
                                return 0;
                            }

                            @Override
                            public int rows() {
                                return 12;
                            }

                            @Override
                            public Set<GraphObjectGui> parents() {
                                return new HashSet<>(Arrays.asList(graphObjectGui1));
                            }

                            @Override
                            public Set<GraphObjectGui> children() {
                                return new HashSet<>();
                            }
                        };

                        return Arrays.asList(graphObjectGui1, graphObjectGui2);
                    }
                }, new GraphTableListener() {
                    @Override
                    public void event(EventType eventType, Object event, GraphObjectGui script) {
                        System.out.println(eventType);
                    }
                });

                frame.add(graphTablePanel);
                frame.setSize(1000, 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}