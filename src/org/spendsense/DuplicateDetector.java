package org.spendsense;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DuplicateDetector {

    public static Set<Integer> findDuplicateIds(
            List<Transaction> transactions) {

        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();

        for (Transaction transaction : transactions) {
            int id = transaction.getId();

            if (!seen.add(id)) {
                duplicates.add(id);
            }
        }

        return duplicates;
    }
}