package ca.jrvs.practice;

public class NotSoSimpleCalculatorImpl implements NotSoSimpleCalculator{

    private SimpleCalculator calc;

    public  NotSoSimpleCalculatorImpl(SimpleCalculator calc){
        this.calc = calc;
    }

    @Override
    public int power(int x, int y) {
        int result = 1;
        for (int i = 0; i < y; i++) {
            result *= x;
        }
        return result;
    }

    @Override
    public int abs(int x) {
        return (x < 0) ? -x : x;
    }

    @Override
    public double sqrt(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of a negative number");
        }
        return Math.sqrt(x);
    }
}
