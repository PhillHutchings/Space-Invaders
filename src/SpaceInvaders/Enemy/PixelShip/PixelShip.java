/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.PixelShip;

import Levels.Level2;
import Ship.SpaceShip;
import Ship.Weapons.Weapon;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
public class PixelShip implements commons{
    
    private SpaceShip ss;
    private ArrayList<Pixel> ship;
    private final int w = PIXEL_SHIP_WIDTH;
    private final int h = PIXEL_SHIP_HEIGHT;
    private final int cl = (BOARD_WIDTH / 2) - (w / 2);          //center line going left // and center point
    private final int cr = (BOARD_WIDTH / 2) + (w / 2);          //center line going right
    private final int py = -200;                                 //top of pixel ship start y;
    private Image lightning;
    private LinkedList<Integer> lightningList;
    private final int centerShip;
    private int xMove;
    private int yMove;
    
    private File shockSound;                //pixel shock sound
    private AudioInputStream aisShock;
    private Clip shockClip; 
    
    private File bounceSound;                //pixel shock sound
    private AudioInputStream aisBounce;
    private Clip bounceClip; 

    public PixelShip(SpaceShip ss) {
    
        this.ss = ss;
        centerShip = 8;
        init();
    }
    
    private void init(){
        
        ship = new ArrayList<>();
        lightningList = new LinkedList<>();
        generatePixelShip();
        xMove = 1;
        yMove = 1;
        
        loadLightning();  
        loadSounds();
    }
      
    /**
     * loads the lightning image used when the centre pixel is gone and player is too close to pixels
     */
    
    private void loadLightning(){ 
        
        try {
            
            BufferedImage getLightning = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/PixelShip/pixelLightning.png"));
            ImageIcon ii = new ImageIcon(getLightning);
            lightning = ii.getImage().getScaledInstance(20, 150, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            java.util.logging.Logger.getLogger(PixelShip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }      
    }
                    
    /**
     * pre loads the sounds for pixel shock
     */  
    
    private void loadSounds(){
        
        try {
            
            shockSound = new File("src/SpaceInvaders/Enemy/PixelShip/pixelShock.wav");
            aisShock = AudioSystem.getAudioInputStream(shockSound.toURI().toURL()); 
            bounceSound = new File("src/SpaceInvaders/Enemy/PixelShip/bounce.wav");
            aisBounce = AudioSystem.getAudioInputStream(bounceSound.toURI().toURL()); 

        } catch (MalformedURLException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);       
        }       
    }    
        
    /**
     * plays the laser sound
     */
    
    private void bounceSound(){
     
        try {
                        //check to stop it playing while entering from top of screen
            if(ship.get(centerShip).getY() > ((ship.get(centerShip).getShieldBounds().getHeight() / 2) - 1)){
                
                bounceSound = new File("src/SpaceInvaders/Enemy/PixelShip/bounce.wav");
                aisBounce = AudioSystem.getAudioInputStream(bounceSound.toURI().toURL());          

                bounceClip = AudioSystem.getClip();
                bounceClip.open(aisBounce);
                bounceClip.start();

            }
            
        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * plays the laser sound
     */
    
    private void shockSound(){
     
        try {
            
            shockSound = new File("src/SpaceInvaders/Enemy/PixelShip/pixelShock.wav");
            aisShock = AudioSystem.getAudioInputStream(shockSound.toURI().toURL());          

            shockClip = AudioSystem.getClip();
            shockClip.open(aisShock);
            shockClip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    /**
     * generates the pixel ship parts
     */
    
    private void generatePixelShip(){
               
        // generateting pixel ship left to right, top to bottom. 
        
        ship.add(new Pixel(ss, (cl - (w * 3)), py, PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));  //1st row 1
        ship.add(new Pixel(ss, (cr + (w * 2)), py, PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));  //1st row 2
        ship.add(new Pixel(ss, (cl - (w * 2)), (py + h), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));  //2nd row left
        ship.add(new Pixel(ss, (cr + w), (py + h), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));  //2nd row right
        ship.add(new Pixel(ss, (cl - w), (py + (h * 2)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //3rd row left
        ship.add(new Pixel(ss, cl, (py + (h * 2)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));       //3rd row center
        ship.add(new Pixel(ss, cr, (py + (h * 2)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));       //3rd row right
        ship.add(new Pixel(ss, (cl - (w * 2)), (py + (h * 3)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));  //4th row left
        ship.add(new Pixel(ss, cl, (py + (h * 3)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));               //4th row center  shield generating pixel
        ship.add(new Pixel(ss, (cr + w), (py + (h * 3)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //4th row right
        ship.add(new Pixel(ss, (cl - (w * 3)), (py + (h * 4)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));       //5th row 1
        ship.add(new Pixel(ss, (cl - (w * 2)), (py + (h * 4)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //5th row 2
        ship.add(new Pixel(ss, (cl - w), (py + (h * 4)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT)); //5th row 3
        ship.add(new Pixel(ss, cl, (py + (h * 4)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //5th row 4
        ship.add(new Pixel(ss, cr, (py + (h * 4)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //5th row 5
        ship.add(new Pixel(ss, (cr + w), (py + (h * 4)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //5th row 6
        ship.add(new Pixel(ss, (cr + (w * 2)), (py + (h * 4)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //5th row 7
        ship.add(new Pixel(ss, (cl - (w * 4)), (py + (h * 5)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //6th row 1
        ship.add(new Pixel(ss, (cl - (w * 2)), (py + (h * 5)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //6th row 2
        ship.add(new Pixel(ss, (cr + w), (py + (h * 5)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //6th row 3
        ship.add(new Pixel(ss, (cr + (w * 3)), (py + (h * 5)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //6th row 4
        ship.add(new Pixel(ss, (cl - (w * 4)), (py + (h * 6)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //7th row 1
        ship.add(new Pixel(ss, (cl - (w * 2)), (py + (h * 6)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //7th row 2
        ship.add(new Pixel(ss, (cl - w), (py + (h * 6)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //7th row 3
        ship.add(new Pixel(ss, cr, (py + (h * 6)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //7th row 4
        ship.add(new Pixel(ss, (cr + w), (py + (h * 6)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //7th row 5
        ship.add(new Pixel(ss, (cr + (w * 3)), (py + (h * 6)), PIXEL_SHIP_WIDTH, PIXEL_SHIP_HEIGHT));   //7th row 6
        
        for(Pixel ps : ship){       //to initiate movement
            
            ps.setMoveX(xMove);
            ps.setMoveY(yMove);
        }
        
    }
    
    /**
     * drawing the pixel ship
     * @param g 
     */
    
    public void drawPixelShip(Graphics2D g){
        
        for(Pixel ps : ship){
            
            ps.drawPixelShip(g);
            
        }
        
        if(ship.get(centerShip).isDestroyed()){
            
            shipProximty(); 
            drawShipAttack(g); 
        }
        
        collision();
        move();
    }
    
    /**
     * pixel ship movement only bounces when hitting walls as hitting player it sometimes gets stuck then throws an oom error
     */
    
    private void move(){
        
        if(!ship.get(centerShip).isDestroyed()){
                                                                            //if ship hits top of board or bottom of player
            if(ship.get(centerShip).getShieldBounds().getY() <= 0 || ship.get(centerShip).getShieldBounds().getY() >= ss.getPosY() + ss.getHeight() && ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){

                setyMove(2);
                
                if(ship.get(centerShip).getShieldBounds().getY() <= 0) {
                    bounceSound();
                }
                if(ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){
            
                    ss.hitDamage(1);
                }
            }
                                                                    //if ship  hits bottom of board or ship hits top of player
            if(ship.get(centerShip).getShieldBounds().getY() >= BOARD_HEIGHT - ship.get(centerShip).getShieldBounds().getHeight() || ship.get(centerShip).getShieldBounds().getY() + ship.get(centerShip).getShieldBounds().getHeight() >= ss.getPosY() && ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){

                setyMove(-2);
                
                if(ship.get(centerShip).getShieldBounds().getY() >= BOARD_HEIGHT - ship.get(centerShip).getShieldBounds().getHeight()) {
                    bounceSound();
                }
                if(ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){
            
                    ss.hitDamage(1);
                }
            }
                                                                        //if ship hits left side of board or left side of player
            if(ship.get(centerShip).getShieldBounds().getX() <= 0 || ship.get(centerShip).getShieldBounds().getX() <= ss.getPosX() + ss.getWidth() && ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){ 

                setxMove(2);
                
                if(ship.get(centerShip).getShieldBounds().getX() <= 0){
                     bounceSound();
                }
                if(ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){
            
                    ss.hitDamage(1);
                }
            }
                                                                        //if ship hits right side of board or right side of player
            if(ship.get(centerShip).getShieldBounds().getX() >= BOARD_WIDTH - ship.get(centerShip).getShieldBounds().getWidth() ||  ship.get(centerShip).getShieldBounds().getX() + ship.get(centerShip).getShieldBounds().getWidth() <= ss.getPosX() && ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){

                setxMove(-2);
                
                if(ship.get(centerShip).getShieldBounds().getX() >= BOARD_WIDTH - ship.get(centerShip).getShieldBounds().getWidth()){
                    bounceSound();
                }
                if(ship.get(centerShip).getShieldBounds().intersects(ss.getBounds())){
            
                    ss.hitDamage(1);
                }
            }  
                                                                          //player and ship will over lap sometimes trying to prevent that.. not working
            if(ship.get(centerShip).getShieldBounds().contains(ss.getBounds())){
                
                setyMove(-getyMove());
                
            }
           
            for(int i = 0 ; i < ship.size(); i++){

                ship.get(i).setX(ship.get(i).getX() + getxMove());          //needed to continuely update positions to keep moving
                ship.get(i).setY(ship.get(i).getY() + getyMove());
            }  
        }      
    }
  
    /**
     * detect the proximity of the space ship to the pixel parts
     */

    private void shipProximty(){
        
        for(int i = 0 ; i < ship.size(); i++){
            
            if(ss.getBounds().intersects(ship.get(i).getAttackBounds()) && !ship.get(i).isDestroyed()){

                if(!lightningList.contains((Integer) i)){
                    
                    shockSound();
                    lightningList.add(i);
                    ss.hitDamage(2);   //damage done here as is much more controlled
                }
                
            }else{
                
                if(lightningList.contains((Integer) i)){
                    
                    lightningList.remove((Integer) i);
                }
            }   
        }       
    }

    
    /**
     * draws the lightning attack on close proximity
     * @param g 
     */

   
    private void drawShipAttack(Graphics2D g){
        
        for(int i = 0 ; i < lightningList.size(); i++){
            
            AffineTransform old = g.getTransform();
            AffineTransform set = new AffineTransform();
            set.rotate(Math.atan2((ss.getPosY() + (ss.getHeight() / 2)) - (ship.get(lightningList.get(i)).getPosY() + (ship.get(lightningList.get(i)).getHeight() /2)),
                    (ss.getPosX() + (ss.getWidth() / 2)) - (ship.get(lightningList.get(i)).getPosX() + (ship.get(lightningList.get(i)).getWidth() /2))) + Math.toRadians(270)
                    , ship.get(lightningList.get(i)).getPosX() + (ship.get(lightningList.get(i)).getWidth() /2), ship.get(lightningList.get(i)).getPosY() + (ship.get(lightningList.get(i)).getHeight() /2));
            g.setTransform(set);
            g.drawImage(lightning, ship.get(lightningList.get(i)).getPosX() + (ship.get(lightningList.get(i)).getWidth() /2), ship.get(lightningList.get(i)).getPosY() + (ship.get(lightningList.get(i)).getHeight() /2), null);
            g.setTransform(old);
            
 
        }      
    }
    
    private void collision(){
        
        for(Weapon missile : ss.getMissiles()){
            
            if(missile.IsLive()){
                
                for(Pixel pixel : ship){
                    
                    if(!pixel.isDestroyed() && missile.getBounds().intersects(pixel.getBounds())){
                        
                        missile.hit();
                        pixel.missileDamage(10);
                    }
                }
            }
        }
        
       
    }

    public ArrayList<Pixel> getShip() {
        
        return ship;
    }

    public int getxMove() {
        
        return xMove;
    }

    public void setxMove(int xMove) {
        
        this.xMove = xMove;
    }

    public int getyMove() {
        
        return yMove;
    }

    public void setyMove(int yMove) {
        
        this.yMove = yMove;
    }

    public int getCenterShip() {
        
        return centerShip;
    }
    
    
}
