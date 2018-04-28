package ru.ezhov.graph.gui.domain;

import ru.ezhov.graph.script.Script;

public class DefaultScriptView implements ScriptView {
    private Script script;

    public DefaultScriptView(Script script) {
        this.script = script;
    }

    @Override
    public String id() {
        return script.id();
    }

    @Override
    public String text() throws Exception {
        return script.text();
    }
}
