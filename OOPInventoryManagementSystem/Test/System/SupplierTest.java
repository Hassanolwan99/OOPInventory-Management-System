package System;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    
    // Helpers
    
    private Supplier createValidSupplier() {
        return new Supplier("SUP-001", "ABC Supplier", "0500000000",
                "test@example.com", "Riyadh", 4.2, true);
    }

  
    // Constructor tests
    
    @Test
    void constructor_nullSupplierCode_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier(null, "Name")
        );
    }

    @Test
    void constructor_blankSupplierCode_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("   ", "Name")
        );
    }

    @Test
    void constructor_nullOrBlankName_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("SUP-1", null)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("SUP-1", "   ")
        );
    }

    @Test
    void constructor_trimsSupplierCode_andName() {
        Supplier s = new Supplier("  SUP-9  ", "  My Supplier  ");
        assertEquals("SUP-9", s.getSupplierCode());
        assertEquals("My Supplier", s.getName());
    }

    
    // Rating tests (clamping 0..5)
   
    @Test
    void setRating_belowZero_clampsToZero() {
        Supplier s = createValidSupplier();
        s.setRating(-10);
        assertEquals(0.0, s.getRating());
    }

    @Test
    void setRating_aboveFive_clampsToFive() {
        Supplier s = createValidSupplier();
        s.setRating(10);
        assertEquals(5.0, s.getRating());
    }

    @Test
    void setRating_inRange_setsExactValue() {
        Supplier s = createValidSupplier();
        s.setRating(3.5);
        assertEquals(3.5, s.getRating());
    }

    
    // Email validation tests
    
    @Test
    void setEmail_nullOrBlank_setsEmptyString() {
        Supplier s = createValidSupplier();

        s.setEmail(null);
        assertEquals("", s.getEmail());

        s.setEmail("   ");
        assertEquals("", s.getEmail());
    }

    @Test
    void setEmail_invalid_throwsException() {
        Supplier s = createValidSupplier();

        assertThrows(IllegalArgumentException.class, () -> s.setEmail("abc"));
        assertThrows(IllegalArgumentException.class, () -> s.setEmail("a@"));
        assertThrows(IllegalArgumentException.class, () -> s.setEmail("a@b"));
        assertThrows(IllegalArgumentException.class, () -> s.setEmail("a@b."));
        assertThrows(IllegalArgumentException.class, () -> s.setEmail("a b@c.com"));
    }

    @Test
    void setEmail_valid_trimsAndSets() {
        Supplier s = createValidSupplier();
        s.setEmail("  user@test.com  ");
        assertEquals("user@test.com", s.getEmail());
    }

    
    // SKU add/remove logic tests
    
    @Test
    void addSuppliedProductSku_nullOrBlank_doesNothing() {
        Supplier s = createValidSupplier();

        s.addSuppliedProductSku(null);
        s.addSuppliedProductSku("   ");

        assertTrue(s.getSuppliedProductSkus().isEmpty());
    }

    @Test
    void addSuppliedProductSku_normalizesToUppercaseAndTrims() {
        Supplier s = createValidSupplier();

        s.addSuppliedProductSku("  ab-12  ");
        List<String> skus = s.getSuppliedProductSkus();

        assertEquals(1, skus.size());
        assertEquals("AB-12", skus.get(0));
    }

    @Test
    void addSuppliedProductSku_preventsDuplicates_afterNormalization() {
        Supplier s = createValidSupplier();

        s.addSuppliedProductSku("ab-12");
        s.addSuppliedProductSku(" AB-12 ");
        s.addSuppliedProductSku("Ab-12");

        assertEquals(1, s.getSuppliedProductSkus().size());
        assertEquals("AB-12", s.getSuppliedProductSkus().get(0));
    }

    @Test
    void removeSuppliedProductSku_returnsTrueWhenRemoved() {
        Supplier s = createValidSupplier();

        s.addSuppliedProductSku("ab-12");
        assertTrue(s.removeSuppliedProductSku(" AB-12 "));

        assertTrue(s.getSuppliedProductSkus().isEmpty());
    }

    @Test
    void removeSuppliedProductSku_nullOrBlank_returnsFalse() {
        Supplier s = createValidSupplier();

        assertFalse(s.removeSuppliedProductSku(null));
        assertFalse(s.removeSuppliedProductSku("   "));
    }

    @Test
    void removeSuppliedProductSku_notFound_returnsFalse() {
        Supplier s = createValidSupplier();

        s.addSuppliedProductSku("AB-12");
        assertFalse(s.removeSuppliedProductSku("XX-99"));
        assertEquals(1, s.getSuppliedProductSkus().size());
    }

    
    // Unmodifiable list tests
    
    @Test
    void getSuppliedProductSkus_returnsUnmodifiableList() {
        Supplier s = createValidSupplier();
        s.addSuppliedProductSku("AB-12");

        List<String> view = s.getSuppliedProductSkus();

        assertThrows(UnsupportedOperationException.class, () -> view.add("NEW-SKU"));
    }

    @Test
    void getSuppliedProductSkus_returnsCopy_notSameInternalReference() {
        Supplier s = createValidSupplier();
        s.addSuppliedProductSku("AB-12");

        List<String> list1 = s.getSuppliedProductSkus();
        List<String> list2 = s.getSuppliedProductSkus();


        assertEquals(list1, list2);

        assertNotSame(list1, list2);
    }

    
    // Contact + active state tests
    
    @Test
    void updateContactInfo_updatesFields() {
        Supplier s = createValidSupplier();

        s.updateContactInfo("0555", "new@mail.com", "Jeddah");

        assertEquals("0555", s.getPhone());
        assertEquals("new@mail.com", s.getEmail());
        assertEquals("Jeddah", s.getAddress());
    }

    @Test
    void activateAndDeactivate_changesActiveState() {
        Supplier s = createValidSupplier();

        s.deactivate();
        assertFalse(s.isActive());

        s.activate();
        assertTrue(s.isActive());
    }

    
    // getShortInfo sanity test
    
    @Test
    void getShortInfo_containsKeyFields() {
        Supplier s = new Supplier("SUP-77", "X", "", "", "", 2.0, true);
        String info = s.getShortInfo();

        assertTrue(info.contains("SUP-77"));
        assertTrue(info.contains("X"));
        assertTrue(info.contains("rating=2.0"));
        assertTrue(info.contains("active=true"));
    }
}
