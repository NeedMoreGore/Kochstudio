/**
 * erstellt JSON-Files f�r die Suche von Rezepten
 */

package com.thor.kochstudio.util.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
@deprecated - Information will now be written in database
 */
@Deprecated
public class JSONWriter 
{
	private FileWriter file = null;
	private ArrayList<String> entries = new ArrayList<String>();
	private int count = 0;
	
	/**
	 * schreibt PageID, Titel und Zutaten eines Rezepts in eine Liste
	 * @param pageID
	 * @param title
	 * @param ingredient
	 */
	public void write(String pageID, String title, ArrayList<String> ingredient)
	{
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();	
		String entry = "";
		
		obj.put("pageID", pageID);
		obj.put("title", title);
		
		for(int i = 0; i < ingredient.size(); i++)
		{
			arr.put(i, ingredient.get(i));
		}
		
		obj.put("Zutaten", arr);
		
		entry = obj.toString();
		
		entries.add(entry);	
		
		count++;
	}
	
	/**
	 * schreibt das JSON-File auf die Festplatte
     * @param path - path to the .json save location
	 */
	public void writToFile(String path)
	{		
		System.out.println(count + "Rezepte verfügbar");
		try 
		{
			file = new FileWriter(path);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
        try 
        {
            file.write(entries.toString());
            System.out.println("Successfully copied JSON Object to file.");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
				file.flush();
			} 
            catch (IOException e) 
            {
				e.printStackTrace();
			}
            try 
            {
				file.close();
			} 
            catch (IOException e) 
            {
				e.printStackTrace();
			}
        }
	}	
}
