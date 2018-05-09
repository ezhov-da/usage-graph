package ru.ezhov.analyse.script;

import ru.ezhov.analyse.AnalyzedObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

class ScriptFile implements AnalyzedObject {

    private String id;

    private File file;

    private int rows = -1;

    ScriptFile(String id, File file) {
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
    public int rows() {
        if (rows == -1) {
            try (InputStream is = new BufferedInputStream(new FileInputStream(file));) {
                byte[] c = new byte[1024];
                int count = 0;
                int readChars = 0;
                boolean empty = true;
                while ((readChars = is.read(c)) != -1) {
                    empty = false;
                    for (int i = 0; i < readChars; ++i) {
                        if (c[i] == '\n') {
                            ++count;
                        }
                    }
                }
                rows = (count == 0 && !empty) ? 1 : (count != 0) ? ++count : count;
            } catch (Exception e) {
                rows = -1;
            }
        }

        return rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScriptFile that = (ScriptFile) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
