package ru.ezhov.graph.script;

import java.util.Set;

public interface Scripts {
    Script get(String id);

    Set<Script> all();

    Set<Script> parents(String id);

    Set<Script> children(String id);
}
