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
	 * Gibt eine ArrayList aus Arrays aller Eintr�ge aus einer Tabelle zur�ck
	 * @param tablename
	 * @param columnNames
	 * @throws Exception
	 * @return
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
     * Gibt eine ArrayList aus Arrays aller Eintr�ge aus einer Tabelle zur�ck
     * @param tablename
     * @param columnNames
     * @throws Exception
     * @return
     */
    public ArrayList<String[]> selectQueryAll(String tablename, String[] columnNames, String[] select)
    {
        String selectString = "";

        for (int i = 0; i < select.length; i++)
        {
            selectString += select[i];

            if(select.length != i+1)
            {
                selectString.concat(",");
            }
        }

        try
        {
            //f�r SQL-Abfragen
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
	 * Gibt eine ArrayList aus Arrays der gesuchten Eintr�ge aus einer Tabelle zur�ck
	 * @param tablename
	 * @param columnNames
	 * @param where
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String[]> queryAllWhere(String tablename, String[] columnNames, String where) throws Exception
	{
		//f�r SQL-Abfragen
		statement = connection.createStatement();
		
		resultSet = statement.executeQuery("SELECT * FROM " + tablename + " WHERE " + where);
		return writeResultSet(resultSet, columnNames);
	}
	
	public String[] queryID(String tablename, String[] columnNames, String id) throws SQLException
	{
		//f�r SQL-Abfragen
		statement = connection.createStatement();

		resultSet = statement.executeQuery("SELECT * FROM " + tablename + " WHERE ID = " + id);
		return writeSingleResultSet(resultSet, columnNames);	
	}
		
	/**
	 * erzeugt ArrayList mit Arrays aus einem ResultSet
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> writeResultSet(ResultSet resultSet, String[] columnnames) throws SQLException
	{
		
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		while(resultSet.next())
		{
			String[] query = new String[columnnames.length];
		
			for(int i = 0; i < columnnames.length; i++)
				query[i] = resultSet.getString(columnnames[i]);
			
			list.add(query);
		}
		
		return list;
	}
	
	public String[] writeSingleResultSet(ResultSet resultSet, String[] columnnames) throws SQLException
	{
		String[] query = new String[columnnames.length];
		
		for(int i = 0; i < columnnames.length; i++)
			query[i] = resultSet.getString(columnnames[i]);
				
		return query;
	}
	/**
	 * ResultSet schlie�en
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
			e.printStackTrace();
		}
	}
}
