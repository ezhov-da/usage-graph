package ru.ezhov.graph.gui.util;

import ru.ezhov.graph.gui.domain.GraphObjectGui;
import ru.ezhov.graph.gui.domain.GraphObjectsGui;
import ru.ezhov.graph.script.Script;
import ru.ezhov.graph.script.Scripts;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScriptsToScriptsGui {
    private Scripts scripts;
    private List<GraphObjectGui> graphObjectGuis;

    public ScriptsToScriptsGui(Scripts scripts) {
        this.scripts = scripts;
    }

    public GraphObjectsGui convert() {
        return new DefaultGraphObjectsGui(scripts);
    }

    private class DefaultGraphObjectsGui implements GraphObjectsGui {
        private Scripts scripts;

        public DefaultGraphObjectsGui(Scripts scripts) {
            this.scripts = scripts;
        }

        @Override
        public List<GraphObjectGui> all() {
            //кешируем список
            if (graphObjectGuis == null) {
                graphObjectGuis = new ArrayList<>();
                for (Script script : scripts.all()) {
                    graphObjectGuis.add(new DefaultGraphObject(script.id()));
                }
            }
            return graphObjectGuis;
        }

        private class DefaultGraphObject implements GraphObjectGui {

            private String id;
            private Set<GraphObjectGui> parents;
            private Set<GraphObjectGui> children;

            public DefaultGraphObject(String id) {
                this.id = id;
            }

            @Override
            public String id() {
                return id;
            }

            @Override
            public String text() throws Exception {
                return scripts.get(id).text();
            }

            @Override
            public double stability() {
                double childrenCount = scripts.children(id).size();
                double parentCount = scripts.parents(id).size();

                return new BigDecimal(
                        (childrenCount + parentCount) != 0 ? childrenCount / (childrenCount + parentCount) : 0
                ).round(new MathContext(2)).doubleValue();
            }

            @Override
            public int rows() {
                return scripts.get(id).rows();
            }

            @Override
            public Set<GraphObjectGui> parents() {
                //кешируем полученный список
                if (parents == null) {
                    parents = convert(scripts.parents(id));
                }
                return parents;
            }

            @Override
            public Set<GraphObjectGui> children() {
                //кешируем полученный список
                if (children == null) {
                    children = convert(scripts.children(id));
                }
                return children;
            }

            private Set<GraphObjectGui> convert(List<Script> scripts) {
                Set<GraphObjectGui> graphObjectGuis = new HashSet<>();
                for (Script script : scripts) {
                    graphObjectGuis.add(new DefaultGraphObject(script.id()));
                }
                return graphObjectGuis;
            }
        }
    }
}
