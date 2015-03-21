package com.thor.kochstudio.fx.model;

import com.thor.kochstudio.constants.Const;
import com.thor.kochstudio.functional.ManageFavourites;
import com.thor.kochstudio.util.db.SQLLiteHandler;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class LazyTreeItem extends TreeItem<String>
{
    private final int id;
    private boolean isRecipe;
    private String urlTitle;
    private boolean hasLoadedChildren = false;

    private SQLLiteHandler sql = new SQLLiteHandler();
    private ManageFavourites mngFav = new ManageFavourites();

    /**
     * Konstruktor für rootItem
     * @param name - Name des Items
     */
    public LazyTreeItem(String name)
    {
        super(name);
        this.id = 1;
        this.urlTitle = "";
        this.isRecipe = false;
    }

    /**
     * Konstruktor für Children
     */
    public LazyTreeItem(String name, int id, String urlTitle) {
        super(name);
        this.id = id;
        this.urlTitle = urlTitle;
        this.isRecipe = false;
    }

    /**
     * Gibt ObservableList der ItemChildren zurück
     */
    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (!hasLoadedChildren) {
            loadChildren();
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (!hasLoadedChildren) {
            loadChildren();
        }
        return super.getChildren().isEmpty();
    }

    /**
     * lädt Children des Trees und erzeugt TreeItems
     */
    @SuppressWarnings("unchecked")
    private void loadChildren() {
        hasLoadedChildren = true;
        //Anzahl der LazyTreeItems pro Parent ermitteln
        ArrayList<String[]> childrenPerParent = queryChildrenPerParentId();
        //Array für Anzahl der Children initialisieren
        LazyTreeItem[] lazyTreeItems = new LazyTreeItem[childrenPerParent.size()];

        //TreeItems aus Children erzeugen
        for (int i = 0; i < childrenPerParent.size(); i++)
        {
            //DB Einträge laden
            String name = childrenPerParent.get(i)[1]; //NAME in FAVOURITES_FOLDER
            int id = Integer.valueOf(childrenPerParent.get(i)[0]); //ID in FAVOURITES_FOLDER
            int exp = Integer.valueOf(childrenPerParent.get(i)[3]); //EXPANDED in FAVOURITES_FOLDER

            //URLTITLE laden wenn Rezept
            if (!(childrenPerParent.get(i)[4] == null))
                urlTitle = childrenPerParent.get(i)[4];
            else
                urlTitle = "";
            //TreeItem erstellen
            lazyTreeItems[i] = new LazyTreeItem(name,id, urlTitle);

            //wenn lazyTreeItem ein Rezept ist auf isRecipe auf true setzen
            if(!urlTitle.equals(""))
                lazyTreeItems[i].setIsRecipe();
            if(exp == 1)
                lazyTreeItems[i].setExpanded(true);
            else
                lazyTreeItems[i].setExpanded(false);
            //Model erstellen
            //mngFav.loadFavourites(new FavouritesModel(name, id, isRecipe));
        }

        super.getChildren().setAll(lazyTreeItems);
    }

    /**
     * Gibt die Anzahl der Ordner auf einer Ebene zurück
     */
    private ArrayList<String[]> queryChildrenPerParentId()
    {
        String[] clmns = {"ID", "NAME", "PARENT_ID", "EXPANDED", "URLTITLE"};
        ArrayList<String[]> query = new ArrayList<>();
        try
        {
            query = sql.query().queryCustom("SELECT ID, NAME, PARENT_ID, EXPANDED, URLTITLE FROM " + Const.TB_FAVOURITES + " WHERE PARENT_ID = " + id, clmns);
        }
        catch(SQLException e)
        {
            if(Const.DEBUGMODE)
                e.printStackTrace();
        }

        return query;
    }

    /**
     * Setzt isRecipe to true
     */
    public void setIsRecipe()
    {
        isRecipe = true;
    }

    public boolean getIsRecipe()
    {
        return isRecipe;
    }

    public int getId()
    {
        return id;
    }

    public String getUrlTitle()
    {
        return urlTitle;
    }
}