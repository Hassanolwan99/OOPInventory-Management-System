package System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inv;
    private Product p1;
    private Product p2;

    @BeforeEach
    void setup() {
        inv = new Inventory();
        p1 = new Product("P-001", "Hammer", "Tools", 25.0, 10, 3, 100);
        p2 = new Product("P-002", "Screwdriver", "Tools", 15.0, 5, 5, 100);
    }

    @Test
    void constructor_startsEmpty() {
        assertTrue(inv.getAllProducts().isEmpty());
    }

    @Test
    void addProduct_valid_adds_andDuplicateRejected() {
        assertTrue(inv.addProduct(p1));
        assertFalse(inv.addProduct(new Product("P-001", "Another", "Tools", 30.0, 1, 0, 100)));
        assertEquals(1, inv.getAllProducts().size());
    }

    @Test
    void findBySku_trimsAndIgnoreCase() {
        inv.addProduct(p1);
        assertNotNull(inv.findBySku("  p-001 "));
        assertEquals("P-001", inv.findBySku("p-001").getSku());
    }

    @Test
    void removeBySku_existing_removes() {
        inv.addProduct(p1);
        inv.addProduct(p2);

        assertTrue(inv.removeBySku("P-001"));
        assertNull(inv.findBySku("P-001"));
        assertEquals(1, inv.getAllProducts().size());
    }

    @Test
    void increaseAndDecreaseStock_updatesQuantity_andPreventsOverDecrease() {
        inv.addProduct(p1);

        assertTrue(inv.increaseStock("P-001", 5));
        assertEquals(15, inv.findBySku("P-001").getQuantity());

        assertTrue(inv.decreaseStock("P-001", 3));
        assertEquals(12, inv.findBySku("P-001").getQuantity());

        assertFalse(inv.decreaseStock("P-001", 999)); // cannot go below zero
        assertEquals(12, inv.findBySku("P-001").getQuantity());
    }

    @Test
    void getLowStockProducts_withLimit_returnsExpected() {
        inv.addProduct(p1); // qty 10
        inv.addProduct(p2); // qty 5

        List<Product> low = inv.getLowStockProducts(5);
        assertEquals(1, low.size());
        assertEquals("P-002", low.get(0).getSku());
    }

    @Test
    void getTotalInventoryValue_correctSum() {
        inv.addProduct(p1); // 25 * 10 = 250
        inv.addProduct(p2); // 15 * 5  = 75
        assertEquals(325.0, inv.getTotalInventoryValue(), 0.0001);
    }
}
