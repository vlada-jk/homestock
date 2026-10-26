package de.mystock.dao;

import de.mystock.model.InventoryItem;

import java.util.List;

/**
 * Stores inventory items. All implementations (in-memory, JDBC) follow the same rules.
 */

public interface InventoryItemDao {
    /**
     * Returns all stored items.
     */
    List<InventoryItem> findAll();

    /**
     * Adds a new item and assigns its id using setId.
     * @throws IllegalArgumentException if the item already has an id
     */
    void add(InventoryItem item);

    /**
     * Saves the changes of an existing item.
     * @throws IllegalArgumentException if the item has no id or is not stored
     */
    void update(InventoryItem item);

    /** Removes the item with the given id */
    void remove(InventoryItem item);
}