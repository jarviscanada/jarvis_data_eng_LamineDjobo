package ca.jrvs.practice;

public class ClimbingStairs {

    public static int climbingStairs(int n ) {
        if(n<= 1) return 1;

        int first = 1;
        int second = 1;

        for( int i = 2; i <= n ; i++) {
            int current  = first + second;
            second = first;
            first = current ;
        }

        return first;
    }
}
