package ru.ezhov.analyse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ezhov.analyse.java.SrcRootFolder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FoldersByName {
    private static final Logger LOG = LoggerFactory.getLogger(FoldersByName.class);
    private SearchFolders searchFolders;

    public FoldersByName(SearchFolders searchFolders) {
        if (searchFolders.all().isEmpty()) {
            throw new IllegalArgumentException("Обязательно должна быть указана папка для поиска");
        }
        this.searchFolders = searchFolders;
    }

    public List<SrcRootFolder> folders() {
        List<SrcRootFolder> files = new ForkJoinPool().invoke(new RootFiles(searchFolders));
        LOG.info("Найдено [{}] папок", files.size());
        return files;
    }

    private class RootFiles extends RecursiveTask<List<SrcRootFolder>> {

        private SearchFolders searchFolders;

        RootFiles(SearchFolders searchFolders) {
            this.searchFolders = searchFolders;
        }

        @Override
        protected List<SrcRootFolder> compute() {
            List<RecursiveFile> recursiveFiles = new ArrayList<>();
            for (SearchFolder searchFolder : searchFolders.all()) {
                RecursiveFile recursiveFile = new RecursiveFile(searchFolder.root(), searchFolder.findSrcFolderName());
                recursiveFile.fork();
                recursiveFiles.add(recursiveFile);
            }
            List<SrcRootFolder> fileList = new ArrayList<>();
            for (RecursiveFile recursiveFile : recursiveFiles) {
                List<File> files = recursiveFile.join();
                for (File file : files) {
                    fileList.add(new SrcRootFolder(file));
                }
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
                    if (file.isDirectory() && nameFolder.equals(file.getName())) {
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
