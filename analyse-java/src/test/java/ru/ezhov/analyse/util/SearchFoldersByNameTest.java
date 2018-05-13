package ru.ezhov.analyse.util;

import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class SearchFoldersByNameTest {
    @Test
    public void folders() throws Exception {
        final File file1 = new File("analyse-java").getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile();
        System.out.printf("Поиск в %s\n", file1.getAbsolutePath());
        SearchFolders searchFolders = new SearchFolders(new HashMap<String, String>() {{
            put(file1.getAbsolutePath(), "src");
        }});
        FoldersByName foldersByName = new FoldersByName(searchFolders);
        assertTrue(!foldersByName.folders().isEmpty());
    }

}