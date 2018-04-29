package ru.ezhov.graph.gui.graphtablepanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;

public interface GraphTableListener {
    void event(EventType eventType, Object event, GraphObjectGui script);
}
