package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class FibonacciTest {

    @Test
    public void fibo() {
        assertEquals(0,Fibonacci.fibo(0));
        assertEquals(1,Fibonacci.fibo(1));
        assertEquals(1,Fibonacci.fibo(2));



    }
}