/**
 * Durchsucht Rezepte nach den gesuchten Zutaten
 */

package com.thor.kochstudio.functional;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.fx.model.SearchModel;
import com.thor.kochstudio.util.db.SQLLiteConnector;
import com.thor.kochstudio.util.db.SQLLiteQuery;
import com.thor.kochstudio.util.json.JSONReader;

import java.util.ArrayList;

public class SearchRecipes 
{
	private static ArrayList<String> matches = new ArrayList<String>();
	
	/**
	 * gibt Infos der gesuchten Rezepte wieder
	 * @param searchFor
	 * @throws Exception 
	 */
	public static void searchMatches(ArrayList<String> searchFor) throws Exception
	{
		String[] columns = new String[2];
		String[] temp = new String[2];
		
		JSONReader reader = new JSONReader();
		SQLLiteConnector connector = new SQLLiteConnector(Const.DBFILEPATH);
		SQLLiteQuery query = new SQLLiteQuery(connector.connect());
		
		String jsonString = reader.readFromFile();
		double m = 0.9;
		int n = 1;
		int size = 0;
		boolean match = false;
		
		//ArrayList f�r TableView clearen
		SearchModel.resetCount();
		matches.clear();
		
		System.out.println("Starte Suche...");
			
//		SQLLiteHandler sql = new SQLLiteHandler();
//		try {
//			sql.execute().createTable("recipeInformation", "ID INTEGER PRIMARY KEY, TITLE VARCHAR(60) UNIQUE NOT NULL, INGREDIENTS VARCHAR(350) NOT NULL");
//			sql.execute().clearTable("recipeInformation");
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		columns[0] = "title";
		columns[1] = "ingredients";
		
		size = query.queryAll("RECIPE_INFORMATION", columns).size();

		for(int i = 0; i < size; i++)
		{
				temp = query.queryID("RECIPE_INFORMATION", columns, Integer.toString(i+1));
				System.out.println("Iteration: " + i);
				
				if(size - (i+1) <= size * m)
				{
					System.out.println(n*10 + " % durchsucht...");
					m = m - 0.1;
					n++;
				}
				
				for(int j = 0; j < searchFor.size(); j++)
				{
					//abbrechen wenn die Zutat nicht im Rezept vorkommt
					if(!temp[1].toLowerCase().contains(searchFor.get(j).toLowerCase()))
					{
						match = false;
						break;
					}
					else
						match = true;
				}
				//match hinzuf�gen wenn alle Zutaten im Rezept vorkommen
				if(match)
					matches.add(temp[0]);
		}	
		System.out.println("Suche abgeschlossen. " + matches.size() + " Rezepte gefunden.");			
	}
	
	public static ArrayList<String> getMatches()
	{
		return matches;
	}
	
}

