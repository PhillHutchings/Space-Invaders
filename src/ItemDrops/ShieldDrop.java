/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ItemDrops;

import Ship.SpaceShip;
import SpaceInvaders.Sprite;
import java.awt.Graphics2D;

/**
 *
 * @author hutch
 */
public class ShieldDrop extends ItemDrop{
    
       private String getImage;

    public ShieldDrop(Sprite sprite, SpaceShip ss) {
        
        super(sprite, ss);
        init();
    }

    private void init(){
        
        getImage = "/Ship/UpgradeImages/shield.png";
        loadImage(getImage);
    }
    
    @Override
    public void draw(Graphics2D g){
        
        if(!isCollected() && checkBounds()){
            
            g.drawImage(getDropImage(), getPosX(), getPosY(), null);
            drop();
            checkCollection();
        }
        
    }
    
    /**
     * check collection method
     */
    
    @Override
    public void checkCollection(){
        
        if(checkBounds()){
            
            if(getBounds().intersects(ss.getBounds())){
                
                setCollected(true);
                ss.addShield();
                itemCollectedSound();
            }            
        }
    } 
    
    
}
