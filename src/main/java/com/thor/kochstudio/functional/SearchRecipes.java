/**
 * Durchsucht Rezepte nach den gesuchten Zutaten
 */

package com.thor.kochstudio.functional;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.fx.model.SearchModel;
import com.thor.kochstudio.util.db.SQLLiteConnector;
import com.thor.kochstudio.util.db.SQLLiteQuery;

import java.sql.SQLException;
import java.util.ArrayList;

public class SearchRecipes 
{
	private static ArrayList<String> matches = new ArrayList<>();

    //Konstruktor
    public SearchRecipes()
    {
    }

    /**
	 * fügt die Infos (Titel, Zutaten) der gesuchten Rezepte zu "matches" hinzu
	 * @param searchFor - Liste mit Zutaten die gesucht werden
	 *
	 */
	public static void searchMatches(ArrayList<String> searchFor) throws Exception
    {
		SQLLiteConnector connector = new SQLLiteConnector(Const.DBFILEPATH);
		SQLLiteQuery query = new SQLLiteQuery(connector.connect());

        String[] columns = {"TITLE", "INGREDIENTS", "PAGE_ID"};
        String[] temp = new String[2];

		int size = query.queryAll("RECIPE_INFORMATION", columns).size();;
		boolean match = false;
		
		//ArrayList für TableView clearen
		SearchModel.resetCount();
        try
        {
            matches.clear();
        }
        catch(NullPointerException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }

        if(Const.DEBUGMODE)
		    System.out.println("Starte Suche...");

        //Alle Rezepte nach den Zutaten durchsuchen
		for(int i = 0; i < size; i++)
		{
            //Zutaten der einzelnen Rezepte anfragen
            try
            {
                temp = query.queryID("RECIPE_INFORMATION", columns, Integer.toString(i+1));
            }
            catch(SQLException e)
            {
                if(Const.DEBUGMODE)
                    e.printStackTrace();
            }

            if(Const.DEBUGMODE)
				System.out.println("Iteration: " + i);

            //Zutaten mit gesuchten Zutaten abgleichen
            for (String aSearchFor : searchFor)
            {
                //abbrechen wenn die Zutat nicht im Rezept vorkommt
                if (!temp[1].toLowerCase().contains(aSearchFor.toLowerCase()))
                {
                    match = false;
                    break;
                }
                else
                    match = true;
            }
				//match hinzufügen wenn alle Zutaten im Rezept vorkommen
				if(match)
					matches.add(temp[0]);
		}
        if(Const.DEBUGMODE)
		    System.out.println("Suche abgeschlossen. " + matches.size() + " Rezepte gefunden.");
	}

    /*
    @return - gibt die Treffer der Suche als ArrayList<String> zurück
     */
	public static ArrayList<String> getMatches()
	{
		return matches;
	}
	
}

