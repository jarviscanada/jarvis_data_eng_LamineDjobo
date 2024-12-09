package ca.jrvs.practice;

import java.util.Map;

public class CompareMapsManually {

    public <K, V> boolean compareMapsManually(Map<K, V> m1, Map<K, V> m2) {
        if (m1.size() != m2.size()) {
            return false;
        }

        for (Map.Entry<K, V> entry : m1.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();

            if (!m2.containsKey(key)) {
                return false;
            }

            if (!value.equals(m2.get(key))) {
                return false;

            }
        }

        return true;

    }
}