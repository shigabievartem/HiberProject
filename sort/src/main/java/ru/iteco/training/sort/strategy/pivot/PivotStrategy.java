package ru.iteco.training.sort.strategy.pivot;

import java.util.List;

public interface PivotStrategy<T> {
    int choose(List<T> objects);
}
