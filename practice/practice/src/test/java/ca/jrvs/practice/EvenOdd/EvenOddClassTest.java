package ca.jrvs.practice.EvenOdd;

import org.junit.Before;
import org.junit.Test;
import sun.awt.geom.Crossings;

import static org.junit.Assert.*;

public class EvenOddClassTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void evenNumber() {
        assertEquals("Even", EvenOddClass.EvenOdd(2));
    }
    @Test
    public void NegativeEvenNumber() {
        assertEquals("Even", EvenOddClass.EvenOdd(-4));
    }

    @Test
    public void oddNumber() {
        assertEquals("Odd", EvenOddClass.EvenOdd(3));
    }

    @Test
    public void NegativeOddNumber() {
        assertEquals("Odd", EvenOddClass.EvenOdd(-7));
    }
}