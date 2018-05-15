package ru.ezhov.source.analyse.plugin.java;

import ru.ezhov.analyse.AnalyseException;
import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.NullAnalyzedObjects;
import ru.ezhov.analyse.java.JavaAnalyzedObjectsFactory;
import ru.ezhov.source.analyse.plugin.AbstractSourceAnalysePanel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaFileSourceAnalysePanel extends AbstractSourceAnalysePanel {
    private JTextField textFieldPath;
    private JTextField textFieldName;
    private JButton buttonSelectDirectory;
    private JButton buttonAdd;

    private JTable table;
    private SelectTableModel selectTableModel = new SelectTableModel();

    public JavaFileSourceAnalysePanel() {
        setLayout(new BorderLayout());
        SelectPanel selectPanel = new SelectPanel();
        TablePanel tablePanel = new TablePanel();
        add(selectPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    @Override
    public AnalyzedObjects analyse() throws AnalyseException {
        try {
            int rc = selectTableModel.getRowCount();
            if (rc == 0) {
                JOptionPane.showMessageDialog(
                        table,
                        "Добавьте выбранную папку в список анализируемых",
                        "Недостаточно информации",
                        JOptionPane.INFORMATION_MESSAGE);
                return NullAnalyzedObjects.NULL_ANALYZED_OBJECTS;
            } else {
                Map<String, String> map = new HashMap();
                for (TableData data : selectTableModel.tableDataList()) {
                    map.put(data.path, data.name);
                }
                return JavaAnalyzedObjectsFactory.fromFile(map);
            }
        } catch (Exception e) {
            throw new AnalyseException(e);
        }
    }

    private class SelectPanel extends JPanel {
        JFileChooser fileChooser = new JFileChooser(new File(""));

        SelectPanel() {
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            setLayout(new BorderLayout());
            textFieldPath = new JTextFieldWithText("Корневую папку для поиска папки с исходниками");
            textFieldPath.setText(new File("").getAbsolutePath());
            textFieldName = new JTextFieldWithText("Название папки с исходными файлами для поиска");
            textFieldName.setText("src");
            textFieldName.setColumns(10);
            buttonSelectDirectory = new JButton("Выбрать папку");
            buttonSelectDirectory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(table)) {
                        textFieldPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
            });
            buttonAdd = new JButton("Добавить в список поиска");
            buttonAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String path = textFieldPath.getText();
                    String name = textFieldName.getText();

                    TableData tableData = new TableData(path, name);
                    if (!selectTableModel.contains(tableData)) {
                        selectTableModel.add(path, name);
                    }
                }
            });

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(textFieldPath, BorderLayout.CENTER);
            panel.add(buttonSelectDirectory, BorderLayout.EAST);

            JPanel panel1 = new JPanel(new BorderLayout());
            panel1.add(panel, BorderLayout.CENTER);
            panel1.add(textFieldName, BorderLayout.EAST);

            JPanel panel2 = new JPanel(new BorderLayout());
            panel2.add(panel1, BorderLayout.CENTER);
            panel2.add(buttonAdd, BorderLayout.EAST);

            add(panel2, BorderLayout.CENTER);
        }
    }

    private class TablePanel extends JPanel {
        TablePanel() {
            setLayout(new BorderLayout());
            table = new JTable(selectTableModel);
            table.setAutoCreateColumnsFromModel(false);
            table.setTableHeader(null);
            table.getColumn(table.getColumnName(1)).setMaxWidth(50);
            table.getColumn(table.getColumnName(2)).setMaxWidth(16);
            table.getColumn(table.getColumnName(2)).setCellRenderer(new ButtonRenderer());

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    if (col == 2) {
                        selectTableModel.remove(row);
                    }
                }
            });
            add(new JScrollPane(table), BorderLayout.CENTER);
        }
    }

    private class SelectTableModel extends AbstractTableModel {
        private List<TableData> tableDataList = new ArrayList<>();


        @Override
        public int getRowCount() {
            return tableDataList.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return tableDataList.get(rowIndex).path;
                case 1:
                    return tableDataList.get(rowIndex).name;
                case 2:
                    return 0;
                default:
                    throw new IllegalArgumentException("Чет не то с количеством столбцов");
            }
        }

        public void add(String path, String name) {
            tableDataList.add(new TableData(path, name));
            fireTableRowsInserted(0, tableDataList.size());
        }

        public void remove(int index) {
            tableDataList.remove(index);
            fireTableRowsInserted(0, tableDataList.size());
        }

        public boolean contains(TableData tableData) {
            return tableDataList.contains(tableData);
        }

        public List<TableData> tableDataList() {
            return tableDataList;
        }
    }

    private class TableData {
        private String path;
        private String name;

        TableData(String path, String name) {
            this.path = path;
            this.name = name;
        }

        @Override
        public String toString() {
            return "TableData{" +
                    "path='" + path + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TableData tableData = (TableData) o;

            if (path != null ? !path.equals(tableData.path) : tableData.path != null) return false;
            return name != null ? name.equals(tableData.name) : tableData.name == null;
        }

        @Override
        public int hashCode() {
            int result = path != null ? path.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        ButtonRenderer() {
            setIcon(new ImageIcon(JavaFileSourceAnalysePanel.class.getResource("/delete_10x10.png")));
            setOpaque(true);
        }

        ButtonRenderer(String text) {
            super();
            setText(text);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            return this;
        }
    }

}
