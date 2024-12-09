package ca.jrvs.practice;

import java.util.HashSet;
import java.util.Set;

public class DuplicateSet {

    public boolean duplicateSet(int[] nums){
        Set<Integer> duplicate = new HashSet<>();
         for (int num : nums){
             if(!duplicate.add(num)){
                 return true;
             }
         }
         return false;
    }
}
