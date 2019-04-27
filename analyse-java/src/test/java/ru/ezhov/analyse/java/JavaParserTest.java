package ru.ezhov.analyse.java;

import org.junit.Test;
import ru.ezhov.analyse.AnalyzedObjects;
import ru.ezhov.analyse.ConsolePrintAnalyzedObjects;
import ru.ezhov.analyse.PlainViewAnalyzedObjects;
import ru.ezhov.analyse.util.FoldersByName;
import ru.ezhov.analyse.util.SearchFolders;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class JavaParserTest {
    @Test
    public void parse() throws Exception {
        List<SrcRootFolder> srcRootFolders = new FoldersByName(new SearchFolders(
                new HashMap<String, String>() {{
                    put(new File("analyse-java").getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath(), "src");
                }}
        )).folders();
        JavaParser javaParser = new JavaParser(srcRootFolders);
        AnalyzedObjects parse = javaParser.parse();
        try {
            new ConsolePrintAnalyzedObjects(new PlainViewAnalyzedObjects(parse).stream()).print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}