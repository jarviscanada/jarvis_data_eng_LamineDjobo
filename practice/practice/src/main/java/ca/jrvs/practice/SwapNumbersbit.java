package ca.jrvs.practice;

public class SwapNumbersbit {

    public static int[] swapUsingBit(int a, int b) {
        a = a ^ b;
        b = a ^ b ;
        a = a ^ b;

        return new int[] {a,b};

    }

}
