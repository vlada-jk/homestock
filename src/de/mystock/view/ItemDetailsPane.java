package de.mystock.view;

import de.mystock.model.InventoryItem;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Read-only side panel that shows all data of the inventory item
 */
public class ItemDetailsPane extends VBox {
    // value labels
    private final Label nameValue = new Label();
    private final Label brandValue = new Label();
    private final Label barcodeValue = new Label();
    private final Label locationValue = new Label();
    private final Label categoryValue = new Label();
    private final Label amountValue = new Label();
    private final Label unitValue = new Label();
    private final Label quantityValue = new Label();
    private final Label expiryValue = new Label();
    private final Label notesValue = new Label();

    // Constructor

    /** Creates the panel with labeled rows */
    public ItemDetailsPane() {
        getStyleClass().add("details-pane");

        Label title = new Label("Details");
        title.getStyleClass().add("details-title");

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);
        int row = 0;

        grid.addRow(row++, key("Name"), nameValue);
        grid.addRow(row++, key("Marke"), brandValue);
        grid.addRow(row++, key("Barcode"), barcodeValue);
        grid.addRow(row++, key("Lagerort"), locationValue);
        grid.addRow(row++, key("Kategorie"), categoryValue);
        grid.addRow(row++, key("Inhalt"), amountValue, unitValue);
        grid.addRow(row++, key("Packungen"), quantityValue);
        grid.addRow(row++, key("MHD"), expiryValue);
        Label notesKey = key("Notizen");
        grid.addRow(row, notesKey, notesValue);
        GridPane.setColumnSpan(notesValue, 2);
        GridPane.setValignment(notesKey, VPos.TOP);   // long notes wrap, the key stays at the top

        for (Label value : new Label[]{nameValue, brandValue, barcodeValue, locationValue, categoryValue,
                amountValue, unitValue, quantityValue, expiryValue, notesValue}) {
            value.getStyleClass().add("details-value");
        }
        nameValue.setWrapText(true);
        notesValue.setWrapText(true);

        getChildren().addAll(title, grid);
        clear();

    }

    // Methods

    /**
     * Shows the data of the given item.
     *
     * @param item the item to show, or null to clear the panel
     */
    public void showDetails(InventoryItem item) {
        if (item == null) {
            clear();
            return;
        }
        nameValue.setText(item.getProduct().getName());
        amountValue.setText(String.valueOf(item.getContentAmount()));
        quantityValue.setText(String.valueOf(item.getQuantity()));
        brandValue.setText(valueOrDash(item.getProduct().getBrand()));
        barcodeValue.setText(valueOrDash(item.getProduct().getBarcode()));
        locationValue.setText(valueOrDash(item.getStorageLocation()));
        categoryValue.setText(valueOrDash(item.getProduct().getCategory()));
        unitValue.setText(valueOrDash(item.getUnit()));
        expiryValue.setText(valueOrDash(item.getExpirationDate()));
        notesValue.setText(valueOrDash(item.getNotes()));

    }

    /**
     * Creates a key label (left column) with the CSS class details-key.
     *
     * @param text the key text
     * @return the label
     */
    private static Label key(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("details-key");
        return label;
    }

//    private static String valueOrDash(String text) {
//        return text == null || text.isBlank() ? "-" : text;
//    }
    /**
     * Converts a value to display text.
     *
     * @param value any value, may be null
     * @return "-" if the value is null or its text is blank, otherwise value.toString()
     */
    private static String valueOrDash(Object value) {
        if (value == null) return "-";
        String text = value.toString();
        return text.isBlank() ? "-" : text;
    }

    /** Resets all values to "-" */
    public void clear() {
//        String text = "Kein Artikel ausgewählt";
        nameValue.setText("-");
        brandValue.setText("-");
        barcodeValue.setText("-");
        locationValue.setText("-");
        categoryValue.setText("-");
        amountValue.setText("-");
        unitValue.setText("");
        quantityValue.setText("-");
        expiryValue.setText("-");
        notesValue.setText("-");
    }
}
