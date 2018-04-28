package ru.ezhov.graph.gui.detailinfopanel;

import ru.ezhov.graph.gui.domain.ScriptGui;

import javax.swing.*;
import java.awt.*;

public class DetailPanelListRender extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            ScriptGui scriptGui = (ScriptGui) value;
            label.setText(scriptGui.id());
        }
        return label;
    }
}