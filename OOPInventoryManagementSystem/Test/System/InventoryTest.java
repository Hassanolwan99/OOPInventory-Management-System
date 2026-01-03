package System;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    
    // Helpers
    
    private Product p1() {
        // Full constructor: sku, name, category, unitPrice, quantity, min, max
        return new Product("P-001", "Hammer", "Tools", 25.0, 10, 3, 100);
    }

    private Product p2() {
        return new Product("P-002", "Screwdriver", "Tools", 15.0, 5, 5, 100);
    }

    private Inventory newInventory() {
        return new Inventory();
    }

    
    // Constructor / initial state
    
    @Test
    void constructor_startsEmpty() {
        Inventory inv = newInventory();
        assertTrue(inv.getAllProducts().isEmpty(), "Inventory should start empty");
    }

    
    // addProduct
    
    @Test
    void addProduct_valid_addsAndReturnsTrue() {
        Inventory inv = newInventory();

        assertTrue(inv.addProduct(p1()));
        assertEquals(1, inv.getAllProducts().size());
        assertNotNull(inv.findBySku("P-001"));
    }

    @Test
    void addProduct_null_returnsFalse() {
        Inventory inv = newInventory();
        assertFalse(inv.addProduct(null));
        assertEquals(0, inv.getAllProducts().size());
    }

    @Test
    void addProduct_duplicateSku_returnsFalseAndDoesNotAdd() {
        Inventory inv = newInventory();

        assertTrue(inv.addProduct(p1()));

        // same SKU different product -> should be rejected
        Product duplicate = new Product("P-001", "Another", "Tools", 30.0, 1, 0, 100);
        assertFalse(inv.addProduct(duplicate));

        assertEquals(1, inv.getAllProducts().size());
    }

    
    // findBySku
    
    @Test
    void findBySku_existing_returnsProduct() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        Product found = inv.findBySku("P-001");
        assertNotNull(found);
        assertEquals("P-001", found.getSku());
    }

    @Test
    void findBySku_trimsAndIgnoreCase() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        Product found = inv.findBySku("  p-001  ");
        assertNotNull(found);
        assertEquals("P-001", found.getSku());
    }

    @Test
    void findBySku_nullOrBlank_returnsNull() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        assertNull(inv.findBySku(null));
        assertNull(inv.findBySku("   "));
    }

    
    // removeBySku
   
    @Test
    void removeBySku_existing_removesAndReturnsTrue() {
        Inventory inv = newInventory();
        inv.addProduct(p1());
        inv.addProduct(p2());

        assertTrue(inv.removeBySku("P-001"));
        assertEquals(1, inv.getAllProducts().size());
        assertNull(inv.findBySku("P-001"));
    }

    @Test
    void removeBySku_notFound_returnsFalse() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        assertFalse(inv.removeBySku("P-999"));
        assertEquals(1, inv.getAllProducts().size());
    }

    @Test
    void removeBySku_nullOrBlank_returnsFalse() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        assertFalse(inv.removeBySku(null));
        assertFalse(inv.removeBySku("   "));
        assertEquals(1, inv.getAllProducts().size());
    }

    
    // removeProductByName / findProductByName
    
    @Test
    void findProductByName_existing_returnsProduct_ignoreCaseAndTrim() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        Product found = inv.findProductByName("  hammer ");
        assertNotNull(found);
        assertEquals("Hammer", found.getName());
    }

    @Test
    void removeProductByName_existing_removesAndReturnsTrue() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        assertTrue(inv.removeProductByName("HAMMER"));
        assertTrue(inv.getAllProducts().isEmpty());
    }

    @Test
    void removeProductByName_notFound_returnsFalse() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        assertFalse(inv.removeProductByName("Wrench"));
        assertEquals(1, inv.getAllProducts().size());
    }

    
    // increaseStock / decreaseStock
    
    @Test
    void increaseStock_existingSku_increasesQuantity() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        int before = inv.findBySku("P-001").getQuantity();
        assertTrue(inv.increaseStock("P-001", 5));
        int after = inv.findBySku("P-001").getQuantity();

        assertEquals(before + 5, after);
    }

    @Test
    void increaseStock_skuNotFound_returnsFalse() {
        Inventory inv = newInventory();
        assertFalse(inv.increaseStock("P-404", 5));
    }

    @Test
    void decreaseStock_existingSku_validAmount_decreasesQuantityAndReturnsTrue() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        int before = inv.findBySku("P-001").getQuantity();
        assertTrue(inv.decreaseStock("P-001", 3));
        int after = inv.findBySku("P-001").getQuantity();

        assertEquals(before - 3, after);
    }

    @Test
    void decreaseStock_moreThanAvailable_returnsFalseAndNoChange() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        int before = inv.findBySku("P-001").getQuantity();
        assertFalse(inv.decreaseStock("P-001", before + 1));
        int after = inv.findBySku("P-001").getQuantity();

        assertEquals(before, after);
    }

    @Test
    void decreaseStock_skuNotFound_returnsFalse() {
        Inventory inv = newInventory();
        assertFalse(inv.decreaseStock("P-404", 1));
    }

    
    // getLowStockProducts (two versions)
    
    @Test
    void getLowStockProducts_withLimit_returnsProductsAtOrBelowLimit() {
        Inventory inv = newInventory();
        inv.addProduct(p1()); // qty 10
        inv.addProduct(p2()); // qty 5

        List<Product> low = inv.getLowStockProducts(5);
        assertEquals(1, low.size());
        assertEquals("P-002", low.get(0).getSku());
    }

    @Test
    void getLowStockProducts_usesMinStockLevel() {
        Inventory inv = newInventory();

        Product p = p1();           // qty 10, min 3 => not low
        p.setMinStockLevel(15);     // now qty 10 <= min 15 => low

        inv.addProduct(p);

        List<Product> low = inv.getLowStockProducts();
        assertEquals(1, low.size());
        assertEquals("P-001", low.get(0).getSku());
    }

    
    // getTotalInventoryValue
   
    @Test
    void getTotalInventoryValue_returnsSumOfStockValues() {
        Inventory inv = newInventory();
        inv.addProduct(p1()); // 25 * 10 = 250
        inv.addProduct(p2()); // 15 * 5  = 75

        // total = 325
        assertEquals(325.0, inv.getTotalInventoryValue(), 0.0001);
    }

    
    // getAllProducts returns copy (not internal list)
    
    @Test
    void getAllProducts_returnsCopy_modifyingReturnedListDoesNotAffectInventory() {
        Inventory inv = newInventory();
        inv.addProduct(p1());

        List<Product> list = inv.getAllProducts();
        assertEquals(1, list.size());

        list.clear(); // should NOT affect inventory internal list
        assertEquals(1, inv.getAllProducts().size());
        assertNotNull(inv.findBySku("P-001"));
    }
}
