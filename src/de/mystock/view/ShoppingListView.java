package de.mystock.view;

import de.mystock.dao.ShoppingItemDao;
import de.mystock.model.Product;
import de.mystock.model.ShoppingItem;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * Shopping list screen: a table of entries to buy.
 */

public class ShoppingListView extends Screen {
    /** DAO for the shopping list entries. */
    private final ShoppingItemDao dao;
    /** All entries loaded from the DAO. */
    private final ObservableList<ShoppingItem> items = FXCollections.observableArrayList();
    /** Table showing the entries. */
    private final TableView<ShoppingItem> table = new TableView<>();
//    private final TextField input = new TextField();

    /**
     * Creates the shopping list screen.
     *
     * @param dao DAO for the shopping list entries
     */
    public ShoppingListView(ShoppingItemDao dao) {
        this.dao = dao;
        setSpacing(12);
        getStyleClass().add("screen");

        setupTable();
        getChildren().addAll(createInputRow(), table, createButtons());
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    /**
     * Creates the row above the table with the "+ Hinzufügen" button.
     *
     * @return the input row
     */
    private HBox createInputRow() {
//        input.setPromptText("Artikel hinzufügen…");
//        input.setOnAction(e -> addItem());              // Enter

        Button addButton = new Button("+ Hinzufügen");
        addButton.setOnAction(e -> addItem());

//        HBox row = new HBox(8, input, addButton);
//        HBox.setHgrow(input, Priority.ALWAYS);
        HBox row = new HBox(8, addButton);
//        HBox.setHgrow(input, Priority.ALWAYS);
        return row;
    }

    /**
     * Creates the button bar below the table.
     *
     * @return the button bar
     */
    private HBox createButtons() {
        Button minusButton = new Button("−");
        Button plusButton = new Button("+");
        Button boughtButton = new Button("Gekauft");
        Button deleteButton = new Button("Löschen");
        Button clearBoughtButton = new Button("Gekaufte entfernen");

        // these four work with the selected row, so they are disabled without selection
        minusButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        plusButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        boughtButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        minusButton.setOnAction(e -> changeQuantity(-1));
        plusButton.setOnAction(e -> changeQuantity(1));
        boughtButton.setOnAction(e -> toggleBought());
        deleteButton.setOnAction(e -> deleteSelected());
        clearBoughtButton.setOnAction(e -> removeBought());

        return new HBox(8, minusButton, plusButton, boughtButton, deleteButton, clearBoughtButton);
    }

    /** Creates the table columns and marks bought rows with the CSS class bought. */
    private void setupTable() {
        TableColumn<ShoppingItem, String> bought = new TableColumn<>("Gekauft");
        bought.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isBought() ? "✓" : ""));

        TableColumn<ShoppingItem, String> name = new TableColumn<>("Produkt");
        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct().getName()));

        TableColumn<ShoppingItem, String> category = new TableColumn<>("Kategorie");
        category.setCellValueFactory(c -> new SimpleStringProperty(
                Objects.toString(c.getValue().getProduct().getCategory(), "–")));

        TableColumn<ShoppingItem, Number> quantity = new TableColumn<>("Menge");
        quantity.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()));

        // column widths: narrow columns stay narrow, the product name gets the free space
        bought.setMinWidth(80);
        bought.setMaxWidth(100);
        name.setMinWidth(220);
        category.setMinWidth(150);
        category.setMaxWidth(260);
        quantity.setMinWidth(80);
        quantity.setMaxWidth(110);

        table.getColumns().addAll(bought, name, category, quantity);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);   // columns fill the table width
        table.setItems(items);

        // bought rows get the CSS class "bought"
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ShoppingItem item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("bought");
                if (!empty && item != null && item.isBought()) {
                    getStyleClass().add("bought");
                }
            }
        });
    }

    /** Opens the ShoppingItemDialog */
    private void addItem() {
        ShoppingItemDialog dialog = new ShoppingItemDialog();
        dialog.initOwner(getScene().getWindow());   // модальное окно поверх главного
        dialog.showAndWait().ifPresent(item -> {
            dao.add(item);
            refresh();
        });
    }

    /**
     * Changes the quantity of the selected entry and saves it.

     * @param delta the change, -1 or +1
     */
    private void changeQuantity(int delta) {
        ShoppingItem item = table.getSelectionModel().getSelectedItem();
        if (item == null) return;
        int newQuantity = item.getQuantity() + delta;
        if (newQuantity < 1) return;                    // quantity never goes below 1
        item.setQuantity(newQuantity);
        dao.update(item);
        table.refresh();                                // repaint only, the selection stays
    }

    /** Toggles the "bought" flag of the selected entry and saves it. */
    private void toggleBought() {
        ShoppingItem item = table.getSelectionModel().getSelectedItem();
        if (item == null) return;
        item.setBought(!item.isBought());
        dao.update(item);
        table.refresh();
    }

    /** Deletes the selected entry */
    private void deleteSelected() {
        ShoppingItem item = table.getSelectionModel().getSelectedItem();
        if (item == null) return;
        dao.remove(item);
        refresh();
    }

    /** Removes all entries marked as bought. */
    private void removeBought() {
        for (ShoppingItem item : dao.findAll()) {
            if (item.isBought()) {
                dao.remove(item);
            }
        }
        refresh();
    }

    /** Reloads all entries */
    @Override
    public void refresh() {
        items.setAll(dao.findAll());
    }
}