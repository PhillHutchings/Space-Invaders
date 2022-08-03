/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.Asteroids;

import SpaceInvaders.Board;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */
public class Asteroids implements commons{
    
    private Image image;
    private Image explode;

    private int x;
    private int y;
    private int moveY;
    private int moveX;
    private int posY;
    private int posX;
    
    private final int minDimension;
    private final int maxDimension;
    
    private final int width;
    private final int height;
    
    private int originaMX;
    private int originalMY;
    
    private final Random random;
    private boolean impact;
    private boolean destroyed;
    private boolean live;
    private int hitsTillDestroyed;
    
    private int degrees;

    public Asteroids(boolean impact, boolean destroyed) {
               
        random = new Random();
        this.x = setX();
        this.y = setY();
        this.moveX = setMoveX();
        this.moveY = setMoveY();
        minDimension = 30;
        maxDimension = 140;
        this.originaMX = moveX;     //to keep the original values for the move method
        this.originalMY = moveY;
        this.width = setHeightAndWidth();
        this.height = width;
        this.hitsTillDestroyed = setHitsTillDestroyed();
        
        this.impact = impact;
        this.destroyed = destroyed;

        loadImage();
        loadDestroyedImage();
    }
    
    private void loadImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("asteroid.png"));
        
        image = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    
        
    private void loadDestroyedImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("asteroidExplode.png"));
        
        explode = ii.getImage().getScaledInstance(width + 20, height + 20, Image.SCALE_SMOOTH);
    }
    
    public void drawAsteroid(Graphics2D g){
        
        posY = y + moveY;
        posX = x + moveX;
           
        if(!impact && !destroyed && inBounds()){            //only draws if not destroyed or in screen
           
            AffineTransform old  = g.getTransform();
            g.rotate(Math.toRadians(degrees++), posX + (width / 2), posY + (height/ 2));
            g.drawImage(image, posX, posY, null);  
            g.setTransform(old);
            move();
            
            if(degrees == 360){
                
                degrees = 1; 
            }
            
        }else if(impact || destroyed && inBounds()){            //image shown when destroyed by missile or ship impact
            
            g.drawImage(explode, posX, posY, null); 
            move();
        }
        
        if(!inBounds() && !impact && !destroyed){            //resets the asteroid to new position if exited the screen
            
            this.x = setX();
            this.y = setY();
            this.moveX = setMoveX();
            this.moveY = setMoveY();
            this.originaMX = moveX;    
            this.originalMY = moveY;
            this.hitsTillDestroyed = setHitsTillDestroyed();
        }
    }
    
    private void move(){
        
        moveY += originalMY;
        moveX += originaMX;
              
    }

    private int setMoveY() {
        
        int velocity = random.nextInt(100);
        
        if(velocity % 2 == 0 || velocity % 3 == 0){
        
            moveY = 2;
    
        }else{
            
            moveY = 3;
        }
        
        return moveY;
    }

    private int setMoveX() {
        
        moveX += x > (BOARD_WIDTH / 2) ? -1 : 1;
        
        int straightDown = random.nextInt(100);
        
        if(straightDown % 3 == 0 || straightDown % 7 == 0 ){
            
            moveX = 0;
        }    
        
        return moveX;
    }
    
    /**
     * plays asteroid destroyed sound
     * called from level 1 collision method
     */
    
    public void asteroidDestroyedSound(){
        
        try{                                                    
            
            File asteroidSound = new File("src/SpaceInvaders/Enemy/Asteroids/asteroidBreak.wav");
            AudioInputStream aisAsteroid = AudioSystem.getAudioInputStream(asteroidSound.toURI().toURL());
            
            Clip clip = AudioSystem.getClip();
            clip.open(aisAsteroid);
            
            float volume = 0.1f;
            FloatControl fc =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);   
            fc.setValue(20f * (float) Math.log10(volume));
            clip.start();
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }       
    }
   
    private int setX(){
        
        return random.nextInt(BOARD_WIDTH);    
    }
    
    private int setY(){
        
        return -100;
    }
    
    private int setHeightAndWidth(){
        
        return (int) (Math.random() * (maxDimension - minDimension)) + minDimension;
    }

    public int getHitsTillDestroyed() {
        
        return hitsTillDestroyed;
    }

    private int setHitsTillDestroyed() {
        
       return width < 50 ? 1 : width < 70 ? 2 : width < 90 ? 3 : width < 110 ? 4 : 5;
           
    }
    
    public void asteroidHit(){
        
        hitsTillDestroyed--;
    }
 
    private boolean inBounds(){
        
        return posY < (BOARD_HEIGHT + height) && posX > (0 - width) && posX < BOARD_WIDTH;
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(posX, posY, width, height);
    }

    public boolean getImpact() {
        
        return impact;
    }

    public void setImpact(boolean impact) {
        
        this.impact = impact;
    }

    public void setDestroyed(boolean destroyed) {
        
        this.destroyed = destroyed;
    }

    public boolean isLive() {
        
        return !impact && !destroyed && inBounds();
    }

   
}
