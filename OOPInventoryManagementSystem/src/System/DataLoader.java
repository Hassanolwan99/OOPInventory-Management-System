package System;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    /**
     * CSV format (recommended):
     * sku,name,quantity,price
     *
     * Supports "," or ";" delimiter.
     * Skips invalid lines safely.
     */
    public static List<Product> loadProducts(String filePath, Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }

        List<Product> loaded = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean firstLineChecked = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("[,;]");
                if (parts.length < 4) continue;

                // Smart header detection once
                if (!firstLineChecked) {
                    firstLineChecked = true;
                    String c0 = parts[0].trim().toLowerCase();
                    String c1 = parts[1].trim().toLowerCase();
                    if (c0.contains("sku") || c1.contains("name")) {
                        continue; // this is header
                    }
                }

                try {
                    String sku = parts[0].trim();
                    String name = parts[1].trim();
                    int quantity = Integer.parseInt(parts[2].trim());
                    double price = Double.parseDouble(parts[3].trim());

                    Product product = new Product(
                            sku,
                            name,
                            "General",
                            price,
                            quantity,
                            0,
                            Integer.MAX_VALUE
                    );

                    if (inventory.addProduct(product)) {
                        loaded.add(product);
                    }

                } catch (NumberFormatException ex) {
                    // skip bad line instead of failing all load
                    // (better for real-world CSVs)
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }

        return loaded;
    }
}
