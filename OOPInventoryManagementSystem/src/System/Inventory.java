package System;

import java.util.ArrayList;
import java.util.List;


public class Inventory {

    // List to store all products in the inventory
    private final List<Product> products;

    // Constructor
    public Inventory() {
        this.products = new ArrayList<>();
    }

    // Add a new product to the inventory (avoid duplicate SKU)
    public boolean addProduct(Product product) {
        if (product == null) return false;

        // Prevent duplicates by SKU
        if (findBySku(product.getSku()) != null) {
            return false;
        }
        products.add(product);
        return true;
    }

    // Check if a product exists by SKU
    public boolean hasProductBySku(String sku) {
        return findBySku(sku) != null;
    }

    // Remove product by SKU (best unique key)
    public boolean removeBySku(String sku) {
        Product toRemove = findBySku(sku);
        if (toRemove != null) {
            products.remove(toRemove);
            return true;
        }
        return false;
    }

    // Find product by SKU (returns first match or null)
    public Product findBySku(String sku) {
        if (sku == null || sku.isBlank()) return null;

        for (Product p : products) {
            if (p.getSku().equalsIgnoreCase(sku.trim())) {
                return p;
            }
        }
        return null;
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
        if (name == null || name.isBlank()) return null;

        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name.trim())) {
                return p;
            }
        }
        return null;
    }

    // Increase stock by SKU (updates Product.quantity)
    public boolean increaseStock(String sku, int amount) {
        Product p = findBySku(sku);
        if (p == null) return false;
        p.increaseQuantity(amount);
        return true;
    }

    // Decrease stock by SKU (updates Product.quantity)
    public boolean decreaseStock(String sku, int amount) {
        Product p = findBySku(sku);
        if (p == null) return false;
        return p.decreaseQuantity(amount);
    }

    // Low stock list using Product's minStockLevel
    public List<Product> getLowStockProducts() {
        List<Product> lowStock = new ArrayList<>();
        for (Product p : products) {
            if (p.isLowStock()) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }

    // Low stock list using external limit
    public List<Product> getLowStockProducts(int limit) {
        List<Product> lowStock = new ArrayList<>();
        for (Product p : products) {
            if (p.getQuantity() <= limit) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }

    // Total inventory value
    public double getTotalInventoryValue() {
        double total = 0;
        for (Product p : products) {
            total += p.getStockValue();
        }
        return total;
    }

    // Return copy of all products
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // Number of products in the inventory
    public int size() {
        return products.size();
    }

    // Print all products in the inventory
    public void printAllProducts() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("=== Inventory Products ===");
            for (Product p : products) {
                System.out.println(p); // uses Product.toString()
            }
        }
    }
}
