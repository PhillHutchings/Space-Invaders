/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship;

import SpaceInvaders.Board;
import SpaceInvaders.commons;
import java.awt.Color;

/**
 *
 * @author hutch
 */
public class BasicShip extends SpaceShip implements commons{
            
    private String shipImage;

    public BasicShip(Board board) {
        
        super(board);       
        init();
    }
    
    private void init(){
        
        shipImage = "basicShip.png";
        loadImage(shipImage, 0 ,0);
        
        setMaxMissiles(5);
        setHealth(200,0,0, Color.RED, 0 , 0);
        setShieldHealth(200, 0, 0 , Color.BLUE, -20, 0);
        
    }   

    
}
