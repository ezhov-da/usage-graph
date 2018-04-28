package ru.ezhov.graph.gui.tablepanel;

import ru.ezhov.graph.gui.domain.ScriptGui;

public interface TableScriptEvent {
    void event(EventType eventType, Object event, ScriptGui script);
}
