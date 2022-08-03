/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.AbandonedShip;

import SpaceInvaders.commons;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class Alien implements commons{
    
    private Image image;
    private Image destroyedImage;
    private int x;
    private int y;
    private int moveX;
    private int moveY;
    private int posY;
    private int posX;
    private final int originalX;
    private int hitPoints;
    private boolean destroyed;
    private boolean moveRight;
    private boolean destroyedAnimation;

    public Alien(int x, int y, boolean destroyed) {
        
        this.x = x;
        this.y = y;
        this.destroyed = destroyed;
        this.originalX = x;
        
        init();
        loadImage();
        loadDestroyedImage();
    }
    
    private void init(){
        
        moveRight = true;
        hitPoints = 7;
    }
 
    /**
     * alien spaceship image
     */
    private void loadImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("Alien.png"));
        
        image = ii.getImage().getScaledInstance(ALIEN_WIDTH , ALIEN_HEIGHT, Image.SCALE_SMOOTH);
    }
    
    /**
     * alien spaceship destroyed image
     */
    
    private void loadDestroyedImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("AlienExplode.png"));
        
        destroyedImage = ii.getImage().getScaledInstance(ALIEN_WIDTH + 100, ALIEN_HEIGHT + 100, Image.SCALE_SMOOTH);
    }
    
    public void draw(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        if(!isDestroyed()){
            g2d.drawImage(image, x + moveX, y + moveY, null);

            posX = x + moveX;
            posY = y + moveY;
            
        }
        if(isDestroyed() && !destroyedAnimation){
            
            g2d.drawImage(destroyedImage, getPosX() - (ALIEN_WIDTH / 2), getPosY() - ALIEN_HEIGHT, null);
            
            destroyTimer();
  
        }
    }
    
    /**
     * timer for animation of destroyed alien ship
     */
    
    private void destroyTimer(){
        
        new javax.swing.Timer(600, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                destroyedAnimation = true;
                
                ((Timer)e.getSource()).stop();
                
            }   
        }).start();      
    }

    public int getPosY(){
        
        return posY;
    }

    public int getPosX(){
        
        return posX;
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(posX, posY, ALIEN_WIDTH, ALIEN_HEIGHT);
    }
    
    public int getOriginalX() {
        
        return originalX;
    }

    public boolean isMoveRight() {
        
        return moveRight;
    }

    public void setMoveRight(boolean moveRight) {
        
        this.moveRight = moveRight;
    }

    public boolean isDestroyed() {
        
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        
        this.destroyed = destroyed;
    }

    public int getMoveX() {
       
        return moveX;
    }

    public void setMoveX(int moveX) {
        
        this.moveX += moveX;
    }

    public int getMoveY() {
        
        return moveY;
    }

    public void setMoveY(int moveY) {
        
        this.moveY += moveY;
    }
    
    public boolean inBoard(){
        
        return getPosY() > 0;
    }
    
    /**
     * records alien ship being hit
     */
    
    public void hit(){
        
        hitPoints--;
    }

    public int getHitPoints() {
        
        return hitPoints;
    }
    
    
}
