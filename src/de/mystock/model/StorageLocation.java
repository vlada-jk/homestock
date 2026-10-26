package de.mystock.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Place in the household where an inventory item is stored.
 * Each constant carries a German display name that is used in the UI.
 */
public enum StorageLocation {
    FRIDGE("Kühlschrank"),
    FREEZER("Gefrierschrank"),
    PANTRY("Vorratsschrank"),
    CUPBOARD("Küchenschrank");

    /** German name shown in the UI. */
    private final String germanName;

    /**
     * Creates a storage location with its German display name.
     *
     * @param germanName the name shown in the UI
     */
    StorageLocation(String germanName) {
        this.germanName = germanName;
    }

    /**
     * Returns the German display name.
     *
     * @return the German name, for example, "Kühlschrank"
     */
    public String getGermanName() {
        return germanName;
    }

    /**
     *
     * @return the German name
     */
    @Override
    public String toString() {
        return germanName;
    }
}
