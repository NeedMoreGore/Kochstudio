/**
 * Controller f�r ContentLayout
 */

package com.thor.kochstudio.fx.view;

import com.thor.kochstudio.MainApplication;
import com.thor.kochstudio.functional.SearchRecipes;
import com.thor.kochstudio.fx.model.SearchModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

	public class ContentLayoutController 
	{
		//Search: TableView
	    @FXML
	    private TableView<SearchModel> matchesTable;
	    @FXML
	    private TableColumn<SearchModel, String> matchNumberColumn;
	    @FXML
	    private TableColumn<SearchModel, String> matchTitelColumn;	 	 
	    //Search: Slider
	    @FXML
	    private Slider slider;
	    @FXML
	    private Label sliderLabel;
	    //Search: Textfields
	    @FXML
	    private TextField tf1;
	    @FXML
	    private TextField tf2;
	    @FXML
	    private TextField tf3;
	    @FXML
	    private TextField tf4;
	    @FXML
	    private TextField tf5;
	    @FXML
	    private TextField tf6;
	    @FXML
	    private TextField tf7;
	    @FXML
	    private TextField tf8;
	    //Favourites: TreeView
	    @FXML
	    private TreeView<String> tree;  
	    //WebView
	    @FXML
	    private WebView webView;
	   
	        
	    /**
	     * Referenz auf die mainApplication
	     */
	    private MainApplication mainApp;
	    private WebEngine webEngine;    
	    private String urlTitle;
	    private TextField[] tfArray = new TextField[8];
	    	    
	    public ContentLayoutController() 
	    {
	    }	    

	    /**
	     * Initialisiert die ControllerKlasse.
	     * Wird automatisch aufgerufen nachdem die .fxml Datei geladen wurde
	     */
	    @FXML
	    private void initialize() 
	    {	    	
	        // Initialisiert den TableView mit Treffern
	        matchNumberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
	        matchTitelColumn.setCellValueFactory(cellData -> cellData.getValue().matchProperty());
	        
	        //webView starten
	        webEngine = webView.getEngine();
	        webEngine.load("http://www.rezeptewiki.org/wiki/Hauptseite");
	        webEngine.setJavaScriptEnabled(true);
	        
	        //Clicks in der Tabelle registrieren
	        matchesTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> showRecipeDetails(newValue)); 
	        
	        //Slider Listener
	        slider.valueProperty().addListener(
	        		(observable, oldValue, newValue) -> setSlider(newValue));
	        
	        //TreeView
	        TreeItem<String> rootItem = new TreeItem<String> ("Inbox");
	        rootItem.setExpanded(true);
	        for (int i = 1; i < 6; i++) {
	            TreeItem<String> item = new TreeItem<String> ("Message" + i);            
	            rootItem.getChildren().add(item);
	        }        
	        tree.setRoot(rootItem);      
	    }

	    /**
	     * Wird von mainApplication aurgerufen mit Referenz auf sich selbst
	     */
	    public void setMainApp(MainApplication mainApp) 
	    {
	        this.mainApp = mainApp;	     
	    }
	    
	    /**
	     * l�dt die Seite f�r das ausgew�hlte Rezept
	     * @param search
	     */
	    private void showRecipeDetails(SearchModel search) 
	    {
	        if (search != null) 
	        {
	        	urlTitle = search.getUrlTitle();
	        	webEngine.load("http://www.rezeptewiki.org/wiki/" + urlTitle);	        	
	        }   
	    }
	    
	    @FXML
	    private void startSearch() throws org.json.simple.parser.ParseException, ParseException, IOException 
	    {
        	ArrayList<String> searchFor = new ArrayList<String>();
	    	
	    	int i = 0;
	    	
	    	tfArray[0] = tf1;
	    	tfArray[1] = tf2;
	    	tfArray[2] = tf3;
	    	tfArray[3] = tf4;
	    	tfArray[4] = tf5;
	    	tfArray[5] = tf6;
	    	tfArray[6] = tf7;
	    	tfArray[7] = tf8;
	    		    		    	
	    	while(i <= 7 && tfArray[i] != null)
	    	{
	    		searchFor.add(tfArray[i].getText());
		    	i++;
	    	}
	    	try 
	    	{
				SearchRecipes.searchMatches(searchFor);
			} catch (Exception e) 
	    	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	MainApplication.search();
	    	
	        // oberservable list Daten der Tabelle hinzuf�gen

	        matchesTable.setItems(mainApp.getMatches());           
	    }	 
	    
	    public void setSlider(Number new_val)
	    {
            slider.setValue(new_val.doubleValue());
            int sliderText = (int) Math.round(slider.getValue());
            sliderLabel.setText(String.valueOf(sliderText)); 
	    }
}

