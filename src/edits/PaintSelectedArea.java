/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edits;

import java.awt.Point;
import java.util.ArrayList;
import javax.swing.undo.UndoableEdit;
import mapeditor.MapManager;
import mapeditor.Tile;

/**
 * Custom class for the paint selected area edit.
 * @author ManMadeMachine
 */
public class PaintSelectedArea implements UndoableEdit{
    private MapManager parent;
    private ArrayList<Point> area;
    private Tile selectedTile;
    
    /**
     * Default constructor. Paints the selected area to the map with the current selected Tile
     * @param parent parent map manager object
     * @param selectedTile currently selected Tile, which is drawn
     */
    public PaintSelectedArea(MapManager parent, Tile selectedTile){
        this.parent = parent;
        this.selectedTile = selectedTile;
        
        area = new ArrayList<Point>();
        
        //Copy the selected area, so that we can redo the painting
        for (int i = 0; i < parent.getSelectedArea().size(); ++i){
            area.add(parent.getSelectedArea().get(i));
            
            Point p = area.get(i);
            //After the Point is copied, we can also paint the corresponding cell
            parent.getMap().setTile(selectedTile, p.y, p.x);
        }
    }
    
    @Override
    public void undo(){
        //Undo the painting by removing the Tiles from the cells
        for (int i = 0; i < area.size(); ++i){
            Point p = area.get(i);
            parent.getMap().removeTile(p.y, p.x);
        }
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo(){
        //Redo by painting the Tiles again
        for (int i = 0; i < area.size(); ++i){
            Point p = area.get(i);
            
            parent.getMap().setTile(this.selectedTile, p.y, p.x);
        }
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void die() {
        this.parent = null;
        this.area.clear();
        this.area = null;
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
        return "Paint selected area";
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo paint selected area";
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo paint selected area";
    }
    
}
