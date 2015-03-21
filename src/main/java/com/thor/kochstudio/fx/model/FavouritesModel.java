package com.thor.kochstudio.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Wird vorerst nicht genutzt
 */
@Deprecated
public class FavouritesModel
{
    private static int index = 0;
	private StringProperty propertyName;
    private int id;
    private boolean isRecipe;

    //rootItemModel
    public FavouritesModel(String name, int id, boolean isRecipe)
    {
        this.propertyName = new SimpleStringProperty(name);
        this.id = id;
        this.isRecipe = isRecipe;
        index = index + 1;
    }

    public void setPropertyNameString(String name)
    {
        this.propertyName = new SimpleStringProperty(name);
    }
    
    public String getPropertyNameString()
    {
        return propertyName.get();
    }

    public StringProperty getPropertyName()
    {
        return propertyName;
    }

    public int getId()
    {
        return id;
    }

    public boolean getIsRecipe()
    {
        return isRecipe;
    }
}
