# MyStock – Home Inventory Manager

**This project was developed as the final project for the "Java" course at Alfatraining.
**

MyStock is a desktop application for managing your household food supply.
It keeps track of what is stored where (fridge, freezer, pantry, cupboard), highlights products
that are about to expire, and comes with a simple shopping list.

The application is written in **Java 21** with **JavaFX** and stores its data in a **MySQL / MariaDB** database via **JDBC**.

> The user interface is in German.

---

## Features

### Inventory ("Vorrat")
- Table of all stored items: product, storage location, number of packages, best-before date (MHD)
- **Search** by product name (case-insensitive)
- **Filter** by storage location: *Kühlschrank* (fridge), *Gefrierschrank* (freezer),
  *Vorratsschrank* (pantry), *Küchenschrank* (kitchen cupboard)
- **Sorting** by clicking a column header
- **Details panel** showing all data of the selected item (brand, barcode, category, content, notes…)
- **Add / edit** items in a dialog with input validation (product name is required, content must be a non-negative number)
- **Use one package** ("Packung verbraucht") – when the last package is used, the item is removed automatically
- **Delete** items with a confirmation dialog
- **Automatic merging**: a new item with the same barcode and storage location as an existing item is
  merged into it – only the number of packages is increased

### Expiration tracking ("Bald ablaufend")
- Rows are colored by expiration status:
  - **expired** – the best-before date is in the past
  - **expiring soon** – expires today or within the next 3 days
- A separate view shows only expired and soon-to-expire items

### Shopping list ("Einkaufsliste")
- Add products with an optional barcode, brand, category and quantity
- Increase / decrease the quantity
- Mark entries as bought (bought rows are highlighted)
- Delete single entries or remove all bought entries at once

---

## Tech Stack

| Area         | Technology                               |
|--------------|------------------------------------------|
| Language     | Java 21                                  |
| UI           | JavaFX 21 (built in code, styled with CSS) |
| Persistence  | JDBC, MySQL / MariaDB                    |
| JDBC driver  | MySQL Connector/J                        |
| IDE          | IntelliJ IDEA                            |
| Docs         | Javadoc (generated into `doc/`)          |

---

## Architecture

The project follows a layered structure:

```
View  ──►  Service  ──►  DAO  ──►  Database
  │                        │
  └────────►  Model  ◄─────┘
```

```
src/de/mystock
├── MainApp.java                  # JavaFX entry point
├── model/                        # Domain classes
│   ├── Product.java              # barcode, name, brand, category
│   ├── InventoryItem.java        # stored item: location, content, packages, expiration date, notes
│   ├── ShoppingItem.java         # shopping list entry
│   ├── StorageLocation.java      # FRIDGE, FREEZER, PANTRY, CUPBOARD
│   ├── Unit.java                 # GRAM, KG, CAN, PIECE, LITER
│   └── ExpirationStatus.java     # OK, EXPIRING_SOON, EXPIRED
├── dao/                          # Data access
│   ├── InventoryItemDao.java     # interface
│   ├── JDBCInventoryItemDao.java # MySQL implementation
│   ├── InMemoryInventoryItemDao.java  # in-memory implementation (no database needed)
│   ├── ShoppingItemDao.java      # interface
│   └── JDBCShoppingItemDao.java  # MySQL implementation
├── service/
│   └── InventoryService.java     # business logic, e.g. merging items
└── view/                         # JavaFX UI
    ├── MainView.java             # main layout: sidebar navigation + current screen
    ├── Screen.java               # base class for all screens
    ├── ViewMode.java             # ALL / EXPIRING
    ├── InventoryView.java        # inventory table with search, filter and details
    ├── ItemDetailsPane.java      # read-only details of the selected item
    ├── ItemDialog.java           # add / edit dialog for inventory items
    ├── ShoppingListView.java     # shopping list screen
    └── ShoppingItemDialog.java   # add dialog for shopping list entries

resources/styles.css              # application stylesheet
mystock_db.sql                    # database schema + sample data
doc/                              # generated Javadoc (not in the repository)
```

`MainView` is the root layout: a sidebar with navigation buttons on the left and the current
screen in the center. Switching screens simply reloads the screen data and puts it in the center.
The *Vorrat* and *Bald ablaufend* buttons share the same `InventoryView`, which is switched between
`ViewMode.ALL` and `ViewMode.EXPIRING`.

---

## Getting Started

### Prerequisites
- JDK 21
- [JavaFX SDK 21](https://gluonhq.com/products/javafx/)
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
- A MySQL or MariaDB server (e.g. [XAMPP](https://www.apachefriends.org/))

### 1. Set up the database

1. Start your MySQL / MariaDB server.
2. Create a database named `mystock_db` and import `mystock_db.sql`
   (e.g. via phpMyAdmin, or from the command line):

   ```bash
   mysql -u root -e "CREATE DATABASE IF NOT EXISTS mystock_db"
   mysql -u root mystock_db < mystock_db.sql
   ```

   This creates the tables `inventory_item` and `shopping_list_item` and adds some sample data.

By default the application connects to:

```
URL:      jdbc:mysql://127.0.0.1:3306/mystock_db
User:     root
Password: (empty)
```

If your setup is different, change the `url`, `user` and `passwort` fields in
`JDBCInventoryItemDao` and `JDBCShoppingItemDao`.

### 2. Open the project in IntelliJ IDEA

1. Open the project folder.
2. Go to **File → Project Structure → Libraries** and add:
   - all JARs from `javafx-sdk-21/lib`
   - the MySQL Connector/J JAR
3. Make sure `src` is marked as *Sources Root* and `resources` as *Resources Root*
   (needed so that `styles.css` is found).

### 3. Run

Create a run configuration for `de.mystock.MainApp` and add these VM options
(adjust the path to your JavaFX SDK):

```
--module-path "/path/to/javafx-sdk-21/lib" --add-modules javafx.controls
```

Then start the application.

---

## Documentation

The Javadoc is not included in the repository. To generate it in IntelliJ IDEA:

1. Go to **Tools → Generate JavaDoc…**
2. Select *Whole project* and set the output directory to `doc/`.
3. Open `doc/index.html` in your browser.
