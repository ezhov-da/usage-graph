package ru.ezhov.graph.view;

import javax.swing.*;
import java.awt.*;

public class ScriptListRender extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            ScriptViewDetail scriptViewDetail = (ScriptViewDetail) value;
            String textEmpty = "<html><font color=\"red\">ID: %s</font>";
            String textOnlyParent = "<html>ID: %s<br>Используется в: %s скрипте(-ах)";
            String textOnlyChildren = "<html>ID: %s<br>Использует: %s скрипта(-ов)";
            String textAll = "<html>ID: %s<br>Используется в: %s скрипте(-ах)<br>Использует: %s скриптов";
            String finalText;
            if (!scriptViewDetail.parents().isEmpty() & !scriptViewDetail.children().isEmpty()) {
                finalText = String.format(textAll, scriptViewDetail.id(), scriptViewDetail.parents().size(), scriptViewDetail.children().size());
            } else if (scriptViewDetail.parents().isEmpty() & !scriptViewDetail.children().isEmpty()) {
                finalText = String.format(textOnlyChildren, scriptViewDetail.id(), scriptViewDetail.children().size());
            } else if (!scriptViewDetail.parents().isEmpty() & scriptViewDetail.children().isEmpty()) {
                finalText = String.format(textOnlyParent, scriptViewDetail.id(), scriptViewDetail.parents().size());
            } else {
                finalText = String.format(textEmpty, scriptViewDetail.id());
            }

            label.setText(finalText);
        }

        return label;
    }
}