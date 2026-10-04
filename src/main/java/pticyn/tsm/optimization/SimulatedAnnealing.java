package pticyn.tsm.optimization;


import java.util.Random;

import static pticyn.tsm.optimization.Configuration.*;

public class SimulatedAnnealing implements OptimizationAlgorithm {
    private final double tempEnd;
    private final double alpha;
    private Solution solution;
    private double tempCurrent;

    public SimulatedAnnealing(Solution solution) {
        this.tempCurrent = T_START;
        this.tempEnd = T_END;
        this.alpha = ALPHA;
        this.solution = solution;
    }

    @Override
    public Solution optimize() {
        Random rand = new Random(SEED);
        while (tempCurrent > tempEnd) {
            Solution newSolution = solution.makeNewSolution();
            double delta = newSolution.getEnergy() - solution.getEnergy();

            if (delta <= 0) solution = newSolution;
            else {
                double probability = Math.exp(-delta / tempCurrent);
                double r = rand.nextDouble();
                if (r < probability) solution = newSolution;
            }
            tempCurrent *= alpha;
        }
        return solution;
    }
}
