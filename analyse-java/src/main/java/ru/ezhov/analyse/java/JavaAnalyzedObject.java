package ru.ezhov.analyse.java;

import ru.ezhov.analyse.AnalyzedObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

class JavaAnalyzedObject implements AnalyzedObject {

    private JavaFile javaFile;
    private int rows = -1;

    public JavaAnalyzedObject(JavaFile javaFile) {
        this.javaFile = javaFile;
    }

    @Override
    public String id() {
        return javaFile.id();
    }

    @Override
    public String text() throws Exception {
        return new String(Files.readAllBytes(javaFile.file().toPath()), "UTF-8");
    }

    @Override
    public int rows() {
        if (rows == -1) {
            try (InputStream is = new BufferedInputStream(new FileInputStream(javaFile.file()));) {
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
}
