/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import structs.EditorPreferences;

/**
 * Class for the tile selection view
 * @author ManMadeMachine
 */
public class TileView extends JPanel implements ActionListener, ItemListener{
    private MapEditor parent;
    
    //the amount of loaded tiles
    private int tileCount;
    
    //Lists for loaded tiles and their texture names
    private ArrayList<Tile> loadedTiles;
    private ArrayList<String> loadedTileNames;
    
    //The currently selected tile
    private Tile selectedTile;

    //The upmost panel in the Tile View. Shows a thumbnail of the Tile
    private JPanel tilePreview;
    
    //The Tile thumbnail
    private JLabel tileImage;
    
    //Area for Tile selection. Has components for loading a new Tile into the
    //editor and for selecting a loaded Tile from a ComboBox.
    private JPanel tileSelection;
    
    //ComboBox for loaded Tiles.
    private JComboBox tiles;
    
    //Button for loading a Tile
    private JButton addTileButton;
    
    //Button for removing a loaded Tile. Tiles don't have to be loaded into the
    //program when opening a map, they just have to be in the Resources/Textures folder.
    private JButton removeTileButton;
    
    //TODO...
    private Color colorFilter;
    
    int width;
    int height;
    
    //Labels for selected tile and the loaded tile selection
    JLabel selectedLabel;
    JLabel tileSelectionLabel;
    
    /**
     * default constructor. Creates the selection view.
     * @param parent the map editor parent class
     */
    public TileView(MapEditor parent){
        this.parent = parent;
        
        //Set width and height
        this.width = 200;
        this.height = parent.getHeight();
        
        //Start with the default grass tile.
        tileCount = 1;
        
        //All tiles that have been loaded to the editor. Visible in a combobox
        loadedTiles = loadTiles();
        
        //All the names of the tiles.
        loadedTileNames = new ArrayList<String>();
        
        //Populate the tile names from the loaded Tiles list
        for(Tile tile : loadedTiles){
            loadedTileNames.add(tile.getTextureName());
        }
        
        //Set the default selected tile
        selectedTile = loadedTiles.get(0);

        tilePreview = new JPanel();
        
        //Set the layout for the Tile Preview section.
        GroupLayout tilePreviewLayout = new GroupLayout(tilePreview);
        tilePreview.setLayout(tilePreviewLayout);
        
        tilePreviewLayout.setAutoCreateGaps(true);
        tilePreviewLayout.setAutoCreateContainerGaps(true);
        
        //fill the combobox with the default tiles
        tiles = new JComboBox(loadedTileNames.toArray());
        tiles.setMaximumSize(new Dimension(150, 25));
        tiles.addItemListener(this);
        
        tileImage = new JLabel();
        ImageIcon icon = new ImageIcon(resizeToFit(selectedTile.getTexture()));
        tileImage.setIcon(icon);
        tileImage.setPreferredSize(new Dimension(64, 64));
        selectedLabel = new JLabel(parent.languages.getString("Selected_Tile"));
        tilePreviewLayout.setVerticalGroup(tilePreviewLayout.createSequentialGroup()
            .addComponent(selectedLabel)
            .addComponent(tileImage));
        tilePreviewLayout.setHorizontalGroup(tilePreviewLayout.createParallelGroup()
            .addComponent(selectedLabel)
            .addComponent(tileImage));
        
        tilePreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //The tile selection section
        tileSelection = new JPanel();
        tileSelectionLabel = new JLabel(parent.languages.getString("Loaded_Tiles"));
        
        //Create Add and Remove button
        addTileButton = new JButton(parent.languages.getString("Add"));
        addTileButton.addActionListener(this);
        removeTileButton = new JButton(parent.languages.getString("Remove"));
        removeTileButton.addActionListener(this);
        
        
        GroupLayout tileSelectionLayout = new GroupLayout(tileSelection);
        tileSelection.setLayout(tileSelectionLayout);
        
        tileSelectionLayout.setAutoCreateGaps(true);
        tileSelectionLayout.setAutoCreateContainerGaps(true);
        
        tileSelectionLayout.setHorizontalGroup(tileSelectionLayout.createParallelGroup()
            .addComponent(tileSelectionLabel)
            .addComponent(tiles)
            .addGroup(tileSelectionLayout.createSequentialGroup()
                .addComponent(addTileButton)
                .addComponent(removeTileButton)));
        
        tileSelectionLayout.setVerticalGroup(tileSelectionLayout.createSequentialGroup()
            .addComponent(tileSelectionLabel)
            .addComponent(tiles)
            .addGroup(tileSelectionLayout.createParallelGroup()
                .addComponent(addTileButton)
                .addComponent(removeTileButton)));
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(tilePreview, 100, 120, 150)
            .addComponent(tileSelection));
        
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(tilePreview)
            .addComponent(tileSelection));
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addTileButton){
            //Show a file chooser for the selection
            JFileChooser fc = new JFileChooser();
            
            int a = fc.showOpenDialog(parent);
            
            if (a == JFileChooser.APPROVE_OPTION){
                File f = fc.getSelectedFile();

                //Create the destination path to the textures folder
                Path dest = Paths.get(EditorPreferences.getAbsoluteTexturePath(), f.getName());
                
                //Copy the file to the textures folder
                try{
                    //Create input streams and filechannels for the source and destination
                    FileInputStream in = new FileInputStream(f);
                    FileChannel ch1 = in.getChannel();
                    
                    FileOutputStream out = new FileOutputStream(dest.toFile());
                    FileChannel ch2 = out.getChannel();
                    
                    ch1.transferTo(0, f.length(), ch2);

                    ch1.close();
                    ch2.close();
                }
                catch(IOException ex){
                    System.err.println("Error while copying image file to texture folder!");
                    ex.printStackTrace();
                }
                
                //Create a new Tile from the copied File and add it to the combo box
                Tile newTile = new Tile(dest.toFile().getName(), parent.getTileSize());
                
                //Add the added tile to the combo box
                loadedTileNames.add(f.getName());
                loadedTiles.add(newTile);
                
                tiles.addItem(loadedTileNames.get(loadedTileNames.size()-1));
            }
            else{
                System.out.println("Tile loading cancelled by the user!");
            }
        }
        else if (e.getSource() == removeTileButton){
            //Remove the selected Tile from the textures folder
            int i = tiles.getSelectedIndex();
            String selectedName = loadedTileNames.get(i);
            Path dest = Paths.get(EditorPreferences.getAbsoluteTexturePath(), selectedName);
            
            //Try to delete the file from the textures folder
            try{
                Files.delete(dest);
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            
            //If we get here, we can delete all other stuff from the Editor
            loadedTileNames.remove(i);
            loadedTiles.remove(i);
            tiles.removeItemAt(i);
            /*tiles.remove(i);*/
            tiles.revalidate();
            tiles.repaint();
        }
    }
    
    /*
     Listener for the combo box
     */
    @Override
    public void itemStateChanged(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED){
            int i = tiles.getSelectedIndex();
            selectedTile = loadedTiles.get(i);
            ImageIcon icon = new ImageIcon(resizeToFit(selectedTile.getTexture()));
            tileImage.setIcon(icon);
        }
    }
    
    /**
     * Method used to translate the selection view to the current selected language
     */
    public void translate(){
        //Set the texts again according to the new Locale
        this.selectedLabel.setText(parent.languages.getString("Selected_Tile"));
        this.tileSelectionLabel.setText(parent.languages.getString("Loaded_Tiles"));
        this.addTileButton.setText(parent.languages.getString("Add"));
        this.removeTileButton.setText(parent.languages.getString("Remove"));
    }
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(this.width, this.height);
    }
    
    /**
     * Loads the tiles from the resource folder to the tile selection on startup.
     * 
     * @return List of loaded tiles 
     */
    public ArrayList<Tile> loadTiles(){
        //List of the Tiles read.
        ArrayList<Tile> ret = new ArrayList<>();

        //Create the path to the texture folder
        Path dest = Paths.get(EditorPreferences.getAbsoluteTexturePath());

        File folder = new File(dest.toString());
        
        for (File file : folder.listFiles()){
            if (!file.isDirectory()){
                //System.out.println("Jee!");
                //Populate the array with the Tile object created from the files
                ret.add(new Tile(file.getName(), parent.getTileSize()));
                //System.out.println("Lisättiin!");
            }
        }

        return ret;
    }
    
    /**
     * Returns the current selected Tile
     * @return current selected Tile
     */
    public Tile getSelectedTile(){
        return selectedTile;
    }
    
    /**
     * Method for resizing the currently selected Tile
     * @param newSize new tile size
     */
    public void resizeSelectedTile(int newSize){
        this.selectedTile = new Tile(this.selectedTile.getTextureName(), newSize);
    }
    
    /**
     * Method for resizing all the loaded tiles in the editor. Used when a new
     * map has been created or loaded.
     * @param newSize new tile size
     */
    public void resizeTiles(int newSize){
        //Resize every loaded tile to the new tile size
        for(int i = 0; i < loadedTiles.size(); ++i){
            loadedTiles.get(i).setTileSize(newSize);
        }
    }
    
    /**
     * Method used to resize the preview BufferedImage to 64x64 image.
     * @param img Image object to resize, usually the selected tile
     * @return new resized BufferedImage
     */
    private BufferedImage resizeToFit(BufferedImage img){
        int w = img.getWidth();
        int h = img.getHeight();
        
        //The new, resized image
        BufferedImage newImg = new BufferedImage(64, 64, img.getType());
        
        //Get the new image's graphics2d -object
        Graphics2D g = newImg.createGraphics();
        
        g.drawImage(img, 0,0, 64, 64, 0,0, w, h, null);
        
        g.dispose();
        return newImg;
    }
}
