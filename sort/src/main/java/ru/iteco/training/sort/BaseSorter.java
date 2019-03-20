package ru.iteco.training.sort;

import ru.iteco.training.sort.strategy.SortStrategy;

import java.util.Comparator;
import java.util.List;

public class BaseSorter<T> implements Sorter<T> {
    private Comparator<T> comparator;
    private SortStrategy<T> strategy;

    BaseSorter(Comparator<T> comparator, SortStrategy<T> strategy) {
        this.comparator = comparator;
        this.strategy = strategy;
    }

    public List<T> sort(List<T> collection) {
        strategy.sort(collection, comparator);
        return collection;
    }
}
