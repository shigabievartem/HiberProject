package ru.iteco.training.sort;

import ru.iteco.training.sort.strategy.SortStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ImmutableSorter<T> extends BaseSorter<T> {
    ImmutableSorter(Comparator<T> comparator, SortStrategy<T> strategy) {
        super(comparator, strategy);
    }

    /**
     * @return копия входной коллекции
     */
    @Override
    public List<T> sort(List<T> collection) {
        List<T> target = new ArrayList<T>(collection);
        return super.sort(target);
    }
}
