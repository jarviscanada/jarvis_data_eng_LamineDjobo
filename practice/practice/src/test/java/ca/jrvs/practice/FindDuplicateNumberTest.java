package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindDuplicateNumberTest {

    @Test
    public void duplicate() {
        FindDuplicateNumber duplicate = new FindDuplicateNumber();
        int[] nums = {1,2,1,4,5};
        assertEquals(1,duplicate.duplicate(nums));
    }
}