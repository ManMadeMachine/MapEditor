package mapeditor;


import java.awt.Point;
import java.awt.event.*;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Class for the map manager's mouse listener.
 * @author ManMadeMachine
 */
public class MapMouseListener extends MouseAdapter{
    //Previous mouse position on the map
    private Point prevPos;
    
    //Current Mouse position on the map
    private Point currentPos;
    
    //The parent map manager
    private MapManager parent;
    
    /**
     * The default constructor.
     * @param parent The parent map manager.
     */
    public MapMouseListener(MapManager parent){
        this.parent = parent;
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        currentPos = e.getPoint();
        
        //repaint the map, so that the mouse moves.
        parent.repaint();
    }
    
    @Override
    public void mousePressed(MouseEvent e){
        //Update the mouse position
        currentPos = e.getPoint();
        
        //Check which button was pressed
        if (e.getButton() == MouseEvent.BUTTON1){
            //Check what the selected action was, and do the corresponding action.
            if (parent.getSelectedAction() == MapActions.Add){
                parent.setTile();   
            }
            else if (parent.getSelectedAction() == MapActions.Remove){
                parent.removeTile();
            }
            else if (parent.getSelectedAction() == MapActions.Select){
                parent.selectTile();
            }
            else if (parent.getSelectedAction() == MapActions.MultiSelect){
                parent.addPointToSelection();
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e){
        //Update the previous and the current positions of the mouse
        prevPos = currentPos;
        currentPos = e.getPoint();
        
        //Get the tile size
        int tileSize = parent.getMap().getTileSize();
        
        //Calculate prev and current indices.
        int prevI = prevPos.y / tileSize;
        int prevJ = prevPos.x / tileSize;
        
        int newI = currentPos.y / tileSize;
        int newJ = currentPos.x / tileSize;
        
        //This button checking works, the default method (as seen in mousePressed)
        //didn't work for some strange reason.
        if (SwingUtilities.isLeftMouseButton(e)){
            //Check if the mouse has moved to the next tile, before calling actions
            if (newI != prevI || newJ != prevJ){
                if (parent.getSelectedAction() == MapActions.Add){
                    parent.setTile();
                }
                else if (parent.getSelectedAction() == MapActions.Remove){
                    parent.removeTile();
                }
                else if (parent.getSelectedAction() == MapActions.MultiSelect){
                    parent.addPointToSelection();
                }
            }
        }
    }
    
    /**
     * Method for returning the current mouse position
     * @return the current mouse position
     */
    public Point getPosition(){
        return currentPos;
    }
}
