/**
 * Updatet die Dateien pageIDs.properties, RezepteInfo.json und kochstudio.db
 */

package com.thor.kochstudio.functional;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.util.ContentReader;
import com.thor.kochstudio.util.Pair;
import com.thor.kochstudio.util.db.SQLLiteHandler;
import com.thor.kochstudio.util.json.JSONReader;
import com.thor.kochstudio.util.json.JSONWriter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

public class UpdateHandler 
{
	private ArrayList<Integer> temp1 = new ArrayList<Integer>();
	private ArrayList<Integer> temp2 = new ArrayList<Integer>();
	private ArrayList<Integer> recipePageIDs = new ArrayList<Integer>();
    
    private ContentReader cr = new ContentReader();
    private JSONReader jsonReader = null;
	private SQLLiteHandler sql = new SQLLiteHandler();
	private Properties props = new Properties();
    
	/**
	 * pageIDs aller Rezepte auslesen von rezepteWiki.org
	 * und als .properties speichern
	 * @throws IOException
	 */
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
	        }
	        j++;
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
        OutputStream out = new FileOutputStream(f);
        HashSet<Integer> hs = new HashSet<Integer>();
        
        //entferne alle Duplikate
        hs.addAll(recipePageIDs);
        recipePageIDs.clear();
        recipePageIDs.addAll(hs);
        
        //Schreibe .properties
        int i = 1;        
        for(int pageID : recipePageIDs)
        {
        	String id = String.valueOf(pageID);
        	String key = String.valueOf(i);
        			          
            props.setProperty(key, id);        
            
            i++;
        }  
        props.store(out, "PageIDs der Rezepte von RezepteWiki.org"); 
        System.out.println("Writing .properties...");
        
        out.close();
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
    	 JSONWriter jsonWriter = new JSONWriter();
         String newtitle = "";
         String pageID = "";
         int j = 1;
         
         while(props.getProperty(String.valueOf(j)) != null)
         {
            pageID = props.getProperty(String.valueOf(j));
         	jsonReader = new JSONReader(cr.read(Const.READPAGEID + pageID));
              
         	try
         	{
	         	//nur valide Daten aufnehmen, keine Bier/Wein Rezepte und keine Bilder (Datei: )
	         	if(jsonReader.readPageTitle() != null && !jsonReader.readPageTitle().contains("Bier:") && !jsonReader.readPageTitle().contains("Wein:") && !jsonReader.readPageTitle().contains("Datei:") && !jsonReader.readPageTitle().contains("Cocktails:"))
	         	{
	         		ArrayList<String> ingredients = new ArrayList<String>();
	
	         		newtitle = jsonReader.readPageTitle();
	             	ingredients = jsonReader.readIngredients();
	         		
	             	//nur wenn das Rezept einen Titel hat
	         		if(newtitle != null)
	         		{
			         	jsonWriter.write(pageID, newtitle, ingredients);
	         		}
	         	}
         	}
         	catch (JSONException e)
         	{
         		e.printStackTrace();
         	}
         	j++;        	
         }        
         jsonWriter.writToFile();
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
