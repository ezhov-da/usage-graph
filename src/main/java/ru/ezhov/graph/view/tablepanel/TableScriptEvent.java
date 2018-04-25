package ru.ezhov.graph.view.tablepanel;

import ru.ezhov.graph.script.Script;

public interface TableScriptEvent {
    void event(EventType eventType, Object event, Script script);
}
