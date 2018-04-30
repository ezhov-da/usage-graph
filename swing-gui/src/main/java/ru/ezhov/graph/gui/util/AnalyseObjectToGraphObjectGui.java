package ru.ezhov.graph.gui.util;

import ru.ezhov.analyse.AnalyzedObject;
import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.domain.GraphObjectsGui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalyseObjectToGraphObjectGui {
    private AnalyzedObjects analyzedObjects;
    private List<GraphObjectGui> graphObjectGuis;

    public AnalyseObjectToGraphObjectGui(AnalyzedObjects analyzedObjects) {
        this.analyzedObjects = analyzedObjects;
    }

    public GraphObjectsGui convert() {
        return new DefaultGraphObjectsGui(analyzedObjects);
    }

    private class DefaultGraphObjectsGui implements GraphObjectsGui {
        private AnalyzedObjects analyzedObjects;

        DefaultGraphObjectsGui(AnalyzedObjects analyzedObjects) {
            this.analyzedObjects = analyzedObjects;
        }

        @Override
        public List<GraphObjectGui> all() {
            //кешируем список
            if (graphObjectGuis == null) {
                graphObjectGuis = new ArrayList<>();
                for (AnalyzedObject analyzedObject : analyzedObjects.all()) {
                    graphObjectGuis.add(new DefaultGraphObject(analyzedObject.id()));
                }
            }
            return graphObjectGuis;
        }

        private class DefaultGraphObject implements GraphObjectGui {
            private String id;
            private Set<GraphObjectGui> parents;
            private Set<GraphObjectGui> children;

            DefaultGraphObject(String id) {
                this.id = id;
            }

            @Override
            public String id() {
                return id;
            }

            @Override
            public String text() throws Exception {
                return analyzedObjects.get(id).text();
            }

            @Override
            public double stability() {
                double childrenCount = analyzedObjects.children(id).size();
                double parentCount = analyzedObjects.parents(id).size();
                return
                        new BigDecimal(
                                (childrenCount + parentCount) != 0 ? childrenCount / (childrenCount + parentCount) : 0
                        ).round(
                                new MathContext(2)
                        ).doubleValue();
            }

            @Override
            public int rows() {
                return analyzedObjects.get(id).rows();
            }

            @Override
            public Set<GraphObjectGui> parents() {
                //кешируем полученный список
                if (parents == null) {
                    parents = convert(analyzedObjects.parents(id));
                }
                return parents;
            }

            @Override
            public Set<GraphObjectGui> children() {
                //кешируем полученный список
                if (children == null) {
                    children = convert(analyzedObjects.children(id));
                }
                return children;
            }

            private Set<GraphObjectGui> convert(List<AnalyzedObject> analyzedObjects) {
                Set<GraphObjectGui> graphObjectGuis = new HashSet<>();
                for (AnalyzedObject analyzedObject : analyzedObjects) {
                    graphObjectGuis.add(new DefaultGraphObject(analyzedObject.id()));
                }
                return graphObjectGuis;
            }
        }
    }
}
