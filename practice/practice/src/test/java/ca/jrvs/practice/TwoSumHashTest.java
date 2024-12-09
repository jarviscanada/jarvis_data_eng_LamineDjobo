package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwoSumHashTest {

    @Test
    public void twoSumHash() {
        TwoSumHash solution = new TwoSumHash();
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        assertArrayEquals(new int[]{0, 1}, solution.twoSumHash(nums, target));
    }
}