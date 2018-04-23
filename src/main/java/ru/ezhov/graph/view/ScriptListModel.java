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
        if (partId != null) {
            String find = partId.toLowerCase().trim();
            if (!"".equals(find)) {
                List<ScriptViewDetail> list = new ArrayList<>();
                for (ScriptViewDetail scriptViewDetail : original) {
                    if (scriptViewDetail.id().toLowerCase().trim().contains(find)) {
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
    }

    private void add(List<ScriptViewDetail> original) {
        for (ScriptViewDetail scriptViewDetail : original) {
            addElement(scriptViewDetail);
        }
    }
}
