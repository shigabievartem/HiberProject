package ru.iteco.training.sort;

import ru.iteco.training.sort.strategy.SortStrategyType;
import ru.iteco.training.sort.strategy.pivot.PivotStrategyType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Конструктор объекта для сортировки коллекций
 */
public class SortBuilder {
    private SortBuilder() {
    }

    private SortStrategyType sortStrategyType;
    private PivotStrategyType pivotStrategyType;
    private boolean returnOnlyView;

    public static SortBuilder newBuilder() {
        return new SortBuilder();
    }

    public SortBuilder sortStrategy(SortStrategyType x) {
        sortStrategyType = x;
        return this;
    }

    public SortBuilder pivotStrategy(PivotStrategyType x) {
        pivotStrategyType = x;
        return this;
    }

    public SortBuilder returnOnlyView(boolean x) {
        returnOnlyView = x;
        return this;
    }

    <T> Sorter<T> build(final Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Undefined comparator");
        }
        if (returnOnlyView) {
            System.out.println("Избыточная конфигурация.");
        }

        checkIfAllOk();
        return new MutableSorter<T>(comparator, SortStrategyFactory.<T>getSortStrategy(sortStrategyType, pivotStrategyType));
    }

    public <T> Sorter<T> buildImmutable(final Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Undefined comparator");
        }

        checkIfAllOk();
        return new ImmutableSorter<T>(comparator, SortStrategyFactory.<T>getSortStrategy(sortStrategyType, pivotStrategyType)) {
            @Override
            public List<T> sort(List<T> collection) {
                List<T> result = super.sort(collection);
                return returnOnlyView ? Collections.unmodifiableList(result) : result;
            }
        };
    }

    private void checkIfAllOk() {
        if (sortStrategyType == null) {
            throw new IllegalArgumentException("Undefined sort method. Please set up sort strategy.");
        }
    }
}
