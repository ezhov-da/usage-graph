package ru.ezhov.graph.view;

import ru.ezhov.graph.script.Script;

import java.util.Set;

public class ScriptViewDetail {
    private Script script;

    private Set<ScriptView> parents;
    private Set<ScriptView> children;

    public ScriptViewDetail(Script script, Set<ScriptView> parents, Set<ScriptView> children) {
        this.script = script;
        this.parents = parents;
        this.children = children;
    }

    public String id() {
        return script.id();
    }

    public String text() throws Exception {
        return script.text();
    }

    public Set<ScriptView> parents() {
        return parents;
    }

    public Set<ScriptView> children() {
        return children;
    }
}
