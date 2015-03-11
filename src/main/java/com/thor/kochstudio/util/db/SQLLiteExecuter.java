package com.thor.kochstudio.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLLiteExecuter 
{
	private Connection connection = null;
	private PreparedStatement prepStatement = null;
	private Statement statement = null;
	
	public SQLLiteExecuter(Connection connection)
	{
		this.connection = connection;
	}
	
	/**
	 * f�gt Eintr�ge zu einer Tabelle hinzu
	 * @param pattern - Muster f�r VALUES(pattern)
	 * @param data - Daten die einer Row hinzugef�gt werden sollen
	 * @param tablename - Name der Tabelle
	 */
	public void insert(String pattern, String[] data, String tablename) throws SQLException
	{
		//SQL-Abfrage
		prepStatement = connection.prepareStatement("INSERT INTO " + tablename +  " VALUES (" + pattern + ")");
	
		for(int i = 0; i < data.length; i++)
		{
			prepStatement.setString(i + 1, data[i]);
		}

		prepStatement.executeUpdate();
	}
	
	/**
	 * l�sche alle Eintr�ge aus Tabelle
	 * @param tablename
	 * @throws SQLException 
	 */
	public void clearTable(String tablename) throws SQLException
	{
		prepStatement = connection.prepareStatement("DELETE FROM " + tablename);
		prepStatement.executeUpdate();
	}
	
	/*
	 * @param tablename - Name der Tabelle
	 * @param execute - SQL Statement ohne CREATE TABLE
	 * e.g. tablename ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, ... 
	 */
	public void createTable(String tablename, String execute) throws SQLException
	{
		statement = connection.createStatement();	
		System.out.println("CREATE TABLE IF NOT EXISTS" + tablename + "(" + execute + ")");
		statement.execute("CREATE TABLE IF NOT EXISTS " + tablename + "(" + execute + ")");
		statement.close();				
	}
}
