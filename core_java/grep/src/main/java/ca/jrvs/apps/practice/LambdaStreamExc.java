package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExc {
    /**
     *
     * @param strings
     * @return
     */
    public Stream<String> createStrStream (String ... strings){
        return Stream.of(strings);
    }

    /**
     *
     * @param strings
     * @return
     */

    public Stream<String> toUpperCase(String ... strings){
        return createStrStream(strings).map(String::toUpperCase);
    }

    public Stream<String> filter (Stream<String> stringStream ,String pattern){
        return stringStream.filter(strg -> !strg.contains(pattern));
    }

    public IntStream createIntStream(int[] arr){
        return IntStream.of(arr);
    }

    public <E> List<E> toList(Stream<E> stream){
        return stream.collect(Collectors.toList());
    }

    public IntStream createIntStream(int start, int end){
        return IntStream.rangeClosed(start,end);
    }

    public DoubleStream squareRootIntStream (IntStream intStream){
        return intStream.mapToDouble(Math::sqrt);
    }

    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(num -> num % 2 != 0);
    }

}
