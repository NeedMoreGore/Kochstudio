package com.thor.kochstudio.fx.view;

import com.thor.kochstudio.functional.UpdateHandler;

import java.io.IOException;
import java.sql.SQLException;

public class RootLayoutController 
{
    public void startUpdate()
    {
      	UpdateHandler update = new UpdateHandler();

    	try {
			update.execUpdate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

    }
}
