package ru.iteco.training.sort.strategy;

import java.util.Comparator;
import java.util.List;

/**
 * Алгоритм сортировки
 */
public interface SortStrategy<T> {

    /**
     * Сортировка списка объектов
     *
     * @param objects    коллекция пользовательских объектов
     * @param comparator метод сравнения объектов
     */
    void sort(List<T> objects, Comparator<T> comparator);
}
