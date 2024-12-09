package ca.jrvs.practice.EvenOdd;

public class EvenOddBit {

    public static String EvenOddBit(int nums){
        return (nums & 1) == 0 ? "even" : "odd";
    }
}
