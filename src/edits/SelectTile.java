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
 * Custom class for the single tile select edit.
 * @author ManMadeMachine
 */
public class SelectTile implements UndoableEdit{
    private MapManager parent;
    private Point selected;
    
    /**
     * Default constructor. Add the selected Point to the list of selected Points (map cells)
     * @param parent the parent map manager object
     * @param selected the selected Point (map cell)
     */
    public SelectTile(MapManager parent, Point selected){
        this.parent = parent;
        
        //Save the selected point
        this.selected = new Point(selected.x, selected.y);

        //Add the Point to the list of selected cells
        this.parent.getSelectedArea().clear();
        this.parent.getSelectedArea().add(this.selected);
    }
    @Override
    public void undo(){
        //To undo selection, we just clear the point list
        this.parent.getSelectedArea().clear();
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo(){
        //First clear the list, just to be sure we only have one point selected.
        //This is, after all, the selection of a SINGLE point/Tile
        this.parent.getSelectedArea().clear();
        
        //And add the point to the list again
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
        return "Select tile";
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo select tile";
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo select tile";
    }
}
