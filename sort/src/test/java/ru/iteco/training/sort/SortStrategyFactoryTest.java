package ru.iteco.training.sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.iteco.training.sort.strategy.BubbleSortStrategy;
import ru.iteco.training.sort.strategy.FastSortStrategy;
import ru.iteco.training.sort.strategy.SortStrategy;
import ru.iteco.training.sort.strategy.SortStrategyType;
import ru.iteco.training.sort.strategy.pivot.PivotStrategyType;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SortStrategyFactoryTest {
    private SortStrategyType type;
    private PivotStrategyType pivot;
    private Class<? extends SortStrategy> expected;

    public SortStrategyFactoryTest(SortStrategyType type, PivotStrategyType pivot, Class<? extends SortStrategy> expected) {
        this.type = type;
        this.pivot = pivot;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(
                new Object[][]{
                        {SortStrategyType.DEFAULT, null, BubbleSortStrategy.class},
                        {SortStrategyType.BUBBLE, null, BubbleSortStrategy.class},
                        {SortStrategyType.BUBBLE, PivotStrategyType.FIRST_ELEMENT, BubbleSortStrategy.class},
                        {SortStrategyType.QUICK, null, FastSortStrategy.class},
                        {SortStrategyType.QUICK, PivotStrategyType.FIRST_ELEMENT, FastSortStrategy.class},
                        {SortStrategyType.QUICK, PivotStrategyType.LAST_ELEMENT, FastSortStrategy.class}
                });
    }

    @Test
    public void getSortStrategy() {
        SortStrategy strategy = SortStrategyFactory.getSortStrategy(type, pivot);
        assertEquals(expected, strategy.getClass());
    }

}