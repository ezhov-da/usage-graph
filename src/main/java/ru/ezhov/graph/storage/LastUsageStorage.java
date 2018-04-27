package ru.ezhov.graph.storage;

import java.util.List;

/**
 * Хранилище последнего использования
 */
public interface LastUsageStorage {
    public List<LastUsageInfo> lastUsages() throws Exception;

    public void add(LastUsageInfo lastUsageInfo) throws Exception;
}
