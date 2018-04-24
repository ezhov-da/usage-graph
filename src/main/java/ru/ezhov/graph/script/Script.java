package ru.ezhov.graph.script;

public interface Script {
    String id();

    String text() throws Exception;

    int rows();
}
