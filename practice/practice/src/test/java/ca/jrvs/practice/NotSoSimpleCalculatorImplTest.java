package ca.jrvs.practice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotSoSimpleCalculatorImplTest {

    NotSoSimpleCalculator calc;

    @Mock
    SimpleCalculator mockSimpleCalc;

    @Before
    public void setUp() throws Exception {
        calc = new NotSoSimpleCalculatorImpl(mockSimpleCalc);
    }

    @Test
    public void power() {
        int expected = 8;
        int actual = calc.power(2, 3);
        assertEquals(expected,actual);
    }

    @Test
    public void abs() {
        // Test for a positive number
        int expectedPositive = 5;
        int actualPositive = calc.abs(5);
        assertEquals(expectedPositive, actualPositive);

        // Test for a negative number
        int expectedNegative = 4;
        int actualNegative = calc.abs(-4);
        assertEquals(expectedNegative, actualNegative);

        // Test for zero
        int expectedZero = 0;
        int actualZero = calc.abs(0);
        assertEquals(expectedZero, actualZero);
    }

    @Test
    public void sqrt() {
        double expectedPositive = 3.0;
        double actualPositive = calc.sqrt(9);
        assertEquals(expectedPositive, actualPositive, 0.0);
    }
}