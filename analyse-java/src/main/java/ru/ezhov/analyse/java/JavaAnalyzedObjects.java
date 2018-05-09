package ru.ezhov.analyse.java;

import ru.ezhov.analyse.AnalyzedObject;
import ru.ezhov.analyse.AnalyzedObjects;

import java.util.*;

class JavaAnalyzedObjects implements AnalyzedObjects {
    private Map<String, AnalyzedObject> all = new HashMap<>();
    private Map<String, Set<String>> parents = new HashMap<>();
    private Map<String, Set<String>> children = new HashMap<>();

    @Override
    public AnalyzedObject get(String id) {
        return all.get(id);
    }

    @Override
    public List<AnalyzedObject> all() {
        return new ArrayList<>(all.values());
    }

    @Override
    public List<AnalyzedObject> parents(String id) {
        List<AnalyzedObject> analyzedObjects = new ArrayList<>();
        if (parents.containsKey(id)) {
            Set<String> parentsId = parents.get(id);
            for (String s : parentsId) {
                analyzedObjects.add(all.get(s));
            }
        }
        return analyzedObjects;
    }

    @Override
    public List<AnalyzedObject> children(String id) {
        List<AnalyzedObject> analyzedObjects = new ArrayList<>();
        if (children.containsKey(id)) {
            Set<String> childrenId = children.get(id);
            for (String s : childrenId) {
                analyzedObjects.add(all.get(s));
            }
        }
        return analyzedObjects;
    }

    public void addAnalyzedObject(String id, AnalyzedObject analyzedObject) {
        all.put(id, analyzedObject);

    }

    public void addParent(String id, String parentId) {
        Set<String> parentsId;
        if (!parents.containsKey(id)) {
            parentsId = new HashSet<>();
            parents.put(id, parentsId);
        } else {
            parentsId = parents.get(id);
        }
        parentsId.add(parentId);
    }

    public void addChild(String id, String childId) {
        Set<String> childrenId;
        if (!children.containsKey(id)) {
            childrenId = new HashSet<String>();
            children.put(id, childrenId);
        } else {
            childrenId = children.get(id);
        }
        childrenId.add(childId);

    }
}
