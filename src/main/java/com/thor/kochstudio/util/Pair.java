package com.thor.kochstudio.util;

@Deprecated
public class Pair<A, B> {
	  private String first;
	  private String second;

	  public Pair(String first, String second) {
	    this.first = first;
	    this.second = second;
	  }
	  
	  public String getFirst()
	  {
		  return first;
	  }
	  
	  public String getSecond()
	  {
		  return second;
	  }

	}