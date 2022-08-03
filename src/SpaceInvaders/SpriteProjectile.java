/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders;

import Ship.SpaceShip;
import SpaceInvaders.Enemy.AlienCloner.AlienCloner;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import static SpaceInvaders.commons.BOARD_WIDTH;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class SpriteProjectile {
    
    protected Image projectile;
    protected Image explosion;
    protected Sprite sprite;
    protected SpaceShip ss;
    protected double homingRotX;          // x rotation
    protected double homingRotY;          //y rotation
    protected double originalRotX;     // original x rotation for non homing
    protected double originalRotY;     //original y rotation for non homing
    protected int centreX;          //centre of rotation x
    protected int centreY;          //centre of rotation y
    protected int x;                // x start position
    protected int y;                // y start position
    protected int moveX;            //x movement
    protected int moveY;            //y movement
    protected int posX;             //x + moveX
    protected int posY;             //y + moveY
    protected boolean fired;        //detect fired
    protected boolean live;         //inbounds and not hit player
    private boolean tracking;       //for homing missile
    protected boolean impact;
    protected int width;
    protected int height;

    public SpriteProjectile(Sprite sprite, SpaceShip ss) {
        
        this.sprite = sprite;
        this.ss = ss;
        this.originalRotX = sprite.getPosX() - ss.getPosX();
        this.originalRotY = sprite.getPosY() - ss.getPosY();
        this.x = sprite.getPosX() + (sprite.getWidth() / 2);
        this.y = sprite.getPosY() + (sprite.getHeight() / 2);
        setLive(true);
    }

    /**
     * set image
     * @param getImage 
     * @param width 
     * @param height 
     */
    
    protected void loadImage(String getImage, int width, int height){
        
        try {
            
            this.width = width;
            this.height = height;
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/" + getImage));
            ImageIcon ii = new ImageIcon(bi);
            projectile = ii.getImage().getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(SpriteProjectile.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
     * load explosion image
     * @param getImage
     * @param width
     * @param height 
     */
    
    protected void loadExplosion(String getImage, int width, int height){
        
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/" + getImage));
            ImageIcon ii = new ImageIcon(bi);
            explosion = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(SpriteProjectile.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    /**
     * explosion sequence
     */
    
    public void explosion(){
        
        setImpact(true);        //true draws explosion image
        setLive(false);         //stops drawing live missile
        
        new javax.swing.Timer(800, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                setImpact(false);           //stops drawing explosion image, now nothing drawn
                ((Timer)e.getSource()).stop();
            }      
        }).start();
    }
    
    public void draw(Graphics2D g){
        
        //each projectile draws itself
        
    }

    public Image getProjectile() {
        
        return projectile;
    }

    public Image getExplosion() {
        
        return explosion;
    }
       
    public int getPosX() {
        
        posX = x + moveX;
        
        return posX;
    }

    public int getPosY() {
        
        posY = y + moveY;
        
        return posY;
    }

    public final double getHomingRotX() {
        
        homingRotX = getPosX() - ss.getPosX(); 
        
        return homingRotX;
    }

    public final double getHomingRotY() {

        homingRotY = getPosY() - ss.getPosY();
        
        return homingRotY;
    }

    public double getOriginalRotX() {
        
        return originalRotX;
    }

    public double getOriginalRotY() {
        
        return originalRotY;
    }

    public int getCentreX() {
        
        centreX = getPosX() + (width / 2);
        
        return centreX;
    }

    public int getCentreY() {
        
        centreY = getPosY() + (height / 2);
        
        return centreY;
    }
 
    public boolean isFired() {
        
        return fired;
    }

    public void setFired(boolean fired) {
        
        this.fired = fired;
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(posX, posY, width, height);
    }
        
    /**
     * check weapon is still in the bounds of the screen
     * @return 
     */
    
    public boolean checkBounds(){
        
        if(getPosX() < (0 - getWidth()) || getPosX() > BOARD_WIDTH){
            
            setLive(false);
            return false;
        }
        if(getPosY() < (0 - getHeight()) || getPosY() > BOARD_HEIGHT){
            
            setLive(false);
            return false;
            
        }else{
            
            return true;
        }        
    }

    public boolean isLive() {
        
        return live;
    }

    public final void setLive(boolean live) {
        
        this.live = live;
    }

    public boolean Hit() {
        
        boolean hit = getBounds().intersects(ss.getBounds()); 
        
        if(hit){

            explosion();
            
        }
        
        return hit;
    }
    
    public void intercepted(){
        
        explosion();
    }

    public boolean isImpact() {
        
        return impact;
    }

    public void setImpact(boolean impact) {
        
        this.impact = impact;
    }

    public int getWidth() {
        
        return width;
    }

    public int getHeight() {
        
        return height;
    }
    
        
    public boolean isTracking() {
        
        return tracking;
    }

    public void setTracking(boolean tracking) {
        
        this.tracking = tracking;
    }

  
    /**
     * to reset the homing missile in level 5 instead of assigning new homing missile
     */
    public void reloadHomingMissile(){
        
        this.x = sprite.getPosX() + (sprite.getWidth() / 2);
        this.y = sprite.getPosY() + (sprite.getHeight() / 2);
        
        moveX = 0;
        moveY = 0;
        
        setLive(true);

    }
}
