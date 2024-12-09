package ca.jrvs.practice;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CompareMapsManuallyTest {

    @Test
    public void compareMapsManually() {

        CompareMapsManually compareManually = new CompareMapsManually();

        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);

        Map<String, Integer> map2 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);

        Map<String, Integer> map3 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 4);

        assertTrue(compareManually.compareMapsManually(map1, map2));

        // Test with unequal maps (different values)
        assertFalse(compareManually.compareMapsManually(map1, map3));

    }
}