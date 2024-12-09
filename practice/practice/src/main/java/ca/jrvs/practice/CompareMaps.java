package ca.jrvs.practice;

import java.util.Map;

public class CompareMaps {

    public <K,V> boolean compareMaps(Map<K,V> m1, Map<K,V> m2) {
        return m1.equals(m2);
    }
}
