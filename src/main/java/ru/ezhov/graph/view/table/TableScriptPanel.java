package ru.ezhov.graph.view.table;

import ru.ezhov.graph.script.Script;
import ru.ezhov.graph.script.Scripts;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

public class TableScriptPanel extends JPanel {

    private JTable table;
    private Scripts scripts;
    private SearchPanel searchPanel;

    private List<TableScriptEvent> scriptEventList;

    private TableScriptModel tableScriptModel;

    public TableScriptPanel(final Scripts scripts, TableScriptEvent... tableScriptEvents) {
        this.scripts = scripts;

        scriptEventList = Arrays.asList(tableScriptEvents);

        setLayout(new BorderLayout());
        searchPanel = new SearchPanel();
        table = new JTable();
        tableScriptModel = new TableScriptModel(scripts);
        table.setModel(tableScriptModel);
        table.setRowSorter(new TableRowSorter<TableModel>(tableScriptModel) {
            private final TableStringConverter tableStringConverterAllTask =
                    new TableStringConverter() {
                        @Override
                        public String toString(TableModel model, int row, int column) {
                            String value = model.getValueAt(row, column).toString();

                            switch (column) {
                                case 0:
                                    return value;
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    return numberForEquals(value);
                                default:
                                    return "";
                            }
                        }

                        private String numberForEquals(String val) {
                            int len = val.length();
                            int adds = 100 - len;
                            String res = new String(new char[adds]).replace("\0", "0") + val;
                            return res;
                        }
                    };

            @Override
            public TableStringConverter getStringConverter() {
                return tableStringConverterAllTask;
            }
        });
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int row = table.convertRowIndexToModel(selectedRow);
                    Script script = scripts.all().get(row);
                    fireEventListener(EventType.MOUSE_RELEASED, e, script);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int row = table.convertRowIndexToModel(selectedRow);
                    Script script = scripts.all().get(row);
                    fireEventListener(EventType.MOUSE_CLICKED, e, script);
                }
            }

        });
    }

    private void fireEventListener(EventType eventType, Object e, Script script) {
        for (TableScriptEvent tableScriptEvent : scriptEventList) {
            tableScriptEvent.event(eventType, e, script);
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
                    String id = (String) entry.getValue(0);
                    return id.toLowerCase().trim().contains(text.toLowerCase().trim());
                }
            }
        };
    }

    private class SearchPanel extends JPanel {
        private JTextField textFieldSearch = new JTextField();
        private JButton buttonClearSearch;

        public SearchPanel() {
            setLayout(new BorderLayout());
            textFieldSearch = new JTextField();
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

    private class TableScriptModel extends AbstractTableModel {

        private Scripts scripts;
        private String[] columns = {"ID", "Используется в", "Использует", "Кол-во строк", "Устойчивость к изменениям"};

        public TableScriptModel(Scripts scripts) {
            this.scripts = scripts;
        }

        @Override
        public int getRowCount() {
            return scripts.all().size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Script script = scripts.all().get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return script.id();
                case 1:
                    return scripts.parents(script.id()).size();
                case 2:
                    return scripts.children(script.id()).size();
                case 3:
                    return script.rows();
                case 4:
                    double childrenCount = scripts.children(script.id()).size();
                    double parentCount = scripts.parents(script.id()).size();
                    return (childrenCount + parentCount) != 0 ? childrenCount / (childrenCount + parentCount) : 0;
                default:
                    throw new UnsupportedOperationException("Неподдерживаемое кол-во столбцов");

            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
