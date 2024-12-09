package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindLargestSmallestTest {

    @Test
    public void findSmallest() {
        FindLargestSmallest findLargeNumber = new FindLargestSmallest();
        int[] nums = {1,4,5,6,7,9};

        assertEquals(9,findLargeNumber.findLargest(nums));
    }

    @Test
    public void findLargest() {
        FindLargestSmallest findSmallNumber = new FindLargestSmallest();
        int[] nums = {1,4,5,6,7,9};

        assertEquals(1,findSmallNumber.findSmallest(nums));
    }
}