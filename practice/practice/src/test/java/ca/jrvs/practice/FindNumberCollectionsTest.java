package ca.jrvs.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindNumberCollectionsTest {

    @Test
    public void findMin() {
        FindNumberCollections findNum = new FindNumberCollections();

        Integer[] nums = {1,2,4,5,6,9};
        assertEquals(1, findNum.FindMin(nums));
    }

    @Test
    public void findMax() {
        FindNumberCollections findNum = new FindNumberCollections();

        Integer[] nums = {1,2,4,5,6,9};
        assertEquals(9, findNum.FindMax(nums));
    }
}