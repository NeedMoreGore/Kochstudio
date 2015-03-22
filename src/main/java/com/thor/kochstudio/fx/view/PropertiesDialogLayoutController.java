package com.thor.kochstudio.fx.view;

import com.thor.kochstudio.MainApplication;
import com.thor.kochstudio.fx.model.PropertiesDialogModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by z0r on 22.03.2015.
 */
public class PropertiesDialogLayoutController
{
    //Listview
    @FXML
    private ListView<PropertiesDialogModel> ingredientsView;
    @FXML
    private ListCell<String> ingredientsCell;
    @FXML
    private ListView<PropertiesDialogModel> ignoreView;
    @FXML
    private Button filterButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private TextField filterText;

    private MainApplication mainApp;

    public void init()
    {
        ingredientsView.setItems(mainApp.getIngredientsList());
    }

    /**
     * Wird von mainApplication aurgerufen mit Referenz auf sich selbst
     */
    public void setMainApp(MainApplication mainApp)
    {
        this.mainApp = mainApp;
    }
}
