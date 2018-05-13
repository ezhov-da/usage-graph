package ru.ezhov.analyse.java;

import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.util.FoldersByName;
import ru.ezhov.analyse.util.SearchFolders;

import java.util.Map;

public class JavaAnalyzedObjectsFactory {
    public static AnalyzedObjects fromFile(Map<String, String> map) throws Exception {
        return new JavaParser(new FoldersByName(new SearchFolders(map)).folders()).parse();
    }
}
