package ru.ezhov.analyse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FoldersByName {
    private static final Logger LOG = LoggerFactory.getLogger(FoldersByName.class);
    private String name;
    private File[] rootFolders;

    public FoldersByName(String name, File... rootFolders) {
        if (rootFolders.length == 0) {
            throw new IllegalArgumentException("Обязательно должна быть указана папка для поиска");
        }
        for (File file : rootFolders) {
            if (!file.isDirectory()) {
                throw new IllegalArgumentException("[ " + file.getAbsolutePath() + "] не папка. Поиск осуществляется только в папке");
            }
        }
        if (name == null || "".equals(name)) {
            throw new IllegalArgumentException("Имя не может быть null или пусто");
        }
        this.name = name;
        this.rootFolders = rootFolders;
    }

    public Folders folders() {
        List<File> files = new ForkJoinPool().invoke(new RootFiles(name, rootFolders));
        LOG.info("Найдено [{}] папок с именем [{}]", files.size(), name);
        return new Folders(new HashSet<File>(files));
    }


    private class RootFiles extends RecursiveTask<List<File>> {

        private File[] rootFolders;
        private String nameFolder;

        RootFiles(String nameFolder, File... rootFolders) {
            this.rootFolders = rootFolders;
            this.nameFolder = nameFolder;
        }

        @Override
        protected List<File> compute() {
            List<RecursiveFile> recursiveFiles = new ArrayList<>();
            for (File file : rootFolders) {
                RecursiveFile recursiveFile = new RecursiveFile(file, nameFolder);
                recursiveFile.fork();
                recursiveFiles.add(recursiveFile);
            }
            List<File> fileList = new ArrayList<>();
            for (RecursiveFile recursiveFile : recursiveFiles) {
                fileList.addAll(recursiveFile.join());
            }
            return fileList;
        }
    }

    private class RecursiveFile extends RecursiveTask<List<File>> {
        private File root;
        private String nameFolder;

        RecursiveFile(File root, String nameFolder) {
            this.root = root;
            this.nameFolder = nameFolder;
        }

        @Override
        protected List<File> compute() {
            return recursiveSearch(root, new ArrayList<File>());
        }

        private List<File> recursiveSearch(File root, List<File> findFolders) {
            File[] files = root.listFiles(new DirectoryFilter());
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && name.equals(file.getName())) {
                        LOG.debug("Найдена папка: {}", file.getAbsolutePath());
                        findFolders.add(file);
                    }
                    if (file.isDirectory()) {
                        recursiveSearch(file, findFolders);
                    }
                }
            }

            return findFolders;
        }
    }

    private class DirectoryFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    }
}
