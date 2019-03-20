package ru.iteco.training.sort;

/**
 * Simple program for sorting arguments in natural order
 */
public class App {
    public static void main(String[] args) {
        int[] values = parse(args);
        values = sort(values);
        print(values);
    }

    private static int[] parse(String[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            int value = Integer.parseInt(data[i]);
            result[i] = value;
        }
        return result;
    }

    private static int[] sort(int[] source) {
        int length = source.length;
        int[] sequence = new int[length];
        System.arraycopy(source, 0, sequence, 0, length);

        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (sequence[i] > sequence[j]) {
                    int buffer = sequence[i];
                    sequence[i] = sequence[j];
                    sequence[j] = buffer;
                }
            }
        }

        return sequence;
    }

    private static void print(int[] values) {
        if (values == null || values.length < 1) return;

        int length = values.length;
        int defaultCapacity = length * 3 + length; // some optimization trick
        StringBuilder msg = new StringBuilder(defaultCapacity);
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                msg.append(" ");
            }
            msg.append(values[i]);
        }
        System.out.println(msg);
    }
}