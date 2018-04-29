package ru.ezhov.analyse;

/**
 * Интерфейс для анализа
 */
public interface SourceAnalyse {
    /**
     * Если необходимо при вызове не производить обработку, возвращать {@link NullAnalyzedObjects}
     *
     * @return анализируемые объекты
     * @throws AnalyseException ошибка анализа
     */
    AnalyzedObjects analyse() throws AnalyseException;
}
