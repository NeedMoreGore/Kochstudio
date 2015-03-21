package com.thor.kochstudio.functional;

import com.thor.kochstudio.MainApplication;
import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.fx.model.FavouritesModel;
import com.thor.kochstudio.util.db.SQLLiteHandler;
import javafx.scene.control.TreeItem;

import java.sql.SQLException;

public class ManageFavourites extends TreeItem<String>
{	
	private SQLLiteHandler sql;

    public ManageFavourites()
    {
        sql = new SQLLiteHandler();
    }

    /**
     * Schreibt geaddeten Favoriten in die Datenbank
     * @param name - Name des Favoriten
     * @param urlTitle - URL des Rezepts
     */
	public void newFavourite(String name, String urlTitle)
	{
        String clmns[] = {"ID", "NAME", "PARENT_ID", "EXPANDED", "URLTITLE"};
		sql.execute().insertIntoTable(Const.TB_FAVOURITES, clmns, name, "2", "0", urlTitle);
	}

    /**
     * Schreibt den neuen Ordner in die Datenbank
     * @param id - ID in der Datenbank
     */
    public void newFolder(int id)
    {
        String clmns[] = {"ID", "NAME", "PARENT_ID", "EXPANDED", "URLTITLE"};
        sql.execute().insertIntoTable(Const.TB_FAVOURITES, clmns, "Neuer Ordner", String.valueOf(id), "1", "");
    }

    /**
     * ändert den Status von EXPANDED in der DB
     * @param id - ID in der Datenbank
     * @param state - 0 = false, 1 = true
     */
    public void expand(String id, String state)
    {
        try {
            sql.execute().updateRowWhere(Const.TB_FAVOURITES, "EXPANDED", state, id);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }
    }

    /**
     * Löscht Parent + Children in Tree
     * @param id - ID in der Datenbank
     */
    public void delete(int id)
    {
        if(id != 1 && id != 2)
        {
            sql.execute().deleteEntry(Const.TB_FAVOURITES, "ID=" + id);
            sql.execute().deleteEntry(Const.TB_FAVOURITES, "PARENT_ID = " + id);
        }
    }



    //-----------------------------------------------//
    //------------------DEPRECATED-------------------//
    //-----------------------------------------------//

    /**
     * lädt dsa FavoritenModel für den View
     * @param model - Model für Favoriten
     * @deprecated - Wird im Moment nicht verwendet
     */
    @Deprecated
    public void loadFavourites(FavouritesModel model)
    {
        MainApplication.loadFavourites(model);
    }
}

