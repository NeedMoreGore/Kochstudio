package com.thor.kochstudio.fx.model;

import com.thor.kochstudio.functional.SearchRecipes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FavouritesModel 
{
	private StringProperty name;
	private String urlTitle;
	private boolean isRecipe;


	/**
	 * Objekt von Treffer(index) erstellen
	 * @param index
	 */
    public FavouritesModel(int index) 
    {
    	this.urlTitle = SearchRecipes.getMatches().get(index).toString();
    	this.name = new SimpleStringProperty(SearchRecipes.getMatches().get(index).replace("_", " "));
    	this.isRecipe = true;
    }
    
    /**
     * Objekt von Ordner erstellen
     * @param folderName
     */
    public FavouritesModel(String name, boolean isRecipe)
    {
    	this.name = new SimpleStringProperty(name);
    	this.isRecipe = isRecipe;
    }
    
    public String getName() 
    {
        return name.get();
    }
    
    public StringProperty nameProperty()
    {
    	return name;
    }
    
    public String getUrlTitle()
    {
    	return urlTitle;
    }
}
