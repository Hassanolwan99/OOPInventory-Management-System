package System;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a supplier in the inventory system.
 * A supplier provides one or more products identified by their SKUs.
 *
 * Implements Storable to satisfy the project interface requirement.
 * Here, supplierCode acts as the unique "SKU/ID" for the supplier entity.
 */
public class Supplier implements Storable {

    // Fields
    private final String supplierCode;              // Unique supplier identifier
    private String name;
    private String phone;
    private String email;
    private String address;
    private double rating;                           // 0.0 – 5.0
    private boolean active;
    private final List<String> suppliedProductSkus;  // Stored as normalized SKUs

    // Constructors

    /**
     * Full constructor.
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
     * Minimal constructor.
     */
    public Supplier(String supplierCode, String name) {
        this(supplierCode, name, "", "", "", 0.0, true);
    }

    // Storable implementation
    @Override
    public String getSku() {
        return supplierCode;
    }

    @Override
    public String getName() {
        return name;
    }

    // Additional getters

    public String getSupplierCode() {
        return supplierCode;
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
     * Returns an unmodifiable list of supplied product SKUs.
     */
    public List<String> getSuppliedProductSkus() {
        return Collections.unmodifiableList(new ArrayList<>(suppliedProductSkus));
    }

    // Setters with validation

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setPhone(String phone) {
        this.phone = (phone == null) ? "" : phone.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            this.email = "";
            return;
        }

        String trimmed = email.trim();

        if (!trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        this.email = trimmed;
    }

    public void setAddress(String address) {
        this.address = (address == null) ? "" : address.trim();
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

    // Business Logic

    private String normalizeSku(String sku) {
        return sku.trim().toUpperCase();
    }

    /**
     * Adds a product SKU supplied by this supplier.
     */
    public void addSuppliedProductSku(String sku) {
        if (sku == null || sku.isBlank()) return;

        String normalized = normalizeSku(sku);
        if (!suppliedProductSkus.contains(normalized)) {
            suppliedProductSkus.add(normalized);
        }
    }

    /**
     * Removes a supplied product SKU.
     *
     * @return true if removed successfully
     */
    public boolean removeSuppliedProductSku(String sku) {
        if (sku == null || sku.isBlank()) return false;
        return suppliedProductSkus.remove(normalizeSku(sku));
    }

    /**
     * Updates contact information.
     */
    public void updateContactInfo(String phone, String email, String address) {
        setPhone(phone);
        setEmail(email);
        setAddress(address);
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    /**
     * Short one-line description.
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
