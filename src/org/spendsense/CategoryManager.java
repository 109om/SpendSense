package org.spendsense;

public class CategoryManager {

    public static String suggestCategory(String title) {
        String text = title.toLowerCase();

        if (containsAny(text, "grocery", "food", "restaurant",
                "pizza", "vegetable", "milk")) {
            return "Food";
        }

        if (containsAny(text, "bus", "train", "fuel",
                "petrol", "taxi", "travel", "auto")) {
            return "Travel";
        }

        if (containsAny(text, "shirt", "clothes", "shopping",
                "shoes", "dress")) {
            return "Shopping";
        }

        if (containsAny(text, "college", "book", "course",
                "fees", "education")) {
            return "Education";
        }

        if (containsAny(text, "electricity", "recharge",
                "internet", "bill", "rent")) {
            return "Bills";
        }

        if (containsAny(text, "doctor", "medicine",
                "hospital", "medical")) {
            return "Healthcare";
        }

        if (containsAny(text, "salary", "income",
                "freelance", "bonus")) {
            return "Income";
        }

        return "Other";
    }

    private static boolean containsAny(String text, String... words) {
        for (String word : words) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }
}