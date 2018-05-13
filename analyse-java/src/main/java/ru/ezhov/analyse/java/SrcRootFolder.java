package ru.ezhov.analyse.java;

import java.io.File;

public class SrcRootFolder {
    private File file;

    public SrcRootFolder(File file) {
        this.file = file;
    }

    public File folder() {
        return file;
    }
}
