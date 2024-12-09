package ca.jrvs.practice;

import java.util.Arrays;

public class TwoSumSort {
    public int[] twoSumSort(int[]nums, int target){
        Arrays.sort(nums);
        int left = 0;
        int right = nums.length -1 ;

        while(left<right) {
            int sum = nums[left] + nums [right];
            if(sum == target) {
                return new int[]{left, right};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        throw new IllegalArgumentException("No solution found");

    }

}
