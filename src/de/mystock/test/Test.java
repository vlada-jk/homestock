package de.mystock.test;

import de.mystock.dao.InMemoryInventoryItemDao;
import de.mystock.model.InventoryItem;
import de.mystock.model.Product;
import de.mystock.model.StorageLocation;
import de.mystock.model.Unit;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * for trying out the model, the in-memory DAO
 * and the connection to the MySQL database.
 */
public class Test {

    public static void main(String[] args) {

//        Map<Product, Integer> products = new HashMap<>();

        Product rice = new Product(4012345, null, "Reis", "JA", "Reis und andere");
        InventoryItem riceItem = new InventoryItem(
                rice,
                StorageLocation.CUPBOARD,
                500,
                Unit.GRAM,
                3,
                LocalDate.of(2026, 12, 1),
                "Something");
//
        Product chicken = new Product("Chicken", "Ja", null);
        InventoryItem chickenItem = new InventoryItem(
                chicken,
                StorageLocation.CUPBOARD,
                150,
                Unit.GRAM,
                1,
                LocalDate.of(2026, 10, 20),
                "Markt");

        Product eggs = new Product(4012345, null, "Eier", "JA", "Eier und andere");
        InventoryItem eggsItem = new InventoryItem(
                eggs,
                StorageLocation.CUPBOARD,
                1,
                Unit.PIECE,
                1,
                LocalDate.of(2026, 10, 10),
                "Markt"
        );

        InMemoryInventoryItemDao memoryDao = new InMemoryInventoryItemDao();
        memoryDao.add(riceItem);
        memoryDao.add(chickenItem);
        memoryDao.add(eggsItem);

        System.out.println(memoryDao.findAll() + "\n");

        memoryDao.update(eggsItem);


        System.out.println(riceItem.isEmpty());
//        System.out.println(chickenItem.useOnePackage());
        System.out.println(riceItem);

//        Product egg = new Product(null, "Eier", null);

         String url = "jdbc:mysql://127.0.0.1:3306/mystock_db?createDatabaseIfNotExist=true";
         String user = "root";
         String passwort = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, passwort);
            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM ");
            System.out.println("Connected to database" + statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

