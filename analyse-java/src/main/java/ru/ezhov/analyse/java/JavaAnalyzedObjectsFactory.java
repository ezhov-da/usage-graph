package ru.ezhov.analyse.java;

import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.util.FoldersByName;

import java.io.File;

public class JavaAnalyzedObjectsFactory {
    public static AnalyzedObjects fromFile(String name, File... roots) throws Exception {
        return new JavaParser(new FoldersByName(name, roots).folders()).parse();
    }
}
