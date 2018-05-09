package ru.ezhov.analyse;

import org.junit.Test;

import java.util.List;

public class PlainViewAnalyzedObjectsTest {
    @Test
    public void stream() throws Exception {
        AnalyzedObjects analyzedObjects = new AnalyzedObjects() {
            @Override
            public AnalyzedObject get(String id) {
                return null;
            }

            @Override
            public List<AnalyzedObject> all() {
                return null;
            }

            @Override
            public List<AnalyzedObject> parents(String id) {
                return null;
            }

            @Override
            public List<AnalyzedObject> children(String id) {
                return null;
            }
        };

        ViewAnalyzedObjects viewAnalyzedObjects = new PlainViewAnalyzedObjects(analyzedObjects);
    }

}