package de.mystock.view;

import de.mystock.model.InventoryItem;
import de.mystock.model.Product;
import de.mystock.model.StorageLocation;
import de.mystock.model.Unit;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

/**
 * Modal window for creating a new item.
 * Returns a new InventoryItem (without an ID) when "Hinzufügen" is clicked;
 */
public class ItemDialog extends Dialog<InventoryItem> {
    /** Optional barcode of the product */
    private final TextField barcode = new TextField();
    /** Product name - mandatory field of the form */
    private final TextField name = new TextField();
    /** Optional brand of the product */
    private final TextField brand = new TextField();
    /** Optional category of the product */
    private final TextField category = new TextField();
    /** Storage location of the item */
    private final ComboBox<StorageLocation> location = new ComboBox<>();
    /** Content amount of a single package */
    private final TextField amount = new TextField();
    /** Unit of the content amount  */
    private final ComboBox<Unit> unit = new ComboBox<>();
    /** Number of packages */
    private final Spinner<Integer> quantity = new Spinner<>(1, 99, 1);
    /** Best-before date, defaults to one week from today. */
    private final DatePicker expiry = new DatePicker(LocalDate.now().plusDays(7));
    /** Optional free-text notes. */
    private final TextArea notes = new TextArea();

    /** Confirm button type shared by add and edit mode */
    private final ButtonType confirmType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    // for edit

    /**
     * Id of the item being edited, or тгдд in add mode.
     * Decides whether createItem() builds a new item or one with an existing id.
     */
    private Integer editedId;

    // Constructor for Add

    /**
     * Creates the dialog in add mode with an empty form.
     */
    public ItemDialog() {
        setTitle("Artikel hinzufügen");
        setHeaderText("Neuen Artikel zum Vorrat hinzufügen");

//        ButtonType addType = new ButtonType("Hinzufügen", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(confirmType, ButtonType.CANCEL);
        getDialogPane().setContent(createForm());

        // "Hinzufügen" ist aktiv nur, wenn wir "Name" haben

        Button addButton = (Button) getDialogPane().lookupButton(confirmType);
        addButton.setText("Hinzufügen");
//        Node addButton = getDialogPane().lookupButton(addType);
        addButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> name.getText().isBlank() || parseAmount() < 0,
                name.textProperty(), amount.textProperty()));

        setResultConverter(button -> button == confirmType ? createItem() : null);
        setOnShown(e -> name.requestFocus());
    }

    // Constructor for Edit

    /**
     * Creates the dialog in edit mode, prefilled with the data of the given item.
     * @param item the item to edit
     */
    public ItemDialog(InventoryItem item) {
        this();                                   // form, buttons, result converter, focus
        this.editedId = item.getId();
        setTitle("Artikel bearbeiten");
        setHeaderText("Artikel bearbeiten");
        ((Button) getDialogPane().lookupButton(confirmType)).setText("Speichern");

        name.setText(item.getProduct().getName());
        barcode.setText(item.getProduct().getBarcode());
        brand.setText(item.getProduct().getBrand());
        category.setText(item.getProduct().getCategory());
        location.getSelectionModel().select(item.getStorageLocation());
        amount.setText(String.valueOf(item.getContentAmount()));
        unit.getSelectionModel().select(item.getUnit());
        quantity.getValueFactory().setValue(item.getQuantity());
        expiry.setValue(item.getExpirationDate());
        notes.setText(item.getNotes());
    }

    // Methoden

    /**
     * Initializes the input controls (prompt texts, choices, defaults)
     *
     * @return the form layout used as dialog content
     */
    private GridPane createForm() {
        barcode.setPromptText("optional, z.B. 4001234567890");
        name.setPromptText("z.B. Reis");
        brand.setPromptText("z.B. Ja!");
        category.setPromptText("z.B. Getreide");

//        location.getItems().addAll("Kühlschrank", "Tiefkühler", "Vorratsschrank");
//        location.setEditable(true);               // можно ввести своё место хранения
        location.getItems().addAll(StorageLocation.values());
        location.getSelectionModel().selectFirst();

        amount.setPromptText("z.B. 500");
        unit.getItems().addAll(Unit.values());
        unit.getSelectionModel().select(Unit.GRAM);

        quantity.setEditable(true);
        notes.setPrefRowCount(3);
        notes.setWrapText(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        int row = 0;
        grid.addRow(row++, new Label("Produkt*"), name);
        grid.addRow(row++, new Label("Barcode"), barcode);
        grid.addRow(row++, new Label("Marke"), brand);
        grid.addRow(row++, new Label("Kategorie"), category);
        grid.addRow(row++, new Label("Lagerort"), location);
        grid.addRow(row++, new Label("Inhalt"), amount, unit);
        grid.addRow(row++, new Label("Packungen"), quantity);
        grid.addRow(row++, new Label("MHD"), expiry);
        grid.addRow(row, new Label("Notizen"), notes);
        GridPane.setColumnSpan(notes, 2);
        return grid;
    }

    /** Empty field = 0, invalid input = -1. */
    private double parseAmount() {
        String text = amount.getText().trim().replace(',', '.');
        if (text.isEmpty()) return 0;
        try {
            double value = Double.parseDouble(text);
            return value >= 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Builds the dialog result from the current form values.
     * @return the new or updated inventory item
     */
    private InventoryItem createItem() {
        Product product = new Product(blankToNull(barcode.getText()), name.getText().trim(),
                blankToNull(brand.getText()), blankToNull(category.getText()));
        if (editedId == null) {
            return new InventoryItem(product, location.getValue(), parseAmount(),
                    unit.getValue(), quantity.getValue(), expiry.getValue(), blankToNull(notes.getText()));
        }
        return new InventoryItem(editedId, product, location.getValue(), parseAmount(),
                unit.getValue(), quantity.getValue(), expiry.getValue(), blankToNull(notes.getText()));

   //        return new InventoryItem(product, blankToNull(location.getEditor().getText()), parseAmount(),
//                unit.getValue(), quantity.getValue(), expiry.getValue(), blankToNull(notes.getText()));
//        return new InventoryItem(product, location.getValue(), parseAmount(),
//                unit.getValue(), quantity.getValue(), expiry.getValue(), blankToNull(notes.getText()));
    }

    /**
     * Normalizes optional text input.
     *
     * @param s the raw text
     * @return null if the text is null or blank, otherwise the trimmed text
     */
    private static String blankToNull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
