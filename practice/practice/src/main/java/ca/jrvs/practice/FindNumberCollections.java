package ca.jrvs.practice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindNumberCollections {

    public int FindMin(Integer[]nums){
        List<Integer> list = Arrays.asList(nums);
        return Collections.min(list);
    }

    public int FindMax(Integer[]nums){
        List<Integer> list = Arrays.asList(nums);
        return Collections.max(list);
    }
}
