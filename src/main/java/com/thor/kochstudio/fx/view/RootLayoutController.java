package com.thor.kochstudio.fx.view;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.functional.UpdateHandler;

import java.io.IOException;
import java.sql.SQLException;

public class RootLayoutController 
{
    /*
    startet das Update
     */
    public void startUpdate()
    {
      	UpdateHandler update = new UpdateHandler();

    	try
        {
			update.execUpdate();
		}
        catch (IOException | org.json.simple.parser.ParseException | SQLException e)
        {
            if(Const.DEBUGMODE)
			    e.printStackTrace();
		}

    }
}
