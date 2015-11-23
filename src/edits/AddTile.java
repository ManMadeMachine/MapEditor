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
 * Custom edit class for Tile Addition
 * @author ManMadeMachine
 */
public class AddTile implements UndoableEdit{
    //Map itself, where the Action happens
    private Map parent;
    
    //The possible previous Tile that was in the position. This way, if the 
    //user paints multiple Tiles on top of each other, the Undo behaviour
    //reveals the previous Tile, instead of just clearing the position.
    private Tile prevTile;
    
    //the newly added Tile
    private Tile added;

    //The position of the added Tile on the map
    private int i;
    private int j;

    /**
     * Default constructor. Creates and does the AddTile edit.
     * @param parent parent Map object, in which the edit is made
     * @param tile the added tile
     * @param i row of the tile
     * @param j column of the tile
     */
    public AddTile(Map parent, Tile tile, int i, int j){
        this.parent = parent;
        
        //Get the previous Tile in the map array, if any (i.e. this can be null)
        this.prevTile = parent.getTile(i, j);
        this.added = tile;
        this.i = i;
        this.j = j;
        
        //Do the actual adding.
        parent.setTile(tile, i, j);
    }
    
    @Override
    public void undo(){
        if (prevTile != null && prevTile != added){
            parent.setTile(prevTile, i, j);
        }
        else{
            parent.removeTile(i, j);
        }
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo(){
        parent.setTile(added, i, j);
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void die() {
        this.parent = null;
        this.added = null;
        this.prevTile = null;
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
        return "Add tile";
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo add tile";
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo add tile";
    }
}
