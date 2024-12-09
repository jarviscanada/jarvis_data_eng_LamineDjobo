package ca.jrvs.practice;

public class MissingSum {

    public int missingNum(int[] nums){
        int n = nums.length;
        int totalNum = 0;
        int arrayNum = 0;

        for(int i=0; i<=n ; i++){
            totalNum += i;
        }

        for(int num : nums) {
            arrayNum += num;
        }

        return totalNum - arrayNum;

    }

}
