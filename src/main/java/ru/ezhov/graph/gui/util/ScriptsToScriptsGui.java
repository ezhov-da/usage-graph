package ru.ezhov.graph.gui.util;

import ru.ezhov.graph.gui.domain.ScriptGui;
import ru.ezhov.graph.gui.domain.ScriptsGui;
import ru.ezhov.graph.script.Script;
import ru.ezhov.graph.script.Scripts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScriptsToScriptsGui {
    private Scripts scripts;
    private List<ScriptGui> scriptGuis;

    public ScriptsToScriptsGui(Scripts scripts) {
        this.scripts = scripts;
    }

    public ScriptsGui convert() {
        return new DefaultScriptsGui(scripts);
    }

    private class DefaultScriptsGui implements ScriptsGui {
        private Scripts scripts;

        public DefaultScriptsGui(Scripts scripts) {
            this.scripts = scripts;
        }

        @Override
        public List<ScriptGui> all() {
            //кешируем список
            if (scriptGuis == null) {
                scriptGuis = new ArrayList<>();
                for (Script script : scripts.all()) {
                    scriptGuis.add(new DefaultScript(script.id()));
                }
            }
            return scriptGuis;
        }

        private class DefaultScript implements ScriptGui {

            private String id;
            private Set<ScriptGui> parents;
            private Set<ScriptGui> children;

            public DefaultScript(String id) {
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
                return (childrenCount + parentCount) != 0 ? childrenCount / (childrenCount + parentCount) : 0;
            }

            @Override
            public int rows() {
                return scripts.get(id).rows();
            }

            @Override
            public Set<ScriptGui> parents() {
                //кешируем полученный список
                if (parents == null) {
                    parents = convert(scripts.parents(id));
                }
                return parents;
            }

            @Override
            public Set<ScriptGui> children() {
                //кешируем полученный список
                if (children == null) {
                    children = convert(scripts.children(id));
                }
                return children;
            }

            private Set<ScriptGui> convert(List<Script> scripts) {
                Set<ScriptGui> scriptGuis = new HashSet<>();
                for (Script script : scripts) {
                    scriptGuis.add(new DefaultScript(script.id()));
                }
                return scriptGuis;
            }
        }
    }
}
