package ru.ezhov.graph.gui.graphtablepanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class GraphTableRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        GraphObjectGui graphObjectGui = (GraphObjectGui) table.getModel().getValueAt(row, column);
        if (graphObjectGui != null) {
            render(graphObjectGui, label, table, value, isSelected, hasFocus, row, column);
        }
        return label;
    }

    private void render(GraphObjectGui graphObjectGui, JLabel label, JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color color;
        if (isSelected) {
            color = Color.BLACK;
        } else {

            if (graphObjectGui.parents().size() == 0 &&
                    graphObjectGui.children().size() == 0) {
                color = Color.GRAY;
            } else {
                color = Color.BLACK;
            }
        }
        label.setForeground(color);

        switch (column) {
            case 0:
                label.setText(graphObjectGui.id());
                break;
            case 1:
                label.setText(graphObjectGui.parents().size() + "");
                break;
            case 2:
                label.setText(graphObjectGui.children().size() + "");
                break;
            case 3:
                label.setText(graphObjectGui.rows() + "");
                break;
            case 4:
                label.setText(graphObjectGui.stability() + "");
                break;
            default:
                throw new UnsupportedOperationException("Неподдерживаемое кол-во столбцов");

        }
    }
}
