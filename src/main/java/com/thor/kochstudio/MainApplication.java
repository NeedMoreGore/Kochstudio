/**
 * Hauptfenster
 */

package com.thor.kochstudio;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.functional.SearchRecipes;
import com.thor.kochstudio.fx.model.FavouritesModel;
import com.thor.kochstudio.fx.model.PropertiesDialogModel;
import com.thor.kochstudio.fx.model.SearchModel;
import com.thor.kochstudio.fx.view.ContentLayoutController;
import com.thor.kochstudio.fx.view.PropertiesDialogLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private Stage primaryStage;

    private BorderPane rootLayout;
    private AnchorPane propertiesDialogLayout;
    
    private static ObservableList<SearchModel> matches = FXCollections.observableArrayList();
    private static ObservableList<PropertiesDialogModel> ingredientsList = FXCollections.observableArrayList();
    private static ObservableList<PropertiesDialogModel> ignoreList = FXCollections.observableArrayList();

    @Deprecated
    private static ObservableList<FavouritesModel> favourites = FXCollections.observableArrayList();

    public MainApplication() 
    {
    	
    }

    /**
     * für jeden Treffer wird ein SearchModel Objekt erzeugt
     */
    @Override
    public void start(Stage primaryStage) 
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Const.WINDOWTITLE);

        initRootLayout();
        showContentLayout();
        initModalDialog();
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


    public void showModalDialog()
    {
        if(propertiesDialogLayout.getScene() != null)
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("/java/com/thor/kochstudio/fx/view/PropertiesDialogLayout.fxml"));

            try
            {
                propertiesDialogLayout = (AnchorPane) loader.load();
                //Zugriff des Controller auf MainApp
                PropertiesDialogLayoutController controller = loader.getController();
                controller.setMainApp(this);
                controller.init();
            }
            catch (IOException e)
            {
                if (Const.DEBUGMODE)
                    e.printStackTrace();
            }
        }

            Stage stage = new Stage();
            Scene page2 = new Scene(propertiesDialogLayout);
            stage.setScene(page2);
            stage.setX(primaryStage.getX() + (primaryStage.getScene().getWidth() / 2));
            stage.setY(primaryStage.getY() + (primaryStage.getScene().getHeight() / 2));
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
    }

    public void initModalDialog()
    {
        initPropertiesList();

        try {
            //lade conententLayout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("/java/com/thor/kochstudio/fx/view/PropertiesDialogLayout.fxml"));
            propertiesDialogLayout = (AnchorPane) loader.load();

            //Zugriff des Controller auf MainApp
            PropertiesDialogLayoutController controller = loader.getController();
            controller.setMainApp(this);
            controller.init();
        } catch (IOException e) {
            if (Const.DEBUGMODE)
                e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Erzeugt für jeden Treffer ein SearchModel
     */
    public static void search()
    {  	
    	matches.clear();
    	for(int i = 0; i < SearchRecipes.getMatches().size(); i++)
    		matches.add(new SearchModel(i));    	
    }



    public static void initPropertiesList()
    {
        int[] size = SearchRecipes.queryIngredientSize();

        for (int i = size[0]; i <= size[1] ; i++)
        {
            ingredientsList.add(new PropertiesDialogModel(size[1] - (size[1] - size[0])));
        }
    }

    public ObservableList<SearchModel> getMatches() 
    {
        return matches;
    }

    public ObservableList<PropertiesDialogModel> getIngredientsList()
    {
        return ingredientsList;
    }

    public static void main(String[] args) 
    {
    	//l�dt das Hauptfenster
    	launch(args);   
      	
    }



    //-----------------------------------------------//
    //------------------DEPRECATED-------------------//
    //-----------------------------------------------//

    @Deprecated
    public static void loadFavourites(FavouritesModel model)
    {
        favourites.add(model);
    }

    @Deprecated
    public ObservableList<FavouritesModel> getFavourites()
    {
        return favourites;
    }
       
}