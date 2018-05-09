package ru.ezhov.analyse.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class FoldersByNameTest {
    @Test
    public void folders() throws Exception {
        File file1 = new File("analyse-java").getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile();
        System.out.printf("Поиск в %s\n", file1.getAbsolutePath());
        FoldersByName foldersByName = new FoldersByName(
                "src",
                file1
        );
        assertTrue(!foldersByName.folders().all().isEmpty());
    }

}