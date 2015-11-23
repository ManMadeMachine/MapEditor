package mapeditor;

import edits.AddTile;
import edits.ClearSelection;
import edits.PaintSelectedArea;
import edits.RemoveTile;
import edits.SelectMultipleTiles;
import edits.SelectTile;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.undo.UndoManager;
import structs.EditorPreferences;
import structs.Map;

/**
 * Class for managing all Map related actions, for example saving, loading and drawing.
 */

public class MapManager extends JPanel{
    //Constants for saving and loading map data
    public static final String MAP_NAME_HEADER = "name:";
    public static final String ROWS_HEADER = "rows:";
    public static final String COLUMNS_HEADER = "columns:";
    public static final String TILESIZE_HEADER = "tileSize:";
    public static final String TILE_HEADER = "tile:";
    
    //Parent class
    private MapEditor parent;
    
    //The actual Map
    private Map map;
    
    //Mouse listener for the map.
    private MapMouseListener mouse;
    
    //The selected Map Action, which defines what the user is doing
    private MapActions selectedAction;
    
    //List of selected tile Points.
    private ArrayList<Point> selectedTiles;
    
    //UndoManager, manages all the edits done on the map
    UndoManager undoManager = new UndoManager();
            
    /**
     * Default Constructor, creates a default sized tilemap, which is 20x20 tiles,
     * with the size 32 (meaning the tiles are 32x32 pixels)
     * 
     * @param parent The parent MapEdtior class
     */
    public MapManager(MapEditor parent){
        this.parent = parent;
        
        //Create the actual map with default values (done when the program starts).
        this.map = new Map("Default map", 20, 20, 32);
        
        //initialize selected points list
        this.selectedTiles = new ArrayList<Point>();
        
        //Create mouse listener and add the listener to the manager.
        mouse = new MapMouseListener(this);
        this.addMouseMotionListener(mouse);
        this.addMouseListener(mouse);
    }
    
    /**
     * Constructor for the manager. Creates a manager based on usually a loaded map.
     * @param parent the parent mapeditor class
     * @param map Map object, which is received from a file for example.
     */
    public MapManager(MapEditor parent, Map map){
        this.parent = parent;
        this.map = map;
        
        this.selectedTiles = new ArrayList<Point>();
        
        //Create mouse listener and add the listener to the manager.
        mouse = new MapMouseListener(this);
        this.addMouseMotionListener(mouse);
        this.addMouseListener(mouse);
    }

    /**
     * Constructor that creates a manager and a new map.
     * 
     * @param parent the parent mapeditor class
     * @param rows  rows of the map
     * @param columns columns of the map
     */
    public MapManager(MapEditor parent, int rows, int columns){
        this.parent = parent;

        this.map = new Map("Default map", rows, columns, 32);
        
        this.selectedTiles = new ArrayList<Point>();
        
        mouse = new MapMouseListener(this);
        this.addMouseMotionListener(mouse);
        this.addMouseListener(mouse);
    }
    
    /**
     * Constructor that creates a manager and a new map.
     * @param parent the parent mapeditor class
     * @param name name of the map
     * @param rows rows of the map
     * @param columns columns of the map
     * @param tileSize tile size of the map
     */
    public MapManager(MapEditor parent, String name, int rows, int columns, int tileSize){
        this.parent = parent;

        this.map = new Map(name, rows, columns, tileSize);
        
        this.selectedTiles = new ArrayList<Point>();
        
        this.mouse = new MapMouseListener(this);
        this.addMouseMotionListener(this.mouse);
        this.addMouseListener(this.mouse);
    }
    
    /**
     * Takes a new map and resets it in the manager
     * @param newMap the new map
     */
    public void createMap(Map newMap){
        this.map = newMap;
        parent.refreshMap(this.map);
    }

    /**
     * Returns the map.
     * @return the current map
     */
    public Map getMap(){
        return map;
    }
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(map.getWidth(), map.getHeight());
    }
    
    /**
     * Method for setting a tile on the map. Creates and adds a new UndoableEdit
     * to the undo manager.
     */
    public void setTile(){
        //Get the mouse position on the map
        Point pos = mouse.getPosition();
        
        //Get the tile size
        int tileSize = this.map.getTileSize();
        
        //Calculate the index in the map array (i = y, j = x)
        int i = pos.y / tileSize;
        int j = pos.x / tileSize;
        
        //Check the index, set new tile and repaint
        if (i >= 0 && i < this.map.getRows() && j >= 0 && j < this.map.getColumns()){
            //Get the selected tile
            Tile tile = parent.getTile();
            
            //add a new AddTile edit and refresh undo and redo menu items
            undoManager.addEdit(new AddTile(this.map, tile, i, j));
            parent.refreshUndoAndRedo();

            //repaint the map
            repaint();   
        }
    }
    
    /**
     * Method for removing a tile on the map. Creates and adds a new UndoableEdit
     * to the undo manager.
     */
    public void removeTile(){
        Point pos = mouse.getPosition();
        
        int tileSize = this.map.getTileSize();
        
        int i = pos.y / tileSize;
        int j = pos.x / tileSize;
        
        if (i >= 0 && i < this.map.getRows() && j >= 0 && j < this.map.getColumns()){
            undoManager.addEdit(new RemoveTile(this.map, i, j));
            parent.refreshUndoAndRedo();
            repaint();
        }
    }
    
    //Select a single Tile from the map. This actually isn't really relevant, 
    //since the implementation of single Tile's properties is still undone. 
    //In the future, the user will be able to select a single Tile, and see it's
    //properties in the right-hand-side tileView. Properties will include stuff
    //like isCollidable, isItem etc., which will also be used in the possible game
    //for which I'm building this editor.
    
    /**
     * Method for selecting a single tile on the map. Creates and adds a new UndoableEdit
     * to the undo manager.
     */
    public void selectTile(){
        Point pos = mouse.getPosition();
        int tileSize = this.map.getTileSize();
        
        int i = pos.y / tileSize;
        int j = pos.x / tileSize;
        
        if (i >= 0 && i < this.map.getRows() && j >= 0 && j < this.map.getColumns()){
            undoManager.addEdit(new SelectTile(this, new Point(j, i)));
            
            //Refresh the menu buttons.
            parent.refreshUndoAndRedo();
            this.repaint();
        }
    }
    
    /**
     * Method for selecting multiple tiles from the map. Adds a single tile to the list
     * of selected tiles.
     */
    public void addPointToSelection(){
        Point pos = mouse.getPosition();
        
        int tileSize = this.map.getTileSize();
        int i = pos.y / tileSize;
        int j = pos.x / tileSize;
        
        if (i >= 0 && i < this.map.getRows() && j >= 0 && j < this.map.getColumns()){
            Point p = new Point(j, i);
            
            //Only add the tile if it isn't already in the list of selected points
            if (!this.selectedTiles.contains(p)){
                undoManager.addEdit(new SelectMultipleTiles(this, p));
                parent.refreshUndoAndRedo();
            }
            
            repaint();
        }
    }
    
    /**
     * Method for painting the selected area.
     */
    public void paintArea(){
        //Only paint the area, if there was an area selected.
        if (this.selectedTiles.isEmpty()){
            System.out.println("ERROR: Selected area was empty!");
        }
        else{
            this.undoManager.addEdit(new PaintSelectedArea(this, parent.getTile()));
            parent.refreshUndoAndRedo();
        }
        
        this.repaint();
    }
    
    /**
     * Method for clearing the selected area.
     */
    public void clearSelection(){
        //Only clear the selection if there was a selection
        if (!this.selectedTiles.isEmpty()){
            undoManager.addEdit(new ClearSelection(this));
            parent.refreshUndoAndRedo();
        }
    }
    
    /**
     * Tells if the undomanager of the map manager can undo edits.
     * @return true if undomanager can undo edits, false otherwise.
     */
    public boolean canUndo(){
        return this.undoManager.canUndo();
    }
    
    /**
     * Tells if the undomanager of the map manager can redo edits.
     * @return true if undomanager can redo edits, false otherwise.
     */
    public boolean canRedo(){
        return this.undoManager.canRedo();
    }
    
    /**
     * Method for setting the selected action.
     * @param action the selected action.
     */
    public void setSelectedAction(MapActions action){
        this.selectedAction = action;
    }
    
    /**
     * Method for getting the selected action
     * @return the selected action
     */
    public MapActions getSelectedAction(){
        return this.selectedAction;
    }
    
    /**
     * Method for getting the selected area
     * @return list of Points i.e. the selected area
     */
    public ArrayList<Point> getSelectedArea(){
        return this.selectedTiles;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        //First we paint the whole map's background
        g.setColor(EditorPreferences.getBackgroundColor());
        g.fillRect(0, 0, this.map.getWidth(), this.map.getHeight());
        
        //Paint the map
        int rows = this.map.getRows();
        int columns = this.map.getColumns();
        
        int tileSize = this.map.getTileSize();
        
        //Paint the map itself
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < columns; ++j){
                //If the map cell has a tile to be drawn, draw it
                if (this.map.getTile(i, j) != null){
                    this.map.getTile(i, j).paint(g, j*tileSize, i*tileSize);
                }
            }
        }
        
        //Paint the grid, if it is painted
        if (EditorPreferences.isDrawingGrid()){
            g.setColor(EditorPreferences.getGridColor());
            
            int w = this.map.getWidth();
            int h = this.map.getHeight();

            //Draw lines between each column and row. This needs two separate loops
            for (int i = 0; i < columns; ++i){
                g.drawLine(i*tileSize, 0, i*tileSize, i*h);
            }
            
            for (int i = 0; i < this.map.getRows(); ++i){
                g.drawLine(0, i*tileSize, i*w, i*tileSize);
            }
        }
        
        //Paint the possible selected area, if any
        if (this.selectedTiles.size() > 0){
            g.setColor(EditorPreferences.getSelectedAreaColor());
            for(int i = 0; i < this.selectedTiles.size(); ++i){
                Point p = this.selectedTiles.get(i);
                g.drawRect(p.x * tileSize, p.y * tileSize, tileSize, tileSize);
            }
        }
        
        //Paint mouseOver highlighting
        Point pos;
                
        if (mouse != null){
            pos = mouse.getPosition();
                    
            if (pos != null){
                g.setColor(EditorPreferences.getTileHighlightColor());
                
                g.drawRect((pos.x / tileSize) * tileSize, (pos.y / tileSize) * tileSize,
                        tileSize, tileSize);   
            }
        }
    }
    
    /**
     * Saves the current map to a text file. 
     * @param mapManager the map manager that is being saved.
     * @param path the path were the map is being saved
     */
    public static void saveMap(MapManager mapManager, String path){
        //There was a path given, where we can save the map.
        if (path != null){
            try{
                String newLine = System.getProperty("line.separator");
                //Create a file output stream writer, and assign a file path for the writer
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
                
                Map map = mapManager.getMap();
                //Write map related data to the beginning of the file
                writer.write(MAP_NAME_HEADER + map.getName() + newLine);
                //Rows, columns, tilesize
                writer.write(ROWS_HEADER + map.getRows() + newLine);
                writer.write(COLUMNS_HEADER + map.getColumns() + newLine);
                writer.write(TILESIZE_HEADER + map.getTileSize() + newLine);
                
                //Get the map before iteration to decrease function calls.
                Tile[][] mapArray = map.getMapArray();
                
                //Iterate through the map array and write tile data (indices and tile name for now)
                for(int i = 0; i < map.getRows(); ++i){
                    for(int j = 0; j < map.getColumns(); ++j){
                        //Write the data if there is a tile
                        if (mapArray[i][j] != null){
                            writer.write(TILE_HEADER + i + "," + j + "," + mapArray[i][j].getTextureName() + newLine);
                        }
                    }
                }
                
                //Close the writer
                writer.close();
                System.out.println("Tallennus onnistui!");
            }
            catch(IOException e){
                System.err.println("Tallennus epäonnistui!");
            }
        }
    }
    
    /**
     * Method for loading a saved map.
     * @param path the path of the saved map
     * @return a new Map object, created from the loaded data.
     */
    public static Map loadMap(String path){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            
            //Map variables, read line by line.
            Map map = null;
            String name = null;
            int rows = 0;
            int columns = 0;
            int tileSize = 0;
            
            //Separator for tile values
            String separator = ",";
            
            String line = reader.readLine();
            
            while (line != null){
                //Probably faster to check for a line with Tile info first..
                if (line.startsWith(TILE_HEADER)){
                    //When the loop gets here, we can first try to create a new Map object
                    if (map == null){
                        map = new Map(name, rows, columns, tileSize);
                    }
                    
                    //if the map has been created, read the tile data. The tile array
                    //has been initialized in the Map constructor, so no need to check for
                    //its existence.
                    
                    //Split the line into parts separated by commas.
                    String[] parts = line.substring(TILE_HEADER.length()).split(separator);

                    //The values are in the order: i, j, tile texture name
                    int i = 0;
                    int j = 0;

                    try{
                        i = Integer.parseInt(parts[0]);
                        j = Integer.parseInt(parts[1]);
                    }
                    catch (NumberFormatException e){
                        System.err.println("Error while parsing tile index data!");
                    }
                    
                    //Create the new Tile and set it to it's position in the array
                    map.setTile(new Tile(parts[2], tileSize), i, j);
                }
                else if (line.startsWith(MAP_NAME_HEADER)){
                    name = line.substring(MAP_NAME_HEADER.length()).trim();
                }
                else if (line.startsWith(ROWS_HEADER)){
                    try{
                        rows = Integer.parseInt(line.substring(ROWS_HEADER.length()).trim());
                    }
                    catch (NumberFormatException e){
                        System.err.println("Error while parsing map rows!");
                    }
                }
                else if (line.startsWith(COLUMNS_HEADER)){
                    try{
                        columns = Integer.parseInt(line.substring(COLUMNS_HEADER.length()).trim());
                    }
                    catch(NumberFormatException e){
                        System.err.println("Error while parsing map columns!");
                    }
                }
                else if (line.startsWith(TILESIZE_HEADER)){
                    try{
                        tileSize = Integer.parseInt(line.substring(TILESIZE_HEADER.length()).trim());
                    }
                    catch(NumberFormatException e){
                        System.err.println("Error while parsing map tilesize!");
                    }
                }
                
                //Read the next line
                line = reader.readLine();
            }
            reader.close();
            return map;
        }
        catch(IOException e){
            System.err.println("Lataus epäonnistui!");
            return null;
        }
    }
}