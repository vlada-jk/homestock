package de.mystock.view;

import de.mystock.model.Product;
import de.mystock.model.ShoppingItem;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Modal window for creating a new item.
 * Returns a new ShoppingItem (without an ID) when "Hinzufügen" is clicked;
 */
public class ShoppingItemDialog extends Dialog<ShoppingItem> {
    /** Optional barcode */
    private final TextField barcode = new TextField();
    /** Product name - mandatory field of the form. */
    private final TextField name = new TextField();
    /** Optional brand. */
    private final TextField brand = new TextField();
    /** Optional category. */
    private final TextField category = new TextField();
    /** How many to buy, defaults to 1. */
    private final Spinner<Integer> quantity = new Spinner<>(1, 99, 1);

    /** button "Hinzufügen". */
    private final ButtonType confirmType = new ButtonType("Hinzufügen", ButtonBar.ButtonData.OK_DONE);

    /**
     * Creates the dialog with an empty form.
     */
    public ShoppingItemDialog() {
        setTitle("Artikel hinzufügen");
        setHeaderText("Neuen Artikel zur Einkaufsliste hinzufügen");

        getDialogPane().getButtonTypes().addAll(confirmType, ButtonType.CANCEL);
        getDialogPane().setContent(createForm());

        // "Hinzufügen" ist aktiv nur, wenn wir "Name" haben
        Button addButton = (Button) getDialogPane().lookupButton(confirmType);
        addButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> name.getText().isBlank(), name.textProperty()));

        setResultConverter(button -> button == confirmType ? createItem() : null);
        setOnShown(e -> name.requestFocus());
    }

    // Methoden

    /**
     * Initializes the input controls and arranges them with their labels in a grid.
     *
     * @return the form layout used as dialog content
     */
    private GridPane createForm() {
        barcode.setPromptText("optional, z.B. 4001234567890");
        name.setPromptText("z.B. Reis");
        brand.setPromptText("z.B. Ja!");
        category.setPromptText("z.B. Getreide");

        quantity.setEditable(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        int row = 0;
        grid.addRow(row++, new Label("Produkt*"), name);
        grid.addRow(row++, new Label("Barcode"), barcode);
        grid.addRow(row++, new Label("Marke"), brand);
        grid.addRow(row++, new Label("Kategorie"), category);
        grid.addRow(row, new Label("Menge"), quantity);
        return grid;
    }

    /**
     * Builds the dialog result from the current form values.
     *
     * @return a new shopping item without id
     */
    private ShoppingItem createItem() {
        Product product = new Product(blankToNull(barcode.getText()), name.getText().trim(),
                blankToNull(brand.getText()), blankToNull(category.getText()));
        return new ShoppingItem(product, quantity.getValue());
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
