package pticyn.tsm.coverage;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ILine;
import org.jacoco.core.tools.ExecFileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pticyn.tsm.optimization.Configuration.*;

public final class JacocoCoverage {
    public static MatrixCoverage build(String targetClass) throws Exception {
        String testClassName = targetClass + "Test";
        String[] testNames = discoverTestNames(testClassName);

        List<Set<Integer>> testCoverage = new ArrayList<>();
        Set<Integer> allRequirements = new LinkedHashSet<>();

        for (String testName : testNames) {
            runTest(testClassName, testName);
            Set<Integer> coveredLines = readCoveredLines(targetClass, testName);
            testCoverage.add(coveredLines);
            allRequirements.addAll(coveredLines);
        }

        List<Integer> requirements = new ArrayList<>(allRequirements);
        boolean[][] matrix = new boolean[testNames.length][requirements.size()];

        for (int test = 0; test < testNames.length; test++)
            for (int req = 0; req < requirements.size(); req++)
                if (testCoverage.get(test).contains(requirements.get(req))) matrix[test][req] = true;

        String[] reqNames = new String[requirements.size()];

        for (int i = 0; i < requirements.size(); i++)
            reqNames[i] = targetClass.substring(targetClass.lastIndexOf('.') + 1) + ":" + requirements.get(i);

        return new MatrixCoverage(matrix, testNames, reqNames);
    }

    private static void runTest(String testClass, String testName) throws IOException, InterruptedException {
        File coverDir = new File(EXEC_DIR);

        if (!coverDir.exists() && !coverDir.mkdirs())
            throw new RuntimeException("Не удалось создать папку " + coverDir);

        File execFile = new File(coverDir, testName + ".exec");
        ProcessBuilder processBuilder = new ProcessBuilder("mvn", "test", "-Dtest=" + testClass + "#" + testName, "-Djacoco.destFile=" + execFile.getAbsolutePath());

        processBuilder.directory(new File(PROJECT_DIR));
        if (DEBUG_MESSAGE) processBuilder.inheritIO();
        Process process = processBuilder.start();
        process.waitFor();

        if (process.waitFor() != 0) throw new RuntimeException("Тест завершился с ошибкой: " + testName);
        if (!execFile.exists()) throw new RuntimeException("JaCoCo не создал файл: " + execFile);
    }

    private static Set<Integer> readCoveredLines(String targetClass, String testName) throws IOException {
        ExecFileLoader execLoader = new ExecFileLoader();
        execLoader.load(new File(EXEC_DIR + "/" + testName + ".exec"));

        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(execLoader.getExecutionDataStore(), coverageBuilder);
        analyzer.analyzeAll(new File(CLASSES_DIR));

        String expectedClassName = targetClass.replace('.', '/');
        Set<Integer> coveredLines = new LinkedHashSet<>();

        for (IClassCoverage classCoverage : coverageBuilder.getClasses()) {
            if (!expectedClassName.equals(classCoverage.getName())) continue;

            for (int line = classCoverage.getFirstLine(); line <= classCoverage.getLastLine(); line++) {
                ILine coverage = classCoverage.getLine(line);
                if (coverage == null) continue;
                if (coverage.getInstructionCounter().getCoveredCount() > 0) coveredLines.add(line);
            }
        }
        return coveredLines;
    }

    private static String[] discoverTestNames(String testClassName) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("mvn", "test", "-Dtest=" + testClassName, "-DfailIfNoTests=false", "-Dmaven.test.failure.ignore=true");

        processBuilder.directory(new File(PROJECT_DIR));
        if (DEBUG_MESSAGE) processBuilder.inheritIO();

        Process process = processBuilder.start();
        process.waitFor();

        File report = new File(PROJECT_DIR + "/target/surefire-reports/TEST-" + testClassName + ".xml");

        if (!report.exists()) throw new RuntimeException("Не найден отчёт Surefire: " + report.getAbsolutePath());

        return parseTestcaseNames(report);
    }

    private static String[] parseTestcaseNames(File report) throws IOException {
        String xml = Files.readString(report.toPath());
        Matcher matcher = Pattern.compile("<testcase[^>]*\\bname=\"([^\"]+)\"").matcher(xml);

        List<String> names = new ArrayList<>();

        while (matcher.find()) names.add(matcher.group(1));

        return names.toArray(new String[0]);
    }
}