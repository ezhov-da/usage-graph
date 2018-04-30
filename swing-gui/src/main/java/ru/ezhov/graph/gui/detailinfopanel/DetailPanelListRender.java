package ru.ezhov.graph.gui.detailinfopanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;

import javax.swing.*;
import java.awt.*;

class DetailPanelListRender extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            GraphObjectGui graphObjectGui = (GraphObjectGui) value;
            label.setText(graphObjectGui.id());
        }
        return label;
    }
}