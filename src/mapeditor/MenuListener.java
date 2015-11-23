package mapeditor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;
import java.awt.event.*;
import dialogs.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import structs.Map;

/**
 * Class for the menu action listener.
 * 
 * @author ManMadeMachine
 */
public class MenuListener implements ActionListener{
    private MapEditor parent;
    
    /**
     * The default menu listener constructor
     * @param parent The map editor parent class
     */
    public MenuListener(MapEditor parent){
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        String cmd = e.getActionCommand();

        if (cmd.equals("NEW_MAP")){
            /*Custom modal dialog*/
            NewMapDialog dialog = new NewMapDialog(parent);
            
            //The dialog returns a Map object, which CAN BE NULL!
            Map newMap = dialog.showDialog();
            
            //Check if the dialog returned a Map object
            if (newMap != null){
                MapManager mapManager = parent.getMapManager();
                mapManager.createMap(newMap);
            }
            else{
                System.err.println("MapData object was null!");
            }
        }
        else if (cmd.equals("OPEN_MAP")){
            JFileChooser fc = new JFileChooser();
            
            //Only accept text files
            FileFilter filter = new FileNameExtensionFilter("Text File", "txt");
            fc.setFileFilter(filter);
            
            int a = fc.showOpenDialog(parent);
            
            if (a == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                
                //Open the map
                parent.openMap(MapManager.loadMap(file.getAbsolutePath()));
            }
            else{
                System.out.println("Opening cancelled by the user.");
            }
        }
        else if (cmd.equals("SAVE_MAP")){
            JFileChooser fc = new JFileChooser();
            
            FileFilter filter = new FileNameExtensionFilter("Text File", "txt");
            fc.setFileFilter(filter);
            
            int a = fc.showSaveDialog(parent);
            
            if (a == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();

                //Save the map data to the selected file
                MapManager.saveMap(parent.getMapManager(), file.getAbsolutePath());
            }
            else{
                System.out.println("Saving cancelled by the user!");
            }
        }
        else if (cmd.equals("UNDO")){
            if (parent.getMapManager().canUndo()){
                parent.getMapManager().undoManager.undo();
                parent.getMapManager().repaint();
            }
            parent.refreshUndoAndRedo();
        }
        else if (cmd.equals("REDO")){
            if (parent.getMapManager().canRedo()){
                parent.getMapManager().undoManager.redo();
                parent.getMapManager().repaint();
            }
            parent.refreshUndoAndRedo();
        }
        else if (cmd.equals("PREFERENCES")){
            SettingsDialog settings = new SettingsDialog(parent);
            parent.getMapManager().repaint();
        }
    }
}
