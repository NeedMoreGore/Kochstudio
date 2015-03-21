package com.thor.kochstudio.util.db;

import com.thor.kochstudio.constants.Const;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLLiteQuery 
{
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public SQLLiteQuery(Connection connection)
	{
		this.connection = connection;
	}
	
	/**
	 * Gibt eine ArrayList aus Arrays aller Einträge aus einer Tabelle zurück
	 * @param tablename - Tabellenname
	 * @param columnNames - Name der Spalten als Array
	 * @return - gibt ArrayList aller Spalteneinträge zurück
	 */
	public ArrayList<String[]> queryAll(String tablename, String[] columnNames)
	{
        try
        {
            //f�r SQL-Abfragen
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM " + tablename);
            return writeResultSet(resultSet, columnNames);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
            return null;
        }
	}


    /**
     * Gibt eine ArrayList aus Arrays aller Einträge aus einer Tabelle zurück
     * @param tablename - Tabellenname
     * @param columnNames - Name der Spalten als Array
     * @return - Gibt ArrayList aus Arrays zurück
     */
    public ArrayList<String[]> querySelect(String tablename, String[] columnNames, String[] select)
    {
        String selectString = "";

        for (int i = 0; i < select.length; i++)
        {
            selectString += select[i];

            if(select.length != i+1)
            {
                selectString += ",";
            }
        }

        try
        {
            //für SQL-Abfragen
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT " + selectString + " FROM " + tablename);
            return writeResultSet(resultSet, columnNames);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
            return null;
        }
    }

    /**
     * Gibt alle Einträge zurück, die gesucht wurden
     * @param tablename - Tabellenname
     * @param columnNames - Name der Spalten als Array
     * @param select - Select statement ohne "SELECT "
     * @param where - Where statement ohne "WHERE "
     * @return - Gibt ArrayList aus Arrays zurück
     */
    public ArrayList<String[]> querySelectWhere(String tablename, String[] columnNames, String[] select, String where)
    {
        String selectString = "";

        for (int i = 0; i < select.length; i++)
        {
            selectString += select[i];

            if(select.length != i+1)
            {
                selectString += ",";
            }
        }

        try
        {
            //für SQL-Abfragen
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT " + selectString + " FROM " + tablename + " WHERE " + where);
            return writeResultSet(resultSet, columnNames);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
            return null;
        }
    }

	/**
	 * Gibt eine ArrayList aus Arrays der gesuchten Einträge aus einer Tabelle zurück
	 * @param tablename - Tablename
	 * @param columnNames - Name der Spalten als Array
	 * @param where - Where statement ohne "WHERE "
	 * @return - Gibt ArrayList aus Arrays zurück
	 * @throws java.sql.SQLException
	 */
	public ArrayList<String[]> queryWhere(String tablename, String[] columnNames, String where) throws SQLException
	{
		//f�r SQL-Abfragen
		statement = connection.createStatement();
		
		resultSet = statement.executeQuery("SELECT * FROM " + tablename + " WHERE " + where);
		return writeResultSet(resultSet, columnNames);
	}

    /**
     * Gibt den Eintrag einer bestimmten ID zurück
     * @param tablename - Tabellenname
     * @param columnNames - Name der Spalten als Array
     * @param id - ID for query
     * @return - Gibt Array des Eintrags zurück
     * @throws SQLException
     */
	public String[] queryID(String tablename, String[] columnNames, String id) throws SQLException
	{
		//für SQL-Abfragen
		statement = connection.createStatement();

		resultSet = statement.executeQuery("SELECT * FROM " + tablename + " WHERE ID = " + id);
		return writeSingleResultSet(resultSet, columnNames);	
	}
		
	/**
	 * erzeugt ArrayList mit Arrays aus einem ResultSet
	 * @param resultSet - ResultSet der Queries
	 * @return - Wandelt resultSet in ArrayList aus Arrays um
	 * @throws SQLException
	 */
	public ArrayList<String[]> writeResultSet(ResultSet resultSet, String[] columnnames) throws SQLException
	{
		
		ArrayList<String[]> list = new ArrayList<>();
		
		while(resultSet.next())
		{
			String[] query = new String[columnnames.length];
		
			for(int i = 0; i < columnnames.length; i++)
				query[i] = resultSet.getString(columnnames[i]);
			
			list.add(query);
		}

		return list;
	}

    /**
     * Erzeugt ein Array einer Abfrage einer Spalte
     * @param resultSet - resultSet von query
     * @param columnnames - Name der Spalten als Array
     * @return - Gibt Array zurück
     * @throws SQLException
     */
	public String[] writeSingleResultSet(ResultSet resultSet, String[] columnnames) throws SQLException
	{
		String[] query = new String[columnnames.length];
		
		for(int i = 0; i < columnnames.length; i++)
			query[i] = resultSet.getString(columnnames[i]);

		return query;
	}

    /**
     * Maximale Zahl einer Spalte anfragen
     * @param tablename - Tabellenname
     * @param column - Spalte mit max-Wert
     * @throws SQLException
     */
    public String[] queryMax(String tablename, String column[]) throws SQLException
    {
        //für SQL-Abfragen
        statement = connection.createStatement();

        resultSet = statement.executeQuery("SELECT MAX(" + column[0] + ") AS \"MaxHierarchyLevel\" FROM " + tablename);
        return writeSingleResultSet(resultSet, column);
    }

    /**
     * Custom SQL Statement
     */
    public ArrayList<String[]> queryCustom(String custom, String columns[]) throws SQLException
    {
        //für SQL-Abfragen
        statement = connection.createStatement();

        resultSet = statement.executeQuery(custom);
        return writeResultSet(resultSet, columns);
    }

    /**
     * ResultSet schließen
     */
	public void close()
	{
		try
		{
			if(resultSet != null)
				resultSet.close();
			if(statement != null)
				statement.close();
			if(connection != null)
				connection.close();
		}
		catch(Exception e)
		{
            if(Const.DEBUGMODE)
			    e.printStackTrace();
		}
	}
}
