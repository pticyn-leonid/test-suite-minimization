package pticyn.tsm.optimization;

public class BruteForce implements OptimizationAlgorithm {
    private Solution solution;

    public BruteForce(Solution solution) {
        this.solution = solution;
    }

    @Override
    public Solution optimize() {
        int testsCount = solution.getSelectedTests().length;
        long combinations = 1L << testsCount; // 2^tests

        for (long mask = 0; mask < combinations; mask++) {
            int testCount = Long.bitCount(mask); // количество тестов

            if (testCount >= solution.getCountSelectedTest()) continue;

            if (coversAllRequirements(mask))
                solution = createSolution(mask);
        }
        return solution;
    }

    private boolean coversAllRequirements(long mask) {
        int reqCount = solution.getReqCount();
        int testsCount = solution.getSelectedTests().length;

        for (int req = 0; req < reqCount; req++) {
            boolean covered = false;
            for (int test = 0; test < testsCount; test++)
                if ((mask & (1L << test)) != 0 && solution.getMatrix().existItem(test, req)) {
                    covered = true;
                    break;
                }
            if (!covered) return false;
        }
        return true;
    }

    private Solution createSolution(long mask) {
        boolean[] selected = new boolean[solution.getSelectedTests().length];

        for (int test = 0; test < selected.length; test++)
            selected[test] = (mask & (1L << test)) != 0;

        return new Solution(solution.getMatrix(), selected);
    }
}
