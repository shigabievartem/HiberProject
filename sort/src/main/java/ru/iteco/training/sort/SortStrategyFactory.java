package ru.iteco.training.sort;

import ru.iteco.training.sort.strategy.BubbleSortStrategy;
import ru.iteco.training.sort.strategy.FastSortStrategy;
import ru.iteco.training.sort.strategy.SortStrategy;
import ru.iteco.training.sort.strategy.SortStrategyType;
import ru.iteco.training.sort.strategy.pivot.LastElementPivotStrategy;
import ru.iteco.training.sort.strategy.pivot.PivotStrategy;
import ru.iteco.training.sort.strategy.pivot.PivotStrategyType;

class SortStrategyFactory {

    static <T> SortStrategy<T> getSortStrategy(SortStrategyType strategy, PivotStrategyType pivotType) {
        if (strategy == null) strategy = SortStrategyType.DEFAULT;

        SortStrategy<T> realization;
        switch (strategy) {
            case DEFAULT:
            case BUBBLE:
                realization = new BubbleSortStrategy<T>();
                break;
            case QUICK:
                realization = new FastSortStrategy<T>(SortStrategyFactory.<T>pivot(pivotType));
                break;
            default:
                throw new RuntimeException("Unknown sort type");
        }
        return realization;
    }

    private static <T> PivotStrategy<T> pivot(PivotStrategyType type) {
        if (type == null) type = PivotStrategyType.DEFAULT;

        PivotStrategy<T> pivot;
        switch (type) {
            case FIRST_ELEMENT:
                pivot = new FirstElementPivotStrategy<T>();
                break;
            case LAST_ELEMENT:
                pivot = new LastElementPivotStrategy<T>();
                break;
            case DEFAULT:
            default:
                pivot = new FirstElementPivotStrategy<T>();
        }
        return pivot;
    }
}
