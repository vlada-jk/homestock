package de.mystock.model;

import java.time.LocalDate;

//Item ist Rice von Ja. 500 gr Packung

/**
 * <pre>
 *     The item is either a single package, 400g, or 2 pieces of chicken breast fillet.
 *     I can buy 2 packages of fillets with different expiration dates for use in different dishes.
 *
 *     I can marinate the chicken and keep it in the freezer.
 *
 * </pre>
 *
 */

public class InventoryItem {

    /** Database id;  */
    private Integer id;

    /** The product belongs this item */
    private Product product;

    /** Where the item is stored. */
    private StorageLocation storageLocation;

    /** Content of a single package, can be 0 */
    private double contentAmount;

    /** Unit of contentAmount */
    private Unit unit;


    /** Number of packages in stock */
    private int quantity;

    /** Expiration date, can be null */
    private LocalDate expirationDate;

    /** Optional notes, may be null */
    private String notes;

    /** Number of days ahead in which an item counts as "expiring soon". */
    public static final int EXPIRING_SOON_DAYS = 3;

    // constructors

    // with id

    /**
     * Creates an item with id
     *
     * @param id              the database id
     * @param product         the product
     * @param storageLocation where the item is stored
     * @param contentAmount   content of a single package
     * @param unit            unit of the content amount
     * @param quantity        number of packages, must be greater than 0
     * @param expirationDate  best-before date, may be null
     * @param notes           free-text notes, may be null
     * @throws IllegalArgumentException if {@code quantity <= 0} or {@code product} is {@code null}
     */
    public InventoryItem(Integer id, Product product, StorageLocation storageLocation, double contentAmount, Unit unit, int quantity, LocalDate expirationDate, String notes) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        this.id = id;
        this.product = product;
        this.storageLocation = storageLocation;
        this.contentAmount = contentAmount;
        this.unit = unit;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.notes = notes;
    }

    // without id

    /**
     * Creates a new item without id; the id is assigned later by
     * {@link de.mystock.dao.InventoryItemDao#add(InventoryItem)}.
     *
     * @param product         the product; must not be {@code null}
     * @param storageLocation where the item is stored
     * @param contentAmount   content of a single package
     * @param unit            unit of the content amount
     * @param quantity        number of packages; must be greater than 0
     * @param expirationDate  best-before date, may be {@code null}
     * @param notes           free-text notes, may be {@code null}
     * @throws IllegalArgumentException if {@code quantity <= 0} or {@code product} is {@code null}
     */
    public InventoryItem(Product product, StorageLocation storageLocation, double contentAmount, Unit unit, int quantity, LocalDate expirationDate, String notes) {
        this(null, product, storageLocation, contentAmount, unit, quantity, expirationDate, notes);

    }


    //Methoden

    /**
     * Uses up one package: decreases the quantity by one.
     * Does nothing if the quantity is already 0.
     */
    public void useOnePackage() {
        if (quantity > 0) {
            quantity--;
        }
    }

    /**
     * Increases the quantity by the given number of packages
     * @param amount number of packages to add
     */
    public void addPackages(int amount) {
        quantity += amount;
    }

    /**
     * Checks whether the other item can be merged into this one,
     * (whether its packages can simply be added to this item)
     * <p>
     * Two items can be merged only if both products have a barcode,
     * the barcodes are equal and both items are stored at the same location.
     * Items without barcode are never merged.
     *
     * @param other the item to compare with, may be null
     * @return true if both items have the same barcode and storage location
     */
    public boolean canMergeWith(InventoryItem other) {
        if (other == null || other.getProduct() == null) {
            return false;
        }
        if (product.getBarcode() == null || other.getProduct().getBarcode() == null) {
            return false;
        }
        // Maybe
        return product.getBarcode() != null && storageLocation != null && product.getBarcode().equals(other.getProduct().getBarcode()) &&
                storageLocation.equals(other.getStorageLocation());
    }



    /**
     * Checks whether all packages are used up.
     *
     * @return true if the quantity is 0
     */
    public boolean isEmpty() {
        return quantity == 0;
    }


    // Expiration date

    /**
     * Checks whether the item expires today or within the given number of days.
     * Already expired items and items without expiration date return false.
     *
     * @param days number of days from today (inclusive)
     * @return true if the expiration date is between today and today + days
     */
    public boolean isExpiringWithin(int days) {
        if (expirationDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        if (!expirationDate.isBefore(today) && !expirationDate.isAfter(today.plusDays(days))) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the expiration date is in the past.
     * An item that expires today is not expired yet.
     *
     * @return true if the expiration date is before today; false if there is no date
     */
    public boolean isExpired() {
        return expirationDate != null
                && expirationDate.isBefore(LocalDate.now());
    }

    /**
     * Returns the expiration status of this item.
     * @return the expiration status
     */
    public ExpirationStatus getExpirationStatus() {
        if (isExpired()) {
            return ExpirationStatus.EXPIRED;
        }

        if (isExpiringWithin(EXPIRING_SOON_DAYS)) {
            return ExpirationStatus.EXPIRING_SOON;
        }

        return ExpirationStatus.OK;
    }

    // setters

    /**
     * Sets the database id. Called by the DAOs after storing or loading the item.
     *
     * @param id the database id
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /** @param storageLocation the new storage location */
    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    /** @param contentAmount the new content of a single package */
    public void setContentAmount(double contentAmount) {
        this.contentAmount = contentAmount;
    }

    /** @param unit the new unit of the content amount */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }


    /** @param expirationDate the new best-before date, may be  null */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /** @param notes the new notes, may be null */
    public void setNotes(String notes) {
        this.notes = notes;
    }


    // getters


    /** @return the database id, or {@code null} if the item is not stored yet */
    public Integer getId() {
        return id;
    }

    /** @return the product, never {@code null} */
    public Product getProduct() {
        return product;
    }

    /** @return the number of packages in stock */
    public int getQuantity() {
        return quantity;
    }

    /** @return the content of a single package */
    public double getContentAmount() {
        return contentAmount;
    }

    /** @return the unit of the content amount */
    public Unit getUnit() {
        return unit;
    }


    /** @return the storage location */
    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    /** @return the best-before date, or null if unknown */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /** @return the notes, or null */
    public String getNotes() {
        return notes;
    }

    /** @return information about this item */
    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", product=" + product +
                ", storageLocation=" + storageLocation +
                ", contentAmount=" + contentAmount +
                ", unit=" + unit +
                ", quantity=" + quantity +
                ", expirationDate=" + expirationDate +
                ", notes=" + notes + '}';
    }
}
