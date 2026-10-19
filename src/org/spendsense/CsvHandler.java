package org.spendsense;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CsvHandler {

    private static final Path FILE =
            Paths.get("data", "transactions.csv");

    public static void save(List<Transaction> transactions)
            throws IOException {

        Files.createDirectories(FILE.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(
                FILE, StandardCharsets.UTF_8)) {

            writer.write("id,title,category,amount,type,date");
            writer.newLine();

            for (Transaction t : transactions) {
                writer.write(
                        t.getId() + "," +
                                clean(t.getTitle()) + "," +
                                clean(t.getCategory()) + "," +
                                t.getAmount() + "," +
                                t.getType() + "," +
                                t.getDate()
                );

                writer.newLine();
            }
        }
    }

    public static List<Transaction> load() throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        if (!Files.exists(FILE)) {
            return transactions;
        }

        Set<Integer> seenIds = new java.util.HashSet<>();
        int lineNumber = 0;

        try (BufferedReader reader = Files.newBufferedReader(
                FILE, StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1 || line.isBlank()) {
                    continue;
                }

                try {
                    String[] parts = line.split(",", -1);

                    if (parts.length != 6) {
                        System.out.println(
                                "Skipping malformed CSV line: " + lineNumber);
                        continue;
                    }

                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String category = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String type = parts[4];
                    LocalDate date = LocalDate.parse(parts[5]);

                    if (id <= 0 || title.isBlank() ||
                            category.isBlank() ||
                            !Double.isFinite(amount) || amount <= 0 ||
                            (!type.equals("INCOME") &&
                                    !type.equals("EXPENSE"))) {
                        System.out.println(
                                "Skipping invalid CSV line: " + lineNumber);
                        continue;
                    }

                    if (!seenIds.add(id)) {
                        System.out.println(
                                "Skipping duplicate ID " + id +
                                        " on line " + lineNumber);
                        continue;
                    }

                    transactions.add(new Transaction(
                            id, title, category, amount, type, date));

                } catch (RuntimeException exception) {
                    System.out.println(
                            "Skipping invalid CSV line: " + lineNumber);
                }
            }
        }

        return transactions;
    }

    private static String clean(String value) {
        return value.replace(",", " ")
                .replace("\n", " ")
                .replace("\r", " ");
    }
}