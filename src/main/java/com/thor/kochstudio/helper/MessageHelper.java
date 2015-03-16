package com.thor.kochstudio.helper;

public class MessageHelper
{
    public static void createMessage(String type, boolean mode, String message, String className)
    {
        if(mode)
            System.out.println(type.toUpperCase() + ": " + message + " (" + className + ")" );
    }

    public static void createMessage(String type, boolean mode, String message)
    {
        if(mode)
            System.out.println(type.toUpperCase() + ": " + message);
    }

    public static void createErrorMessage(boolean mode, String message, String className)
    {
        if(mode)
            System.out.println("ERROR: " + message + " (" + className + ")" );
    }

    public static void createErrorMessage(boolean mode, String message)
    {
        if(mode)
            System.out.println("ERROR: " + message);
    }

    public static void createInfoMessage(boolean mode, String message, String className)
    {
        if(mode)
            System.out.println("INFO: " + message + " (" + className + ")" );
    }

    public static void createInfoMessage(boolean mode, String message)
    {
        if(mode)
            System.out.println("INFO: " + message);
    }

    public static void createWarningMessage(boolean mode, String message, String className)
    {
        if(mode)
            System.out.println("WARNING: " + message + " (" + className + ")" );
    }

    public static void createWarningMessage(boolean mode, String message)
    {
        if(mode)
            System.out.println("WARNING: " + message);
    }
}
