package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwoSumBruteTest {

    @Test
    public void twoSum() {
        TwoSumBrute twosum = new TwoSumBrute();
        int[] nums = {2,4,5,6,7,8};
        int target = 9;

        assertArrayEquals(new int[]{0,1} , twosum.twoSum(nums,target));
    }
}