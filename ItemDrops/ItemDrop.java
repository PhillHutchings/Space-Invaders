/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ItemDrops;

import Ship.SpaceShip;
import SpaceInvaders.Board;
import SpaceInvaders.Sprite;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */

public class ItemDrop implements commons{
    
    protected Image dropImage;      //item drop image
    protected Sprite sprite;        //sprite dropping item
    protected SpaceShip ss;         //player
    protected int moveY;            //item drop y movement
    protected int posX;         //item drop current x position
    protected int posY;         //item drop current y position
    protected int width;        //item drop width
    protected int height;       //item drop height
    protected boolean collected;       
    protected File collectedSound;
    protected AudioInputStream aisCollected;

    public ItemDrop(Sprite sprite, SpaceShip ss) {
        
        this.sprite = sprite;
        this.ss = ss;
        
        init();
    }
    
    private void init(){
        
        this.width = ITEM_WIDTH;
        this.height = ITEM_HEIGHT;
        loadSoundFile();
    }
    
    /**
     * loads item drop image
     * @param getImage
     */
    
    protected void loadImage(String getImage){

        ImageIcon ii = new ImageIcon(getClass().getResource(getImage));
        dropImage = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        
    }
   
    public void draw(Graphics2D g){
        
  
    }
    
    /**
     * item dropping
     */
    
    protected void drop(){
        
        moveY++;
    }

    public Image getDropImage() {
        
        return dropImage;
    }

    public int getPosX() {
        
        posX = sprite.getPosX() + (sprite.getWidth() / 2);;
        
        return posX;
    }

    public int getPosY() {
        
        posY = sprite.getPosY() + (sprite.getHeight() / 2) + moveY;
        
        return posY;
    }

    public int getWidth() {
        
        return width;
    }

    public int getHeight() {
        
        return height;
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(getPosX(), getPosY(), width, height);
    }

    public boolean isCollected() {
       
        return collected;
    }

    public void setCollected(boolean collected) {
        
        this.collected = collected;
    }
    
    
    public boolean checkBounds(){
        
        if(getPosY() > BOARD_HEIGHT){
                
            return false;
            
        }else{

            return true;
        }
    }
    
    public void checkCollection(){
        
        //each drop sorts its own collection so it can add itself to ship
    }
    
    /**
     * loads sound file
     */
    
    private void loadSoundFile(){
        
        try {
            
            collectedSound = new File("src/ItemDrops/itemCollect.wav");
            aisCollected = AudioSystem.getAudioInputStream(collectedSound.toURI().toURL());
            
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(ItemDrop.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(ItemDrop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * plays item collected sound
     */
    
    protected void itemCollectedSound(){
        
        try {
            
            Clip clip = AudioSystem.getClip();
            clip.open(aisCollected);       
            clip.start();
            
        } catch (LineUnavailableException | IOException ex) {
            
            Logger.getLogger(ItemDrop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
