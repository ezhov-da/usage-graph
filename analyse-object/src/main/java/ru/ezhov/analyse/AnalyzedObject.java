package ru.ezhov.analyse;

public interface AnalyzedObject {

    String id();

    String text() throws Exception;

    int rows();
}
