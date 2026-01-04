package System;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

   
    // Business logic
   

    /**
     * @return true if the product is already expired
     */
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    /**
     * @return true if the product expires today
     */
    public boolean expiresToday() {
        return expiryDate.isEqual(LocalDate.now());
    }

    /**
     * @return number of days remaining until expiration
     *         (negative value means already expired)
     */
    public int daysUntilExpiry() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    /**
     * Checks if the product is close to expiration.
     *
     * @param days number of days before expiry
     */
    public boolean isNearExpiry(int days) {
        return !isExpired() && daysUntilExpiry() <= days;
    }

    /**
     * Applies a discount if the product is near expiry.
     *
     * @param daysBefore number of days before expiry to trigger discount
     * @param percent discount percentage
     */
    public void applyExpiryDiscount(int daysBefore, double percent) {
        if (isNearExpiry(daysBefore)) {
            applyDiscount(percent); // method from Product
        }
    }

   
    // Utility
    

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
