package de.mystock.view;


import javafx.scene.layout.VBox;

/**
 * Base class of the screens that can be shown in the center of the MainView.
 */
public abstract class Screen extends VBox {
    /**
     * Reloads the data of the screen from its DAO.
     * Called by MainView every time the screen is shown.
     */
    public abstract void refresh();   // «read again in DAO»
}