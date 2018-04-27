package ru.ezhov.graph.storage;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilePathStore implements PathStore {

    private static final String NAME_FILE_STORE = "paths.txt";
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    private List<Path> paths;

    @Override
    public List<Path> paths() throws Exception {
        paths = new ArrayList<>();
        File file = new File(NAME_FILE_STORE);
        if (file.exists()) {
            paths = read(file);
        }
        return paths;
    }

    @Override
    public void path(String path) throws Exception {
        if (paths.isEmpty()) {
            paths.add(new DefaultPath(path));
        } else {
            for (int i = 0; i < paths.size(); i++) {
                if (paths.get(i).path().equals(path)) {
                    paths.remove(paths.get(i));
                    paths.add(0, new DefaultPath(path));
                    break;
                }
            }
        }
        write(new File(NAME_FILE_STORE));
    }

    private List<Path> read(File file) throws Exception {
        if (file.exists()) {
            List<Path> paths = new ArrayList<>();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] s = line.split(" ");
                    paths.add(new DefaultPath(s[0]));
                }
                return paths;
            }
        }
        return Collections.emptyList();
    }

    private void write(File file) throws Exception {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (Path path : paths) {
                bufferedWriter.write(path.path() + "\n");
            }
        }
    }
}
