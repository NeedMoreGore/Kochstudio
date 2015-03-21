package com.thor.kochstudio.util.db;

import com.thor.kochstudio.constants.Const;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLLiteExecuter 
{
	private Connection connection;
	private PreparedStatement prepStatement;
	private Statement statement;
	
	public SQLLiteExecuter(Connection connection)
	{
		this.connection = connection;
	}
	
	/**
	 * fügt Einträge zu einer Tabelle hinzu
	 * @param pattern - Muster für VALUES(pattern)
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
	 * lösche alle Einträge aus Tabelle
	 * @param tablename - Tabellenname
	 * @throws SQLException 
	 */
	public void clearTable(String tablename) throws SQLException
	{
		prepStatement = connection.prepareStatement("DELETE FROM " + tablename);
		prepStatement.executeUpdate();
	}

    /**
     * Lösche Eintrag aus Tabelle mit WHERE-Bedingung
     * @param tablename - Tabellenname
     * @param where - Where Bedingung ohne "WHERE "
     */
    public void deleteEntry(String tablename, String where)
    {
        try
        {
            statement = connection.createStatement();
            statement.execute("DELETE FROM " + tablename + " WHERE " + where);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }
    }
	
	/*
	 * @param tablename - Name der Tabelle
	 * @param execute - SQL Statement ohne CREATE TABLE
	 * e.g. tablename ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, ... 
	 */
	public void createTable(String tablename, String execute) throws SQLException
	{
		statement = connection.createStatement();	

        statement.execute("CREATE TABLE IF NOT EXISTS " + tablename + "(" + execute + ")");
		statement.close();				
	}

    /**
     * Fügt neue Einträge einer Tabelle hinzu
     * @param tablename - Tabellenname
     * @param columns - Name der Spalten als Array
     * @param val - Werte  als Array (erste Spalte(ID) ist automatisch NULL)
     */
    public void insertIntoTable(String tablename, String[] columns, String... val)
    {
        String clms = "(";
        String vls = "(NULL,";

        try
        {
            statement = connection.createStatement();
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }

        //Werte formatieren
        for (int i = 0; i < columns.length; i++)
        {
            clms += columns[i];

            if (i + 1 == columns.length)
            {
                clms += ")";
            }
            else
            {
                clms += ",";
            }
        }

        for (int i = 0; i < val.length; i++)
        {
            vls += "'" + val[i] + "'";

            if (i + 1 == val.length)
            {
                vls += ")";
            }
            else
            {
                vls += ",";
            }
        }

        try
        {
            statement.execute(("INSERT INTO " + tablename + " " + clms + " VALUES " + vls));
        }
        catch (SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }
    }

    public void updateRowWhere(String tablename, String setAttribute, String setValue, String id) throws SQLException
    {
        statement = connection.createStatement();

        statement.execute("UPDATE " + tablename + " SET " + setAttribute + " = '" + setValue +  "' WHERE ID = " + id);
        statement.close();
    }

    /**
     * Löscht Einträge aus RECIPE_INFORMATION, wenn die Rezepte nicht mehr existieren
     */
    public void deleteIfNotExists()
    {
        try
        {
            statement = connection.createStatement();
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }

        try
        {
           statement.execute("DELETE FROM RECIPE_INFORMATION " +
                   "WHERE NOT EXISTS (" +
                   "SELECT p.PAGE_ID, r.PAGE_ID " +
                   "FROM PAGE_IDS p, RECIPE_INFORMATION r " +
                   "WHERE r.PAGE_ID = p.PAGE_ID)");
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }
    }
}
