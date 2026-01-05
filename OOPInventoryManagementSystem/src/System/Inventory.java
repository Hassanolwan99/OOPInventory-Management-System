package System;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory manages the product catalog in the system.
 * It supports adding/removing products, searching, updating stock,
 * and reporting low-stock items.
 *
 * Note: This class stores products in memory using a list.
 */
public class Inventory {

    // List to store all products in the inventory
    private final List<Product> products;

    // Constructor
    public Inventory() {
        this.products = new ArrayList<>();
    }

    /**
     * Add a new product to the inventory (prevents duplicate SKU).
     *
     * @param product product to add
     * @return true if added, false if null or duplicate SKU
     */
    public boolean addProduct(Product product) {
        if (product == null) return false;

        // Prevent duplicates by SKU
        if (findBySku(product.getSku()) != null) {
            return false;
        }
        products.add(product);
        return true;
    }

    /**
     * Check if a product exists by SKU.
     */
    public boolean hasProductBySku(String sku) {
        return findBySku(sku) != null;
    }

    /**
     * Remove product by SKU.
     */
    public boolean removeBySku(String sku) {
        Product toRemove = findBySku(sku);
        if (toRemove != null) {
            products.remove(toRemove);
            return true;
        }
        return false;
    }

    /**
     * Find product by SKU (returns first match or null).
     */
    public Product findBySku(String sku) {
        if (sku == null || sku.isBlank()) return null;

        for (Product p : products) {
            if (p.getSku().equalsIgnoreCase(sku.trim())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Remove product by name (first match).
     */
    public boolean removeProductByName(String name) {
        Product toRemove = findProductByName(name);
        if (toRemove != null) {
            products.remove(toRemove);
            return true;
        }
        return false;
    }

    /**
     * Find product by name (returns first match or null).
     */
    public Product findProductByName(String name) {
        if (name == null || name.isBlank()) return null;

        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name.trim())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Search products by partial name (returns all matches).
     * This fulfills the "Search product" requirement in a more practical way.
     *
     * @param keyword part of the product name
     * @return list of matching products (can be empty)
     */
    public List<Product> searchByName(String keyword) {
        List<Product> result = new ArrayList<>();
        if (keyword == null || keyword.isBlank()) return result;

        String k = keyword.trim().toLowerCase();
        for (Product p : products) {
            if (p.getName() != null && p.getName().toLowerCase().contains(k)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Search products by category (returns all matches).
     *
     * @param category category name
     * @return list of products in that category (can be empty)
     */
    public List<Product> searchByCategory(String category) {
        List<Product> result = new ArrayList<>();
        if (category == null || category.isBlank()) return result;

        String c = category.trim().toLowerCase();
        for (Product p : products) {
            if (p.getCategory() != null && p.getCategory().toLowerCase().contains(c)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Increase stock by SKU (updates Product.quantity).
     */
    public boolean increaseStock(String sku, int amount) {
        Product p = findBySku(sku);
        if (p == null) return false;
        p.increaseQuantity(amount);
        return true;
    }

    /**
     * Decrease stock by SKU (updates Product.quantity).
     */
    public boolean decreaseStock(String sku, int amount) {
        Product p = findBySku(sku);
        if (p == null) return false;
        return p.decreaseQuantity(amount);
    }

    /**
     * Update product unit price by SKU.
     *
     * @param sku product SKU
     * @param newPrice new unit price (must be >= 0)
     * @return true if updated, false if product not found
     */
    public boolean updateProductPrice(String sku, double newPrice) {
        Product p = findBySku(sku);
        if (p == null) return false;
        p.setUnitPrice(newPrice);
        return true;
    }

   
    public boolean updateMinMaxStockLevels(String sku, int min, int max) {
        Product p = findBySku(sku);
        if (p == null) return false;
        p.setMinStockLevel(min);
        p.setMaxStockLevel(max);
        return true;
    }

    /**
     * Returns products considered low stock using each product's minStockLevel.
     * This fulfills the "Display low stock items" requirement.
     */
    public List<Product> getLowStockProducts() {
        List<Product> lowStock = new ArrayList<>();
        for (Product p : products) {
            if (p.isLowStock()) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }

    /**
     * Low stock list using external limit.
     */
    public List<Product> getLowStockProducts(int limit) {
        List<Product> lowStock = new ArrayList<>();
        for (Product p : products) {
            if (p.getQuantity() <= limit) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }

    /**
     * Returns products that are completely out of stock (quantity == 0).
     *
     * @return list of out-of-stock products
     */
    public List<Product> getOutOfStockProducts() {
        List<Product> out = new ArrayList<>();
        for (Product p : products) {
            if (p.getQuantity() == 0) {
                out.add(p);
            }
        }
        return out;
    }

    /**
     * Calculates total inventory value.
     */
    public double getTotalInventoryValue() {
        double total = 0;
        for (Product p : products) {
            total += p.getStockValue();
        }
        return total;
    }

    /**
     * Return a defensive copy of all products.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Number of products in the inventory.
     */
    public int size() {
        return products.size();
    }

    /**
     * Print all products in the inventory.
     */
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
