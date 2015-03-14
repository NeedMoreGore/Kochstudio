/**
 * Updatet die Dateien pageIDs.properties, RezepteInfo.json und kochstudio.db
 */

package com.thor.kochstudio.functional;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.util.ContentReader;
import com.thor.kochstudio.util.Pair;
import com.thor.kochstudio.util.db.SQLLiteHandler;
import com.thor.kochstudio.util.json.JSONReader;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

public class UpdateHandler 
{
    private ArrayList<Integer> recipePageIDs;
    private ContentReader cr;
    private JSONReader jsonReader;
    private SQLLiteHandler sql;
    private Properties props;
    private ArrayList<Integer> temp1;
    private ArrayList<Integer> temp2;

    public UpdateHandler()
    {
        recipePageIDs = new ArrayList<Integer>();
        cr = new ContentReader();
        jsonReader = null;
        sql = new SQLLiteHandler();
        props = new Properties();
        temp1 = new ArrayList<Integer>();
        temp2 = new ArrayList<Integer>();
    }


	/**
	 * pageIDs aller Rezepte auslesen von rezepteWiki.org
	 * und als .properties speichern
	 * @throws IOException
	 */
/*    public void updatePageIDs() throws IOException
    {
        jsonReader = new JSONReader(cr.read(Const.READALLCAT));

        recipePageIDs = jsonReader.readRecipePageIDs();

        String[] clmns = {"ID",Const.TB_PAGE_IDS};

        for (int i = 0; i < recipePageIDs.size(); i++) {
            sql.execute().updateTable(Const.TB_PAGE_IDS, clmns, recipePageIDs.get(i).toString());

            if(Const.DEBUGMODE)
                System.out.println("Info: Iteration " + i + " done.");
        }
    }*/















    public void updatePageIDs() throws IOException
    {
        int j = 0;

        do
        {
            if(j == 0)
            {
                jsonReader = new JSONReader(cr.read(Const.READALLCAT));

                temp1 = jsonReader.readCatPageIDs();
            }

            if(j != 0)
                temp1 = temp2;

            temp2.clear();

            for(int i = 0; i < temp1.size(); i++)
            {
                //get PageIDs von Rezepte und Kategorien

                jsonReader = new JSONReader(cr.read(Const.READALLSUBCAT1 + temp1.get(i).toString() + Const.READALLSUBCAT2));

                recipePageIDs.addAll(jsonReader.readRecipePageIDs());

                temp2.addAll(jsonReader.readCatPageIDs());

                if(Const.DEBUGMODE)
                    System.out.println(temp1.get(i));
            }
            j++;

            if(Const.DEBUGMODE)
                System.out.println("Info: Iteration " + j + " done.");

        }while(temp2.size() > 0);

        writeProperties();
    }

























    /**
     * Speichert die PageIDs in eine .properties datei
     * @throws IOException
     */
    public void writeProperties() throws IOException
    {
        File f = new File(Const.IDSFILEPATH);
        HashSet<Integer> hs = new HashSet<Integer>();
        String[] columnnames = {"ID", "PAGE_ID"};
        
        //entferne alle Duplikate
        hs.addAll(recipePageIDs);
        recipePageIDs.clear();
        recipePageIDs.addAll(hs);
        
        //update page ids in der datenbank
        for(int pageID : recipePageIDs)
        {
            String id = String.valueOf(pageID);
            sql.execute().updateTable(Const.TB_PAGE_IDS, columnnames, id);
        }

    }
    
    /**
     * erneuert die Datenbankeintr�ge
     * @param title
     * @param ingredients
     * @throws Exception 
     */
    public void updateDatabase(String title, String ingredients) throws Exception
    {	
    	String[] columnNames = new String[3];
    	columnNames[0] = null;
    	columnNames[1] = title;
    	columnNames[2] = ingredients;
    	
    	sql.execute().insert("?, ?, ?", columnNames, "recipeInformation");
    }
    
    public void writeToJSON() throws IOException
    {
        String newtitle;
        ArrayList<String> ingredients;
        ArrayList<String[]> pageIDList = new ArrayList<String[]>();
        String[] recipe_clms = {"ID", "TITLE", "INGREDIENTS"};
        String[] pageId_clms = {"PAGE_ID"};
        String[] select = {"PAGE_ID"};
        String[] pageID;
        String ingredientString;

        pageIDList = sql.query().selectQueryAll(Const.TB_PAGE_IDS, pageId_clms, select);

        for (int i = 0; i < pageIDList.size(); i++)
        {
            pageID = pageIDList.get(i);
            ingredientString = "";


            jsonReader = new JSONReader(cr.read(Const.READPAGEID + pageID[0]));

         	try
         	{
	         	//nur valide Daten aufnehmen, keine Bier/Wein Rezepte und keine Bilder (Datei: )
	         	if(jsonReader.readPageTitle() != null && !jsonReader.readPageTitle().contains("Bier:") && !jsonReader.readPageTitle().contains("Wein:") && !jsonReader.readPageTitle().contains("Datei:") && !jsonReader.readPageTitle().contains("Cocktails:"))
	         	{
	         		newtitle = jsonReader.readPageTitle();
	             	ingredients = jsonReader.readIngredients();

                    for (int j = 0; j < ingredients.size(); j++)
                    {
                        ingredientString += ingredients.get(j);
                        System.out.println(ingredients.get(j));
                        if(!(j+1 == ingredients.size()))
                            ingredientString += ", ";
                    }
                    System.out.println(ingredientString);

                    String vls[] = {"'" + newtitle + "'", "'" + ingredientString + "'"};



	             	//nur wenn das Rezept einen Titel hat
	         		if(newtitle != null)
	         		{
			         	//jsonWriter.write(pageID, newtitle, ingredients);
                        sql.execute().updateTable(Const.TB_RECIPEINFORMATION, recipe_clms, vls );
	         		}
	         	}
         	}
         	catch (JSONException e)
         	{
         		e.printStackTrace();
         	}
        }


        System.out.println("Update Complete.");
    }
    
    public void execUpdate() throws IOException, ParseException, SQLException
	{
		String[] temp = new String[2];
		Pair<String, String> list = null;	
		sql.execute().clearTable(Const.TB_RECIPEINFORMATION);

		//JSONReader reader = new JSONReader();
		String jsonString = "";
		int size = 0;
		double m = 0.9;
		int n = 1;
				
		System.out.println("Starte Update...");
		
		//pageIDs aller Rezepte holen
		updatePageIDs();
		//pageID, Titel und Zutaten in .json Datei schreiben
		writeToJSON();			
		// .json als String einlesen
		
		jsonString = jsonReader.readFromFile();
		// Anzahl der Rezepte
		size = jsonReader.readEntries(jsonString).size();
		//.json Datei nach den gegebenen Zutaten durchsuchen
		for(int i = 0; i < jsonReader.readEntries(jsonString).size(); i++)
		{		
			list = jsonReader.readEntries(jsonString).get(i);
			temp[0] = list.getFirst(); //Titel
			temp[1] = list.getSecond(); //Zutaten
			
			//update db
			try 
			{
				if(temp[0] != null && temp[1] != null && !temp[0].contains("Datei:"))
				{
					System.out.println(temp[0]);
					updateDatabase(temp[0], temp[1]);
				}

			} 
			catch (Exception e) 
			{
                if(Const.DEBUGMODE)
				e.printStackTrace();
			}
					
			if(size - (i+1) <= size * m)
			{
				System.out.println(n*10 + " % durchsucht...");
				m = m - 0.1;
				n++;
			}			
		}		
	}
}
