package ru.ezhov.analyse.script;

import ru.ezhov.analyse.AnalyzedObjects;

import java.io.File;

public class ScriptsAnalyzedObjectsFactory {
    public static AnalyzedObjects fromFile(File root) throws Exception {
        return new ScriptsFile(root);
    }
}
