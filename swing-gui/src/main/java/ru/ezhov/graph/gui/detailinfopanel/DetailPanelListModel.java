package ru.ezhov.graph.gui.detailinfopanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;

import javax.swing.*;
import java.util.*;

public class DetailPanelListModel extends DefaultListModel {

    public DetailPanelListModel(Set<GraphObjectGui> graphObjectGuis) {
        List<GraphObjectGui> views = new ArrayList<>();
        views.addAll(graphObjectGuis);
        Collections.sort(views, new Comparator<GraphObjectGui>() {
            @Override
            public int compare(GraphObjectGui o1, GraphObjectGui o2) {
                return o1.id().compareTo(o2.id());
            }
        });
        for (GraphObjectGui view : views) {
            addElement(view);
        }
    }
}