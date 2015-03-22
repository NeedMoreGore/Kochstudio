package com.thor.kochstudio.constants;

public class Const 
{
    //debugging
    public static final boolean DEBUGMODE = true;

    //fenster
    public static final String WINDOWTITLE = "Kochstudio v0.2";

	//dateipfad
	public static final String DBFILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\db\\kochstudio.db";

	//RezepteWiki webAPI
	public static final String READPAGEID = "http://www.rezeptewiki.org/api.php?format=json&action=parse&pageid=";
	public static final String READALLCAT = "http://www.rezeptewiki.org/api.php?format=json&action=query&list=categorymembers&cmtitle=Kategorie:Rezepte&cmlimit=5000";
	public static final String READALLSUBCAT1 = "http://www.rezeptewiki.org/api.php?format=json&action=query&list=categorymembers&cmpageid=";
	public static final String READALLSUBCAT2 = "&cmlimit=5000";

    //SQL
    public static final String TB_RECIPEINFORMATION = "RECIPE_INFORMATION";
    public static final String TB_PAGE_IDS = "PAGE_IDS";
    public static final String TB_INGREDIENTS = "INGREDIENTS";
    public static final String TB_FAVOURITES = "FAVOURITES_FOLDER";

    //UpdateMode
    public static final boolean UPDATE_INGREDIENTS = false;
}
