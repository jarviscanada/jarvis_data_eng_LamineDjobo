package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class SwapNumbersbitTest {

    @Test
    public void swapUsingBit() {
        int [] result = SwapNumbersbit.swapUsingBit(5,10);
        assertEquals(10,result[0]);
        assertEquals(5,result[1]);
    }
}