package com.thor.kochstudio.functional;

import com.thor.kochstudio.util.db.SQLLiteHandler;

import java.util.ArrayList;

public class ManageFavourites 
{	
	private SQLLiteHandler sqlHandler = new SQLLiteHandler();
	
	public void addFavourites()
	{
		
	}
	
	public void loadFavourites()
	{

	}
	
	/*
	 * L�dt die Ordnerstruktur der Favoriten
	 */
	public void loadStructure()
	{
		String[] columns = {"ID","NAME","PARENT_ID"};
		ArrayList<String[]> result = null;

		try {
			result = sqlHandler.query().queryAllWhere("FAVOURITES_FOLDER", columns, "PARENT_ID IS NULL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < result.size(); i++)
		{
			String singleResult[] = result.get(i);
			
			for(int j = 0; j < singleResult.length; j++)	
				System.out.println(singleResult[j]);
		}
	}
}

