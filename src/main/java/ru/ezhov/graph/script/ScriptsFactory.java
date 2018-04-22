package ru.ezhov.graph.script;

import java.io.File;

public class ScriptsFactory {
    public static Scripts fromFile(File root) throws Exception {
        return new FileScripts(root);
    }
}
