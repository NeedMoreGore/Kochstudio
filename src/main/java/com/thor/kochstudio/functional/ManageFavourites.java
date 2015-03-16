package com.thor.kochstudio.functional;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.util.db.SQLLiteHandler;

import java.util.ArrayList;

public class ManageFavourites 
{	
	private SQLLiteHandler sqlHandler;

    public ManageFavourites()
    {
        sqlHandler = new SQLLiteHandler();
    }
	
	public void addFavourites()
	{
		
	}
	
	public void loadFavourites()
	{

	}

    /*
    lädt die Ordnerstruktur der Favoriten
     */
	public void loadStructure()
	{
		String[] columns = {"ID","NAME","PARENT_ID"};
		ArrayList<String[]> result = null;

		try
        {
			result = sqlHandler.query().queryWhere("FAVOURITES_FOLDER", columns, "PARENT_ID IS NULL");
		}
        catch (Exception e)
        {
            if(Const.DEBUGMODE)
			    e.printStackTrace();
		}
	}
}

