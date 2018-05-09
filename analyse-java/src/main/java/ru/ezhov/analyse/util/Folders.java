package ru.ezhov.analyse.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Folders {
    private Set<File> folders;


    public Folders() {
        this(new HashSet<File>());
    }

    public Folders(Set<File> files) {
        if (files == null) {
            throw new IllegalArgumentException("files не может быть null");
        }
        for (File file : files) {
            checkIsDirectory(file);
        }
        this.folders = files;
    }

    private void checkIsDirectory(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("[" + file.getAbsolutePath() + "] не является папкой");
        }
    }

    public void addFolder(File file) {
        checkIsDirectory(file);
        folders.add(file);
    }

    public Set<File> all() {
        return folders;
    }
}
