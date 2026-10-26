package de.mystock.model;

import javafx.scene.paint.Color;

/**
 * for Expiration View
 */


public enum ExpirationStatus {
    /** No expiration date, or it is more than InventoryItem#EXPIRING_SOON_DAYS days away. */
    OK,
    /** Expires today or within the next InventoryItem#EXPIRING_SOON_DAYS days. */
    EXPIRING_SOON,
    /** The expiration date is in the past. */
    EXPIRED;
}
//
//public enum ExpirationStatus {
//    OK(Color.TRANSPARENT),
//    EXPIRING_SOON(Color.rgb(255, 215, 0, 0.25)),
//    EXPIRED(Color.rgb(255, 0, 0, 0.25));
//
//    private final Color color;
//
//    ExpirationStatus(Color color) {
//        this.color = color;
//    }
//
//    public Color getColor() {
//        return color;
//    }
//}
