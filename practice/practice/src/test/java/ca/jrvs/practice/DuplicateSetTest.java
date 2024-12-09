package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class DuplicateSetTest {

    @Test
    public void duplicateSet() {
        DuplicateSet duplicateSet = new DuplicateSet();
        int[] nums = {1,3,4,4,7,2};
        assertTrue(duplicateSet.duplicateSet(nums));
    }
}