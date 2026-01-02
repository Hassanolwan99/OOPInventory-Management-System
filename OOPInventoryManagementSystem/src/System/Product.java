package System;

public class Product {

    // Fields (Attributes)
    private String productId;
    private String name;
    private double price;
    private int quantity;
    private String category;
    private int minStockLevel;

    //  Constructor 
    public Product(String productId, String name, double price, int quantity,
                   String category, int minStockLevel) {
        this.productId = productId;
        this.name = name;
        setPrice(price);              
        setQuantity(quantity);       
        this.category = category;
        this.minStockLevel = Math.max(minStockLevel, 0);
    }

    //  Getters and Setters (Encapsulation) 
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        if (productId != null && !productId.isBlank()) {
            this.productId = productId;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            System.out.println("Price cannot be negative. Keeping old value: " + this.price);
            return;
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative. Keeping old value: " + this.quantity);
            return;
        }
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category != null && !category.isBlank()) {
            this.category = category;
        }
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(int minStockLevel) {
        if (minStockLevel >= 0) {
            this.minStockLevel = minStockLevel;
        }
    }

    //  Business methods for stock management 

    // Increase product quantity in stock
    public void increaseQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        } else {
            System.out.println("Amount to increase must be positive.");
        }
    }

    // Decrease product quantity in stock (with basic validation)
    public boolean decreaseQuantity(int amount) {
        if (amount <= 0) {
            System.out.println("Amount to decrease must be positive.");
            return false;
        }
        if (amount > this.quantity) {
            System.out.println("Not enough stock to decrease by " + amount);
            return false;
        }
        this.quantity -= amount;
        return true;
    }

    // Check if this product is low in stock
    public boolean isLowStock() {
        return this.quantity <= this.minStockLevel;
    }

    // Restock product to a specific target quantity
    public void restockTo(int targetQuantity) {
        if (targetQuantity < 0) {
            System.out.println("Target quantity cannot be negative.");
            return;
        }
        if (targetQuantity > this.quantity) {
            int diff = targetQuantity - this.quantity;
            increaseQuantity(diff);
        }
    }

    // Apply discount percentage to price 
    public void applyDiscount(double percentage) {
        if (percentage <= 0 || percentage >= 100) {
            System.out.println("Discount percentage must be between 0 and 100.");
            return;
        }
        double discountAmount = price * (percentage / 100.0);
        setPrice(price - discountAmount);
    }

    
    @Override
    public String toString() {
        return "Product {" +
                "id='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", minStockLevel=" + minStockLevel +
                '}';
    }

    // Helper method to print using toString 
    public void printInfo() {
        System.out.println(toString());
    }
}
