package ca.jrvs.practice;

public class FibonacciDp {

    public static int fiboDp(int n){
        if(n<=1){
            return n;
        }

        int  first = 0;
        int second = 1;

        for(int i=2; i<=n; i++) {
            int current = first + second;
            second = first;
            first = current;
        }

       return first;


    }
}
