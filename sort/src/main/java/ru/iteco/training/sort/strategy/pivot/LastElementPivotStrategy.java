package ru.iteco.training.sort.strategy.pivot;

import java.util.List;

public class LastElementPivotStrategy<T> implements PivotStrategy<T> {
    public int choose(List<T> objects) {
        return objects.size() - 1;
    }
}
