package ru.ezhov.graph.gui.graphtablepanel;

import ru.ezhov.graph.gui.components.JTextFieldWithText;
import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.domain.GraphObjectsGui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

public class GraphTablePanel extends JPanel {

    private JTable table;
    private GraphObjectsGui graphObjectsGui;
    private SearchPanel searchPanel;
    private InfoPanel infoPanel;

    private List<GraphTableListener> scriptEventList;

    private GraphTableModel graphTableModel;

    public GraphTablePanel(final GraphObjectsGui graphObjectsGui, GraphTableListener... graphTableListeners) {
        this.graphObjectsGui = graphObjectsGui;

        scriptEventList = Arrays.asList(graphTableListeners);

        setLayout(new BorderLayout());
        searchPanel = new SearchPanel();
        table = new JTable();
        graphTableModel = new GraphTableModel(graphObjectsGui);
        table.setModel(graphTableModel);
        table.setDefaultRenderer(Object.class, new GraphTableRender());
        table.setRowSorter(new GraphTableRowSorter(graphTableModel));

        infoPanel = new InfoPanel();
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int row = table.convertRowIndexToModel(selectedRow);
                    GraphObjectGui script = graphObjectsGui.all().get(row);
                    fireEventListener(EventType.MOUSE_RELEASED, e, script);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int row = table.convertRowIndexToModel(selectedRow);
                    GraphObjectGui script = graphObjectsGui.all().get(row);
                    fireEventListener(EventType.MOUSE_CLICKED, e, script);
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int row = table.convertRowIndexToModel(selectedRow);
                    GraphObjectGui script = graphObjectsGui.all().get(row);
                    fireEventListener(EventType.LIST_SELECTION_EVENT, e, script);
                }
            }
        });
    }

    private void fireEventListener(EventType eventType, Object e, GraphObjectGui script) {
        for (GraphTableListener graphTableListener : scriptEventList) {
            graphTableListener.event(eventType, e, script);
        }
    }

    private RowFilter rowFilter() {
        return new RowFilter() {
            @Override
            public boolean include(Entry entry) {
                String text = searchPanel.getText();
                if ("".equals(text)) {
                    return true;
                } else {
                    GraphObjectGui graphObjectGui = (GraphObjectGui) entry.getValue(0);
                    return graphObjectGui.id().toLowerCase().trim().contains(text.toLowerCase().trim());
                }
            }
        };
    }

    private class SearchPanel extends JPanel {
        private JTextField textFieldSearch;
        private JButton buttonClearSearch;

        public SearchPanel() {
            setLayout(new BorderLayout());
            textFieldSearch = new JTextFieldWithText("Введите не менее 3х символов для поиска по ID...");
            buttonClearSearch = new JButton(new ImageIcon(this.getClass().getResource("/clear_16x16.png")));
            buttonClearSearch.setToolTipText("Очистить");
            Dimension dimension = new Dimension(20, buttonClearSearch.getHeight());
            buttonClearSearch.setSize(dimension);
            buttonClearSearch.setMaximumSize(dimension);
            buttonClearSearch.setMinimumSize(dimension);
            buttonClearSearch.setPreferredSize(dimension);

            add(textFieldSearch, BorderLayout.CENTER);
            add(buttonClearSearch, BorderLayout.EAST);

            textFieldSearch.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String text = textFieldSearch.getText();
                    if (text.length() >= 3 || "".equals(text.trim())) {
                        ((TableRowSorter) table.getRowSorter()).setRowFilter(rowFilter());
                    }
                }
            });

            buttonClearSearch.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textFieldSearch.setText("");
                    ((TableRowSorter) table.getRowSorter()).setRowFilter(rowFilter());
                }
            });
        }

        public String getText() {
            return textFieldSearch.getText();
        }
    }

    private class InfoPanel extends JPanel {
        private JLabel labelCommon;
        private JLabel labelEmpty;

        public InfoPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            labelCommon = new JLabel(String.format("Всего объектов: %s", graphObjectsGui.all().size()));
            int count = 0;
            for (GraphObjectGui objectGui : graphObjectsGui.all()) {
                if (objectGui.parents().isEmpty() && objectGui.children().isEmpty()) {
                    count++;
                }
            }
            labelEmpty = new JLabel(String.format("Несвязанных объектов: %s", count));
            add(labelCommon);
            add(labelEmpty);
        }
    }
}
