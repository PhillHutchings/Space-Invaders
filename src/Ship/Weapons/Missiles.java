/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship.Weapons;

import Ship.SpaceShip;
import SpaceInvaders.commons;
import java.awt.Graphics2D;

/**
 *
 * @author hutch
 */
public class Missiles extends Weapon implements commons{
    
    private String getImage;
    
    public Missiles(SpaceShip ss) {
        
        super(ss);
        init();
    }

    private void init(){
        
        getImage = "basicMissile.png";
        loadImage(getImage, MISSILE_WIDTH, MISSILE_HEIGHT);
    }
    
    @Override
    public void draw(Graphics2D g){
         
        if(IsLive()){
            
            g.drawImage(getImage(), getPosX(), getPosY(), null);
            fireMissile();
            checkBounds();
           
        }
    }
    
    private void fireMissile(){
        
        moveY -= 4;
   
    }
}
