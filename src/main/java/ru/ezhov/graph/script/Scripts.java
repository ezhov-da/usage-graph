package ru.ezhov.graph.script;

import java.util.List;

public interface Scripts {
    Script get(String id);

    List<Script> all();

    List<Script> parents(String id);

    List<Script> children(String id);
}
