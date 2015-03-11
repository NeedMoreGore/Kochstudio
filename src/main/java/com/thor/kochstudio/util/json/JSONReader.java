/**
 * Liest den Inhalt von JSON-Files aus
 */

package com.thor.kochstudio.util.json;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSONReader 
{
	private JSONObject obj = null;
	
	public JSONReader(String jsonObject)
	{
		this.obj = new JSONObject(jsonObject);
	}
	
	public JSONReader()
	{
		
	}
	       
	/**
	 * liest PageIDs der Kategorien aus
	 * @return
	 */
	public ArrayList<Integer> readCatPageIDs()
	{
		ArrayList<Integer> pageIDs = new ArrayList<Integer>();
	    JSONArray arr = obj.getJSONObject("query").getJSONArray("categorymembers");
	    
	    for (int i = 0; i < arr.length(); i++)
	    {
	        int pageID = arr.getJSONObject(i).getInt("pageid");
	        
	        if(arr.getJSONObject(i).getString("title").contains("Kategorie") && !arr.getJSONObject(i).getString("title").contains("Getr\u00e4nke") &&!arr.getJSONObject(i).getString("title").contains("Pralinen, Bonbons und \u00c4hnliches"));
	        	pageIDs.add(pageID);
	    }
	    
	    return pageIDs;
	}
	
	/**
	 * Liest alle pageIDs der Rezepte aus
	 * @return
	 */
	public ArrayList<Integer> readRecipePageIDs()
	{
		ArrayList<Integer> pageIDs = new ArrayList<Integer>();
	    JSONArray arr = obj.getJSONObject("query").getJSONArray("categorymembers");
	    
	    for (int i = 0; i < arr.length(); i++)
	    {
	        int pageID = arr.getJSONObject(i).getInt("pageid");
	        
	        if(!arr.getJSONObject(i).getString("title").contains("Kategorie"))
	        	pageIDs.add(pageID);
	    }
	    
	    return pageIDs;
	}
		
	/**
	 * liest den Seitentitel aus f�r Verlinkung
	 * @return
	 */
	public String readPageTitle()
	{		
		//kann bei getJSONObject(0) abst�rzen, wenn es nicht vorhanden ist
		try
		{
			String title = obj.getJSONObject("parse").getJSONArray("sections").getJSONObject(0).getString("fromtitle");
			return title;
		}
		catch(JSONException e)
		{
            return "";
		}
	}
	
	/**
	 * liest die Zutaten eines Rezeptes aus
	 * @return
	 */
	public ArrayList<String> readIngredients()
	{
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.clear();
		int i = 0;
		
		while(obj.getJSONObject("parse").getJSONArray("links").length() > i)
		{			
			if(obj.getJSONObject("parse").getJSONArray("links").getJSONObject(i).toString().contains("Zutat:"))
			{
				ingredients.add(obj.getJSONObject("parse").getJSONArray("links").getJSONObject(i).get("*").toString().replace("Zutat:", "").replace(",", ""));
			}

			i++;
		}
				
		return ingredients;
	}
	
	
	/**
	 * lese json-file
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */	
	public String readFromFile() throws IOException, ParseException
	{		
		FileInputStream is = new FileInputStream(Const.JSONFILEPATH);
		InputStreamReader isr = new InputStreamReader(is);
		JSONParser parser = new JSONParser();
		org.json.simple.JSONArray arr = new org.json.simple.JSONArray();
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
		String jsonString = "";
		
		arr = (org.json.simple.JSONArray) parser.parse(isr);
		jsonString = arr.toString();
		return jsonString;
	}
			
	/**
	 * gibt die Eintr�ge aus .json Datei zur�ck
	 * @param jsonString
	 * @return
	 */
	public ArrayList<Pair<String, String>> readEntries(String jsonString)
	{
		String[] infos = new String[2];
		ArrayList<Pair<String, String>> entries = new ArrayList<Pair<String, String>>();
		JSONArray array = new JSONArray(jsonString);
		
		for(int i = 0; i < array.length(); i++)
		{
			entries.add(new Pair(array.getJSONObject(i).getString("title"),array.getJSONObject(i).getJSONArray("Zutaten").toString()));  //= array.getJSONObject(i).getString("title");
			//infos[1] = array.getJSONObject(i).getJSONArray("Zutaten").toString();
			
			//entries.add(infos);
		}
		
		return entries;
	}	
}
	