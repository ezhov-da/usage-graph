package ru.ezhov.graph.view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ScriptListModel extends DefaultListModel {

    private List<ScriptViewDetail> original;

    public ScriptListModel(List<ScriptViewDetail> original) {
        this.original = original;
        add(original);
    }

    public void find(String partId) {
        if (!"".equals(partId)) {
            List<ScriptViewDetail> list = new ArrayList<>();
            for (ScriptViewDetail scriptViewDetail : original) {
                if (scriptViewDetail.id().contains(partId)) {
                    list.add(scriptViewDetail);
                }
            }
            removeAllElements();
            add(list);
        } else {
            removeAllElements();
            add(original);
        }
    }

    private void add(List<ScriptViewDetail> original) {
        for (ScriptViewDetail scriptViewDetail : original) {
            addElement(scriptViewDetail);
        }
    }
}
