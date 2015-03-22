package com.thor.kochstudio.fx.view;

import com.thor.kochstudio.MainApplication;
import com.thor.kochstudio.functional.ManagePopupDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by z0r on 22.03.2015.
 */
public class PropertiesDialogLayoutController
{
    //Listview
    @FXML
    private ListView<String> ingredientsView;
    @FXML
    private ListView<String> ignoreView;
    @FXML
    private Button filterButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private TextField filterText;

    private MainApplication mainApp;

    public void initialize()
    {
        addButton.setOnAction(event -> addToIgnore());
        removeButton.setOnAction(event -> removeFromIgnore());
    }

    public void init()
    {
        ingredientsView.setItems(mainApp.getIngredientsList());
        ignoreView.setItems(mainApp.getIgnoreList());
    }

    /**
     * Wird von mainApplication aurgerufen mit Referenz auf sich selbst
     */
    public void setMainApp(MainApplication mainApp)
    {
        this.mainApp = mainApp;
    }

    private void addToIgnore()
    {
        String select = ingredientsView.getSelectionModel().getSelectedItem();
        ManagePopupDialog.addToIgnore(select);
    }

    private void removeFromIgnore()
    {
        ManagePopupDialog.removeFromIgnore(ignoreView.getSelectionModel().getSelectedItem());
    }


}
