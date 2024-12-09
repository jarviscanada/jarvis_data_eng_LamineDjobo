package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwoSumSortTest {

    @Test
    public void twoSumSort() {
        TwoSumSort twoSum = new TwoSumSort();
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        assertArrayEquals(new int []{0,1}, twoSum.twoSumSort(nums,target));
    }
}