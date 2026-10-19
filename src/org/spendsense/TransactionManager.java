package org.spendsense;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionManager {

    private final ArrayList<Transaction> transactions =
            new ArrayList<>();

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public int generateId() {
        int maxId = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getId() > maxId) {
                maxId = transaction.getId();
            }
        }

        return maxId + 1;
    }

    public boolean addTransaction(Transaction transaction) {
        for (Transaction existing : transactions) {
            if (existing.getId() == transaction.getId()) {
                return false;
            }
        }

        transactions.add(transaction);
        return true;
    }

    public void displayAll() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate)
                        .reversed())
                .forEach(System.out::println);
    }

    public Transaction findById(int id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }

        return null;
    }

    public List<Transaction> searchByCategory(String category) {
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getCategory()
                    .equalsIgnoreCase(category)) {
                result.add(transaction);
            }
        }

        return result;
    }

    public boolean updateTransaction(
            int id, String title, String category,
            double amount, String type, LocalDate date) {

        Transaction transaction = findById(id);

        if (transaction == null) {
            return false;
        }

        transaction.setTitle(title);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDate(date);

        return true;
    }

    public boolean deleteTransaction(int id) {
        Transaction transaction = findById(id);

        if (transaction == null) {
            return false;
        }

        transactions.remove(transaction);
        return true;
    }

    public void clearTransactions() {
        transactions.clear();
    }
}