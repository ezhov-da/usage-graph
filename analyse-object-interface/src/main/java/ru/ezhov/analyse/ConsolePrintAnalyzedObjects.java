package ru.ezhov.analyse;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class ConsolePrintAnalyzedObjects implements PrintAnalyzedObjects {

    private InputStream inputStream;

    public ConsolePrintAnalyzedObjects(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void print() throws Exception {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);) {
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = bufferedInputStream.read(buffer)) != -1) {
                System.out.print(new String(buffer, 0, read, "UTF-8"));
            }
        }
    }
}
