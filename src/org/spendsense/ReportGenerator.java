package org.spendsense;

import java.time.YearMonth;
import java.util.Map;
import java.util.List;

public class ReportGenerator {

    public static void generate(List<Transaction> transactions) {
        double income =
                ExpenseAnalyzer.totalByType(transactions, "INCOME");

        double expenses =
                ExpenseAnalyzer.totalByType(transactions, "EXPENSE");

        System.out.println("\n========== SPENDSENSE REPORT ==========");
        System.out.printf("Total Income   : Rs. %.2f%n", income);
        System.out.printf("Total Expenses : Rs. %.2f%n", expenses);
        System.out.printf("Balance        : Rs. %.2f%n", income - expenses);

        System.out.println("\n--- Category-wise Expenses ---");

        Map<String, Double> categories =
                ExpenseAnalyzer.categoryTotals(transactions);

        if (categories.isEmpty()) {
            System.out.println("No expense records found.");
        } else {
            categories.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue()
                            .reversed())
                    .forEach(entry -> System.out.printf(
                            "%-15s Rs. %.2f%n",
                            entry.getKey(), entry.getValue()));
        }

        System.out.println("\n--- Monthly Expenses ---");

        Map<YearMonth, Double> monthly =
                ExpenseAnalyzer.monthlyTotals(transactions);

        if (monthly.isEmpty()) {
            System.out.println("No monthly expenses found.");
        } else {
            monthly.forEach((month, amount) ->
                    System.out.printf("%s : Rs. %.2f%n", month, amount));
        }

        System.out.println("\n--- Spending Insights ---");

        System.out.println("Highest Spending Category: " +
                ExpenseAnalyzer.highestSpendingCategory(transactions));

        Transaction largest =
                ExpenseAnalyzer.largestExpense(transactions);

        if (largest != null) {
            System.out.println("Largest Expense: " +
                    largest.getTitle() + " (Rs. " +
                    String.format("%.2f", largest.getAmount()) + ")");
        } else {
            System.out.println("Largest Expense: None");
        }

        System.out.println("=======================================");
    }
}