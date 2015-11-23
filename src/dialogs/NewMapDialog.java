/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import mapeditor.MapEditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import structs.Map;

/**
 * Class for the New Map dialog
 * @author ManMadeMachine
 */
public class NewMapDialog extends JDialog{
    private MapEditor parent;
    private JTextField nameField;
    private JTextField rowsField;
    private JTextField columnsField;
    private JTextField tileSizeField;
    
    private JButton createButton;
    private JButton cancelButton;
    
    private Map newMap;
    
    /**
     * Default constructor. Creates the dialog, but doesn't show it
     * @param parent parent MapEditor object
     */
    public NewMapDialog(MapEditor parent){
        this.parent = parent;
        
        //Create the dialog
        this.setLayout(new GridLayout(6, 2));
        nameField = new JTextField();
        rowsField = new JTextField();
        columnsField = new JTextField();
        tileSizeField = new JTextField();
        getContentPane().add(new JLabel(parent.languages.getString("Name") + ":"));
        getContentPane().add(nameField);
        getContentPane().add(new JLabel(parent.languages.getString("Width") + ":"));
        getContentPane().add(columnsField);
        getContentPane().add(new JLabel(parent.languages.getString("Height") + ":"));
        getContentPane().add(rowsField);
        getContentPane().add(new JLabel(parent.languages.getString("Tile_Size") + ":"));
        getContentPane().add(tileSizeField);
        
        createButton = new JButton(parent.languages.getString("Create"));
        cancelButton = new JButton(parent.languages.getString("Cancel"));
        
        //Add action listener for the create button.
        createButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String name = nameField.getText();
                try{
                    int rows = Integer.parseInt(rowsField.getText());
                    int columns = Integer.parseInt(columnsField.getText());
                    int tileSize = Integer.parseInt(tileSizeField.getText());

                    //A little validation for null values
                    if (name != null && columns > 0 && rows > 0 && tileSize > 0){
                        newMap = new Map(name, rows, columns, tileSize);
                    }
                }
                catch (NumberFormatException ex){
                    System.out.println("Input strings were null!");
                }
                
                //Close the dialog
                setVisible(false);
            } 
        });
        
        cancelButton.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               newMap = null;
               dispose();
           } 
        });
        
        getContentPane().add(createButton);
        getContentPane().add(cancelButton);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle(parent.languages.getString("Create_New_Map"));
        setLocationRelativeTo(parent);
    }
    
    /**
     * Method for showing the dialog. Returns the new map, or null if the user
     * cancelled the creation (or values were null)
     * @return new Map object
     */
    public Map showDialog(){
        setVisible(true);

        return newMap;
    }
}
