package ru.iteco.training.sort;

import java.util.List;

/**
 * Сортировка кастомных объектов
 *
 * @param <T> пользовательский сортируемый тип
 */
public interface Sorter<T> {

    /**
     * Сортировка списка объектов
     *
     * @return отсортированный список
     */
    List<T> sort(List<T> collection);
}
