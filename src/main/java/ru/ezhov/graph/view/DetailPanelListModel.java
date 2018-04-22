package ru.ezhov.graph.view;

import javax.swing.*;
import java.util.Set;

public class DetailPanelListModel extends DefaultListModel {

    public DetailPanelListModel(Set<ScriptView> scriptViews) {
        for (ScriptView view : scriptViews) {
            addElement(view);
        }
    }
}