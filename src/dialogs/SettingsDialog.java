/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import mapeditor.MapEditor;
import structs.EditorPreferences;

/**
 * Class for the settings dialog
 * @author ManMadeMachine
 */
public class SettingsDialog extends JDialog implements ActionListener{
    private MapEditor parent;
    private JCheckBox drawGrid;
    private JTable colorTable;
    private String[] languages;
    private JComboBox languageSelection;
    
    /**
     * Default constructor. Creates the dialog and shows it.
     * @param parent 
     */
    public SettingsDialog(MapEditor parent){
        this.parent = parent;
        
        //Create the language selection array
        languages = new String[2];
        languages[0] = parent.languages.getString("Finnish");
        languages[1] = parent.languages.getString("English");  //Supported Languages
        
        //I don't think this does anything.. I'll just leave it be.. :)
        setSize(1000, 500);
        
        //Set the layout to GridBag, and create the Constraints class.
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        Container cp = getContentPane();
        
        //Create GUI
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        cp.add(new JLabel(parent.languages.getString("Draw_Grid") + ":"));
        
        drawGrid = new JCheckBox();
        drawGrid.setSelected(EditorPreferences.isDrawingGrid());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        cp.add(drawGrid, c);
        
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 20;
        c.gridwidth = 2;
        colorTable = new JTable(new ColorTableModel(this.parent));
        
        //Set the custom editor and renderer for the Color column.
        colorTable.setDefaultEditor(Color.class, new ColorEditor(this.parent));
        colorTable.setDefaultRenderer(Color.class, new ColorRenderer());
        
        JScrollPane tablePane = new JScrollPane(colorTable);
        colorTable.setFillsViewportHeight(true);
        
        cp.add(tablePane, c);
        
        JButton saveButton = new JButton(parent.languages.getString("Save"));
        saveButton.addActionListener(this);
        
        JButton cancelButton = new JButton(parent.languages.getString("Cancel"));
        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        
        JLabel languageLabel = new JLabel(parent.languages.getString("Language"));
        c.gridwidth = 1;
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 2;
        cp.add(languageLabel, c);
        
        languageSelection = new JComboBox(this.languages);
        c.gridx = 1;
        c.gridy = 2;
        cp.add(languageSelection, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        
        cp.add(saveButton, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        cp.add(cancelButton, c);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //The only one using this is the Save button, so no need to check
        //for the source component or anything. Just save the Values to the EditorPreferences class.
        //And exit.
        EditorPreferences.isDrawingGrid(drawGrid.isSelected());
        EditorPreferences.setGridColor((Color)this.colorTable.getValueAt(0, 1));
        EditorPreferences.setTileHighlightColor((Color)this.colorTable.getValueAt(1, 1));
        EditorPreferences.setSelectedAreaColor((Color)this.colorTable.getValueAt(2, 1));
        EditorPreferences.setBackgroundColor((Color)this.colorTable.getValueAt(3,1));

        //Check the selected language
        String selectedLanguage = (String)languageSelection.getSelectedItem();
        
        if (selectedLanguage.equals(parent.languages.getString("English"))){
            if (!EditorPreferences.getCurrentLanguage().equals("en")){
                EditorPreferences.setCurrentLocale("en", "US");
                parent.updateTranslation();
            }
        }
        else if (selectedLanguage.equals(parent.languages.getString("Finnish"))){
            if (!EditorPreferences.getCurrentLanguage().equals("fi")){
                EditorPreferences.setCurrentLocale("fi", "FI");
                parent.updateTranslation();
            }
        }
        
        //Shut the settings dialog
        this.dispose();
    }
}

/**
 * Class for the custom color selection Table
 * @author ManMadeMachine
 */
class ColorTableModel extends AbstractTableModel{
    private MapEditor parent;
    private String[] columnNames = new String[2];
    
    private Object[][] data = new Object[4][2];
    
    public ColorTableModel(MapEditor parent){
        this.parent = parent;
        
        columnNames[0] = parent.languages.getString("Property");
        columnNames[1] = parent.languages.getString("Color");
    
        data[0][0] = parent.languages.getString("Grid");
        data[0][1] = EditorPreferences.getGridColor();
        data[1][0] = parent.languages.getString("Mouse_Highlight");
        data[1][1] = EditorPreferences.getTileHighlightColor();
        data[2][0] = parent.languages.getString("Selected_Area");
        data[2][1] = EditorPreferences.getSelectedAreaColor();
        data[3][0] = parent.languages.getString("Background_Color");
        data[3][1] = EditorPreferences.getBackgroundColor();
    }

    @Override
    public int getRowCount() {
        return this.data.length;
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.data[rowIndex][columnIndex];
    }
    
    @Override
    public String getColumnName(int c){
        return this.columnNames[c];
    }
    
    @Override
    public Class getColumnClass(int c){
        return getValueAt(0, c).getClass();
    }
    
    @Override
    public boolean isCellEditable(int row, int col){    
        //Only the second column is editable
        return (col == 1) && (row < getRowCount());
    }
    
    @Override
    public void setValueAt(Object value, int row, int col){
        if (col == 1 && row < getRowCount()){
            //Only the second column is editable..
            this.data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }
}

/**
 * Custom Editor for the color selection. Shows a JColorChooser for color selection.
 * @author ManMadeMachine
 */
class ColorEditor extends AbstractCellEditor implements
                                            TableCellEditor, ActionListener{
    private MapEditor parent;
    private Color currentColor;
    private JButton button;
    private JColorChooser colorChooser;
    JDialog dialog;
    
    /**
     * Default constructor.
     * @param parent the parent map editor object
     */
    public ColorEditor(MapEditor parent){
        this.parent = parent;
        button = new JButton();
        button.setActionCommand("EDIT");
        button.addActionListener(this);
        button.setBorderPainted(false);
        
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button,
                                            parent.languages.getString("Pick_A_Color"),
                                            true, //modal
                                            colorChooser,
                                            this, //OK button handler
                                            null); //no CANCEL button handler
    }
    @Override
    public Object getCellEditorValue() {
        return currentColor;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentColor = (Color)value;
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("EDIT")){
            //The cell was pressed, show the dialog
            button.setBackground(currentColor);
            colorChooser.setColor(currentColor);
            dialog.setVisible(true);
            
            //Make the renderer reappear
            fireEditingStopped();
        }
        else{
            //user pressed OK
            currentColor = colorChooser.getColor();
        }
    }
    
}

/**
 * Custom renderer for the color selection Table
 */
class ColorRenderer extends JLabel implements TableCellRenderer{
    /**
     * Default constructor.
     */
    public ColorRenderer(){
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column) {
        Color newColor = (Color)color;
        setBackground(newColor);
        
        return this;
    }
}