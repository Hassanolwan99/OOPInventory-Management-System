package System;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataLoader is responsible for loading product data from a CSV file
 * and converting it into Product objects.
 *
 * Expected CSV format (with header):
 * sku,name,quantity,price
 */
public class DataLoader {

    /**
     * Loads products from a CSV file.
     *
     * @param filePath path to the CSV file
     * @return list of Product objects
     */
    public static List<Product> loadProducts(String filePath) {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 4) {
                    continue; // skip invalid lines
                }

                String sku = parts[0].trim();
                String name = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());
                double price = Double.parseDouble(parts[3].trim());

                // Default values for missing fields
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

                products.add(product);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format in CSV file", e);
        }

        return products;
    }
}
