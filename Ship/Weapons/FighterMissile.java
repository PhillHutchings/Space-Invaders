/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship.Weapons;

import Ship.SpaceShip;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author hutch
 */
public class FighterMissile extends Weapon{

    private String getImage;
    
    public FighterMissile(SpaceShip ss) {
        
        super(ss);
        init();
    }
       
    private void init(){
        
        getImage = "fighterMissile.png";
        loadImage(getImage, MISSILE_WIDTH, MISSILE_HEIGHT);
    }
    
    @Override
    public void draw(Graphics2D g){
        
        if(IsLive()){
            
            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();            
            at.rotate(Math.atan2(rotY, rotX) - Math.toRadians(90), getPosX() + (width / 2),  getPosY() + (height / 2));
            g.setTransform(at);
            g.drawImage(getImage(), getPosX(), getPosY(), null);
            g.setTransform(old);
            
            fire();
            checkBounds();

        }
    }
    
    /**
     * missile movement
     */
    
    private void fire(){
               
        moveX -= (int) Math.round(6 * Math.cos(Math.atan2(rotY, rotX)));
        moveY -= (int) Math.round(6 * Math.sin(Math.atan2(rotY, rotX)));
               
    }
    
}
