package ru.ezhov.graph.gui.domain;

import java.util.Set;

public interface ScriptGui {
    String id();

    String text() throws Exception;

    double stability();

    int rows();

    Set<ScriptGui> parents();

    Set<ScriptGui> children();
}
