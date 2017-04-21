package edu.banda.coel.web;

import com.banda.core.util.RandomUtil;

public class NDimShiftSymmetricConfiguration {

    public static boolean[] generate(int[] shift, int size) {
        int dim = shift.length;
        boolean[] config = new boolean[(int) Math.pow(size, dim)];
        boolean[] visited = new boolean[(int) Math.pow(size, dim)];

        for (int i = 0; i < config.length; i++) {
            int innerIndex = i;
            int[] pos = getPosition(size, dim, innerIndex);

            boolean state = RandomUtil.nextBoolean();

            while (!visited[innerIndex]) {
                visited[innerIndex] = true;
                config[innerIndex] = state;

                for (int j = 0; j < dim; j++) {
                    pos[j] = (pos[j] + shift[j]) % size;
                }

                innerIndex = getInnerIndex(size, dim, pos);
            }
        }
        return config;
    }

    private static int getInnerIndex(int n, int dim, int[] position) {
        int index = 0;
        for (int i = 0; i < dim; i++) {
            index = (n * index) + position[i];
        }
        return index;
    }

    private static int[] getPosition(int n, int dim, int index) {
        int[] pos = new int[dim];
        for (int i = 0; i < dim; i++) {
            pos[dim -i - 1] = index % n;
            index = index / n;
        }
        return pos;
    }
}