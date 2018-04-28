package ru.ezhov.graph.gui.detailinfopanel;

import ru.ezhov.graph.gui.domain.ScriptView;

import javax.swing.*;
import java.util.Set;

public class DetailPanelListModel extends DefaultListModel {

    public DetailPanelListModel(Set<ScriptView> scriptViews) {
        for (ScriptView view : scriptViews) {
            addElement(view);
        }
    }
}