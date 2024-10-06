package ca.jrvs.practice;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
public class SimpleCalculatorImplTest {

    SimpleCalculator calculator;

    @Before
    public void setUp() throws Exception {
            calculator = new SimpleCalculatorImpl();
    }


    @Test
    public void add() {
        int expected =4 ;
        int actual = calculator.add(2,2);
        assertEquals(expected,actual);
    }

    @Test
    public void subtract() {
        int expected =0 ;
        int actual = calculator.subtract(2,2);
        assertEquals(expected,actual);
    }

    @Test
    public void multiply() {
        int expected =4 ;
        int actual = calculator.multiply(2,2);
        assertEquals(expected,actual);
    }

    @Test
    public void divide() {
        int expected =1 ;
        int actual = calculator.divide(2,2);
        assertEquals(expected,actual);
    }
}