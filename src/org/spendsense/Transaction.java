package org.spendsense;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private String title;
    private String category;
    private double amount;
    private String type;
    private LocalDate date;

    public Transaction(int id, String title, String category,
                       double amount, String type, LocalDate date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Date: %s | %-25s | %-8s | %-12s | Rs. %.2f",
                id, date, title, type, category, amount
        );
    }
}