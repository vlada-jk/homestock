package de.mystock.dao;

import de.mystock.model.ShoppingItem;

import java.util.List;

/**
 * Stores the entries of the shopping list.
 */
public interface ShoppingItemDao {
    /**
     * Returns all stored items. Changing the returned list does not change the storage.
     *
     * @return all shopping list entries, never {@code null}
     */
    List<ShoppingItem> findAll();

    /**
     * Adds a new item and assigns its id using setId.
     * @throws IllegalArgumentException if the item already has an id
     */
    void add(ShoppingItem item);

    /**
     * Saves the changes of an existing item.
     * @throws IllegalArgumentException if the item has no id or is not stored
     */
    void update(ShoppingItem item);

    /** Removes the item with the given id. Does nothing if there is no such item. */
    void remove(ShoppingItem item);
}
