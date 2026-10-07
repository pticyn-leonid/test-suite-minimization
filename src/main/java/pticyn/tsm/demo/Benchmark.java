package pticyn.tsm.demo;

import pticyn.tsm.coverage.MatrixCoverage;
import pticyn.tsm.optimization.AlgorithmFactory;
import pticyn.tsm.optimization.OptimizationAlgorithm;
import pticyn.tsm.optimization.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Benchmark {
    private final long[] times;
    private final List<boolean[]> selectedTests = new ArrayList<>();
    private final MatrixCoverage matrix;
    private final byte iterations;

    public Benchmark(byte iterations, MatrixCoverage matrix) {
        this.iterations = iterations;
        this.matrix = matrix;
        this.times = new long[iterations];
    }

    public void runBenchmarkMatrix(AlgorithmFactory factory) {
        selectedTests.clear();

        int count = iterations;
        Solution solution = new Solution(matrix);

        while (count-- > 0) {
            solution.setRandom(new Random(count));
            OptimizationAlgorithm algorithm = factory.create(solution);

            long start = System.nanoTime();
            solution = algorithm.optimize();
            times[count] = System.nanoTime() - start;

            selectedTests.add(solution.getSelectedTests());
        }
    }

    public void print() {
        System.out.println("Количество запусков: " + iterations);
        System.out.println("Результаты количества тестов:");
        System.out.println("Начальное: " + matrix.getRow().length);
        System.out.println("Лучший: " + getBestCountTests());
        System.out.println("Худший: " + getBadCountTests());
        System.out.printf("Средний: %.3f%n", getAverageCountTests());
        System.out.printf("Общее время выполнения: %.3f мс%n", getTotalTime());
    }

    public double getTotalTime() {
        return (double) Arrays.stream(times).sum() / 1_000_000.0;
    }

    public double getAverageCountTests() {
        int total = 0;
        for (boolean[] row : selectedTests)
            for (boolean cell : row)
                if (cell) total++;

        return (double) total / iterations;
    }

    public int getBestCountTests() {
        int best = selectedTests.getFirst().length;
        int temp;
        for (boolean[] row : selectedTests) {
            temp = 0;
            for (boolean cell : row)
                if (cell) temp++;
            if (temp < best) best = temp;
        }
        return best;
    }

    public int getBadCountTests() {
        int bad = 0;
        int temp;
        for (boolean[] row : selectedTests) {
            temp = 0;
            for (boolean cell : row)
                if (cell) temp++;
            if (temp > bad) {
                if (temp == row.length) return bad;
                bad = temp;
            }
        }
        return bad;
    }
}
