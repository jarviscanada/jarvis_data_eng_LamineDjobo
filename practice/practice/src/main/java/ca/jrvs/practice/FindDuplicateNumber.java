package ca.jrvs.practice;

import java.util.Arrays;

public class FindDuplicateNumber {

    public int duplicate(int[] nums){
        Arrays.sort(nums);

        for(int i=1; i< nums.length ; i++){
            if(nums[i] == nums[i -1]){
                return nums[i];
            }
        }
        throw new IllegalArgumentException("No duplicate found");

    }

}
