package System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    private Supplier s;

    @BeforeEach
    void setup() {
        s = new Supplier("SUP-001", "ABC Supplier", "0500000000",
                "test@example.com", "Riyadh", 4.2, true);
    }

    @Test
    void constructor_invalidCodeOrName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Supplier(null, "Name"));
        assertThrows(IllegalArgumentException.class, () -> new Supplier("   ", "Name"));
        assertThrows(IllegalArgumentException.class, () -> new Supplier("SUP-1", "   "));
    }

    @Test
    void setRating_clampsBetween0And5() {
        s.setRating(-10);
        assertEquals(0.0, s.getRating());

        s.setRating(10);
        assertEquals(5.0, s.getRating());

        s.setRating(3.5);
        assertEquals(3.5, s.getRating());
    }

    @Test
    void setEmail_blankBecomesEmpty_invalidThrows_validTrims() {
        s.setEmail("   ");
        assertEquals("", s.getEmail());

        assertThrows(IllegalArgumentException.class, () -> s.setEmail("a@b"));
        s.setEmail("  user@test.com  ");
        assertEquals("user@test.com", s.getEmail());
    }

    @Test
    void addAndRemoveSku_normalizesAndPreventsDuplicates() {
        s.addSuppliedProductSku("  ab-12  ");
        s.addSuppliedProductSku("AB-12");
        assertEquals(1, s.getSuppliedProductSkus().size());
        assertEquals("AB-12", s.getSuppliedProductSkus().get(0));

        assertTrue(s.removeSuppliedProductSku(" ab-12 "));
        assertTrue(s.getSuppliedProductSkus().isEmpty());
    }

    @Test
    void getSuppliedProductSkus_returnsUnmodifiableView() {
        s.addSuppliedProductSku("AB-12");
        List<String> view = s.getSuppliedProductSkus();
        assertThrows(UnsupportedOperationException.class, () -> view.add("NEW-SKU"));
    }

    @Test
    void updateContactInfo_updatesFields() {
        s.updateContactInfo("0555", "new@mail.com", "Jeddah");
        assertEquals("0555", s.getPhone());
        assertEquals("new@mail.com", s.getEmail());
        assertEquals("Jeddah", s.getAddress());
    }

    @Test
    void activateAndDeactivate_changesActiveState() {
        s.deactivate();
        assertFalse(s.isActive());

        s.activate();
        assertTrue(s.isActive());
    }
}
