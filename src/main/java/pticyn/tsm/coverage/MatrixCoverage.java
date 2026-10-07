package pticyn.tsm.coverage;

import lombok.Getter;

@Getter
public class MatrixCoverage {
    private final boolean[][] matrix;
    private final String[] row;
    private final String[] col;

    public MatrixCoverage(boolean[][] matrix, String[] tests, String[] requirements) {
        this.matrix = matrix.clone();
        this.row = tests.clone();
        this.col = requirements.clone();
    }

    public MatrixCoverage(boolean[][] matrix) {
        this.matrix = matrix.clone();
        this.row = initTest(matrix.length);
        this.col = initReq(matrix[0].length);
    }

    public MatrixCoverage(int[][] matrix) {
        this.matrix = new boolean[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                this.matrix[i][j] = matrix[i][j] == 1;

        this.row = initTest(matrix.length);
        this.col = initReq(matrix[0].length);
    }

    private String[] initReq(int count) {
        String[] req = new String[count];
        for (int i = 0; i < count; i++)
            req[i] = "R_" + i;
        return req;
    }

    private String[] initTest(int count) {
        String[] tests = new String[count];
        for (int i = 0; i < count; i++)
            tests[i] = "Test_" + i;
        return tests;
    }

    public boolean existItem(int row, int col) {
        return matrix[row][col];
    }

    public void print() {
        String corner = "Test \\ Req";

        // Считаем ширину колонки с именами тестов
        int testWidth = corner.length();
        for (String t : row) testWidth = Math.max(testWidth, t.length());

        // Считаем ширину колонки с именами требований
        int reqWidth = 3; // минимум под "R_0"
        for (String r : col) reqWidth = Math.max(reqWidth, r.length());

        // Форматы: | значение | значение | ...
        String testFmt = "| %-" + testWidth + "s ";
        String reqFmt = "| %-" + reqWidth + "s ";

        // Разделительная линия +-----+-----+-----+
        StringBuilder sb = new StringBuilder("+");
        sb.append("-".repeat(testWidth + 2)).append("+");
        for (int j = 0; j < col.length; j++)
            sb.append("-".repeat(reqWidth + 2)).append("+");
        String separator = sb.toString();

        // 1. Заголовок таблицы
        System.out.println(separator);

        // 2. Шапка: угловая метка + имена требований
        System.out.printf(testFmt, corner);
        for (String r : col) System.out.printf(reqFmt, r);
        System.out.println("|");
        System.out.println(separator);

        // 3. Строки матрицы: имя теста + X/. по каждому требованию
        for (int i = 0; i < row.length; i++) {
            System.out.printf(testFmt, row[i]);
            for (int j = 0; j < col.length; j++)
                System.out.printf(reqFmt, matrix[i][j] ? "X" : ".");
            System.out.println("|");
        }

        System.out.println(separator);
    }
}
