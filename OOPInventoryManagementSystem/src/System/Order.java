package System;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private final String orderId;
    private final LocalDate orderDate;
    private OrderStatus status;

    private final List<OrderItem> items;

    public Order(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        this.orderId = orderId.trim();
        this.orderDate = LocalDate.now();
        this.status = OrderStatus.NEW;
        this.items = new ArrayList<>();
    }

    public String getOrderId() { return orderId; }
    public LocalDate getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items); // defensive copy
    }

    // --------------------
    // Items logic
    // --------------------
    public void addItem(Product product, int quantity) {
        ensureEditable();

        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        String sku = product.getSku().trim().toUpperCase();

        // If SKU already exists, just increase quantity
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
        double total = 0;
        for (OrderItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    // --------------------
    // Status logic
    // --------------------
    public void cancel() {
        if (status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Completed order cannot be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void complete() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot complete an empty order");
        }
        this.status = OrderStatus.COMPLETED;
    }

    private void ensureEditable() {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Order cannot be modified unless status is NEW");
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", items=" + items +
                ", total=" + getTotalAmount() +
                '}';
    }
}
