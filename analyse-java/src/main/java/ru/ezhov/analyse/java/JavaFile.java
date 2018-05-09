package ru.ezhov.analyse.java;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

class JavaFile {
    private File file;
    private String packageName;
    private Set<String> imports;
    private Set<String> declareClassess;
    private Set<String> words;

    JavaFile(File file, String packageName) {
        this.file = file;
        this.packageName = packageName;
        this.imports = new HashSet<>();
        this.declareClassess = new HashSet<>();
        this.words = new HashSet<>();
    }

    JavaFile(File file) {
        this(file, "unknown");
    }

    void imports(String importName) {
        this.imports.add(importName);
    }

    Set<String> imports() {
        return this.imports;
    }

    void declareClass(String importName) {
        this.declareClassess.add(importName);
    }

    void word(String word) {
        this.words.add(word);
    }

    void packageName(String packageName) {
        this.packageName = packageName;
    }

    String packageName() {
        return this.packageName;
    }

    String name() {
        return file.getName().replaceAll("\\.java", "");
    }

    String id() {
        return packageName + "." + name();
    }

    File file() {
        return file;
    }
}
