package pticyn.tsm.optimization;

public final class Configuration {
    public static final double T_START = 100.0;
    public static final double COVERAGE_PENALTY = T_START;
    public static final double T_END = 0.001;
    public static final double ALPHA = 0.999;

    public static final String PROJECT_DIR = System.getProperty("user.dir");
    public static final String EXEC_DIR = PROJECT_DIR + "/target/coverage";
    public static final String CLASSES_DIR = PROJECT_DIR + "/target/classes";

    public static final boolean DEBUG_MESSAGE = false;

    public static final byte SEED = 1;
    public static final byte ITERATIONS = 30;
    public static final byte DIMENSION = 25;
}