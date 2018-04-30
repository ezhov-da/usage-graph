package ru.ezhov.graph.gui.graphtablepanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.domain.GraphObjectsGui;

import javax.swing.table.AbstractTableModel;

class GraphTableModel extends AbstractTableModel {

    private GraphObjectsGui scripts;
    private String[] columns = {"ID", "Используется в", "Использует", "Кол-во строк", "Стабильность"};

    public GraphTableModel(GraphObjectsGui scripts) {
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
        GraphObjectGui script = scripts.all().get(rowIndex);
        return script;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
