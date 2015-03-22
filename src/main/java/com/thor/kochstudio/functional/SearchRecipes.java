/**
 * Durchsucht Rezepte nach den gesuchten Zutaten
 */

package com.thor.kochstudio.functional;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.fx.model.SearchModel;
import com.thor.kochstudio.helper.MessageHelper;
import com.thor.kochstudio.util.db.SQLLiteConnector;
import com.thor.kochstudio.util.db.SQLLiteHandler;
import com.thor.kochstudio.util.db.SQLLiteQuery;

import java.sql.SQLException;
import java.util.ArrayList;

public class SearchRecipes 
{
	private static ArrayList<String> matches = new ArrayList<>();
    private static int sliderValue;
    private static boolean sliderState;
    /**
	 * fügt die Infos (Titel, Zutaten) der gesuchten Rezepte zu "matches" hinzu
	 * @param searchFor - Liste mit Zutaten die gesucht werden
	 *
	 */
	public static void searchMatches(ArrayList<String> searchFor)
    {
		SQLLiteConnector connector = new SQLLiteConnector(Const.DBFILEPATH);
        SQLLiteQuery query = null;
        try
        {
            query = new SQLLiteQuery(connector.connect());
        }
        catch(Exception e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }
        String[] columns = {"TITLE", "INGREDIENTS", "PAGE_ID", "QUANTITY"};
        String[] temp = new String[4];

		int size = query.queryAll("RECIPE_INFORMATION", columns).size();
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

        //Debug
        MessageHelper.createInfoMessage(Const.DEBUGMODE, "Starte Suche...", SearchRecipes.class.getName());

        //Alle Rezepte nach den Zutaten durchsuchen
		for(int i = 0; i < size; i++)
		{
            //Zutaten der einzelnen Rezepte anfragen
            try
            {
                temp = query.queryID(Const.TB_RECIPEINFORMATION, columns, Integer.toString(i+1));
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
                {
                    if(sliderState)
                    {
                        int buyAdditional = Integer.valueOf(temp[3]);

                        if (buyAdditional <= sliderValue)
                            matches.add(temp[0]);
                    }
                    else
                        matches.add(temp[0]);
                }
		}
            //Debug
            MessageHelper.createInfoMessage(Const.DEBUGMODE, "Suche abgeschlossen. " + matches.size() + " Rezepte gefunden.", SearchRecipes.class.getName());

            query.close();
	}


    public static int[] queryIngredientSize()
    {
        SQLLiteHandler sql = new SQLLiteHandler();
        int[] size = new int[2];
        String[] clmn = {"ID"};
        try {
            size[0] = Integer.valueOf(sql.query().queryMin(Const.TB_INGREDIENTS, clmn)[0]);
            size[1] = Integer.valueOf(sql.query().queryMax(Const.TB_INGREDIENTS, clmn)[0]);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
            return null;
        }

        return size;
    }


    public static String queryIngredient(int id)
    {
        SQLLiteHandler sql = new SQLLiteHandler();

        String[] clmn = {"Name"};
        String name;

        name = sql.query().querySelectWhere(Const.TB_INGREDIENTS, clmn, clmn, "ID=" + id).get(0)[0];

        return name;
    }

    public static void setSliderValue(int add)
    {
        sliderValue = add;
    }

    public static void setSliderState(boolean state)
    {
        sliderState = state;
    }

    public static boolean getSliderState()
    {
        return sliderState;
    }

    /*
    @return - gibt die Treffer der Suche als ArrayList<String> zurück
     */
	public static ArrayList<String> getMatches()
	{
		return matches;
	}






    //-----------------------------------------------//
    //------------------DEPRECATED-------------------//
    //-----------------------------------------------//


    /**
     * Zähle die Anzahl der Zutaten - die gesuchten Zutaten
     * @param ignore - Gesuchte Zutaten
     * @param ingredients - ZUtaten des Rezepts
     * @return - Anzahl für maximale Zukäufe als int
     * @deprecated - Anzahl steht in der Datenbank
     */
    @Deprecated
    private static int countIngredients(ArrayList<String> ignore, ArrayList<String> ingredients)
    {
        return ingredients.size() - ignore.size();
    }

    /**
     * Wandelt String der durch "," getrennt ist in Liste um
     * @param input - String
     * @return - ArrayList aus Strings
     */
    @Deprecated
    private static ArrayList<String> StringToList(String input)
    {
        ArrayList<String> output = new ArrayList<>();
        //solange mehr als eine Zutat im String steht
        while(input.contains(","))
        {
            int endIndex;
            String add;

            endIndex = input.indexOf(",", 1);
            System.out.println("ENDINDEX :" + endIndex);
            add = input.substring(0, endIndex);
            System.out.println("ADD : " + add);
            input = input.replace(add, "");
            output.add(add);
        }

        output.add(input);

        return output;
    }
}

