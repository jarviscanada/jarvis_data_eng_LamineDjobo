package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class FDuplicateSetTest {

    @Test
    public void duplicateSet() {
        FDuplicateSet duplicateSet = new FDuplicateSet();
        int[] nums = {1,2,4,5,5,2};
        assertEquals(5,duplicateSet.duplicateSet(nums));
    }
}