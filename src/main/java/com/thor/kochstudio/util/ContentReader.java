/**
 * Liest den Inhalt einer Seite aus, der über die WebAPI von RezepteWiki.org geliefert wird
 */

package com.thor.kochstudio.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class ContentReader 
{	

	/**
	 * liest den Inhalt einer Seite und gibt ihn als String zurück
	 * @param urlString - url als String
	 * @return - Inhalt der Seite als String
	 * @throws IOException
	 */
	public String read(String urlString) throws IOException
	{		
		String output = "";
		String output2 = "";
		URL url = new URL(urlString);
    
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		while ((output = in.readLine()) != null)
		{
			output2 = output;
		}
		
		in.close();		
		
		return output2;
	}
}
