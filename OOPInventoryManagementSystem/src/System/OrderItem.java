package System;

public class OrderItem {

    private final String sku;
    private final String name;
    private final double unitPrice;  // snapshot at order time
    private int quantity;

    public OrderItem(String sku, String name, double unitPrice, int quantity) {
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("SKU cannot be empty");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        if (unitPrice < 0) throw new IllegalArgumentException("Unit price cannot be negative");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        this.sku = sku.trim().toUpperCase();
        this.name = name.trim();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    public void increaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.quantity += amount;
    }

    public boolean decreaseQuantity(int amount) {
        if (amount <= 0) return false;
        if (amount > this.quantity) return false;
        this.quantity -= amount;
        return true;
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", lineTotal=" + getLineTotal() +
                '}';
    }
}
