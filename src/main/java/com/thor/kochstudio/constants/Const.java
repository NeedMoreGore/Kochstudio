package com.thor.kochstudio.constants;

public class Const 
{
	//dateipfad
	public static final String DBFILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\db\\kochstudio.db";
	public static final String JSONFILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\json\\RezepteInfo.json";
	public static final String IDSFILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\id\\PageIDs.properties";
	public static final String FAVOURITESPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\json\\Favourites.json";
	
	//RezepteWiki webAPI
	public static final String READPAGEID = "http://www.rezeptewiki.org/api.php?format=json&action=parse&pageid=";
	public static final String READALLCAT = "http://www.rezeptewiki.org/api.php?format=json&action=query&list=categorymembers&cmtitle=Kategorie:Rezepte&cmlimit=5000";
	public static final String READALLSUBCAT1 = "http://www.rezeptewiki.org/api.php?format=json&action=query&list=categorymembers&cmpageid=";
	public static final String READALLSUBCAT2 = "&cmlimit=5000";
    public static final String TB_RECIPEINFORMATION = "RECIPE_INFORMATION";

    public static final boolean DEBUGMODE = false;
}
