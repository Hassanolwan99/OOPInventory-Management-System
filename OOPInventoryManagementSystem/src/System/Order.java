package System;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    // Fields
    private final String orderId;
    private final LocalDate orderDate;
    private OrderStatus status;
    private final List<OrderItem> items;

    // Constructor
    public Order(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        this.orderId = orderId.trim();
        this.orderDate = LocalDate.now();
        this.status = OrderStatus.NEW;
        this.items = new ArrayList<>();
    }

    // Getters
    public String getOrderId() { return orderId; }
    public LocalDate getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items); // defensive copy
    }

    // Item management
    public void addItem(Product product, int quantity) {
        ensureEditable();

        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        String sku = product.getSku().trim().toUpperCase();

        // If item already exists, increase quantity
        OrderItem existing = findItemBySku(sku);
        if (existing != null) {
            existing.increaseQuantity(quantity);
            return;
        }

        items.add(new OrderItem(
                sku,
                product.getName(),
                product.getUnitPrice(),
                quantity
        ));
    }

    public boolean removeItem(String sku) {
        ensureEditable();

        OrderItem item = findItemBySku(sku);
        if (item == null) return false;

        return items.remove(item);
    }

    public OrderItem findItemBySku(String sku) {
        if (sku == null || sku.isBlank()) return null;

        String normalized = sku.trim().toUpperCase();
        for (OrderItem item : items) {
            if (item.getSku().equalsIgnoreCase(normalized)) {
                return item;
            }
        }
        return null;
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    // Order status logic

    public void confirm() {
        if (this.status != OrderStatus.NEW) {
            throw new IllegalStateException("Only NEW orders can be confirmed");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm an empty order");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void complete() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order must be CONFIRMED before completion");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Completed order cannot be cancelled");
        }
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    // Optional lifecycle steps (because enum contains them)
    public void markPaid() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order must be CONFIRMED before PAID");
        }
        this.status = OrderStatus.PAID;
    }

    public void ship() {
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be PAID before SHIPPED");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order must be SHIPPED before DELIVERED");
        }
        this.status = OrderStatus.DELIVERED;
    }

    private void ensureEditable() {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Order can only be modified when status is NEW");
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", items=" + items +
                ", totalAmount=" + getTotalAmount() +
                '}';
    }
}
