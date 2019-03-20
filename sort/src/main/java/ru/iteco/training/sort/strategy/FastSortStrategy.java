package ru.iteco.training.sort.strategy;

import ru.iteco.training.sort.strategy.pivot.FirstElementPivotStrategy;
import ru.iteco.training.sort.strategy.pivot.PivotStrategy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FastSortStrategy<T> implements SortStrategy<T> {
    private final PivotStrategy<T> pivot;

    public FastSortStrategy(PivotStrategy<T> pivot) {
        this.pivot = pivot == null ? new FirstElementPivotStrategy<T>() : pivot;
    }

    public void sort(List<T> objects, Comparator<T> comparator) {
        pivot.choose(objects);
        Collections.sort(objects, comparator);
    }

}