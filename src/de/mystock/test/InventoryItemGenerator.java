
package de.mystock.test;

import de.mystock.model.InventoryItem;
import de.mystock.model.Product;
import de.mystock.model.StorageLocation;
import de.mystock.model.Unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates sample inventory items for manual tests of the UI.
 */
public class InventoryItemGenerator {

    /**
     * Creates ten sample items relative to today, covering all expiration states:
     * expired, expiring today and soon, OK, and without expiration date.
     * The items have no id.
     *
     * @return a new list of sample items
     */
    public static List<InventoryItem> generate() {
        LocalDate today = LocalDate.now();

        List<InventoryItem> items = new ArrayList<>();

        items.add(new InventoryItem(
                new Product("4001", "Milch", "Weihenstephan", "Milchprodukte"),
                StorageLocation.FRIDGE, 1, Unit.LITER, 2,
                today.minusDays(2), "Bereits abgelaufen"
        ));

        items.add(new InventoryItem(
                new Product("4002", "Hähnchenbrust", "Gut & Günstig", "Fleisch"),
                StorageLocation.FRIDGE, 500, Unit.GRAM, 1,
                today.minusDays(1), "Bitte prüfen"
        ));

        items.add(new InventoryItem(
                new Product("4003", "Joghurt", "Landliebe", "Milchprodukte"),
                StorageLocation.FRIDGE, 150, Unit.GRAM, 4,
                today, "Naturjoghurt"
        ));

        items.add(new InventoryItem(
                new Product("4004", "Champignons", "Frisch & Gut", "Gemüse"),
                StorageLocation.FRIDGE, 250, Unit.GRAM, 1,
                today.plusDays(1), "Frische Champignons"
        ));

        items.add(new InventoryItem(
                new Product("4005", "Gouda", "Milram", "Käse"),
                StorageLocation.FRIDGE, 200, Unit.GRAM, 2,
                today.plusDays(3), "Scheibenkäse"
        ));

        items.add(new InventoryItem(
                new Product("4006", "Tiefkühlpizza", "Dr. Oetker", "Tiefkühl"),
                StorageLocation.FREEZER, 1, Unit.PIECE, 3,
                today.plusDays(60), null
        ));

        items.add(new InventoryItem(
                new Product("4007", "Reis", "Uncle Ben's", "Trockenprodukte"),
                StorageLocation.PANTRY, 500, Unit.GRAM, 2,
                today.plusDays(180), "Langkornreis"
        ));

        items.add(new InventoryItem(
                new Product("4008", "Olivenöl", "Bertolli", "Öl"),
                StorageLocation.CUPBOARD, 1, Unit.LITER, 1,
                today.plusDays(120), null
        ));

        items.add(new InventoryItem(
                new Product("4009", "Eier", "Freiland", "Eier"),
                StorageLocation.FRIDGE, 10, Unit.PIECE, 1,
                today.plusDays(7), "Freilandhaltung"
        ));

        items.add(new InventoryItem(
                new Product("4010", "Brot", "Bäckerei", "Backwaren"),
                StorageLocation.CUPBOARD, 500, Unit.GRAM, 1,
                null, "Kein Ablaufdatum eingetragen"
        ));

        return items;
    }
}
