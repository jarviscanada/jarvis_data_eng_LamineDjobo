package ca.jrvs.practice;

import java.util.HashSet;
import java.util.Set;

public class FDuplicateSet {

    public int duplicateSet (int[] nums){
        Set<Integer> duplicate = new HashSet<>();
        for(int num : nums){
            if(!duplicate.add(num)){
                return num;
            }
        }
        throw new IllegalArgumentException("No duplicate found");

    }

}
