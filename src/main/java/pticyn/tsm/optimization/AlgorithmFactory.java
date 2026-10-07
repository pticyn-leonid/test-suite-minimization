package pticyn.tsm.optimization;

@FunctionalInterface
public interface AlgorithmFactory {
    OptimizationAlgorithm create(Solution solution);
}