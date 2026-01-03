package System;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    
    // Helpers
    
    private Product createValidProduct() {
        return new Product("P-001", "Hammer", "Tools", 25.50, 10, 3, 100);
    }

   
    // Constructor tests
   

    @Test
    void constructor_full_valid_createsProduct() {
        Product p = createValidProduct();

        assertEquals("P-001", p.getSku());
        assertEquals("Hammer", p.getName());
        assertEquals("Tools", p.getCategory());
        assertEquals(25.50, p.getUnitPrice());
        assertEquals(10, p.getQuantity());
        assertEquals(3, p.getMinStockLevel());
        assertEquals(100, p.getMaxStockLevel());
    }

    @Test
    void constructor_simple_setsDefaults() {
        Product p = new Product("P-002", "Screwdriver", 9.99);

        assertEquals("P-002", p.getSku());
        assertEquals("Screwdriver", p.getName());
        assertEquals("General", p.getCategory());
        assertEquals(9.99, p.getUnitPrice());
        assertEquals(0, p.getQuantity());
        assertEquals(0, p.getMinStockLevel());
        assertEquals(Integer.MAX_VALUE, p.getMaxStockLevel());
    }

    @Test
    void constructor_nullOrBlankSku_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product(null, "Hammer", "Tools", 25, 1, 0, 10)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Product("   ", "Hammer", "Tools", 25, 1, 0, 10)
        );
    }

    @Test
    void constructor_sku_trimsSpaces() {
        Product p = new Product("  P-003  ", "Wrench", 12.0);
        assertEquals("P-003", p.getSku());
    }

    @Test
    void constructor_nullOrBlankName_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product("P-004", null, 10.0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Product("P-004", "   ", 10.0)
        );
    }

    
    // Setters validation tests
    

    @Test
    void setCategory_nullOrBlank_setsGeneral() {
        Product p = createValidProduct();

        p.setCategory(null);
        assertEquals("General", p.getCategory());

        p.setCategory("   ");
        assertEquals("General", p.getCategory());
    }

    @Test
    void setUnitPrice_negative_throwsException() {
        Product p = createValidProduct();
        assertThrows(IllegalArgumentException.class, () -> p.setUnitPrice(-1));
    }

    @Test
    void setQuantity_negative_throwsException() {
        Product p = createValidProduct();
        assertThrows(IllegalArgumentException.class, () -> p.setQuantity(-5));
    }

    @Test
    void setMinStockLevel_negative_throwsException() {
        Product p = createValidProduct();
        assertThrows(IllegalArgumentException.class, () -> p.setMinStockLevel(-1));
    }

    @Test
    void setMaxStockLevel_lessThanMin_throwsException() {
        Product p = createValidProduct();
        p.setMinStockLevel(10);
        assertThrows(IllegalArgumentException.class, () -> p.setMaxStockLevel(5));
    }

    
    // Business methods tests
    

    @Test
    void increaseQuantity_positive_increases() {
        Product p = createValidProduct();
        p.increaseQuantity(5);
        assertEquals(15, p.getQuantity());
    }

    @Test
    void increaseQuantity_zeroOrNegative_throwsException() {
        Product p = createValidProduct();
        assertThrows(IllegalArgumentException.class, () -> p.increaseQuantity(0));
        assertThrows(IllegalArgumentException.class, () -> p.increaseQuantity(-2));
    }

    @Test
    void decreaseQuantity_valid_decreasesAndReturnsTrue() {
        Product p = createValidProduct();
        boolean ok = p.decreaseQuantity(4);

        assertTrue(ok);
        assertEquals(6, p.getQuantity());
    }

    @Test
    void decreaseQuantity_zeroOrNegative_returnsFalseAndNoChange() {
        Product p = createValidProduct();
        assertFalse(p.decreaseQuantity(0));
        assertFalse(p.decreaseQuantity(-1));
        assertEquals(10, p.getQuantity());
    }

    @Test
    void decreaseQuantity_moreThanAvailable_returnsFalseAndNoChange() {
        Product p = createValidProduct();
        assertFalse(p.decreaseQuantity(999));
        assertEquals(10, p.getQuantity());
    }

    @Test
    void getStockValue_returnsUnitPriceTimesQuantity() {
        Product p = new Product("P-010", "Glue", "Tools", 2.5, 4, 0, 100);
        assertEquals(10.0, p.getStockValue());
    }

    @Test
    void isLowStock_trueWhenQuantityAtOrBelowMin() {
        Product p = new Product("P-011", "Tape", "Tools", 1.0, 3, 3, 100);
        assertTrue(p.isLowStock());

        p.setQuantity(2);
        assertTrue(p.isLowStock());
    }

    @Test
    void isLowStock_falseWhenAboveMin() {
        Product p = new Product("P-012", "Nails", "Tools", 1.0, 4, 3, 100);
        assertFalse(p.isLowStock());
    }

    @Test
    void isOverstocked_trueWhenQuantityAtOrAboveMax() {
        Product p = new Product("P-013", "Bolts", "Tools", 1.0, 100, 0, 100);
        assertTrue(p.isOverstocked());

        p.setQuantity(101);
        assertTrue(p.isOverstocked());
    }

    @Test
    void isOverstocked_falseWhenBelowMax() {
        Product p = new Product("P-014", "Washers", "Tools", 1.0, 99, 0, 100);
        assertFalse(p.isOverstocked());
    }

    @Test
    void applyDiscount_valid_updatesPriceRoundedTo2Decimals() {
        Product p = new Product("P-015", "Marker", "Stationery", 10.00, 0, 0, 100);
        p.applyDiscount(10); // 10% => 9.00
        assertEquals(9.00, p.getUnitPrice());
    }

    @Test
    void applyDiscount_invalidPercent_throwsException() {
        Product p = createValidProduct();
        assertThrows(IllegalArgumentException.class, () -> p.applyDiscount(-1));
        assertThrows(IllegalArgumentException.class, () -> p.applyDiscount(101));
    }

    
    // Utility tests
    

    @Test
    void toString_containsKeyFields() {
        Product p = createValidProduct();
        String s = p.toString();

        assertTrue(s.contains("P-001"));
        assertTrue(s.contains("Hammer"));
        assertTrue(s.contains("unitPrice="));
        assertTrue(s.contains("quantity="));
    }
}
