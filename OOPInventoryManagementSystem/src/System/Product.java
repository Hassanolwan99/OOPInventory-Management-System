package System;


public class Product {

    //  Fields (Attributes) 
    private String name;
    private double price;
    private int quantity;

    //  Constructor 
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    //  Getters and Setters (Encapsulation) 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Business methods for stock management 

    // Increase product quantity in stock
    public void increaseQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        } else {
            System.out.println("Amount to increase must be positive.");
        }
    }

    // Decrease product quantity in stock 
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

    //  Method to print product information
    public void printInfo() {
        System.out.println("Product: " + name +
                           ", Price: " + price +
                           ", Quantity: " + quantity);
    }
}
