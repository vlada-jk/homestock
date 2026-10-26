package de.mystock.model;

import java.time.LocalDate;
import java.util.Objects;
//   Rice von JA
//   Rice passt für Suchi und Rice mit Gemüse

/**
 *
 * <pre>
 *
 *     Blick us dem Fenster:
 *
 *     Ich kaufe Hähnchenbrust entweder bei Rewe von unterschiedlichen Marken oder am Markt (ohne barcode oder brand).
 *
 *     Ich kann mit Hächnchen unterschiedliche Gerichte machen (z.B Suppe, Reis, Schnitzel)
 *
 *
 * </pre>
 */
public class Product {


    /** id */
    private Integer id;

    /** Barcode (EAN), or null for products without one, for example, from the market. */
    private String barcode;

    /** item name */
    private String name;

    /** item brand */
    private String brand;

    // fruits, vegetables, pasta, etc.
    private String category;


    // constructors

    // with id

    /**
     * Creates a product with all data.
     * A blank or missing category is replaced with "Sonstiges", otherwise it is trimmed.
     *
     * @param id       the database id, may be null
     * @param barcode  the barcode, may be null
     * @param name     the product name; must not be blank
     * @param brand    the brand, may be null
     * @param category the category, may be null or blank
     * @throws IllegalArgumentException if name is null or blank
     */
    public Product(Integer id, String barcode, String name, String brand, String category) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be empty");
        }

        if (category == null || category.isBlank()) {
            this.category = "Sonstiges";
        } else {
            this.category = category.trim();
        }
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.brand = brand;
    }

    /**
     * Creates a product without id.
     *
     * @param barcode  the barcode, may be null
     * @param name     the product name; must not be blank
     * @param brand    the brand, may be null
     * @param category the category; "Sonstiges" if blank
     * @throws IllegalArgumentException if name is null or blank
     */
    public Product(String barcode, String name, String brand, String category) {
        this(null, barcode, name, brand, category);
    }



    // without id and barcode

    /**
     * Creates a product without id and barcode, e.g. bought at the market.
     *
     * @param name     the product name; must not be blank
     * @param brand    the brand, may be {@code null}
     * @param category the category; "Sonstiges" if blank
     * @throws IllegalArgumentException if {@code name} is {@code null} or blank
     * @see #Product(Integer, String, String, String, String)
     */
    public Product(String name, String brand, String category) {
        this(null, null, name, brand, category);
    }
//

    // setters

    /** @param id id */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @param barcode the new barcode, may be null */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /** @param brand the new brand, may be null */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @param category the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }


    // getters


    /** @return the database id */
    public Integer getId() {
        return id;
    }

    /** @return the barcode*/
    public String getBarcode() {
        return barcode;
    }

    /** @return the product name, never blank */
    public String getName() {
        return name;
    }

    /** @return the category, "Sonstiges" if none was given */
    public String getCategory() {
        return category;
    }

    /** @return the brand */
    public String getBrand() {
        return brand;
    }

    // equals and hashcode


    /**
     * Two products are equal if name, brand and category are equal.
     *
     * @param o the object to compare with
     * @return {@code true} if name, brand and category are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(brand, product.brand) && Objects.equals(category, product.category);
    }

    /** @return a hash code based on name, brand and category, consistent with {@link #equals(Object)} */
    @Override
    public int hashCode() {
        return Objects.hash(name, brand, category);
    }

    /** @return information about the product */
    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", barcode=" + barcode + ", name=" + name + ", brand=" + brand + ", category=" + category + '}';
    }
}
