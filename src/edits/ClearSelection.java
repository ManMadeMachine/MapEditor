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

/**
 * Custom class for the clear selection edit
 * @author ManMadeMachine
 */
public class ClearSelection implements UndoableEdit{
    private MapManager parent;
    private ArrayList<Point> selectedArea;
    
    /**
     * Default constructor. Clears the area selection in the map manager
     * @param parent map manager parent
     */
    public ClearSelection(MapManager parent){
        this.parent = parent;
        
        //Create a new arrayList and copy the old points to the new list
        this.selectedArea = new ArrayList<Point>();
        
        for(int i = 0; i < parent.getSelectedArea().size(); ++i){
            this.selectedArea.add(parent.getSelectedArea().get(i));
        }
        
        //Then, we can clear the old list
        parent.getSelectedArea().clear();
    }
    @Override
    public void undo(){
        //To undo the clearing of the selected area, we need to fill the old one again
        for(int i = 0; i < this.selectedArea.size(); ++i){
            parent.getSelectedArea().add(this.selectedArea.get(i));
        }
        
        //Then we might as well clear the copy array
        this.selectedArea.clear();
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo(){
        //And here we just redo the clearing
        for(int i = 0; i < parent.getSelectedArea().size(); ++i){
            this.selectedArea.add(parent.getSelectedArea().get(i));
        }
        
        //Then, we can clear the old list
        parent.getSelectedArea().clear();
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void die() {
        this.parent = null;
        this.selectedArea = null;
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
        return "Clear selection";
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo clear selection";
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo clear selection";
    }
}
