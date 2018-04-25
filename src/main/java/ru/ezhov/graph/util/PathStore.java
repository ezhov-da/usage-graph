package ru.ezhov.graph.util;

import java.util.Date;
import java.util.List;

public interface PathStore {

    public List<Path> paths() throws Exception;

    public void path(String path, Date date) throws Exception;
}
