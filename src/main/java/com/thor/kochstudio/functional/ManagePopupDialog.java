package com.thor.kochstudio.functional;

import com.thor.kochstudio.MainApplication;
import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.util.db.SQLLiteHandler;

import java.sql.SQLException;

/**
 * Created by z0r on 22.03.2015.
 */
public class ManagePopupDialog
{
    static MainApplication mainApp;

    public static int[] queryIngredientSize()
    {
        SQLLiteHandler sql = new SQLLiteHandler();
        int[] size = new int[2];
        String[] clmn = {"ID"};
        try {
            size[0] = Integer.valueOf(sql.query().queryMin(Const.TB_INGREDIENTS, clmn)[0]);
            size[1] = Integer.valueOf(sql.query().queryMax(Const.TB_INGREDIENTS, clmn)[0]);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
            return null;
        }

        return size;
    }

    public static String queryIngredient(int id)
    {
        SQLLiteHandler sql = new SQLLiteHandler();

        String[] clmn = {"Name"};
        String name;

        name = sql.query().querySelectWhere(Const.TB_INGREDIENTS, clmn, clmn, "ID=" + id).get(0)[0];

        return name;
    }

    public static void addToIgnore(String select)
    {
        mainApp.getIngredientsList().remove(select);
        mainApp.getIgnoreList().add(select);
    }

    public static void removeFromIgnore(String select)
    {
        mainApp.getIngredientsList().add(select);
        mainApp.getIgnoreList().remove(select);
    }

}
