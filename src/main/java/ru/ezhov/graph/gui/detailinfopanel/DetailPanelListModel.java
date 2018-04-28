package ru.ezhov.graph.gui.detailinfopanel;

import ru.ezhov.graph.gui.domain.ScriptGui;

import javax.swing.*;
import java.util.*;

public class DetailPanelListModel extends DefaultListModel {

    public DetailPanelListModel(Set<ScriptGui> scriptGuis) {
        List<ScriptGui> views = new ArrayList<>();
        views.addAll(scriptGuis);
        Collections.sort(views, new Comparator<ScriptGui>() {
            @Override
            public int compare(ScriptGui o1, ScriptGui o2) {
                return o1.id().compareTo(o2.id());
            }
        });
        for (ScriptGui view : views) {
            addElement(view);
        }
    }
}