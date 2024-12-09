package ca.jrvs.practice;

public class FindLargestSmallest {
    public int findLargest(int[]nums){
        int max = nums[0];
        for(int i=1 ; i<nums.length ; i++){
            if(nums[i] > max) {
                max = nums[i];
            }
        }
        return max;
    }

    public int findSmallest(int[]nums){
        int min = nums[0];
        for(int i=1 ; i<nums.length ; i++){
            if(nums[i] < min) {
                min = nums[i];
            }
        }
        return min;
    }
}
