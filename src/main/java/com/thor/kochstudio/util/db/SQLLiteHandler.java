package com.thor.kochstudio.util.db;

import com.thor.kochstudio.constants.Const;

import java.sql.Connection;

public class SQLLiteHandler 
{
	private SQLLiteQuery reader = null;
	private SQLLiteExecuter writer = null;

    public SQLLiteHandler()
	{
		SQLLiteConnector connector = new SQLLiteConnector(Const.DBFILEPATH);
        Connection connection = null;

        try
        {
			connection = connector.connect();
		}
        catch (Exception e)
        {
            if(Const.DEBUGMODE)
			    e.printStackTrace();
		}
		
		SQLLiteQuery reader = new SQLLiteQuery(connection);
		SQLLiteExecuter writer = new SQLLiteExecuter(connection);
		
		this.reader = reader;
		this.writer = writer;
	}
	
	public SQLLiteQuery query()
	{
		return reader;
	}
	
	public SQLLiteExecuter execute()
	{
		return writer;
	}
	

}