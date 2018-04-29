package ru.ezhov.analyse;

import java.util.List;

public class NullAnalyzedObjects implements AnalyzedObjects {

    public static final NullAnalyzedObjects NULL_ANALYZED_OBJECTS = new NullAnalyzedObjects();

    private NullAnalyzedObjects() {
    }

    @Override
    public AnalyzedObject get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnalyzedObject> all() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnalyzedObject> parents(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnalyzedObject> children(String id) {
        throw new UnsupportedOperationException();
    }
}
