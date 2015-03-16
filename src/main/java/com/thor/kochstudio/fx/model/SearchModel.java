/**
 * Beinhaltet die anzuzeigenden Daten
 */

package com.thor.kochstudio.fx.model;

import com.thor.kochstudio.functional.SearchRecipes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchModel 
{
	private static int count = 0;
	private StringProperty match;
	private StringProperty number;
	private String urlTitle;

	/**
	 * Objekt von Treffer(index) erstellen
     * Hält Daten für das Layout vor
	 * @param index
	 */
    public SearchModel(int index) 
    {
        count++;
    	this.match = new SimpleStringProperty(SearchRecipes.getMatches().get(index).replace("_", " "));
    	this.number = new SimpleStringProperty(String.valueOf(count)); 
    	this.urlTitle = SearchRecipes.getMatches().get(index);
    }

    public String getMatch() 
    {
        return match.get();
    }
    
    public StringProperty matchProperty()
    {
    	return match;
    }
    
    public String getNumber()
    {
    	return number.get();
    }
    
    public StringProperty numberProperty()
    {
		return number;
    	
    }
    
    public static void resetCount()
    {
    	count = 0;
    }
    
    public String getUrlTitle()
    {
    	return urlTitle;
    }
}
