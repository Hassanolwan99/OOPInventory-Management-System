package System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product p;

    @BeforeEach
    void setup() {
        p = new Product("P-001", "Hammer", "Tools", 25.50, 10, 3, 100);
    }

    @Test
    void constructor_full_valid_createsProduct() {
        assertEquals("P-001", p.getSku());
        assertEquals("Hammer", p.getName());
        assertEquals("Tools", p.getCategory());
        assertEquals(25.50, p.getUnitPrice());
        assertEquals(10, p.getQuantity());
        assertEquals(3, p.getMinStockLevel());
        assertEquals(100, p.getMaxStockLevel());
    }

    @Test
    void constructor_simple_setsDefaults_andTrimsSku() {
        Product p2 = new Product("  P-002  ", "Screwdriver", 9.99);

        assertEquals("P-002", p2.getSku());
        assertEquals("Screwdriver", p2.getName());
        assertEquals("General", p2.getCategory());
        assertEquals(0, p2.getQuantity());
        assertEquals(Integer.MAX_VALUE, p2.getMaxStockLevel());
    }

    @Test
    void constructor_invalidSkuOrName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("   ", "Hammer", 10.0));
        assertThrows(IllegalArgumentException.class,
                () -> new Product("P-003", "   ", 10.0));
    }

    @Test
    void setters_validation_negativeValues_throwException() {
        assertThrows(IllegalArgumentException.class, () -> p.setUnitPrice(-1));
        assertThrows(IllegalArgumentException.class, () -> p.setQuantity(-5));
        assertThrows(IllegalArgumentException.class, () -> p.setMinStockLevel(-1));

        p.setMinStockLevel(10);
        assertThrows(IllegalArgumentException.class, () -> p.setMaxStockLevel(5));
    }

    @Test
    void increaseAndDecreaseQuantity_workCorrectly() {
        p.increaseQuantity(5);
        assertEquals(15, p.getQuantity());

        assertTrue(p.decreaseQuantity(4));
        assertEquals(11, p.getQuantity());

        assertFalse(p.decreaseQuantity(999)); // cannot over-decrease
        assertEquals(11, p.getQuantity());
    }

    @Test
    void lowStock_and_stockValue_areCorrect() {
        assertEquals(25.50 * 10, p.getStockValue());
        assertFalse(p.isLowStock());

        p.setMinStockLevel(10);
        assertTrue(p.isLowStock());
    }

    @Test
    void applyDiscount_validAndInvalid() {
        p.applyDiscount(10); // 25.50 -> 22.95
        assertEquals(22.95, p.getUnitPrice());

        assertThrows(IllegalArgumentException.class, () -> p.applyDiscount(-1));
        assertThrows(IllegalArgumentException.class, () -> p.applyDiscount(101));
    }
}
