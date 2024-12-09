package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class FibonacciDpTest {

    @Test
    public void fiboDp() {
        assertEquals(0,FibonacciDp.fiboDp(0));
        assertEquals(1,Fibonacci.fibo(1));
        assertEquals(34,FibonacciDp.fiboDp(10));
    }
}