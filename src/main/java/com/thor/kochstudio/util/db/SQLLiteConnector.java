package com.thor.kochstudio.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLLiteConnector 
{
	private static Connection connect = null;
	private String path = "";
	
	public SQLLiteConnector(String path)
	{
		this.path = path;
	}
	
	public Connection connect() throws Exception
	{
		try 
		{
			//lädt den db driver
			Class.forName("org.sqlite.JDBC");
			
			//konfiguriere connection
			connect = DriverManager.getConnection("jdbc:sqlite:" + path);	
			System.out.println("Datenbank erfolgreich geöffnet.");
			
			return connect;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			
			System.out.println("Fehler: Datenbank konnte nicht geöffnet werden.");
			
			return null;
		}
	}
}
