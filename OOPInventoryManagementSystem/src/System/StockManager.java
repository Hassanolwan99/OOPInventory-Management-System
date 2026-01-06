package System;

public class StockManager implements StockOperations {

    private final Inventory inventory;

    public StockManager(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }
        this.inventory = inventory;
    }

    private String normSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }
        return sku.trim().toUpperCase();
    }

    @Override
    public void addProduct(String sku, int quantity) {
        String key = normSku(sku);
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Product p = inventory.findBySku(key);
        if (p == null) {
            throw new IllegalStateException("Product not found in inventory: " + key);
        }

        // Safer: initialize by setting only if current is 0, otherwise increase
        // (prevents accidental overwrite)
        if (p.getQuantity() == 0) {
            p.setQuantity(quantity);
        } else {
            p.increaseQuantity(quantity);
        }
    }

    /**
     * Optional helper: explicitly overwrite stock quantity (use carefully).
     */
    public void setStock(String sku, int quantity) {
        String key = normSku(sku);
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");

        Product p = inventory.findBySku(key);
        if (p == null) throw new IllegalStateException("Product not found in inventory: " + key);

        p.setQuantity(quantity);
    }

    @Override
    public void increaseStock(String sku, int quantity) {
        String key = normSku(sku);
        if (!inventory.increaseStock(key, quantity)) {
            throw new IllegalStateException("Failed to increase stock for: " + key);
        }
    }

    @Override
    public void decreaseStock(String sku, int quantity) {
        String key = normSku(sku);
        boolean ok = inventory.decreaseStock(key, quantity);
        if (!ok) {
            throw new IllegalStateException("Insufficient stock for product: " + key);
        }
    }

    @Override
    public int getStock(String sku) {
        String key = normSku(sku);
        Product p = inventory.findBySku(key);
        if (p == null) {
            throw new IllegalStateException("Product not found in inventory: " + key);
        }
        return p.getQuantity();
    }

    @Override
    public boolean hasProduct(String sku) {
        String key = normSku(sku);
        return inventory.hasProductBySku(key);
    }
}
