package ca.jrvs.practice.EvenOdd;

import org.junit.Test;

import static org.junit.Assert.*;

public class EvenOddBitTest {

    @Test
    public void evenBit() {
        assertEquals("even", EvenOddBit.EvenOddBit(2));
    }

    @Test
    public void oddBit() {
        assertEquals("odd", EvenOddBit.EvenOddBit(3));
    }
}