/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.AlienCloner;

import Ship.SpaceShip;
import SpaceInvaders.Sprite;
import SpaceInvaders.SpriteProjectile;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author hutch
 */
public class HomingMissile extends SpriteProjectile{

    private String getImage;
    private String getExplosion;   
    private double finalAngleX;         //to save current trajectory to continue moving without homing
    private double finalAngleY;         //to save current trajectory to continue moving without homing
    

    public HomingMissile(Sprite sprite, SpaceShip ss) {
        
        super(sprite, ss);
        init();
    }
 
    
    private void init(){
      
        getImage = "AlienCloner/homingMissile.png";
        getExplosion = "Planet/missileExplosion.png";
        loadImage(getImage, 30, 50);
        loadExplosion(getExplosion, width + 20, height + 20);

    }
    
    @Override
    public void draw(Graphics2D g){
         
        if(isImpact()){         //image shown when player hit or intercepted
                
            g.drawImage(getExplosion(), getPosX() - width / 2, getPosY() - height / 2, null);
                
        }
        
        if(isLive() && checkBounds()){

                AffineTransform old = g.getTransform();
                AffineTransform at = new AffineTransform();            
                at.rotate(Math.atan2(getHomingRotY(), getHomingRotX()) - Math.toRadians(90),  getCentreX(), getCentreY());
                g.setTransform(at);
                g.drawImage(getProjectile(), getPosX(), getPosY(), null);
                g.setTransform(old);

                trackingFire();

        }
    }

    /**
     * homing response
     */
    
    private void trackingFire(){
    
        moveX -= (int) Math.round(3 * Math.cos(Math.atan2(getHomingRotY(), getHomingRotX())));
        moveY -= (int) Math.round(3 * Math.sin(Math.atan2(getHomingRotY(), getHomingRotX())));
      
    }


    public double getFinalAngleX() {
        
        return finalAngleX;
    }

    public void setFinalAngleX(double finalAngleX) {
        
        this.finalAngleX = finalAngleX;
    }

    public double getFinalAngleY() {
        
        return finalAngleY;
    }

    public void setFinalAngleY(double finalAngleY) {
        
        this.finalAngleY = finalAngleY;
    }
    
    
}
