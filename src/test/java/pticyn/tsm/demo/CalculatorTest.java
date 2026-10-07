package pticyn.tsm.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
    @Test
    @DisplayName("Сложение двух положительных чисел")
    void testAddPositive() {
        assertEquals(8.0, Calculator.add(5, 3));
    }

    @Test
    @DisplayName("Сложение двух положительных чисел - дубликат")
    void testAddPositiveDuplicate() {
        assertEquals(7.0, Calculator.add(4, 3));
    }

    @Test
    @DisplayName("Сложение отрицательного числа и положительного")
    void testAddNegativeAndPositive() {
        assertEquals(-2.0, Calculator.add(-5, 3));
    }

    @Test
    @DisplayName("Сложение с отрицательными числами")
    void testAddNegative() {
        assertEquals(-8.0, Calculator.add(-5, -3));
    }

    @Test
    @DisplayName("Вычитание")
    void testSubtract() {
        assertEquals(7.0, Calculator.subtract(10, 3));
        assertEquals(-7.0, Calculator.subtract(3, 10));
        assertEquals(0.0, Calculator.subtract(5, 5));
    }

    @Test
    @DisplayName("Умножение, включая умножение на ноль")
    void testMultiply() {
        assertEquals(42.0, Calculator.multiply(6, 7));
        assertEquals(0.0, Calculator.multiply(5, 0));
        assertEquals(-15.0, Calculator.multiply(5, -3));
    }

    @Test
    @DisplayName("Деление")
    void testDivide() {
        assertEquals(5.0, Calculator.divide(20, 4));
        assertEquals(0.5, Calculator.divide(1, 2));
    }

    @Test
    @DisplayName("Деление на ноль")
    void testDivideByZero() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> Calculator.divide(5, 0));
        assertEquals("Деление на ноль невозможно", ex.getMessage());
    }

    @Test
    @DisplayName("Возведение в степень")
    void testPower() {
        assertEquals(1024.0, Calculator.power(2, 10), 0.0001);
        assertEquals(1.0, Calculator.power(5, 0), 0.0001);
        assertEquals(0.25, Calculator.power(2, -2), 0.0001);
    }

    @Test
    @DisplayName("Квадратный корень")
    void testSqrt() {
        assertEquals(12.0, Calculator.sqrt(144), 0.0001);
        assertEquals(0.0, Calculator.sqrt(0), 0.0001);
    }

    @Test
    @DisplayName("Квадратный корень - дубликат")
    void testSqrtDuplicate() {
        assertEquals(1.0, Calculator.sqrt(1), 0.0001);
    }

    @Test
    @DisplayName("Корень из отрицательного числа")
    void testSqrtNegative() {
        assertThrows(ArithmeticException.class, () -> Calculator.sqrt(-9));
    }
}
