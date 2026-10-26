package de.mystock.view;

import de.mystock.dao.InventoryItemDao;
import de.mystock.model.InventoryItem;
import de.mystock.model.StorageLocation;
import de.mystock.service.InventoryService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * Inventory screen: a table of all items with search, storage location filter
 * and a details panel for the selected row.
 * <p>
 * Items can be added, edited, used up package by package, and deleted.
 * Rows are highlighted by their ExpirationStatus
 */
public class InventoryView extends Screen {
    /** DAO for loading, updating and removing items. */
    private final InventoryItemDao dao;
    /** Service for adding new items. */
    private final InventoryService service;
    /** All items loaded from the DAO. */
    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    /** Items matching search text, storage location and view mode. */
    private final FilteredList<InventoryItem> filtered = new FilteredList<>(items, i -> true);
    /** Table showing the filtered and sorted items. */
    private final TableView<InventoryItem> table = new TableView<>();
    /** Search field; filters by product name, case-insensitive. */
    private final TextField search = new TextField();

    /** Panel with the details of the selected item. */
    private final ItemDetailsPane detailsPane = new ItemDetailsPane();

    /** Storage location filter; null means all locations. */
    private StorageLocation selectedLocation = null;
    /** Whether all items or only expiring ones are shown. */
    private ViewMode mode = ViewMode.ALL;

//    public InventoryView(InventoryItemDao dao) {
//        this.dao = dao;
//        setSpacing(12);
//        getStyleClass().add("screen");
//
//        setupTable();
////        getChildren().addAll(createToolbar(), createFilter(), table);
//        HBox tableBox = new HBox(table, detailsPane);
//        getChildren().addAll(createToolbar(), createFilter(), tableBox);
//        VBox.setVgrow(table, Priority.ALWAYS);    // таблица занимает всю высоту
//    }

    /**
     * Creates the inventory screen.
     *
     * @param dao     DAO for loading, updating and removing items
     * @param service service
     */
    public InventoryView(InventoryItemDao dao, InventoryService service) {
        this.dao = dao;
        this.service = service;
        setSpacing(12);
        getStyleClass().add("screen");

        setupTable();

        detailsPane.setPrefWidth(280);
        detailsPane.setMinWidth(280);

//        HBox.setHgrow(table, Priority.ALWAYS);        // table takes the free width
//        HBox tableBox = new HBox(12, table, detailsPane);
//
//        // when the selected row changes, the pane shows it (null = nothing selected)
//        table.getSelectionModel().selectedItemProperty()
//                .addListener((obs, old, item) -> detailsPane.showDetails(item));
//
//        getChildren().addAll(createToolbar(), createFilter(), tableBox);
//        VBox.setVgrow(tableBox, Priority.ALWAYS);     // the whole row takes the free height


        SplitPane splitPane = new SplitPane(table, detailsPane);
        splitPane.setDividerPositions(0.6);

        table.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, item) -> detailsPane.showDetails(item));

        getChildren().addAll(createToolbar(), createFilter(), splitPane);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
    }

    /**
     * Creates the toolbar with the search field and the action buttons.
     * Edit, consume, and delete are disabled while no row is selected.
     *
     * @return the toolbar
     */
    private HBox createToolbar() {
        search.setPromptText("Suche…");
//        search.textProperty().addListener((obs, old, text) ->
//                filtered.setPredicate(i ->
//                        i.getProduct().getName().toLowerCase().contains(text.toLowerCase())));
//
        search.textProperty().addListener((obs, old, text) -> updateFilter());

        Button editButton = new Button("Bearbeiten");
        editButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        editButton.setOnAction(e -> editItem());

        Button consumeButton = new Button("Packung verbraucht");
        consumeButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        consumeButton.setOnAction(e -> consumeSelected());

        Button deleteButton = new Button("Löschen");
        deleteButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.setOnAction(e -> deleteSelected());

        Button addButton = new Button("+ Artikel hinzufügen");
        addButton.getStyleClass().add("primary");
        addButton.setOnAction(e -> addItem());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);   // толкает кнопки вправо
        return new HBox(8, search, spacer, editButton, consumeButton, deleteButton, addButton);
    }



    /**
     * Creates the storage location filter: "Alle" plus one toggle button per StorageLocation.
     *
     * @return the filter bar
     */
    private HBox createFilter() {
        ToggleGroup group = new ToggleGroup();
        HBox bar = new HBox(8);

        ToggleButton allButton = new ToggleButton("Alle");
        allButton.setToggleGroup(group);
        allButton.setSelected(true);
        allButton.setOnAction(e -> {
            selectedLocation = null;
            updateFilter();
        });
        bar.getChildren().add(allButton);

        for (StorageLocation location : StorageLocation.values()) {
            ToggleButton button = new ToggleButton(location.toString());
            button.setToggleGroup(group);
            button.setOnAction(e -> {
                selectedLocation = location;
                updateFilter();
            });
            bar.getChildren().add(button);
        }
        return bar;
    }

    /**
     * Applies search text, storage location and view mode to the table.
     */
    private void updateFilter() {
        String text = search.getText().toLowerCase();

        filtered.setPredicate(item -> {
            boolean matchesSearch   = text.isEmpty() || item.getProduct().getName().toLowerCase().contains(text);
            boolean matchesLocation = selectedLocation == null || item.getStorageLocation() == selectedLocation;
            boolean matchesMode     = mode == ViewMode.ALL
                    || item.isExpired()
                    || item.isExpiringWithin(InventoryItem.EXPIRING_SOON_DAYS);
            return matchesSearch && matchesLocation && matchesMode;
        });
    }



    /**
     * Creates the table columns, enables sorting by clicking a column header
     * and colors the rows by expiration status.
     */
    private void setupTable() {
        TableColumn<InventoryItem, String> name = new TableColumn<>("Produkt");
        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduct().getName()));

        TableColumn<InventoryItem, String> location = new TableColumn<>("Lagerort");
//        location.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStorageLocation()));
        location.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStorageLocation()).asString());


        TableColumn<InventoryItem, Number> qty = new TableColumn<>("Packungen");
        qty.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()));

        TableColumn<InventoryItem, LocalDate> expiry = new TableColumn<>("MHD");
        expiry.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getExpirationDate()));
//        expiry.setCellFactory(col -> new TableCell<>() {
//            @Override
//            protected void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                getStyleClass().removeAll("soon", "expired");   // ячейки переиспользуются!
//                if (empty || date == null) { setText(null); return; }
//                setText(date.toString());
//                long days = ChronoUnit.DAYS.between(LocalDate.now(), date);
//                if (days < 0) getStyleClass().add("expired");
//                else if (days <= 3) getStyleClass().add("soon");
//            }
//        });

        table.getColumns().addAll(name, location, qty, expiry);

        SortedList<InventoryItem> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());  // сортировка по клику на заголовок

        // add CSS classes to rows based on their status (new)
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(InventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("status-soon", "status-expired");   // rows are reused: always reset
                if (empty || item == null) return;
                switch (item.getExpirationStatus()) {
                    case EXPIRED -> getStyleClass().add("status-expired");
                    case EXPIRING_SOON -> getStyleClass().add("status-soon");
                    case OK -> { }
                }
//                if (item.isExpired()) {
//                    getStyleClass().add("expired");
//                } else if (item.isExpiringWithin(InventoryItem.EXPIRING_SOON_DAYS)) {
//                    getStyleClass().add("soon");
//                }
            }
        });
        table.setItems(sorted);
    }

//    private void consumeSelected() {
//        InventoryItem item = table.getSelectionModel().getSelectedItem();
//        item.useOnePackage();
//        if (item.isEmpty()) dao.remove(item.getId());     // пустая упаковка пропадает из запаса
//        refresh();
//    }

    /**
     * Opens the ItemDialog for the selected item and saves the result via the DAO.
     */
    private void editItem() {
        InventoryItem item = table.getSelectionModel().getSelectedItem();
        if (item == null) return;
        ItemDialog dialog = new ItemDialog(item);
        dialog.initOwner(getScene().getWindow());   // modal window on top of the main window
        dialog.showAndWait().ifPresent(el -> {
            dao.update(el);
            refresh();
        });
    }

    /**
     * Uses up one package of the selected item.
     * When the last package is used, the item is removed from the inventory.
     */
    private void consumeSelected() {
        InventoryItem item = table.getSelectionModel().getSelectedItem();
        if (item == null) return;
        item.useOnePackage();
        if (item.isEmpty()) {
            dao.remove(item);
        } else {
            dao.update(item);
        }
        refresh();
    }

    /**
     * Opens an empty ItemDialog and adds the new item
     */
    private void addItem() {
        ItemDialog dialog = new ItemDialog();
        dialog.initOwner(getScene().getWindow());   // modal window on top of the main window
        dialog.showAndWait().ifPresent(item -> {
//            dao.add(item);
            service.addOrMerge(item) ;
            refresh();
        });
    }

    /** Deletes the selected item after confirmation. */
    private void deleteSelected() {
        InventoryItem item = table.getSelectionModel().getSelectedItem();
        if (item == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bist Du sicher?");
        alert.showAndWait();
        if (!alert.getResult().equals(ButtonType.OK)) return;
        dao.remove(item);
        refresh();

    }

    /**
     * Switches between all items and only expiring ones.
     *
     * @param mode the new view mode
     */
    public void setMode(ViewMode mode) {
        this.mode = mode;
        updateFilter();
    }

    /** Reloads all items from the DAO */
    @Override
    public void refresh() {
        items.setAll(dao.findAll());
    }
}