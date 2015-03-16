/**
 * Hauptfenster
 */

package com.thor.kochstudio;

import com.thor.kochstudio.functional.SearchRecipes;
import com.thor.kochstudio.fx.model.FavouritesModel;
import com.thor.kochstudio.fx.model.SearchModel;
import com.thor.kochstudio.fx.view.ContentLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    private static ObservableList<SearchModel> matches = FXCollections.observableArrayList();
    private static ObservableList<FavouritesModel> favourites = FXCollections.observableArrayList();

    public MainApplication() 
    {
    	
    }

    /**
     * f�r jeden Treffer wird ein SearchModel Objekt erzeugt
     */
    @Override
    public void start(Stage primaryStage) 
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Kochstudio v0.1a");

        initRootLayout();    
        showContentLayout();     
    }

    /**
     * rootLayout initialisieren
     */
    public void initRootLayout() 
    {
        try 
        {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("/java/com/thor/kochstudio/fx/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * zeigt ContentLayout innherhalb von rootLayout an
     */
    public void showContentLayout() 
    {
        try {
            //lade conententLayout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("/java/com/thor/kochstudio/fx/view/ContentLayout.fxml"));
            AnchorPane contentLayout = (AnchorPane) loader.load();
                     
            //contentLayout zentrieren
            rootLayout.setCenter(contentLayout);
                    
            //Zugriff des Controller auf MainApp
            ContentLayoutController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void search()
    {  	
    	matches.clear();
    	for(int i = 0; i < SearchRecipes.getMatches().size(); i++)
    		matches.add(new SearchModel(i));    	
    }
    
    public static void getFavourites()
    {
    	
    }
    
    //testen
    public static SearchModel addToFavourites(int treffer)
    {
    	return matches.get(treffer - 1);    	
    }
    
    public ObservableList<SearchModel> getMatches() 
    {
        return matches;
    }

    public static void main(String[] args) 
    {
    	//l�dt das Hauptfenster
    	launch(args);   
      	
    }
       
}