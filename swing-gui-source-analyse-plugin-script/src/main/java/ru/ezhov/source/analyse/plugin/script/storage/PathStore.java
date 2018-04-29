package ru.ezhov.source.analyse.plugin.script.storage;

import java.util.List;

public interface PathStore {

    public List<Path> paths() throws Exception;

    public void path(String path) throws Exception;
}
