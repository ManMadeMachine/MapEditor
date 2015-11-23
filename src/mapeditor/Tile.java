package mapeditor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.imageio.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import structs.EditorPreferences;

/**
 * Class for a single Tile object
 * @author ManMadeMachine
 */
public class Tile{
    //Texture name, for example "grass.png"
    private String textureName;
    
    //The actual texture
    private BufferedImage texture = null;
    
    //The resource path
    private final String TEXTURE_PATH = "resources/textures/";
    
    //size of the tile
    private int size;

    /**
     * Constructor for the Tile object.
     * @param tileName the name of the tile/texture
     * @param tileSize the size of the tile
     */
    public Tile(String tileName, int tileSize){
        this.textureName = tileName;
        this.size = tileSize;
        loadTile();
    }
    
    /**
     * Method for loading the tile from the resources.
     */
    private void loadTile(){
        try{
            Path dest = Paths.get(EditorPreferences.getAbsoluteTexturePath(), textureName);
            
            if (dest.toFile().exists()){
                //texture = ImageIO.read(this.getClass().getClassLoader().getResource(TEXTURE_PATH + textureName));
                texture = ImageIO.read(dest.toFile());
            }
            else{
                //If the texture was not found, replace it with the placeholder default texture.
                this.textureName = "default.png";
                dest = Paths.get(EditorPreferences.getAbsoluteTexturePath(), textureName);
                //texture = ImageIO.read(this.getClass().getClassLoader().getResource(TEXTURE_PATH + textureName));
                texture = ImageIO.read(dest.toFile());
            }
        }
        catch(IOException ex){
            System.err.println("Error reading texture file: " + ex.toString());
        }
        
        //Asetetaan ladattu tiili oikean kokoiseksi
        this.texture = resize(this.size);
    }
    
    /**
     * Returns the texture name
     * @return the name of the texture
     */
    public String getTextureName(){
        return this.textureName;
    }
    
    /**
     * Method for setting the tile size
     * @param tileSize the size of the tile
     */
    public void setTileSize(int tileSize){
        this.size = tileSize;
        this.texture = resize(this.size);
    }

    /**
     * Method for getting the tile's texture
     * @return the BufferedImage, i.e. the texture
     */
    public BufferedImage getTexture(){
        return this.texture;
    }
    
    /**
     * Method for setting the texture. Gets the texture's name and reloads the tile
     * @param name texture's name
     */
    public void setTexture(String name){
        this.textureName = name;
        loadTile();
    }
    
    /**
     * Method for resizing the tile. This is used when a new texture has been loaded
     * to resize into the map's tile size
     * @param tileSize the new size
     * @return a new, resized BufferdImage object
     */
    private BufferedImage resize(int tileSize){
        if (this.texture != null){
            int w = this.texture.getWidth();
            int h = this.texture.getHeight();
            
            //Create a temporary texture and create a graphics object from it
            BufferedImage temp = new BufferedImage(tileSize, tileSize, this.texture.getType());
            Graphics2D g = temp.createGraphics();
            
            //Draw the temporary texture to the old texture
            g.drawImage(this.texture, 0, 0, tileSize, tileSize, 0, 0, w, h, null);
            g.dispose();
            
            return temp;
        }
        else{
            return null;
        }
    }
    
    /**
     * Method for drawing the Tile object.
     * @param g The Graphics object to draw with
     * @param x the x position 
     * @param y the y position
     */
    public void paint(Graphics g, int x, int y){
        //If the texture hasn't been loaded yet, draw only a rectangle
        g.drawImage(this.texture, x, y, null);
    }
}
