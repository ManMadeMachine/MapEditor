/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edits;

import java.awt.Point;
import javax.swing.undo.UndoableEdit;
import mapeditor.MapManager;

/**
 * Custom class for multiple tile selection edit
 * @author ManMadeMachine
 */
public class SelectMultipleTiles implements UndoableEdit{
    private MapManager parent;
    private Point selected;
    
    /**
     * Default constructor. Adds the selected Point to the list of selected Points (map cells)
     * @param parent parent map manager object
     * @param selected the selected Point (map cell) on the map
     */
    public SelectMultipleTiles(MapManager parent, Point selected){
        this.parent = parent;
        
        //Save the latest selected Point
        this.selected = selected;
        
        //Add the selected Point to the list of selected points
        this.parent.getSelectedArea().add(selected);
    }

    @Override
    public void undo(){
        //We undo the selection by removing the latest selected Point from the list
        this.parent.getSelectedArea().remove(this.selected);
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo(){
        //And the redo is just the adding of the Point back to the list
        this.parent.getSelectedArea().add(this.selected);
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void die() {
        this.parent = null;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return "Select Multiple tiles";
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo select multiple tiles";
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo select multiple tiles";
    }
}
