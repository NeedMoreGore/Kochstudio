/**
 * Controller f�r ContentLayout
 */

package com.thor.kochstudio.fx.view;

import com.thor.kochstudio.MainApplication;
import com.thor.kochstudio.functional.ManageFavourites;
import com.thor.kochstudio.functional.SearchRecipes;
import com.thor.kochstudio.fx.model.LazyTreeItem;
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
        //Search: properties button
        @FXML
        private Button propertiesButton;
        //Search: ContextMenu
        @FXML
        private MenuItem addFavouritesContext;
	    //Favourites: TreeView
	    @FXML
	    private TreeView<String> tree;
        //Favourites: ContextMenu
        @FXML
        private MenuItem addNewFolder;
        @FXML
        private MenuItem deleteFavourites;
	    //WebView
	    @FXML
	    private WebView webView;

	    /**
	     * Referenz auf die mainApplication
	     */
	    private MainApplication mainApp;
	    private WebEngine webEngine;
        private TextField[] tfArray = new TextField[8];
        private ManageFavourites favourites = new ManageFavourites();
        private LazyTreeItem rootItem;

	    /**
	     * Initialisiert die ControllerKlasse.
	     * Wird automatisch aufgerufen nachdem die .fxml Datei geladen wurde
	     */
	    public void initialize()
	    {	    	
	        // Initialisiert den TableView mit Treffern
	        matchNumberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
	        matchTitelColumn.setCellValueFactory(cellData -> cellData.getValue().matchProperty());
	        
	        //webView starten
	        webEngine = webView.getEngine();
	        webEngine.load("http://www.rezeptewiki.org/wiki/Hauptseite");
            //webEngine.load("file:///G:/Workspace/IntelliJIDEA/kochstudio/src/test/resources/Favourites/Google.htm") ;
            webEngine.setJavaScriptEnabled(true);
	        
	        //Clicks in der Tabelle registrieren
	        matchesTable.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showRecipeDetails(newValue));

	        //Slider Listener
	        slider.valueProperty().addListener(
	        		(observable, oldValue, newValue) -> setSlider(newValue));

            //TreeView
            LazyTreeItem rootItem = new LazyTreeItem("Favoriten");
            tree.setRoot(rootItem);
            rootItem.setExpanded(true);

            //lade Rezept, wenn selektiertes Item ein Rezept ist
            tree.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showRecipeDetails((LazyTreeItem) newValue));
            //ändere Expand-Status des markierten Items bei Mausklick
            tree.setOnMouseClicked(event -> setExpanded((LazyTreeItem) tree.getSelectionModel().selectedItemProperty().getValue()));
            //Kontext Menu
            addFavouritesContext.setOnAction(event -> addToFavourites(matchesTable.getSelectionModel().selectedItemProperty().getValue()));
            addNewFolder.setOnAction(event -> addNewFolder(((LazyTreeItem) tree.getSelectionModel().selectedItemProperty().getValue())));
            deleteFavourites.setOnAction(event -> deleteEntry((LazyTreeItem)tree.getSelectionModel().selectedItemProperty().getValue()));

            propertiesButton.setOnAction(event -> showDialog());



	    }

	    /**
	     * Wird von mainApplication aurgerufen mit Referenz auf sich selbst
	     */
	    public void setMainApp(MainApplication mainApp) 
	    {
	        this.mainApp = mainApp;	     
	    }
	    
	    /**
	     * Seite des angeklickten Treffers im Browser anzeigen
	     * @param search - SearchModel der Treffer
	     */
	    private void showRecipeDetails(SearchModel search) 
	    {
	        if (search != null) 
	        {
                String urlTitle = search.getUrlTitle();
	        	webEngine.load("http://www.rezeptewiki.org/wiki/" + urlTitle);	        	
	        }   
	    }

        /**
         * Zeigt die Rezepte der selektierten Favoriten an
         * @param item - selektiertes TreeItem
         */
        private void showRecipeDetails(LazyTreeItem item)
        {
            if(item.getIsRecipe())
            {
                String urlTitle = item.getUrlTitle();
                webEngine.load(("http://www.rezeptewiki.org/wiki/" + urlTitle));
            }
        }

        /**
         * RezeptDetails holen für Favoriten holen
         * @param search - SearchModel ,
         */
        private void addToFavourites(SearchModel search)
        {
            String urlTitle = "";
            String name = "";

            if (search != null)
            {
                urlTitle = search.getUrlTitle();
                name = search.getMatch();
            }

            favourites.newFavourite(name, urlTitle);
            rootItem = new LazyTreeItem("Favoriten");
            rootItem.setExpanded(true);
            tree.setRoot(rootItem);
        }

        /**
         * fügt neuen Ordner hinzu und lädt den Tree neu
         * @param item - TreeItem
         */
        private void addNewFolder(LazyTreeItem item) throws NullPointerException
        {
            if(!item.getIsRecipe())
            {
                favourites.newFolder(item.getId());
                rootItem = new LazyTreeItem("Favoriten");
                rootItem.setExpanded(true);
                tree.setRoot(rootItem);
            }
        }

        /**
         * Löscht einen Ordner oder Rezept aus Favoriten
         */
        private void deleteEntry(LazyTreeItem item)
        {
            favourites.delete(item.getId());
            rootItem = new LazyTreeItem("Favoriten");
            rootItem.setExpanded(true);
            tree.setRoot(rootItem);
        }

        /**
         * Ändert den Expand-Status des selektierten TreeItems
         */
        @FXML
        private void setExpanded(LazyTreeItem item) throws NullPointerException
        {
            if(!item.isExpanded())
            {
                favourites.expand(String.valueOf(item.getId()), "0");
            }
            else {
                favourites.expand(String.valueOf(item.getId()), "1");
            }
        }

        /**
         *
         * Wird vom "Starte Suche" Button ausgeführt
         */
	    @FXML
	    public void startSearch() throws org.json.simple.parser.ParseException, ParseException, IOException
	    {
        	ArrayList<String> searchFor = new ArrayList<>();
	    	
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
			}
            catch (Exception e)
	    	{
				e.printStackTrace();
			}
	    	MainApplication.search();
	    	
	        // oberservable list Daten der Tabelle hinzufügen
	        matchesTable.setItems(mainApp.getMatches());           
	    }	 

        //Slider Wert aktualisieren
	    public void setSlider(Number new_val)
	    {
            slider.setValue(new_val.doubleValue());
            int sliderText = (int) Math.round(slider.getValue());
            if(sliderText > -1)
            {
                sliderLabel.setText(String.valueOf(sliderText));
                SearchRecipes.setSliderValue(sliderText);
                setSliderState(true);
            }
            else
            {
                sliderLabel.setText("Off");
                setSliderState(false);
            }

	    }

        /**
         * Slider Status aktualisieren und switchen
         */
        public void setSliderState(boolean state)
        {
            SearchRecipes.setSliderState(state);
        }

        public void showDialog()
        {
            mainApp.showModalDialog();
        }

}

