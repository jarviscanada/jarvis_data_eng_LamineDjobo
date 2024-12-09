package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class DuplicateTest {

    @Test
    public void containsDuplicate() {
        Duplicate duplicate = new Duplicate();
        int[] nums = {1,3,5,6,9,1};

        assertTrue(duplicate.containsDuplicate(nums));
    }
}