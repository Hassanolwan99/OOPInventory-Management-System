package System;

/**
 * Represents any entity that can be stored in the inventory system.
 * 
 * This interface provides a common contract for all storable items
 * such as Product or Supplier.
 */
public interface Storable {

    /**
     * @return unique identifier of the stored entity
     */
    String getSku();

    /**
     * @return display name of the stored entity
     */
    String getName();

    /**
     * @return current quantity if applicable, or -1 if not relevant
     */
    default int getQuantity() {
        return -1; // not all storables have quantity
    }

    /**
     * @return type of storable item (e.g. PRODUCT, SUPPLIER)
     */
    default String getType() {
        return this.getClass().getSimpleName();
    }
}
