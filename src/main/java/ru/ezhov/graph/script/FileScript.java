package ru.ezhov.graph.script;

import java.io.File;
import java.nio.file.Files;

class FileScript implements Script {

    private String id;

    private File file;

    public FileScript(String id, File file) {
        this.id = id;
        this.file = file;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String text() throws Exception {
        return new String(Files.readAllBytes(file.toPath()), "UTF-8");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileScript that = (FileScript) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
