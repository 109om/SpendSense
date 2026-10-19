package org.spendsense;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final TransactionManager manager =
            new TransactionManager();

    public static void main(String[] args) {
        loadTransactions();

        boolean running = true;

        while (running) {
            System.out.println("\n========== SPENDSENSE ==========");
            System.out.println("1. Add Transaction");
            System.out.println("2. View All Transactions");
            System.out.println("3. Search by ID");
            System.out.println("4. Search by Category");
            System.out.println("5. Update Transaction");
            System.out.println("6. Delete Transaction");
            System.out.println("7. Generate Expense Report");
            System.out.println("8. Check Duplicate IDs");
            System.out.println("9. Save Transactions");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> addTransaction();
                case 2 -> manager.displayAll();
                case 3 -> searchById();
                case 4 -> searchByCategory();
                case 5 -> updateTransaction();
                case 6 -> deleteTransaction();
                case 7 -> ReportGenerator.generate(
                        manager.getAllTransactions());
                case 8 -> checkDuplicates();
                case 9 -> saveTransactions();
                case 0 -> {
                    saveTransactions();
                    running = false;
                    System.out.println("Thank you for using SpendSense!");
                }
                default -> System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }

    private static void addTransaction() {
        System.out.println("\n--- Add Transaction ---");

        String title = readText("Enter title: ");
        String suggested = CategoryManager.suggestCategory(title);

        System.out.println("Suggested category: " + suggested);
        String category = readText(
                "Enter category (press Enter to use suggestion): ");

        if (category.isBlank()) {
            category = suggested;
        }

        String type = readType();
        double amount = readPositiveAmount();
        LocalDate date = readDate();

        Transaction transaction = new Transaction(
                manager.generateId(), title, category,
                amount, type, date);

        if (manager.addTransaction(transaction)) {
            System.out.println("Transaction added successfully.");
            saveTransactions();
        } else {
            System.out.println("Unable to add transaction.");
        }
    }

    private static void searchById() {
        int id = readInt("Enter transaction ID: ");
        Transaction transaction = manager.findById(id);

        if (transaction == null) {
            System.out.println("Transaction not found.");
        } else {
            System.out.println(transaction);
        }
    }

    private static void searchByCategory() {
        String category = readText("Enter category: ");
        List<Transaction> result =
                manager.searchByCategory(category);

        if (result.isEmpty()) {
            System.out.println("No transactions found in this category.");
        } else {
            result.forEach(System.out::println);
        }
    }

    private static void updateTransaction() {
        int id = readInt("Enter transaction ID to update: ");
        Transaction existing = manager.findById(id);

        if (existing == null) {
            System.out.println("Transaction not found.");
            return;
        }

        System.out.println("Current record: " + existing);

        String title = readText("Enter new title: ");
        String category = readText("Enter new category: ");
        double amount = readPositiveAmount();
        String type = readType();
        LocalDate date = readDate();

        if (manager.updateTransaction(
                id, title, category, amount, type, date)) {
            System.out.println("Transaction updated successfully.");
            saveTransactions();
        }
    }

    private static void deleteTransaction() {
        int id = readInt("Enter transaction ID to delete: ");

        if (manager.deleteTransaction(id)) {
            System.out.println("Transaction deleted successfully.");
            saveTransactions();
        } else {
            System.out.println("Transaction not found.");
        }
    }

    private static void checkDuplicates() {
        Set<Integer> duplicates = DuplicateDetector.findDuplicateIds(
                manager.getAllTransactions());

        if (duplicates.isEmpty()) {
            System.out.println("No duplicate transaction IDs found.");
        } else {
            System.out.println("Duplicate IDs: " + duplicates);
        }
    }

    private static void loadTransactions() {
        try {
            List<Transaction> loaded = CsvHandler.load();

            for (Transaction transaction : loaded) {
                manager.addTransaction(transaction);
            }

            System.out.println(
                    loaded.size() + " transaction(s) loaded.");
        } catch (IOException exception) {
            System.out.println("Could not load transactions: " +
                    exception.getMessage());
        }
    }

    private static void saveTransactions() {
        try {
            CsvHandler.save(manager.getAllTransactions());
        } catch (IOException exception) {
            System.out.println("Could not save transactions: " +
                    exception.getMessage());
        }
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();

            if (!value.isBlank()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    private static int readInt() {
        return readInt("");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static double readPositiveAmount() {
        while (true) {
            System.out.print("Enter amount: ");

            try {
                double amount =
                        Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(amount) && amount > 0) {
                    return amount;
                }
            } catch (NumberFormatException exception) {
                // Show the validation message below.
            }

            System.out.println("Enter a valid positive amount.");
        }
    }

    private static String readType() {
        while (true) {
            System.out.print("Enter type (INCOME/EXPENSE): ");
            String type = sc.nextLine().trim().toUpperCase();

            if (type.equals("INCOME") || type.equals("EXPENSE")) {
                return type;
            }

            System.out.println("Type must be INCOME or EXPENSE.");
        }
    }

    private static LocalDate readDate() {
        while (true) {
            System.out.print("Enter date (YYYY-MM-DD): ");

            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException exception) {
                System.out.println(
                        "Invalid date. Example: 2026-10-19");
            }
        }
    }
}