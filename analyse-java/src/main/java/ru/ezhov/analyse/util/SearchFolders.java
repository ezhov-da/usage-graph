package ru.ezhov.analyse.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchFolders {
    private Set<SearchFolder> searchFolders;

    /**
     * Путь к папке/искомая папка с исходниками
     *
     * @param map
     */
    public SearchFolders(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException("Аргумент не может быть null");
        }
        this.searchFolders = new HashSet<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            searchFolders.add(new SearchFolder(entry.getKey(), entry.getValue()));
        }
    }

    public SearchFolders() {
        this.searchFolders = new HashSet<>();
    }

    public void addFolder(String pathToRoot, String nameFolder) {
        searchFolders.add(new SearchFolder(pathToRoot, nameFolder));
    }

    public Set<SearchFolder> all() {
        return searchFolders;
    }
}
