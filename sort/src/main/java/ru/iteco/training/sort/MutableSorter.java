package ru.iteco.training.sort;

import ru.iteco.training.sort.strategy.SortStrategy;

import java.util.Comparator;
import java.util.List;

public class MutableSorter<T> extends BaseSorter<T> {
    MutableSorter(Comparator<T> comparator, SortStrategy<T> strategy) {
        super(comparator, strategy);
    }

    /**
     * @return ссылка на исходную коллекцию
     */
    @Override
    public List<T> sort(List<T> collection) {
        return super.sort(collection);
    }
}
