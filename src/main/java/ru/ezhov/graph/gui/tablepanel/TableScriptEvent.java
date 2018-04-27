package ru.ezhov.graph.gui.tablepanel;

import ru.ezhov.graph.script.Script;

public interface TableScriptEvent {
    void event(EventType eventType, Object event, Script script);
}
