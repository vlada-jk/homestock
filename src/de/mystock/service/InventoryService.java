package de.mystock.service;

import de.mystock.dao.InventoryItemDao;
import de.mystock.dao.JDBCInventoryItemDao;
import de.mystock.model.InventoryItem;

import java.util.List;

/**
 * logic for the inventory that goes beyond simple storing,
 * for example, merging a new item into an existing one.
 */
public class InventoryService {
    /** DAO used to read and store the items */
    InventoryItemDao dao = new JDBCInventoryItemDao();

    /**
     *
     * @param dao the DAO used to read and store the items
     */
    public InventoryService(InventoryItemDao dao) {
        this.dao = dao;
    }

    /**
     * Adds a new item to the inventory or merges it into an existing one.
     * <p>
     * If a stored item can be merged with the new one
     * (same barcode and storage location, see {@link InventoryItem#canMergeWith(InventoryItem)}),
     * the packages of the new item are added to the first such item and it is updated.
     * Otherwise the new item is added and gets an id.
     *
     * @param item the new item without id
     * @return always {@code true}
     */
    public boolean addOrMerge(InventoryItem item) {
        List<InventoryItem> items = dao.findAll();

        for (InventoryItem existingItem : items) {
            if (existingItem.canMergeWith(item)) {
                existingItem.addPackages(item.getQuantity());
                dao.update(existingItem);
                return true;
            }
        }

        dao.add(item);
        return true;
    }
}
