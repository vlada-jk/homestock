package de.mystock.dao;

import de.mystock.model.InventoryItem;
import de.mystock.test.InventoryItemGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  that keeps the items in a map in memory.
 *  used for tests and without a database.
 */
public class InMemoryInventoryItemDao implements InventoryItemDao {

    /** Stored items by their id. */
    private Map<Integer, InventoryItem> items = new HashMap<>();
    /** ids start at 1 */
    private int nextId = 1;

    // Generator for test data

//    public InMemoryInventoryItemDao() {
//        List<InventoryItem> testItems =
//                InventoryItemGenerator.generate();
//
//        for (InventoryItem item : testItems) {
//            add(item);
//        }
//    }

    /** finds all items */
    @Override
    public List<InventoryItem> findAll() {
        return new ArrayList<>(items.values());
    }

    /** adds an item in an list */
    @Override
    public void add(InventoryItem item) {
        if (item.getId() != null) {
            throw new IllegalArgumentException("Item already has an id");
        } else {
            item.setId(nextId++);
            items.put(item.getId(), item);
        }

    }

    /** The stored object is replaced with the given one. */
    @Override
    public void update(InventoryItem item) {
        if (item.getId() == null || !items.containsKey(item.getId())) {
            throw new IllegalArgumentException("Item has no id or is not stored");
        } else {
            items.put(item.getId(), item);
        }
    }

    /** removes an item from a list */
    @Override
    public void remove(InventoryItem item) {
        items.remove(item);
    }
}
