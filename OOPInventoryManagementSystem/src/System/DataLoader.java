package System;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataLoader {

    /**
     * Loads products from a CSV file, adds them to Inventory, and initializes StockManager quantities.
     *
     * @param filePath     path to the CSV file
     * @param inventory    Inventory instance to store loaded Product objects
     * @param stockManager StockManager instance to initialize stock quantities
     * @return list of successfully loaded (added) products
     */
    public static List<Product> loadProducts(String filePath, Inventory inventory, StockManager stockManager) {

        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }
        if (stockManager == null) {
            throw new IllegalArgumentException("StockManager cannot be null");
        }

        List<Product> loaded = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Support both "," and ";" delimiters
                String[] parts = line.split("[,;]");
                if (parts.length < 4) continue;

                String sku = parts[0].trim();
                String name = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());
                double price = Double.parseDouble(parts[3].trim());

                // Defaults for fields not included in CSV
                String category = "General";
                int minStockLevel = 0;
                int maxStockLevel = Integer.MAX_VALUE;

                Product product = new Product(
                        sku,
                        name,
                        category,
                        price,
                        quantity,
                        minStockLevel,
                        maxStockLevel
                );

                // Add to Inventory (avoid duplicates)
                boolean addedToInventory = inventory.addProduct(product);
                if (addedToInventory) {
                    loaded.add(product);
                }

                // Initialize StockManager for SKU (avoid duplicates)
                if (!stockManager.hasProduct(sku)) {
                    stockManager.addProduct(sku, quantity);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format in CSV file: " + filePath, e);
        }

        return loaded;
    }
}
