package ca.jrvs.practice.EvenOdd;

public class EvenOddClass {

    public static String EvenOdd(int nums) {

        if(nums % 2 == 0) {
            return "Even";
        } else {
            return "Odd";
        }

    }

    public static void main(String[] args) {
        int nums =6;
        System.out.println(EvenOdd(nums));;
    }

}
