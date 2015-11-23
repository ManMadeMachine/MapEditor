/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.awt.Color;
import java.util.Locale;
import mapeditor.MapEditor;

/**
 * A helper class to hold all the editor settings.
 */
public class EditorPreferences {
    private static boolean drawGrid = true;
    private static Color gridColor = Color.GREEN;
    private static Color tileHighlightColor = Color.RED;
    private static Color selectedAreaColor = Color.YELLOW;
    private static Color backgroundColor = Color.CYAN;
    
    //This is a TODO...
    private static int brushSize = 1;

    //Absolute path for the textures
    
    private static final String absoluteTexturePathDEBUG = System.getProperty("user.dir") + "/build/classes/resources/textures/";
    
    private static final String absoluteTexturePath = System.getProperty("user.dir") + "/resources/textures/";
    
    //i18n options
    private static String language = "fi";
    private static String country = "FI";
    
    private static Locale currentLocale = new Locale(language, country);
    
    /**
     * Tells if the grid is drawn
     * @return true if grid is drawn, false otherwise
    */
    public static boolean isDrawingGrid(){
        return drawGrid;
    }
    
    /**
     * Method for setting the grid drawing boolean
     * @param draw boolean for grid drawing
     */
    public static void isDrawingGrid(boolean draw){
        drawGrid = draw;
    }
    
    /**
     * Returns the grid color
     * @return Color of the grid
     */
    public static Color getGridColor(){
        return gridColor;
    }
    
    /**
     * Sets the grid color
     * @param c Color to set the grid to
     */
    public static void setGridColor(Color c){
        if (c != null){
            gridColor = c;
        }
        else{
            System.err.println("ERROR: EditorPreferences; setGridColor(Color c): c was null!");
        }
    }
    
    /**
     * Returns the mouseover highlight color
     * @return mouseover highlight color
     */
    public static Color getTileHighlightColor(){
        return tileHighlightColor;
    }
    
    /**
     * Sets the mouseover highlight color
     * @param c the new mouseover highlight color
     */
    public static void setTileHighlightColor(Color c){
        if (c != null){
            tileHighlightColor = c;
        }
        else{
            System.err.println("ERROR: EditorPreferences; setTileHighlightColor(Color c): c was null!");
        }
    }
    
    /**
     * Returns the selected area highlight color
     * @return selected area highlight color
     */
    public static Color getSelectedAreaColor(){
        return selectedAreaColor;
    }
    
    /**
     * Sets the selected area highlight color
     * @param c new selected area higlight color
     */
    public static void setSelectedAreaColor(Color c){
        if (c != null){
            selectedAreaColor = c;
        }
        else{
            System.err.println("ERROR: EditorPreferences; setSelectedAreaColor(Color c): c was null!");
        }
    }
    
    /**
     * Returns the map background color
     * @return the color of the map's background
     */
    public static Color getBackgroundColor(){
        return backgroundColor;
    }
    
    /**
     * Sets the map background color
     * @param c new background color
     */
    public static void setBackgroundColor(Color c){
        if (c != null){
            backgroundColor = c;
        }
    }

    /**
     * Returns the absolute path for the texture folder
     * @return absolute texture folder path
     */
    public static String getAbsoluteTexturePath(){
        if (MapEditor.DEBUG){
            return absoluteTexturePathDEBUG;
        }
        else{
            return absoluteTexturePath;
        }
    }
    
    /**
     * Returns the current language
     * @return current language
     */
    public static String getCurrentLanguage(){
        return language;
    }
    
    /**
     * Returns the current country
     * @return current country
     */
    public static String getCurrentCountry(){
        return country;
    }
    
    /**
     * Returns the current Locale
     * @return current Locale
     */
    public static Locale getCurrentLocale(){
        return currentLocale;
    }
    
    /**
     * Sets the current locale
     * @param lang new language
     * @param ctry new country
     */
    public static void setCurrentLocale(String lang, String ctry){
        language = lang;
        country = ctry;
        
        currentLocale = new Locale(language, country);
    }
}
