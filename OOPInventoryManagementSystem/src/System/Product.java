package System;

public class Product implements Storable {

    // Fields (Attributes)
    private final String sku;      // Unique product code (cannot be changed)
    private String name;
    private String category;
    private double unitPrice;
    private int quantity;
    private int minStockLevel;     // Minimum quantity before we consider it "low stock"
    private int maxStockLevel;     // Optional upper limit to detect over-stock

    // Constructors

    /**
     * Full constructor with all fields.
     */
    public Product(String sku,
                   String name,
                   String category,
                   double unitPrice,
                   int quantity,
                   int minStockLevel,
                   int maxStockLevel) {

        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }
        this.sku = sku.trim();

        setName(name);
        setCategory(category);
        setUnitPrice(unitPrice);
        setQuantity(quantity);

        // Important: set max first so min can validate against it safely
        setMaxStockLevel(maxStockLevel);
        setMinStockLevel(minStockLevel);
    }

    /**
     * Simpler constructor – default category = "General",
     * minStockLevel = 0, maxStockLevel = Integer.MAX_VALUE.
     */
    public Product(String sku, String name, double unitPrice) {
        this(sku, name, "General", unitPrice, 0, 0, Integer.MAX_VALUE);
    }

    // Getters

    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public int getMinStockLevel() { return minStockLevel; }
    public int getMaxStockLevel() { return maxStockLevel; }

    // Setters (mutators) with validation

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            category = "General";
        }
        this.category = category.trim();
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void setMinStockLevel(int minStockLevel) {
        if (minStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        // NEW: keep min/max consistent
        if (minStockLevel > this.maxStockLevel) {
            throw new IllegalArgumentException("Min stock level cannot be greater than max stock level");
        }
        this.minStockLevel = minStockLevel;
    }

    public void setMaxStockLevel(int maxStockLevel) {
        // keep non-negative (optional but safer)
        if (maxStockLevel < 0) {
            throw new IllegalArgumentException("Max stock level cannot be negative");
        }
        // NEW: keep min/max consistent
        if (maxStockLevel < this.minStockLevel) {
            throw new IllegalArgumentException("Max stock level cannot be less than min stock level");
        }
        this.maxStockLevel = maxStockLevel;
    }

    // Business methods for stock management

    /**
     * Increase product quantity in stock.
     *
     * @param amount how many units to add (must be > 0)
     */
    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to increase must be positive");
        }
        this.quantity += amount;
    }

    /**
     * Decrease product quantity in stock.
     *
     * @param amount how many units to remove (must be > 0 and <= current quantity)
     * @return true if the operation succeeded, false otherwise
     */
    public boolean decreaseQuantity(int amount) {
        if (amount <= 0) return false;
        if (amount > this.quantity) return false;

        this.quantity -= amount;
        return true;
    }

    /**
     * Calculates the total value of this product in stock.
     */
    public double getStockValue() {
        return unitPrice * quantity;
    }

    /**
     * Returns true if the quantity is at or below the minimum stock level.
     */
    public boolean isLowStock() {
        return quantity <= minStockLevel;
    }

    /**
     * Returns true if the quantity is at or above the maximum stock level.
     */
    public boolean isOverstocked() {
        return quantity >= maxStockLevel;
    }

    /**
     * Applies a discount to the unit price.
     *
     * @param percent discount in percent (0–100)
     */
    public void applyDiscount(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }
        double factor = 1 - (percent / 100.0);
        double newPrice = unitPrice * factor;

        // Round to 2 decimals
        this.unitPrice = Math.round(newPrice * 100.0) / 100.0;
    }

    // Utility methods

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", minStockLevel=" + minStockLevel +
                ", maxStockLevel=" + maxStockLevel +
                '}';
    }

    // FIX: remove TODO and make it useful
    public void printInfo() {
        System.out.println(this);
    }
}
