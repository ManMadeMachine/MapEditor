/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import mapeditor.Tile;

/**
 * This class hold the actual map data.
 * @author ManMadeMachine
 */
public class Map {
    //Map variables
    private String name;
    private int width;
    private int height;
    private int rows;
    private int columns;
    private int tileSize;
    
    //The actual Tile map array that holds all the individual tiles.
    private Tile[][] map;
    
    /**
     * Default Map class constructor.
     * @param name name of the map
     * @param rows map rows
     * @param columns map columns
     * @param tileSize tile size for the map
     */
    public Map(String name, int rows, int columns, int tileSize){
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.width = columns * tileSize;
        this.height = rows * tileSize;
        this.tileSize = tileSize;
        
        this.map = new Tile[rows][columns];
    }
    
    /**
     * Returns the map's name
     * @return map name
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Returns the map's height in pixels
     * @return map's height in pixels
     */
    public int getHeight(){
        return this.height;
    }
    
    /**
     * Returns the map's width in pixels
     * @return map's width in pixels
     */
    public int getWidth(){
        return this.width;
    }
    
    /**
     * Returns the row count of the map
     * @return map's row count
     */
    public int getRows(){
        return this.rows;
    }
    
    /**
     * Returns the column count of the map
     * @return map's column count
     */
    public int getColumns(){
        return this.columns;
    }
    
    /**
     * Returns the tile size of the map
     * @return map's tile size
     */
    public int getTileSize(){
        return this.tileSize;
    }
    
    /**
     * Returns the map array
     * @return map array
     */
    public Tile[][] getMapArray(){
        return this.map;
    }
    
    /**
     * Returns a single Tile from the map array
     * @param i row index of the tile
     * @param j columns index of the tile
     * @return Tile object from the map array
     */
    public Tile getTile(int i, int j){
        if (i >= 0 && i < this.rows && j >= 0 && j < this.columns){
            return map[i][j];
        }
        else{
            return null;
        }
    }
    
    /**
     * Sets a single tile in the map array to the given Tile
     * @param tile Tile to be set to the array
     * @param i row index on the map array
     * @param j columns index on the map array
     */
    public void setTile(Tile tile, int i, int j){
        if (tile != null && i >= 0 && j >= 0 && i < this.rows && j < this.columns){
            this.map[i][j] = tile;
        }
    }
    
    /**
     * Removes a single Tile from the map array
     * @param i row
     * @param j column
     */
    public void removeTile(int i, int j){
        if (i >= 0 && j >= 0 && i < this.rows && j < this.columns){
            this.map[i][j] = null;
        }
    }
}
