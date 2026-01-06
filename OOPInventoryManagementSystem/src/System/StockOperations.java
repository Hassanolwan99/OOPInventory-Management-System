package System;

/**
 * Defines basic stock operations.
 * This interface abstracts stock-related behavior.
 */
public interface StockOperations {

    void addProduct(String sku, int quantity);

    void increaseStock(String sku, int quantity);

    void decreaseStock(String sku, int quantity);

    int getStock(String sku);

    boolean hasProduct(String sku);
}
