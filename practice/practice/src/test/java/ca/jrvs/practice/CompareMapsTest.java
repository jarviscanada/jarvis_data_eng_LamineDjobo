package ca.jrvs.practice;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CompareMapsTest {

    @Test
    public void compareMaps() {
        CompareMaps compareMaps = new CompareMaps();

        Map<String, Integer> m1 = new HashMap<>();
        m1.put("a", 1);
        m1.put("b", 2);

        Map<String, Integer> m2 = new HashMap<>();
        m2.put("a", 1);
        m2.put("b", 2);

        Map<String, Integer> m3 = new HashMap<>();
        m3.put("a", 1);
        m3.put("b", 2);

    assertTrue(compareMaps.compareMaps(m1,m2));
        assertTrue(compareMaps.compareMaps(m1,m3));

    }
}