package org.spendsense;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpenseAnalyzer {

    public static double totalByType(
            List<Transaction> transactions, String type) {

        double total = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase(type)) {
                total += transaction.getAmount();
            }
        }

        return total;
    }

    public static Map<String, Double> categoryTotals(
            List<Transaction> transactions) {

        Map<String, Double> totals = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("EXPENSE")) {
                String category = transaction.getCategory();

                totals.put(category,
                        totals.getOrDefault(category, 0.0)
                                + transaction.getAmount());
            }
        }

        return totals;
    }

    public static Map<YearMonth, Double> monthlyTotals(
            List<Transaction> transactions) {

        Map<YearMonth, Double> totals = new TreeMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("EXPENSE")) {
                YearMonth month = YearMonth.from(transaction.getDate());

                totals.put(month,
                        totals.getOrDefault(month, 0.0)
                                + transaction.getAmount());
            }
        }

        return totals;
    }

    public static Transaction largestExpense(
            List<Transaction> transactions) {

        Transaction largest = null;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("EXPENSE")) {
                if (largest == null ||
                        transaction.getAmount() > largest.getAmount()) {
                    largest = transaction;
                }
            }
        }

        return largest;
    }

    public static String highestSpendingCategory(
            List<Transaction> transactions) {

        Map<String, Double> totals = categoryTotals(transactions);
        String highestCategory = "None";
        double highestAmount = 0;

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            if (entry.getValue() > highestAmount) {
                highestAmount = entry.getValue();
                highestCategory = entry.getKey();
            }
        }

        return highestCategory;
    }
}