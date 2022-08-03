/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.Planet;

import Ship.SpaceShip;
import SpaceInvaders.Sprite;
import SpaceInvaders.SpriteProjectile;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author hutch
 */
public class PlanetShipMissile extends SpriteProjectile implements commons  {

    private String getImage;
    private String getExplosion;

    public PlanetShipMissile(Sprite sprite, SpaceShip ss) {
        
        super(sprite, ss);
        init();
    }

    private void init(){
        
        getImage = "Planet/PlanetShipMissile.png";
        getExplosion = "Planet/missileExplosion.png";
        width = 15;
        height = 35;
        loadImage(getImage, width, height);
        loadExplosion(getExplosion, width + 20, height + 20);
    }   
    
    @Override
    public void draw(Graphics2D g){
        
        if(isImpact()){         //image shown when player hit
                
            g.drawImage(getExplosion(), getPosX() - width / 2, getPosY() - height / 2, null);
                
        }
        
        if(isLive() && checkBounds()){

            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();            
            at.rotate(Math.atan2(getOriginalRotY(), getOriginalRotX()) - Math.toRadians(90), getCentreX(),  getCentreY());
            g.setTransform(at);
            g.drawImage(getProjectile(), getPosX(), getPosY(), null);

            fire();

            g.setTransform(old);
            
        }
    }
    
    /**
     * missile movement
     */
    
    private void fire(){
               
        moveX -= (int) Math.round(6 * Math.cos(Math.atan2(getOriginalRotY(), getOriginalRotX())));
        moveY -= (int) Math.round(6 * Math.sin(Math.atan2(getOriginalRotY(), getOriginalRotX())));
               
    }
    
}
