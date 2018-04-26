package ru.ezhov.graph.util;

import java.util.List;

public interface PathStore {

    public List<Path> paths() throws Exception;

    public void path(String path) throws Exception;
}
