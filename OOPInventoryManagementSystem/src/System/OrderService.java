package System;

public class OrderService {

    private final StockManager stockManager;

    public OrderService(StockManager stockManager) {
        if (stockManager == null) {
            throw new IllegalArgumentException("StockManager cannot be null");
        }
        this.stockManager = stockManager;
    }

    public void placeOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place an empty order");
        }

        // 1) Ensure order is confirmable (will throw if not NEW or empty if you added that check)
        order.confirm();

        // 2) Check stock availability
        for (OrderItem item : order.getItems()) {
            int available = stockManager.getStock(item.getSku());
            if (available < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for SKU: " + item.getSku());
            }
        }

        // 3) Deduct stock
        for (OrderItem item : order.getItems()) {
            stockManager.decreaseStock(item.getSku(), item.getQuantity());
        }
    }
}
