package ca.jrvs.practice;

import java.util.Arrays;

public class FindNumberStream {
    public int FindMaxStream(int[]nums){
       return Arrays.stream(nums).max()
               .orElseThrow(() -> new IllegalArgumentException("Array is empty or null"));
    }

    public int FindMinStream(int[]nums){
        return Arrays.stream(nums).min()
                .orElseThrow(() -> new IllegalArgumentException("Array is empty or null"));
    }

}
