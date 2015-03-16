/**
 * Updatet die Dateien pageIDs.properties, RezepteInfo.json und kochstudio.db
 */

package com.thor.kochstudio.functional;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.util.ContentReader;
import com.thor.kochstudio.util.db.SQLLiteHandler;
import com.thor.kochstudio.util.json.JSONReader;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class UpdateHandler 
{
    private ArrayList<Integer> recipePageIDs;
    private ContentReader cr;
    private JSONReader jsonReader;
    private SQLLiteHandler sql;
    private ArrayList<Integer> temp1;
    private ArrayList<Integer> temp2;
    private static ArrayList<String> ingredientsList;

    //Konstruktor
    public UpdateHandler()
    {
        recipePageIDs = new ArrayList<Integer>();
        cr = new ContentReader();
        jsonReader = null;
        sql = new SQLLiteHandler();
        temp1 = new ArrayList<>();
        temp2 = new ArrayList<>();
        ingredientsList =  new ArrayList<>();
    }

    /*
    Liest die PageIDs von RezepteWiki.org aus und speichert sie in der Datenbank
     */
    public void updatePageIDs() throws IOException
    {
        int j = 0;

        do
        {
            if(j == 0)
            {
                //pageIDs aller Kategorien als JSON auslesen
                jsonReader = new JSONReader(cr.read(Const.READALLCAT));
                temp1 = jsonReader.readCatPageIDs();
            }

            if(j != 0)
                temp1 = temp2;

            temp2.clear();

            for (Integer aTemp1 : temp1)
            {
                //get PageIDs von Rezepte und Kategorien
                jsonReader = new JSONReader(cr.read(Const.READALLSUBCAT1 + aTemp1.toString() + Const.READALLSUBCAT2));
                recipePageIDs.addAll(jsonReader.readRecipePageIDs());
                temp2.addAll(jsonReader.readCatPageIDs());

                if (Const.DEBUGMODE)
                    System.out.println(aTemp1);
            }
            j++;

            if(Const.DEBUGMODE)
                System.out.println("Info: Iteration " + j + " done.");

        }while(temp2.size() > 0); //solange bis keine Kategorien mehr vorkommen

        //IDs in die Datenbank schreiben
        writePageIds();
    }

    /*
    Speichert pageIDs in die Datenbank unter "PAGE_IDS"
     */
    public void writePageIds()
    {
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

    /*
    Rezeptinformationen (Titel, Zutaten, pageID) in die Datenbank schreiben
     */
    public void writeRecipeInformation()
    {
        String newtitle;
        ArrayList<String> ingredients;
        ArrayList<String[]> pageIDList;
        String[] recipe_clms = {"ID", "TITLE", "INGREDIENTS", "PAGE_ID", "QUANTITY"};
        String[] pageId_clms = {"PAGE_ID"};
        String[] select = {"PAGE_ID"};
        String[] pageID;
        String ingredientString;

        //Alle pageIDs aus der DB auswählen, die nicht in RECIPE_INFORMATION stehen
        pageIDList = sql.query().querySelectWhere(Const.TB_PAGE_IDS, pageId_clms, select, "PAGE_ID NOT IN (SELECT PAGE_ID FROM RECIPE_INFORMATION)");

        //Einträge in RECIPE_INFORMATION löschen, die keiner page_id aus PAGE_IDS zugewiesen werden können
        try
        {
            sql.execute().deleteIfNotExists();
        }
        catch (JSONException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }

        //jede pageID auslesen
        for (String[] aPageIDList : pageIDList)
        {
            pageID = aPageIDList;
            ingredientString = "";

            try
            {
                //auslesen als JSON
                jsonReader = new JSONReader(cr.read(Const.READPAGEID + pageID[0]));
            }
            catch(IOException e)
            {
                if (Const.DEBUGMODE)
                    e.printStackTrace();
            }

            //nur valide Daten aufnehmen, keine Bier/Wein Rezepte und keine Bilder (Datei: )
            if (jsonReader.readPageTitle() != null && !jsonReader.readPageTitle().contains("Bier:") && !jsonReader.readPageTitle().contains("Wein:") && !jsonReader.readPageTitle().contains("Datei:") && !jsonReader.readPageTitle().contains("Cocktails:"))
            {
                newtitle = jsonReader.readPageTitle();
                ingredients = jsonReader.readIngredients();

                //update INGREDIENTS in db
                if(Const.UPDATE_INGREDIENTS)
                    createIngredientList(ingredients);

                for (int j = 0; j < ingredients.size(); j++)
                {
                    ingredientString += ingredients.get(j);

                    //String mit , abtrennen, wenn es nicht die letzte Zutat ist
                    if (!(j + 1 == ingredients.size()))
                        ingredientString += ", ";
                }

                //String DB-gerecht formatieren
                ingredientString = ingredientString.replace("'", "");

                String vls[] = {"'" + newtitle + "'", "'" + ingredientString + "'", "'" + pageID[0] + "'", "'" + ingredients.size() + "'"};

                //nur wenn das Rezept einen Titel hat
                if (newtitle != null) {
                    //jsonWriter.write(pageID, newtitle, ingredients);
                    sql.execute().updateTable(Const.TB_RECIPEINFORMATION, recipe_clms, vls);
                }
            }
        }
        if(Const.UPDATE_INGREDIENTS)
            updateIngredientsList();

        if(Const.DEBUGMODE)
            System.out.println("Update Complete.");
    }

    /**
     * Speichert Zutaten in eine ArrayList, wenn Update Zutaten aktiviert ist
     * @param ingredients
     */
    public void createIngredientList(ArrayList<String> ingredients)
    {
        HashSet<String> hs = new HashSet<>();

        hs.addAll(ingredients);
        ingredientsList.addAll(hs);
    }

    /**
     * Updatet die Tabelle INGREDIENTS in der DB, wenn Update Zutaten aktiviert ist
     */
    public void updateIngredientsList()
    {
        String[] val = new String[1];
        String[] clms = {"ID", "NAME"};

        for(String ingredient : ingredientsList)
        {
            val[0] = ingredient;
            sql.execute().updateTable(Const.TB_INGREDIENTS, clms, "'" + val[0] + "'");
        }
    }

    /*
    Update ausführen
     */
    public void execUpdate() throws IOException, ParseException, SQLException
	{
		//pageIDs aller Rezepte holen
		updatePageIDs();
		//pageID, Titel und Zutaten in DB schreiben
		writeRecipeInformation();
	}
}
