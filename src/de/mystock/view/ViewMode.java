package de.mystock.view;

/**
 * Which items the InventoryView shows.
 */
public enum ViewMode {
    /** All items. */
    ALL,
    /** Only expired items and items expiring within EXPIRING_SOON_DAYS days. */
    EXPIRING
}