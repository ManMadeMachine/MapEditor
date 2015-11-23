/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edits;

import javax.swing.undo.UndoableEdit;
import mapeditor.Tile;
import structs.Map;

/**
 * Custom class for the tile remove edit.
 * @author ManMadeMachine
 */
public class RemoveTile implements UndoableEdit {
    private Map parent;
    private Tile removed;
    private int i;
    private int j;
    
    /**
     * Default constructor. Removes the selected Tile from the map
     * @param parent parent Map object
     * @param i row of the deletion
     * @param j column of the deletion
     */
    public RemoveTile(Map parent, int i, int j){
        this.parent = parent;

        //Save the to-be-removed Tile before actually removing it
        this.removed = parent.getTile(i, j);
        
        //Save the position of the tile
        this.i = i;
        this.j = j;
        
        //remove the Tile from the map
        parent.removeTile(i, j);
        
        System.out.println("RemoveTile Edit called!");
    }
    
    @Override
    public void undo(){
        //This is easy, just set the Tile again.
        parent.setTile(this.removed, this.i, this.j);
    }

    @Override
    public boolean canUndo() {
        //this action is undoable
        return true;
    }

    @Override
    public void redo(){
        //This too is pretty easy, just remove the Tile again
        parent.removeTile(this.i, this.j);
    }

    @Override
    public boolean canRedo() {
        //this action is redoable
        return true;
    }

    @Override
    public void die() {
        this.parent = null;
        this.removed = null;
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
        return "Remove tile";
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo remove tile";
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo remove tile";
    }
}
