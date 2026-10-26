package de.mystock.model;

import javafx.beans.property.Property;

/**
 * Entry on the shopping list: a product, how many of it to buy
 * and whether it has already been bought.
 */

public class ShoppingItem {
    /** Database id; null until the item is stored by a DAO. */
    private Integer id;
    /** The product to buy; never null. */
    private Product product;
    /** How many to buy; always greater than 0. */
    private int quantity;
    /** true once the item has been bought; false for new items. */
    private boolean bought;

    // constructors

    /**
     * Creates a shopping list entry with a known id, which is not bought yet.
     *
     * @param id       the database id, may be null
     * @param product  the product; must not be null
     * @param quantity how many to buy; must be greater than 0
     * @throws IllegalArgumentException if quantity <= 0 or product is null
     */
    public ShoppingItem(Integer id, Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }

        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Creates a new shopping list entry without id; the id is assigned later by
     *
     * @param product  the product; must not be null
     * @param quantity how many to buy; must be greater than 0
     * @throws IllegalArgumentException if quantity <= 0 or product is null
     */
    public ShoppingItem(Product product, int quantity) {
        this(null, product, quantity);
    }



    // methods

    /** @return true if the item has already been bought */
    public boolean isBought() {
        return bought;
    }

    /**
     * Changes the quantity to buy.

     * @param newValue the new quantity; must be greater than 0
     */
    public void setQuantity(int newValue) {
        if (newValue <= 0) {
            new IllegalArgumentException("Quantity must be greater than 0").printStackTrace();
            return;
        }
        this.quantity = newValue;
    }



    // setters

    /**
     * Sets the database id.
     * @param id the database id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @param bought true if the item has been bought */
    public void setBought(boolean bought) {
        this.bought = bought;
    }
    // getters

    /** @return the database id */
    public Integer getId() {
        return id;
    }


    /** @return the product */
    public Product getProduct() {
        return product;
    }

    /** @return how many to buy */
    public int getQuantity() {
        return quantity;
    }




}
