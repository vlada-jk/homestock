package de.mystock.test;

import de.mystock.dao.InMemoryInventoryItemDao;
import de.mystock.dao.InventoryItemDao;
import de.mystock.model.InventoryItem;
import de.mystock.model.Product;
import de.mystock.model.StorageLocation;
import de.mystock.model.Unit;
import de.mystock.model.ExpirationStatus;

import java.time.LocalDate;

/**
 * Simple self-made test runner for the model classes and InMemoryInventoryItemDao
 * Every check prints "OK" or "FAIL"; the number of failed checks
 * is printed at the end.
 */
public class ModelAndDaoCheck {

    /** Number of failed checks so far. */
    private static int failed = 0;

    // prints OK or FAIL and counts failures

    /**
     * @param name      description of the check
     * @param condition {@code true} if the check passed
     */
    private static void check(String name, boolean condition) {
        if (condition) {
            System.out.println("OK   " + name);
        } else {
            System.out.println("FAIL " + name);
            failed++;
        }
    }

    // passes only if the action throws IllegalArgumentException

    /**
     * @param name   description of the check
     * @param action code that is expected to throw {@link IllegalArgumentException}
     */
    private static void checkThrows(String name, Runnable action) {
        boolean thrown = false;
        try {
            action.run();
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(name, thrown);
    }

    // passes only if the action throws nothing

    /**
     * @param name   description of the check
     * @param action code that is expected to throw no {@link RuntimeException}
     */
    private static void checkDoesNotThrow(String name, Runnable action) {
        boolean thrown = false;
        try {
            action.run();
        } catch (RuntimeException e) {
            thrown = true;
        }
        check(name, !thrown);
    }

    // a fresh rice item without id

    /**
     * @param quantity number of packages
     * @return a new rice item without id
     * @throws IllegalArgumentException if {@code quantity <= 0}
     */
    private static InventoryItem newItem(int quantity) {
        Product rice = new Product("4012345", "Reis", "JA", "Reis und andere");
        return new InventoryItem(rice, StorageLocation.CUPBOARD, 500, Unit.GRAM, quantity,
                LocalDate.of(2026, 12, 1), "");
    }

    /**
     * Runs all checks for model, DAO and expiration logic.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {

        // ---------- model ----------

        {
            InventoryItem item = newItem(3);
            item.useOnePackage();
            check("useOnePackage: 3 -> 2", item.getQuantity() == 2);
            check("not empty at 2", !item.isEmpty());
        }
        {
            InventoryItem item = newItem(1);
            item.useOnePackage();
            check("useOnePackage: 1 -> 0", item.getQuantity() == 0);
            check("empty at 0", item.isEmpty());
            item.useOnePackage();
            check("useOnePackage at 0 stays 0", item.getQuantity() == 0);
        }
        checkThrows("constructor: quantity 0 throws", () -> newItem(0));
        checkThrows("constructor: quantity -1 throws", () -> newItem(-1));
        checkThrows("constructor: product null throws", () ->
                new InventoryItem(null, StorageLocation.CUPBOARD, 500, Unit.GRAM, 1,
                        LocalDate.of(2026, 12, 1), ""));
        checkThrows("product: blank name throws", () ->
                new Product("4012345", " ", "JA", "Reis und andere"));

        // ---------- dao ----------

        {
            InventoryItemDao dao = new InMemoryInventoryItemDao();
            InventoryItem first = newItem(1);
            InventoryItem second = newItem(1);
            check("id is null before add", first.getId() == null);
            dao.add(first);
            dao.add(second);
            check("first id is 1", Integer.valueOf(1).equals(first.getId()));
            check("second id is 2", Integer.valueOf(2).equals(second.getId()));
            check("findAll has 2 items", dao.findAll().size() == 2);
        }
        {
            InventoryItemDao dao = new InMemoryInventoryItemDao();
            InventoryItem item = newItem(1);
            dao.add(item);
            checkThrows("add twice throws", () -> dao.add(item));
        }
        {
            InventoryItemDao dao = new InMemoryInventoryItemDao();
            dao.add(newItem(1));
            dao.findAll().clear();
            check("findAll returns a copy", dao.findAll().size() == 1);
        }
        {
            InventoryItemDao dao = new InMemoryInventoryItemDao();
            InventoryItem first = newItem(1);
            dao.add(first);
            dao.add(newItem(1));
//            dao.remove(first.getId());
            check("remove: 1 item left", dao.findAll().size() == 1);
//            checkDoesNotThrow("remove unknown id does nothing", () -> dao.remove(999));
            check("still 1 item after remove(999)", dao.findAll().size() == 1);
        }
        {
            InventoryItemDao dao = new InMemoryInventoryItemDao();
            InventoryItem notStored = newItem(1);
            checkThrows("update without id throws", () -> dao.update(notStored));

            InventoryItem ghost = newItem(1);
            ghost.setId(999);
            checkThrows("update with unknown id throws", () -> dao.update(ghost));
        }
        {
            InventoryItemDao dao = new InMemoryInventoryItemDao();
            InventoryItem item = newItem(3);
            dao.add(item);
            item.useOnePackage();
            dao.update(item);
            check("update keeps the new quantity", dao.findAll().get(0).getQuantity() == 2);
        }
        {
            InventoryItemDao dao = new InMemoryInventoryItemDao();
            InventoryItem original = newItem(1);
            dao.add(original);

            Product milk = new Product("999", "Milch", "Ja", "Milchprodukte");
            InventoryItem edited = new InventoryItem(original.getId(), milk, StorageLocation.CUPBOARD,
                    1000, Unit.GRAM, 2, LocalDate.of(2026, 12, 1), "");
            dao.update(edited);

            check("update replaces the data",
                    dao.findAll().size() == 1 && "Milch".equals(dao.findAll().get(0).getProduct().getName()));
            check("update keeps the id", original.getId().equals(dao.findAll().get(0).getId()));
        }

        System.out.println();

        // ---------- expiration ----------
        LocalDate today = LocalDate.now();
        int soon = InventoryItem.EXPIRING_SOON_DAYS;

        check("isExpired: yesterday", newItemExpiring(today.minusDays(1)).isExpired());
        check("isExpired: today is not expired", !newItemExpiring(today).isExpired());
        check("isExpiringWithin: no date", !newItemExpiring(null).isExpiringWithin(soon));

        check("status: yesterday is EXPIRED",
                newItemExpiring(today.minusDays(1)).getExpirationStatus() == ExpirationStatus.EXPIRED);
        check("status: today is EXPIRING_SOON",
                newItemExpiring(today).getExpirationStatus() == ExpirationStatus.EXPIRING_SOON);
        check("status: last 'soon' day is EXPIRING_SOON",
                newItemExpiring(today.plusDays(soon)).getExpirationStatus() == ExpirationStatus.EXPIRING_SOON);
        check("status: one day later is OK",
                newItemExpiring(today.plusDays(soon + 1)).getExpirationStatus() == ExpirationStatus.OK);
        check("status: no date is OK",
                newItemExpiring(null).getExpirationStatus() == ExpirationStatus.OK);

        System.out.println("Failed checks: " + failed);

    }

    // an item with the given expiration date (can be null)

    /**
     * @param date the expiration date, may be {@code null}
     * @return a new rice item with one package and the given expiration date
     */
    private static InventoryItem newItemExpiring(LocalDate date) {
        Product rice = new Product("4012345", "Reis", "JA", "Reis und andere");
        return new InventoryItem(rice, StorageLocation.CUPBOARD, 500, Unit.GRAM, 1, date, "");
    }
}