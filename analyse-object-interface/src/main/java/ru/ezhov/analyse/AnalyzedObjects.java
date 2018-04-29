package ru.ezhov.analyse;

import java.util.List;

public interface AnalyzedObjects {
    AnalyzedObject get(String id);

    List<AnalyzedObject> all();

    List<AnalyzedObject> parents(String id);

    List<AnalyzedObject> children(String id);
}
