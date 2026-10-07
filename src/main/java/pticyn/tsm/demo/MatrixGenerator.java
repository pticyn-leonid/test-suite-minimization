package pticyn.tsm.demo;

import java.util.Random;

public final class MatrixGenerator {
    public static boolean[][] generate(int row, int col, byte seed) {
        Random rand = new Random(seed);
        boolean[][] matrix = new boolean[row][col];

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                matrix[i][j] = rand.nextBoolean();

        return matrix;
    }

    public static boolean[][] generate(int n, byte seed) {
        return generate(n, n, seed);
    }
}
