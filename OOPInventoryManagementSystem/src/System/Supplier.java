package System;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a supplier in the inventory system.
 * A supplier provides one or more products identified by their SKUs.
 */
public class Supplier {

    //  Fields (Attributes)
    private final String supplierCode;      // Unique code for this supplier (cannot change)
    private String name;                    // Supplier name
    private String phone;                   // Contact phone
    private String email;                   // Contact e-mail
    private String address;                 // Physical address
    private double rating;                  // 0.0 – 5.0
    private boolean active;                 // Is this supplier active?
    private final List<String> suppliedProductSkus;  // SKUs of products supplied

    //  Constructors 

    /**
     * Full constructor with all important supplier information.
     */
    public Supplier(String supplierCode,
                    String name,
                    String phone,
                    String email,
                    String address,
                    double rating,
                    boolean active) {

        if (supplierCode == null || supplierCode.isBlank()) {
            throw new IllegalArgumentException("Supplier code cannot be empty");
        }
        this.supplierCode = supplierCode.trim();
        this.suppliedProductSkus = new ArrayList<>();

        setName(name);
        setPhone(phone);
        setEmail(email);
        setAddress(address);
        setRating(rating);
        this.active = active;
    }

    /**
     * Simple constructor with minimal information.
     * Other fields will get default values.
     */
    public Supplier(String supplierCode, String name) {
        this(supplierCode, name,
                "", "", "",
                0.0, true);
    }

    //  Getters 

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Returns an unmodifiable copy of the supplied product SKUs.
     */
    public List<String> getSuppliedProductSkus() {
        return Collections.unmodifiableList(new ArrayList<>(suppliedProductSkus));
    }

    // === Setters with basic validation ===

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setPhone(String phone) {
        if (phone == null) {
            phone = "";
        }
        this.phone = phone.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            this.email = "";
        } else if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        } else {
            this.email = email.trim();
        }
    }

    public void setAddress(String address) {
        if (address == null) {
            address = "";
        }
        this.address = address.trim();
    }

    public void setRating(double rating) {
        if (rating < 0.0) {
            this.rating = 0.0;
        } else if (rating > 5.0) {
            this.rating = 5.0;
        } else {
            this.rating = rating;
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //  Business methods 

    /**
     * Adds a product SKU to the list of products supplied by this supplier.
     */
    public void addSuppliedProductSku(String sku) {
        if (sku == null || sku.isBlank()) {
            return;
        }
        String normalized = sku.trim();
        if (!suppliedProductSkus.contains(normalized)) {
            suppliedProductSkus.add(normalized);
        }
    }

    /**
     * Removes a product SKU from the list of products supplied by this supplier.
     *
     * @return true if the SKU was removed, false otherwise.
     */
    public boolean removeSuppliedProductSku(String sku) {
        if (sku == null) {
            return false;
        }
        return suppliedProductSkus.remove(sku.trim());
    }

    /**
     * Updates the contact information for this supplier.
     */
    public void updateContactInfo(String phone, String email, String address) {
        setPhone(phone);
        setEmail(email);
        setAddress(address);
    }

    /**
     * Deactivates this supplier (for example, no longer used).
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Reactivates this supplier.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Short one-line info, useful for lists.
     */
    public String getShortInfo() {
        return "Supplier{" +
                "code='" + supplierCode + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", active=" + active +
                '}';
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierCode='" + supplierCode + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", active=" + active +
                ", suppliedProductSkus=" + suppliedProductSkus +
                '}';
    }
}
