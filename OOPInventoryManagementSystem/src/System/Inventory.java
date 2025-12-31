package System;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    // List to store all products in the inventory
    private List<Product> products;

    // Constructor
    public Inventory() {
        this.products = new ArrayList<>();
    }

    // Add a new product to the inventory
    public void addProduct(Product product) {
        if (product != null) {
            products.add(product);
        }
    }

    // Remove product by name (first match)
    public boolean removeProductByName(String name) {
        Product toRemove = findProductByName(name);
        if (toRemove != null) {
            products.remove(toRemove);
            return true;
        }
        return false;
    }

    // Find product by name (returns first match or null)
    public Product findProductByName(String name) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    // Get list of products with quantity less than or equal given limit
    public List<Product> getLowStockProducts(int limit) {
        List<Product> lowStock = new ArrayList<>();
        for (Product p : products) {
            if (p.getQuantity() <= limit) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }

    // Print all products in the inventory
    public void printAllProducts() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("=== Inventory Products ===");
            for (Product p : products) {
                p.printInfo();
            }
        }
    }
}
