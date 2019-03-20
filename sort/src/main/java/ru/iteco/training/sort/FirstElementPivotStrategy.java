package ru.iteco.training.sort;

import ru.iteco.training.sort.strategy.pivot.PivotStrategy;

import java.util.List;

/**
 * Выбор первого элемента в качестве базиса
 */
public class FirstElementPivotStrategy<T> implements PivotStrategy<T> {

    public int choose(List<T> collection) {
        return 0;
    }
}
