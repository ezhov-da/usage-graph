package ru.ezhov.analyse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PlainViewAnalyzedObjects implements ViewAnalyzedObjects {

    private AnalyzedObjects analyzedObjects;

    public PlainViewAnalyzedObjects(AnalyzedObjects analyzedObjects) {
        this.analyzedObjects = analyzedObjects;
    }

    @Override
    public InputStream stream() {
        StringBuilder stringBuilder = new StringBuilder();
        for (AnalyzedObject analyzedObject : analyzedObjects.all()) {
            stringBuilder
                    .append(new String(new char[100]).replace("\0", "=")).append("\n")
                    .append("ID: ").append(analyzedObject.id()).append("\n");

            List<AnalyzedObject> parents = analyzedObjects.parents(analyzedObject.id());
            if (!parents.isEmpty()) {
                stringBuilder.append("Родители:").append("\n");
                for (AnalyzedObject parent : parents) {
                    stringBuilder.append("\t").append(parent.id()).append("\n");
                }
            }

            List<AnalyzedObject> children = analyzedObjects.children(analyzedObject.id());
            if (!children.isEmpty()) {
                stringBuilder.append("Дети:").append("\n");
                for (AnalyzedObject child : children) {
                    stringBuilder.append("\t").append(child.id()).append("\n");
                }
            }

        }
        return new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
