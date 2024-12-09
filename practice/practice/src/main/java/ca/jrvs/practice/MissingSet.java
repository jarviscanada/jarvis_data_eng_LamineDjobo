package ca.jrvs.practice;

import java.util.HashSet;
import java.util.Set;

public class MissingSet {

    public int missingSet(int[] nums){
        Set<Integer> setNum = new HashSet<>();

        for(int num : nums) {
            setNum.add(num);
        }

        for(int i = 0; i< nums.length ; i++){
            if (!setNum.contains(i)){
                return i;
            }
        }
        throw new IllegalArgumentException("No missing number found");

    }

}
