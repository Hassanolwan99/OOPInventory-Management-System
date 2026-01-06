package System;

import java.io.*;
import java.util.List;

public class CsvStore {

    // New CSV format (7 columns):
    // sku,name,category,quantity,price,minStockLevel,maxStockLevel

    public static void ensureFileExists(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                File parent = f.getParentFile();
                if (parent != null) parent.mkdirs();

                try (PrintWriter out = new PrintWriter(new FileWriter(f))) {
                    out.println("sku,name,category,quantity,price,minStockLevel,maxStockLevel");
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot create CSV file: " + filePath, e);
            }
        }
    }

    public static void reloadProducts(String filePath, Inventory inventory) {
        if (inventory == null) throw new IllegalArgumentException("Inventory cannot be null");

        for (Product p : inventory.getAllProducts()) {
            inventory.removeBySku(p.getSku());
        }
        loadProducts(filePath, inventory);
    }

    public static void loadProducts(String filePath, Inventory inventory) {
        if (inventory == null) throw new IllegalArgumentException("Inventory cannot be null");

        ensureFileExists(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstChecked = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("[,;]");
                if (parts.length < 4) continue;

                // header detection once
                if (!firstChecked) {
                    firstChecked = true;
                    String c0 = parts[0].trim().toLowerCase();
                    if (c0.contains("sku")) continue;
                }

                try {
                    String sku = parts[0].trim();
                    String name = parts[1].trim();

                    // Backward compatible:
                    // Old file: sku,name,quantity,price (4 cols)
                    // New file: sku,name,category,quantity,price,min,max (7 cols)

                    String category;
                    int quantity;
                    double price;
                    int min;
                    int max;

                    if (parts.length >= 7) {
                        category = parts[2].trim();
                        quantity  = Integer.parseInt(parts[3].trim());
                        price     = Double.parseDouble(parts[4].trim());
                        min       = Integer.parseInt(parts[5].trim());
                        max       = Integer.parseInt(parts[6].trim());
                    } else {
                        // Old format
                        category = "General";
                        quantity = Integer.parseInt(parts[2].trim());
                        price    = Double.parseDouble(parts[3].trim());
                        min      = 0;
                        max      = Integer.MAX_VALUE;
                    }

                    Product p = new Product(sku, name, category, price, quantity, min, max);
                    inventory.addProduct(p);

                } catch (NumberFormatException ignored) {
                    // skip bad line
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV: " + filePath, e);
        }
    }

    public static void saveProducts(String filePath, Inventory inventory) {
        if (inventory == null) throw new IllegalArgumentException("Inventory cannot be null");

        ensureFileExists(filePath);

        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.println("sku,name,category,quantity,price,minStockLevel,maxStockLevel");

            for (Product p : inventory.getAllProducts()) {
                out.printf("%s,%s,%s,%d,%.2f,%d,%d%n",
                        p.getSku(),
                        escapeCsv(p.getName()),
                        escapeCsv(p.getCategory()),
                        p.getQuantity(),
                        p.getUnitPrice(),
                        p.getMinStockLevel(),
                        p.getMaxStockLevel()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving CSV: " + filePath, e);
        }
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        String t = s.replace("\"", "\"\"");
        if (t.contains(",") || t.contains("\"")) return "\"" + t + "\"";
        return t;
    }
}
