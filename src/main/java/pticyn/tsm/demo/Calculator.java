package pticyn.tsm.demo;

public class Calculator {

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        if (b == 0) throw new ArithmeticException("Деление на ноль невозможно");
        return a / b;
    }

    public static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public static double sqrt(double value) {
        if (value < 0) throw new ArithmeticException("Корень из отрицательного числа");
        return Math.sqrt(value);
    }
}