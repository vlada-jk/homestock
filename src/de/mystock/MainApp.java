package de.mystock;

import de.mystock.dao.*;
import de.mystock.service.InventoryService;
import de.mystock.view.MainView;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of the MyStock JavaFX application.
 * Creates the DAOs and the main view and shows the main window.
 */
public class MainApp extends Application {
    /**
     * @param primaryStage the main window provided by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
//        InventoryItemDao inventoryDao = new InMemoryInventoryItemDao();
        InventoryItemDao inventoryDao = new JDBCInventoryItemDao();
        InventoryService service = new InventoryService(inventoryDao);

        ShoppingItemDao shoppingItemDao = new JDBCShoppingItemDao();

        MainView root = new MainView(inventoryDao, shoppingItemDao);

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("MyStock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Starts the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}

