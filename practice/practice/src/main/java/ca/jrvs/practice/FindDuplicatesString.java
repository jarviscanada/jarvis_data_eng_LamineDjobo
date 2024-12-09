package ca.jrvs.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDuplicatesString {
    public List<Character> findDuplicates (String input) {
        Map<Character,Integer> charCount = new HashMap<>();
        List<Character> duplicates = new ArrayList<>();

        for ( char ch : input.toLowerCase().toCharArray()){
            if(Character.isLetterOrDigit(ch)){
                charCount.put(ch, charCount.getOrDefault(ch,0) + 1);
            }
        }

        for (Map.Entry<Character , Integer> entry : charCount.entrySet()){
            if(entry.getValue()>1) {
                duplicates.add(entry.getKey());
            }
        }

        return duplicates;

    }

}
