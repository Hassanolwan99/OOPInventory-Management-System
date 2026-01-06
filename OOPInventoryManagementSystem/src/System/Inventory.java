package System;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory manages products and suppliers in the system.
 * It supports adding/removing products, searching, updating stock,
 * reporting low-stock items, and linking products to suppliers.
 *
 * Note: This class stores data in memory using lists.
 */
public class Inventory {

    // List to store all products in the inventory
    private final List<Product> products;

    // List to store all suppliers
    private final List<Supplier> suppliers;

    // Constructor
    public Inventory() {
        this.products = new ArrayList<>();
        this.suppliers = new ArrayList<>();
    }

    /* =========================
       Products
       ========================= */

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
     * Also unlinks this product SKU from all suppliers.
     */
    public boolean removeBySku(String sku) {
        Product toRemove = findBySku(sku);
        if (toRemove != null) {
            products.remove(toRemove);

            // unlink product from suppliers
            unlinkProductFromAllSuppliers(toRemove.getSku());

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
     * Also unlinks this product SKU from all suppliers.
     */
    public boolean removeProductByName(String name) {
        Product toRemove = findProductByName(name);
        if (toRemove != null) {
            products.remove(toRemove);

            // unlink product from suppliers
            unlinkProductFromAllSuppliers(toRemove.getSku());

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
     */
    public boolean updateProductPrice(String sku, double newPrice) {
        Product p = findBySku(sku);
        if (p == null) return false;
        p.setUnitPrice(newPrice);
        return true;
    }

    /**
     * Update minimum and maximum stock levels for a product.
     */
    public boolean updateMinMaxStockLevels(String sku, int min, int max) {
        Product p = findBySku(sku);
        if (p == null) return false;
        p.setMinStockLevel(min);
        p.setMaxStockLevel(max);
        return true;
    }

    /**
     * Returns products considered low stock using each product's minStockLevel.
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
                System.out.println(p);
            }
        }
    }

    /* =========================
       Suppliers (NEW)
       ========================= */

    /**
     * Add a supplier (prevents duplicate supplierCode).
     *
     * @return true if added, false otherwise
     */
    public boolean addSupplier(Supplier supplier) {
        if (supplier == null) return false;

        if (findSupplierByCode(supplier.getSupplierCode()) != null) {
            return false;
        }
        suppliers.add(supplier);
        return true;
    }

    /**
     * Find supplier by code (returns first match or null).
     */
    public Supplier findSupplierByCode(String supplierCode) {
        if (supplierCode == null || supplierCode.isBlank()) return null;

        String code = supplierCode.trim();
        for (Supplier s : suppliers) {
            if (s.getSupplierCode().equalsIgnoreCase(code)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Return defensive copy of suppliers.
     */
    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers);
    }

    /**
     * Remove supplier by code (does not remove products).
     */
    public boolean removeSupplierByCode(String supplierCode) {
        Supplier s = findSupplierByCode(supplierCode);
        if (s == null) return false;
        return suppliers.remove(s);
    }

    /**
     * Links a product SKU to a supplier code.
     * The relationship is stored inside Supplier.suppliedProductSkus.
     *
     * @return true if product and supplier exist and linking succeeded
     */
    public boolean assignSupplierToProduct(String supplierCode, String productSku) {
        Supplier supplier = findSupplierByCode(supplierCode);
        if (supplier == null) return false;

        Product product = findBySku(productSku);
        if (product == null) return false;

        supplier.addSuppliedProductSku(product.getSku());
        return true;
    }

    /**
     * Returns all products supplied by the given supplier code.
     */
    public List<Product> getProductsBySupplier(String supplierCode) {
        List<Product> result = new ArrayList<>();

        Supplier supplier = findSupplierByCode(supplierCode);
        if (supplier == null) return result;

        for (String sku : supplier.getSuppliedProductSkus()) {
            Product p = findBySku(sku);
            if (p != null) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Helper: removes a product SKU from all suppliers.
     * Called automatically when a product is deleted.
     */
    private void unlinkProductFromAllSuppliers(String sku) {
        if (sku == null || sku.isBlank()) return;

        for (Supplier s : suppliers) {
            s.removeSuppliedProductSku(sku);
        }
    }
}
