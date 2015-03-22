package com.thor.kochstudio.fx.model;

import com.thor.kochstudio.functional.SearchRecipes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by z0r on 22.03.2015.
 */
public class PropertiesDialogModel
{
    int id;
    StringProperty name;

    public PropertiesDialogModel(int index)
    {
        this.id = index;
        this.name = new SimpleStringProperty(SearchRecipes.queryIngredient(index));
    }

    public StringProperty getPropertyName()
    {
        return name;
    }

    public String getName()
    {
        return name.toString();
    }

    public int getId()
    {
        return id;
    }
}
