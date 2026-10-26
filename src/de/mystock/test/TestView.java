package de.mystock.test;

import de.mystock.dao.InMemoryInventoryItemDao;
import de.mystock.dao.InventoryItemDao;
import de.mystock.model.InventoryItem;
import de.mystock.model.Product;
import de.mystock.model.StorageLocation;
import de.mystock.model.Unit;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

/**
 * UI prototype: a table of two in-memory sample items
 * with a button to use up one package of the selected item.
 */
public class TestView extends Application {
    /**
     * Fills an in-memory DAO with two sample items and shows them in a table.
     * When the last package of an item is used, it is removed from the DAO and the table.
     *
     * @param primaryStage the main window provided by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {

        InventoryItemDao dao = new InMemoryInventoryItemDao();

        dao.add(new InventoryItem(new Product("Rice", "Ja", "Rice und andere"),
                StorageLocation.CUPBOARD,
                500,
                Unit.GRAM,
                2,
                LocalDate.of(2026, 12, 1),
                "Test"));
        dao.add(new InventoryItem(new Product("Hähnchen", "Ja", "Fleisch"),
                StorageLocation.FRIDGE,
                600,
                Unit.GRAM,
                1,
                LocalDate.of(2026, 10, 26),
                "Test2"));


        primaryStage.setTitle("Inventory");
        BorderPane root = new BorderPane();

        ObservableList<InventoryItem> inventoryItems = FXCollections.observableArrayList(dao.findAll());

        TableView<InventoryItem> inventoryTable = new TableView<>(inventoryItems);
        TableColumn<InventoryItem, String> nameColumn = new TableColumn<>("Name");
        TableColumn<InventoryItem, String> storageLocationColumn = new TableColumn<>("Lagerort");
        TableColumn<InventoryItem, Integer> quantityColumn = new TableColumn<>("Menge");
        TableColumn<InventoryItem, LocalDate> expirationDateColumn = new TableColumn<>("Ablaufdatum");

        inventoryTable.getColumns().addAll(nameColumn, storageLocationColumn, quantityColumn, expirationDateColumn);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        storageLocationColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStorageLocation()).asString());
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));
        expirationDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getExpirationDate()));


        root.setTop(new Text("Top"));
        root.setLeft(new Text("Left"));
        root.setCenter(inventoryTable);

        HBox buttonBar = new HBox(10);
        Button useOnePackageButton = new Button("Packung verbrauchen");
        buttonBar.getChildren().add(useOnePackageButton);

        useOnePackageButton.setOnAction(e -> {
            InventoryItem item = inventoryTable.getSelectionModel().getSelectedItem();
            if (item != null) {
                item.useOnePackage();
                if (item.isEmpty()) {
                    dao.remove(item);
                    inventoryItems.remove(item);
                }
                else {
                    dao.update(item);
                }
                inventoryTable.refresh();
            }
        });


        root.setBottom(buttonBar);

        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
}


//        ObservableList<InventoryItem> inventoryItems = FXCollections.observableArrayList();
//        Product rice = new Product(4012345, null, "Reis", "JA", "Reis und andere");
//        InventoryItem riceItem = new InventoryItem(
//                rice,
//                "Schrank",
//                500,
//                Unit.GRAM,
//                3,
//                LocalDate.of(2026, 12, 1),
//                "Something");
////
//        Product chicken = new Product("Chicken", "Ja", null);
//        InventoryItem chickenItem = new InventoryItem(
//                chicken,
//                "Kühlschrank",
//                150,
//                Unit.GRAM,
//                1,
//                LocalDate.of(2026, 10, 20),
//                "Markt");

//        inventoryItems.addAll(riceItem, chickenItem);