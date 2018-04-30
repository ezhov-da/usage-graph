package ru.ezhov.graph.gui.graphtablepanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class GraphTableRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int rowConvert = table.convertRowIndexToModel(row);
        int columnConvert = table.convertColumnIndexToModel(column);
        GraphObjectGui graphObjectGui = (GraphObjectGui) table.getModel().getValueAt(rowConvert, columnConvert);
        if (graphObjectGui != null) {
            render(graphObjectGui, label, table, value, isSelected, hasFocus, rowConvert, columnConvert);
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
                double stability = graphObjectGui.stability();
                if (stability >= 0 && stability <= 0.1) {
                    label.setText(stabilityHtml(stability, "#228B22"));
                } else if (stability > 0.1 && stability <= 0.2) {
                    label.setText(stabilityHtml(stability, "#6B8E23"));
                } else if (stability > 0.2 && stability <= 0.3) {
                    label.setText(stabilityHtml(stability, "#BDB76B"));
                } else if (stability > 0.3 && stability <= 0.4) {
                    label.setText(stabilityHtml(stability, "#EEE8AA"));
                } else if (stability > 0.4 && stability <= 0.5) {
                    label.setText(stabilityHtml(stability, "#FFD700"));
                } else if (stability > 0.5 && stability <= 0.6) {
                    label.setText(stabilityHtml(stability, "#DAA520"));
                } else if (stability > 0.6 && stability <= 0.7) {
                    label.setText(stabilityHtml(stability, "#B8860B"));
                } else if (stability > 0.7 && stability <= 0.8) {
                    label.setText(stabilityHtml(stability, "#BC8F8F"));
                } else if (stability > 0.8 && stability <= 0.9) {
                    label.setText(stabilityHtml(stability, "#CD5C5C"));
                } else if (stability > 0.9 && stability <= 1) {
                    label.setText(stabilityHtml(stability, "#B22222"));
                } else {
                    label.setText(value + "");
                }
                break;
            default:
                throw new UnsupportedOperationException("Неподдерживаемое кол-во столбцов");

        }
    }

    private String stabilityHtml(double value, String htmlColor) {
        return String.format("<html><font color=\"%s\">%s</font>", htmlColor, value);
    }
}
