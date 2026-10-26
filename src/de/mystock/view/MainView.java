package de.mystock.view;

import de.mystock.dao.InventoryItemDao;
import de.mystock.dao.JDBCInventoryItemDao;
import de.mystock.dao.ShoppingItemDao;
import de.mystock.service.InventoryService;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Root layout of the main window: a navigation sidebar on the left
 * and the current {@link Screen} in the center.
 * <p>
 * Navigation: "Vorrat" shows all inventory items, "Bald ablaufend" shows the
 * same {@link InventoryView} in {@link ViewMode#EXPIRING} mode,
 * "Einkaufsliste" shows the {@link ShoppingListView}.
 */
public class MainView extends BorderPane {
    /** Inventory screen, used for both "Vorrat" and "Bald ablaufend". */
    private final InventoryView inventoryView;
    /** Shopping list screen. */
    private final ShoppingListView shoppingListView;

    /** Service used by the inventory screen; works with its own {@link JDBCInventoryItemDao}. */
    private final InventoryService service = new InventoryService(new JDBCInventoryItemDao());

    /**
     * Creates the main view with the inventory screen as start screen.
     *
     * @param dao             DAO for the inventory items
     * @param shoppingItemDao DAO for the shopping list
     */
    public MainView(InventoryItemDao dao, ShoppingItemDao shoppingItemDao) {
        inventoryView = new InventoryView(dao, service);
        shoppingListView = new ShoppingListView(shoppingItemDao);
        setLeft(createSidebar());
        show(inventoryView);                      // стартовый экран
    }

    /**
     * Reloads the screen data and shows the screen in the center.
     *
     * @param screen the screen to show
     */
    private void show(Screen screen) {
        screen.refresh();
        setCenter(screen);                        // вот и всё «переключение»
    }

    /**
     * Creates the sidebar with the app title and the navigation buttons.
     * "Vorrat" is selected at start.
     *
     * @return the sidebar
     */
    private VBox createSidebar() {
        ToggleGroup group = new ToggleGroup();    // подсвечена только одна кнопка
//        ToggleButton inventory = navButton("Vorrat", group, () -> show(inventoryView));

        ToggleButton inventory = navButton("Vorrat", group, () -> {
            inventoryView.setMode(ViewMode.ALL);
            show(inventoryView);
        });
        ToggleButton expiring = navButton("Bald ablaufend", group, () -> {
            inventoryView.setMode(ViewMode.EXPIRING);
            show(inventoryView);
        });
        ToggleButton shopping  = navButton("Einkaufsliste", group, () -> show(shoppingListView));

//        ToggleButton expiring  = navButton("Bald abläuft", group, () -> show(expiringView));
        inventory.setSelected(true);

        Label title = new Label("MyStock");
        title.getStyleClass().add("app-title");

        VBox box = new VBox(8, title, inventory, expiring, shopping);
        box.getStyleClass().add("sidebar");
        return box;
    }

    /**
     * Creates a navigation button.
     *
     * @param text   the button label
     * @param group  the toggle group, so only one button is selected at a time
     * @param action what to do when the button is clicked
     * @return the button
     */
    private ToggleButton navButton(String text, ToggleGroup group, Runnable action) {
        ToggleButton b = new ToggleButton(text);
        b.setToggleGroup(group);
        b.setOnAction(e -> action.run());
        return b;
    }
}