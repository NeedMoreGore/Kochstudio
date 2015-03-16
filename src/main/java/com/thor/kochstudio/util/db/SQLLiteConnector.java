package com.thor.kochstudio.util.db;

import com.thor.kochstudio.constants.Const;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLLiteConnector 
{
    private String path;
	
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
            Connection connect = DriverManager.getConnection("jdbc:sqlite:" + path);

            if(Const.DEBUGMODE)
			    System.out.println("Datenbank erfolgreich geöffnet.");
			
			return connect;
		}
		catch(SQLException e)
		{
            if(Const.DEBUGMODE)
            {
                e.printStackTrace();
                System.out.println("Fehler: Datenbank konnte nicht geöffnet werden.");
            }
            return null;
		}
	}
}
