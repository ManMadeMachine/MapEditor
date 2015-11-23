package mapeditor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.border.BevelBorder;
import structs.EditorPreferences;
import structs.Map;

/**
 *
 * @author Eetu Suonpää
 * 
 * Main class for the map editor.
 */
public class MapEditor extends JFrame implements ActionListener{
    
    public final static boolean DEBUG = true;
    
    //Scrollpane for the map's viewport
    private JScrollPane viewPort;
    
    /*Different screen areas*/
    
    //The manager for the map itself. Manager handles all map-related stuff. (Drawing, etc.)
    private MapManager mapView;
    
    //Tile selection view
    private TileView tileView;
    
    //Menu objects
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu settingsMenu;
    private JMenuItem exitItem;
    private JMenuItem newMapItem;
    private JMenuItem openMapItem;
    private JMenuItem saveMapItem;
    private JMenuItem prefsItem;

    private JMenuItem undoItem;
    private JMenuItem redoItem;
    
    //Variable for the selected Map Action
    private MapActions selectedAction = MapActions.Add;
    
    //Buttons for the possible actions that the user can do to the map.
    private JButton addActionButton;
    private JButton removeActionButton;
    private JButton selectActionButton;
    private JButton multiSelectActionButton;
    private JButton clearSelectionActionButton;
    private JButton paintAreaActionButton;
    
    //Label, that tells the user what action is selected
    private JLabel selectedActionText;
    
    //ResourceBundle for i18n files.
    public ResourceBundle languages;
    
    /**
     * The default Map Editor constructor. Initializes the language resource bundle
     */
    public MapEditor(){
        //Initialize the language bundle, the default locale is read from the Preferences class.
        languages = ResourceBundle.getBundle("LanguageBundle", EditorPreferences.getCurrentLocale());
    }
    
    /**
    * Initializes the Map Editor GUI.
    */
    public void initialize(){
        //Build the menu for the window
        buildMenu();
        
        //Initialize the Map Manager
        mapView = new MapManager(this);
        
        //Set the default Action for the manager
        mapView.setSelectedAction(this.selectedAction);
        
        //Set the manager to the JScrollPane, so that the map can be scrolled.
        viewPort = new JScrollPane(mapView);
        viewPort.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        viewPort.setMaximumSize(new Dimension(1000, 800));
        
        //There was a mysterious drawing bug, this is to fix it.. Not sure 
        //how, or what this does, but it works. I think it is a different sort of
        //double-buffering mode.
        viewPort.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        
        //Initialize the tile selection view.
        tileView = new TileView(this);
        tileView.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
  
        //Create the action selection buttons.
        addActionButton = new JButton(languages.getString("Draw_Tile"));
        removeActionButton = new JButton(languages.getString("Remove_Tile"));
        selectActionButton = new JButton(languages.getString("Select_Tile"));
        multiSelectActionButton = new JButton(languages.getString("Select_Area"));
        clearSelectionActionButton = new JButton(languages.getString("Clear_Area"));
        paintAreaActionButton = new JButton(languages.getString("Paint_Area"));
        
        //initialize the selected action text
        selectedActionText = new JLabel(languages.getString("Selected_Action") + ": " + languages.getString("Draw_Tile"));
        
        //Add action listener for the buttons
        addActionButton.addActionListener(this);
        removeActionButton.addActionListener(this);
        selectActionButton.addActionListener(this);
        multiSelectActionButton.addActionListener(this);
        clearSelectionActionButton.addActionListener(this);
        paintAreaActionButton.addActionListener(this);
        
        //Set hotkeys for the selection buttons.
        addActionButton.setMnemonic(KeyEvent.VK_A);
        removeActionButton.setMnemonic(KeyEvent.VK_R);
        selectActionButton.setMnemonic(KeyEvent.VK_S);
        multiSelectActionButton.setMnemonic(KeyEvent.VK_M);
        clearSelectionActionButton.setMnemonic(KeyEvent.VK_C);
        paintAreaActionButton.setMnemonic(KeyEvent.VK_P);
        
        //Create a panel to hold the buttons, and add the buttons to it.
        JPanel actionButtons = new JPanel();
        actionButtons.setLayout(new BoxLayout(actionButtons, BoxLayout.Y_AXIS));
        actionButtons.add(addActionButton);
        actionButtons.add(removeActionButton);
        actionButtons.add(selectActionButton);
        actionButtons.add(multiSelectActionButton);
        actionButtons.add(clearSelectionActionButton);
        actionButtons.add(paintAreaActionButton);
        actionButtons.add(selectedActionText);
        
        //Create and set the layout for the editor.
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //Automate gap creation.
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        //Set the root layout groups and add views to them.
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addComponent(viewPort)
                    .addGroup(layout.createParallelGroup()
                            .addComponent(tileView)
                            .addComponent(actionButtons)));
        
        layout.setVerticalGroup(
                layout.createParallelGroup()
                    .addComponent(viewPort)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(tileView)
                            .addComponent(actionButtons)));
        
        //Set the default close operation. 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Set the title to the map's name
        this.setTitle(mapView.getMap().getName());
        this.pack();
        
        //Show the editor
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        //Check which action selection button was pressed, and set the action
        //to the map manager.
        if (e.getSource() == addActionButton){
            this.selectedAction = MapActions.Add;
            this.selectedActionText.setText(languages.getString("Selected_Action") + ": " + languages.getString("Draw_Tile"));
            this.mapView.clearSelection();
            this.mapView.repaint();
        }
        else if (e.getSource() == removeActionButton){
            this.selectedAction = MapActions.Remove;
            this.selectedActionText.setText(languages.getString("Selected_Action") + ": " + languages.getString("Remove_Tile"));
            this.mapView.clearSelection();
            this.mapView.repaint();
        }
        else if (e.getSource() == selectActionButton){
            this.selectedAction = MapActions.Select;
            this.selectedActionText.setText(languages.getString("Selected_Action") + ": " + languages.getString("Select_Tile"));
        }
        else if (e.getSource() == multiSelectActionButton){
            this.selectedAction = MapActions.MultiSelect;
            this.selectedActionText.setText(languages.getString("Selected_Action") + ": " + languages.getString("Select_Area"));
        }
        else if (e.getSource() == clearSelectionActionButton){
            this.mapView.clearSelection();
            this.mapView.repaint();
        }
        else if (e.getSource() == paintAreaActionButton){
            this.mapView.paintArea();
        }
        
        //When the Action has been selected, set it to the Map Manager
        this.mapView.setSelectedAction(this.selectedAction);
    }    
    
    /**
     * Builds the menu for the editor window.
     */
    private void buildMenu(){
        
        //Create menu elements
        menuBar = new JMenuBar();
        fileMenu = new JMenu(languages.getString("File"));
        editMenu = new JMenu(languages.getString("Edit"));
        settingsMenu = new JMenu(languages.getString("Tools"));
        
        //Initialize a new menu listener
        MenuListener ml = new MenuListener(this);
        
        //Exit menu item
        exitItem = new JMenuItem(languages.getString("Exit"));
        
        //Add action listener to the exit item. Shows a confirmation dialog 
        //before exiting.
        exitItem.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               int exit = JOptionPane.showConfirmDialog(null, languages.getString("Really_Quit"), languages.getString("Confirm_Exit"), JOptionPane.YES_NO_OPTION);
               if (exit == JOptionPane.YES_OPTION){
                   System.exit(0);
               }
           } 
        });
        
        //new map item
        newMapItem = new JMenuItem(languages.getString("New_Map"));
        newMapItem.setActionCommand("NEW_MAP");
        newMapItem.addActionListener(ml);
        newMapItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        //open map item
        openMapItem = new JMenuItem(languages.getString("Open_Map"));
        openMapItem.setActionCommand("OPEN_MAP");
        openMapItem.addActionListener(ml);
        openMapItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        //save map item
        saveMapItem = new JMenuItem(languages.getString("Save_Map"));
        saveMapItem.setActionCommand("SAVE_MAP");
        saveMapItem.addActionListener(ml);
        saveMapItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        //Edit menu items
        undoItem = new JMenuItem(languages.getString("Undo"));
        undoItem.setActionCommand("UNDO");
        undoItem.addActionListener(ml);
        undoItem.setEnabled(false);  //TODO: vaihda tän arvoksi MapManager.canUndo();
        undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        redoItem = new JMenuItem(languages.getString("Redo"));
        redoItem.setActionCommand("REDO");
        redoItem.addActionListener(ml);
        redoItem.setEnabled(false);
        redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        //Settings Menu items
        prefsItem = new JMenuItem(languages.getString("Settings"));
        prefsItem.setActionCommand("PREFERENCES");
        prefsItem.addActionListener(ml);
        
        //Add the menu items to the menus
        fileMenu.add(newMapItem);
        fileMenu.add(openMapItem);
        fileMenu.add(saveMapItem);
        fileMenu.add(exitItem);
        
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        
        settingsMenu.add(prefsItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(settingsMenu);
        
        this.setJMenuBar(menuBar);
    }
    
    /**
     * This function is called when the language of the program changes. Updates
     * the GUI components.
     */
    public void updateTranslation(){
        //Get the new language bundle
        languages = ResourceBundle.getBundle("LanguageBundle", EditorPreferences.getCurrentLocale());
        
        //Painstakingly write the texts.. again..
        fileMenu.setText(languages.getString("File"));
        editMenu.setText(languages.getString("Edit"));
        undoItem.setText(languages.getString("Undo"));
        redoItem.setText(languages.getString("Redo"));
        settingsMenu.setText(languages.getString("Tools"));
        exitItem.setText(languages.getString("Exit"));
        newMapItem.setText(languages.getString("New_Map"));
        openMapItem.setText(languages.getString("Open_Map"));
        saveMapItem.setText(languages.getString("Save_Map"));
        prefsItem.setText(languages.getString("Settings"));
        
        //Ask the tile selection view to translate itself
        tileView.translate();
        
        addActionButton.setText(languages.getString("Draw_Tile"));
        removeActionButton.setText(languages.getString("Remove_Tile"));
        selectActionButton.setText(languages.getString("Select_Tile"));
        multiSelectActionButton.setText(languages.getString("Select_Area"));
        clearSelectionActionButton.setText(languages.getString("Clear_Area"));
        paintAreaActionButton.setText(languages.getString("Paint_Area"));
        
        //Update the selected action text
        String actionString = "";
        if (this.selectedAction == MapActions.Add)
            actionString = languages.getString("Draw_Tile");
        else if (this.selectedAction == MapActions.Remove)
            actionString = languages.getString("Remove_Tile");
        else if (this.selectedAction == MapActions.Select)
            actionString = languages.getString("Select_Tile");
        else if (this.selectedAction == MapActions.MultiSelect)
            actionString = languages.getString("Select_Area");
        
        this.selectedActionText.setText(languages.getString("Selected_Action") + ": " + actionString);
    }

    /**
     * Returns the selected tile from tile selection view.
     * @return Tile object that is selected.
    */
    public Tile getTile(){
        return tileView.getSelectedTile();
    }
    
    /**
     * Returns the tile size of the map.
     * @return the tile size of the map as an int.
    */
    public int getTileSize(){
        return mapView.getMap().getTileSize();
    }
    
    /**
     * Returns the map manager object.
     * 
     * @return MapManager of the editor.
     */
    public MapManager getMapManager(){
        return this.mapView;
    }
    
    /**
     * Refreshes the undo and redo menu items based on whether or not the map
     * manager's undomanager has undoable or redoable edits.
     */
    public void refreshUndoAndRedo(){
        this.undoItem.setEnabled(mapView.canUndo());
        this.redoItem.setEnabled(mapView.canRedo());
    }
    
    /**
     * Refreshes the map view. This method also asks for tile selection view
     * to resize the loaded tiles to the new map's tile size.
     * 
     * @param newMap Map that has been loaded or created. 
     */
    public void refreshMap(Map newMap){
        //Ask the tile view to resize loaded tiles according to the map's tile size.
        tileView.resizeTiles(newMap.getTileSize());
        
        //Set the map manager to the viewport again, revalidate the viewport, 
        //and repaint the map.
        viewPort.setViewportView(mapView);
        viewPort.revalidate();
        viewPort.repaint();
        
        //Reset the title to the new map's name
        setTitle(mapView.getMap().getName());
    }
    
    /**
     * Method that is called when a map has been loaded. Resets the map manager
     * with the new, loaded map.
     * 
     * @param newMap Map that has been loaded.
     */
    public void openMap(Map newMap){
        //Check if the opened map was null. Refresh the map manager if map wasn't null.
        if (newMap == null){
            System.err.println("New Map was null!");
        }
        else{
            mapView = new MapManager(this, newMap);
            mapView.setSelectedAction(selectedAction);
            this.refreshMap(this.mapView.getMap());
        }
    }
    
    //The main function. Starts the program.
    public static void main(String[] args){
        MapEditor app = new MapEditor();
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                app.initialize();
            }
        });
    }
}
