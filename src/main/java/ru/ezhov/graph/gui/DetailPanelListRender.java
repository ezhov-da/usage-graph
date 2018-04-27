package ru.ezhov.graph.gui;

import javax.swing.*;
import java.awt.*;

public class DetailPanelListRender extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            ScriptView scriptView = (ScriptView) value;
            label.setText(scriptView.id());
        }
        return label;
    }
}