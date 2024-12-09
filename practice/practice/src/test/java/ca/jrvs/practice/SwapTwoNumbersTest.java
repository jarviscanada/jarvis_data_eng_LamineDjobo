package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class SwapTwoNumbersTest {

    @Test
    public void swapNumbers() {

        int [] result = SwapTwoNumbers.swapNumbers(5,10);
        assertEquals(10,result[0]);
        assertEquals(5,result[1]);

        result = SwapTwoNumbers.swapNumbers(-7,3);
        assertEquals(3,result[0]);
        assertEquals(-7,result[1]);
    }
}