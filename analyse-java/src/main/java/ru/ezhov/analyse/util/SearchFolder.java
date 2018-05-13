package ru.ezhov.analyse.util;

import java.io.File;

public class SearchFolder {
    private File root;
    private String findSrcFolderName;

    public SearchFolder(String rootFolder, String findSrcFolderName) {
        File file = new File(rootFolder);
        if (!file.exists()) {
            throw new IllegalArgumentException("Корневая папка [" + rootFolder + "] должна существовать");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("[" + rootFolder + "] не является папкой");
        }
        this.root = file;
        this.findSrcFolderName = findSrcFolderName;
    }

    public File root() {
        return root;
    }

    public String findSrcFolderName() {
        return findSrcFolderName;
    }
}
