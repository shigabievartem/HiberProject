package ru.iteco.training.sort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class AppTest {
    private final PrintStream systemOut = System.out;

    private ByteArrayOutputStream testOut;

    @Before
    public void setUp() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @Test
    public void testNormalSort() {
        String[] args = new String[]{"3", "1", "2"};
        App.main(args);

        assertEquals("1 2 3", testOut.toString().trim());
    }

    @Test
    public void testEmptyArgumentList() {
        String[] args = new String[]{};
        App.main(args);

        assertEquals("", testOut.toString());
    }

    @Test
    public void testSingleElementSort() {
        String[] args = new String[]{"777"};
        App.main(args);

        assertEquals("777", testOut.toString().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnparsableArgument() {
        String[] args = new String[]{"as888"};
        App.main(args);
    }

    @After
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
    }
}
