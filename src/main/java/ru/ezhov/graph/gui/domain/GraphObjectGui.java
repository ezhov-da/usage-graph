package ru.ezhov.graph.gui.domain;

import java.util.Set;

public interface GraphObjectGui {
    /**
     * Получение уникального идентификатора
     *
     * @return уникальный идентификатор
     */
    String id();

    /**
     * Текст этого объекта
     *
     * @return текст
     * @throws Exception
     */
    String text() throws Exception;

    /**
     * Стабильность объекта
     *
     * @return исходящих зависимостей / (исходящие + входящие)
     */
    double stability();

    /**
     * Кол-во строк в этом объекте
     *
     * @return
     */
    int rows();

    /**
     * Родители
     *
     * @return родители
     */
    Set<GraphObjectGui> parents();

    /**
     * Дети
     *
     * @return дети
     */
    Set<GraphObjectGui> children();
}
