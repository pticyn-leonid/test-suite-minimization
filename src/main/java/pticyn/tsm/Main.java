package pticyn.tsm;

import pticyn.tsm.coverage.JacocoCoverage;
import pticyn.tsm.coverage.MatrixCoverage;
import pticyn.tsm.demo.Benchmark;
import pticyn.tsm.demo.MatrixGenerator;
import pticyn.tsm.optimization.BruteForce;
import pticyn.tsm.optimization.SimulatedAnnealing;

import static pticyn.tsm.optimization.Configuration.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("\n==== Демонстрация работы алгоритмов оптимизации (минимизации) ====");

        System.out.println("\n== 1. Запуск алгоритма на подготовленной матрице ==");
        runFirstScript();
        System.out.println("\n== 2. На примере тестирования класса калькулятора ==");
        runSecondScript();
    }

    public static void runFirstScript() {
        MatrixCoverage matrix = new MatrixCoverage(MatrixGenerator.generate(DIMENSION, SEED));
        System.out.println("Исходная матрица размерности " + DIMENSION);
        matrix.print();
        Benchmark benchmark = new Benchmark(ITERATIONS, matrix);
        System.out.println("\n-- Бенчмарк алгоритма SA --");
        benchmark.runBenchmarkMatrix(SimulatedAnnealing::new);
        benchmark.print();
        System.out.println("\n-- Бенчмарк алгоритма BF --");
        benchmark.runBenchmarkMatrix(BruteForce::new);
        benchmark.print();
    }

    public static void runSecondScript() throws Exception {
        System.out.println("Начало сбора покрытия...");
        MatrixCoverage matrix = JacocoCoverage.build("pticyn.tsm.demo.Calculator");
        System.out.println("Исходная матрица покрытия:");
        matrix.print();
        Benchmark benchmark = new Benchmark(ITERATIONS, matrix);
        System.out.println("\n-- Бенчмарк алгоритма SA --");
        benchmark.runBenchmarkMatrix(SimulatedAnnealing::new);
        benchmark.print();
        System.out.println("\n-- Бенчмарк алгоритма BF --");
        benchmark.runBenchmarkMatrix(BruteForce::new);
        benchmark.print();
    }
}