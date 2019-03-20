package ru.iteco.training.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JavaUtils {

    public static Properties parsePropertyFile(final Class clazz, String propFileName) {
        Properties property = new Properties();

        try (InputStream is = clazz.getResourceAsStream(propFileName)) {
            property.load(is);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

        return property;

    }

    public static void main(String[] args) {
        System.out.println(testMethod(3));
    }

    private static String testMethod(int a) {
        try {
            if (3 == a) {
                throw new IOException("afdsf");
            }
        } finally {
            return "Zero";
        }
    }
}
