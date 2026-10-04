package pticyn.tsm;

import pticyn.tsm.demo.MatrixGenerator;
import pticyn.tsm.model.MatrixCoverage;
import pticyn.tsm.optimization.BruteForce;
import pticyn.tsm.optimization.SimulatedAnnealing;
import pticyn.tsm.optimization.Solution;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("==== Демонстрация работы алгоритмов оптимизации (минимизации) ====");
        System.out.println("== 1. Запуск алгоритма на подготовленной матрице ==");

        MatrixCoverage matrixCoverage = new MatrixCoverage(MatrixGenerator.generate(30));
        System.out.println("Исходная матрица:");
        matrixCoverage.print();

        Solution solutionSA = new Solution(matrixCoverage);
        Solution solutionBF = new Solution(matrixCoverage);

        SimulatedAnnealing sa = new SimulatedAnnealing(solutionSA);
        BruteForce bf = new BruteForce(solutionBF);

        solutionSA = sa.optimize();
        solutionBF = bf.optimize();

        solutionSA.print();
        solutionBF.print();
    }
}