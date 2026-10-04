package pticyn.tsm.demo;

import java.util.Random;

import static pticyn.tsm.optimization.Configuration.SEED;

public final class MatrixGenerator {
    public static boolean[][] generate(int row, int col) {
        Random rand = new Random(SEED);
        boolean[][] matrix = new boolean[row][col];

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                matrix[i][j] = rand.nextBoolean();

        return matrix;
    }

    public static boolean[][] generate(int n) {
        return generate(n, n);
    }
}
