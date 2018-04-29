package ru.ezhov.analyse.script;

import ru.ezhov.analyse.AnalyzedObjects;

import java.io.File;

public class ScriptsFactory {
    public static AnalyzedObjects fromFile(File root) throws Exception {
        return new FileScripts(root);
    }
}
