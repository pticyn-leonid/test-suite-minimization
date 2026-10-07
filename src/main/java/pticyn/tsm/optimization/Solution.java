package pticyn.tsm.optimization;

import lombok.Getter;
import lombok.Setter;
import pticyn.tsm.coverage.MatrixCoverage;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static pticyn.tsm.optimization.Configuration.COVERAGE_PENALTY;
import static pticyn.tsm.optimization.Configuration.SEED;

@Getter
@Setter
public class Solution {
    private final MatrixCoverage matrix;
    private final boolean[] selectedTests;
    private Random random = new Random(SEED);

    public Solution(MatrixCoverage matrix) {
        this.matrix = matrix;
        this.selectedTests = new boolean[matrix.getRow().length];
        Arrays.fill(this.selectedTests, true);
    }

    public Solution(MatrixCoverage matrix, boolean[] selected) {
        this.matrix = matrix;
        this.selectedTests = selected.clone();
    }

    public double getEnergy() {
        int covered = 0;
        int requirements = matrix.getCol().length;

        for (int col = 0; col < requirements; col++)
            for (int row = 0; row < matrix.getRow().length; row++)
                if (selectedTests[row] && matrix.existItem(row, col)) {
                    covered++;
                    break;
                }

        int uncovered = requirements - covered;
        return getCountSelectedTest() + uncovered * COVERAGE_PENALTY;
    }

    public Solution makeNewSolution(int row) {
        boolean[] tests = selectedTests.clone();
        tests[row] = !tests[row];
        return new Solution(matrix, tests);
    }

    public Solution makeNewSolution() {
        int row = random.nextInt(selectedTests.length);
        return makeNewSolution(row);
    }

    public int getCountSelectedTest() {
        int testsCount = 0;
        for (boolean item : selectedTests)
            if (item) ++testsCount;
        return testsCount;
    }

    public int getReqCount() {
        return matrix.getCol().length;
    }

    public void print() {
        System.out.println("Оптимизированный набор тестов: " +
                Arrays.toString(IntStream.range(0, selectedTests.length)
                        .filter(i -> selectedTests[i])
                        .mapToObj(i -> matrix.getRow()[i])
                        .toArray(String[]::new)));
        System.out.println("Количество требований:         " + matrix.getCol().length);
        System.out.println("Начальное количество тестов:   " + matrix.getRow().length);
        System.out.println("Конечное количество тестов:    " + getCountSelectedTest());
    }
}
