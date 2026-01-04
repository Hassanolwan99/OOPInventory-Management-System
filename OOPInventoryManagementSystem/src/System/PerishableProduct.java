package System;

import java.time.LocalDate;

public class PerishableProduct extends Product {

    // Extra field for perishable items
    private LocalDate expiryDate;

    /**
     * Full constructor for perishable product.
     */
    public PerishableProduct(String sku,
                             String name,
                             String category,
                             double unitPrice,
                             int quantity,
                             int minStockLevel,
                             int maxStockLevel,
                             LocalDate expiryDate) {

        super(sku, name, category, unitPrice, quantity, minStockLevel, maxStockLevel);
        setExpiryDate(expiryDate);
    }

    /**
     * Simple constructor: category=General, qty=0, min=0, max=MAX
     */
    public PerishableProduct(String sku, String name, double unitPrice, LocalDate expiryDate) {
        super(sku, name, unitPrice);
        setExpiryDate(expiryDate);
    }

    // Getter
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    // Setter with validation
    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate == null) {
            throw new IllegalArgumentException("Expiry date cannot be null");
        }
        this.expiryDate = expiryDate;
    }

    // Business methods
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public boolean expiresToday() {
        return expiryDate.isEqual(LocalDate.now());
    }

    @Override
    public String toString() {
        return "PerishableProduct{" +
                "sku='" + getSku() + '\'' +
                ", name='" + getName() + '\'' +
                ", category='" + getCategory() + '\'' +
                ", unitPrice=" + getUnitPrice() +
                ", quantity=" + getQuantity() +
                ", minStockLevel=" + getMinStockLevel() +
                ", maxStockLevel=" + getMaxStockLevel() +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
