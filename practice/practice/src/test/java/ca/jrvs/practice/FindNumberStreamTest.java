package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindNumberStreamTest {

    @Test
    public void findMaxStream() {
        FindNumberStream findNumber = new FindNumberStream();
        int [] nums = {1,2,3,5,6,8,19};

        assertEquals(19,findNumber.FindMaxStream(nums));
    }

    @Test
    public void findMinStream() {
        FindNumberStream findNumber = new FindNumberStream();
        int [] nums = {1,2,3,5,6,8,19};

        assertEquals(1,findNumber.FindMinStream(nums));
    }
}