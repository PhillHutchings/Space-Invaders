/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship.Weapons;

import Ship.FighterShip;
import Ship.SpaceShip;
import SpaceInvaders.commons;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */
public class Weapon implements commons{

    protected SpaceShip ss;     //player
    protected Image image;      //weapon image
    protected int width;        //weapon width
    protected int height;       //weapon height
    protected int x;            //weapon x coord
    protected int y;            //weapon y coord
    protected int moveX;        //weapon x movement
    protected int moveY;        //weapon y movement
    protected int posX;         //weapon current x position
    protected int posY;         //weapon current y position
    protected double rotY;      //weapon rotation x
    protected double rotX;      //weapon rotation y
    protected boolean live;     //weapon status

    public Weapon(SpaceShip ss) {
        
        this.ss = ss;  
        
        x = ss.getPosX() + (ss.getWidth() / 2);
        y = ss.getPosY() + (ss.getHeight() / 2);
        
        setRotX(ss.getAngleX());            //facing angle of player
        setRotY(ss.getAngleY());
           
        setLive(true);          //weapon starts active
               
    }

    /**
     * loads weapon image
     * @param getImage
     * @param width
     * @param height 
     */
    
    protected void loadImage(String getImage, int width, int height){
         
        try {
            
            this.width = width;
            this.height = height;
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/Ship/Weapons/" + getImage));
            ImageIcon ii = new ImageIcon(bi);
            image = ii.getImage().getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Weapon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public Image getImage() {
        
        return image;
    }

    public int getMoveY() {
        
        return moveY;
    }

    public void setMoveY(int moveY) {
        
        this.moveY = moveY;
    }

    public int getPosX() {
        
        posX = x + moveX;
        
        return posX;
    }
   
    public int getPosY() {
        
        posY = y + moveY;
        
        return posY;
    }
 
    private void setRotY(double rotY) {
        
        this.rotY = rotY;
    }

    private void setRotX(double rotX) {
        
        this.rotX = rotX;
    }
    
    private void setLive(boolean live) {
        
        this.live = live;
    }

    public int getWidth() {
        
        return width;
    }

    public int getHeight() {
        
        return height;
    }
   
    public boolean IsLive() {
      
        return live;
    }
    
    public void hit(){
        
        setLive(false);
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

    public void draw(Graphics2D g) {
        
        //each weapon draws itself
    }

    public Rectangle  getBounds() {
        
        return new Rectangle(getPosX(), getPosY(), width, height);
    }
   
}
