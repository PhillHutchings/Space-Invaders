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
public class LifeDrop extends ItemDrop{
    
    private String getImage;

    public LifeDrop(Sprite sprite, SpaceShip ss) {
        
        super(sprite, ss);
        init();
    }

    private void init(){
        
        getImage = "/Ship/UpgradeImages/heart.png";
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
                ss.getBoard().addLife();
                itemCollectedSound();
            }            
        }
    }
    
}
