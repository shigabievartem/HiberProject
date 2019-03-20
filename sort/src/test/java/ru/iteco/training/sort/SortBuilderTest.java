package ru.iteco.training.sort;

import org.junit.Test;
import ru.iteco.training.sort.strategy.SortStrategyType;
import ru.iteco.training.sort.strategy.pivot.PivotStrategyType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class SortBuilderTest {

    @Test
    public void newBuilder() {
        assertNotNull(SortBuilder.newBuilder());
    }

    @Test
    public void sortStrategy() {
        SortBuilder target = SortBuilder.newBuilder().sortStrategy(SortStrategyType.QUICK);
        assertNotNull(target);
    }

    @Test
    public void pivotStrategy() {
        SortBuilder target = SortBuilder.newBuilder().pivotStrategy(PivotStrategyType.FIRST_ELEMENT);
        assertNotNull(target);
    }

    @Test
    public void returnOnlyView() {
        SortBuilder target = SortBuilder.newBuilder().returnOnlyView(true);
        assertNotNull(target);
    }

    @Test
    public void build() {
        Sorter<String> sorter = SortBuilder.newBuilder()
                .pivotStrategy(PivotStrategyType.FIRST_ELEMENT)
                .sortStrategy(SortStrategyType.QUICK)
                .build(new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        int x = Integer.parseInt(o1);
                        int y = Integer.parseInt(o2);

                        if (x == y) return 0;
                        return x > y ? 1 : -1;
                    }
                });

        List<String> source = Arrays.asList("3", "2", "1");
        List<String> result = sorter.sort(source);

        assertArrayEquals(new String[]{"1", "2", "3"}, result.toArray());
        assertSame(source, result);
    }

    @Test
    public void buildImmutable() {
        Sorter<String> sorter = SortBuilder.newBuilder()
                .pivotStrategy(PivotStrategyType.FIRST_ELEMENT)
                .sortStrategy(SortStrategyType.QUICK)
                .buildImmutable(new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        int x = Integer.parseInt(o1);
                        int y = Integer.parseInt(o2);

                        if (x == y) return 0;
                        return x > y ? 1 : -1;
                    }
                });

        List<String> source = Arrays.asList("3", "2", "1");
        List<String> result = sorter.sort(source);

        assertArrayEquals(new String[]{"1", "2", "3"}, result.toArray());
        assertNotSame(source, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void buildImmutableViewReturn() {
        Sorter<String> sorter = SortBuilder.newBuilder()
                .pivotStrategy(PivotStrategyType.FIRST_ELEMENT)
                .sortStrategy(SortStrategyType.QUICK)
                .returnOnlyView(true)
                .buildImmutable(new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        int x = Integer.parseInt(o1);
                        int y = Integer.parseInt(o2);

                        if (x == y) return 0;
                        return x > y ? 1 : -1;
                    }
                });

        List<String> source = Arrays.asList("3", "2", "1");
        List<String> result = sorter.sort(source);

        result.add("4");
    }
}