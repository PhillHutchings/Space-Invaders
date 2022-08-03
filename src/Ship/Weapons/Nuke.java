/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship.Weapons;

import Levels.Level2;
import Ship.SpaceShip;
import SpaceInvaders.commons;
import static SpaceInvaders.commons.SPACESHIP_WIDTH;
import java.awt.Graphics2D;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class Nuke extends Weapon implements commons{
    
    private String getImage;
    private Image nukeAvailable;
    private Image nukeDropReloading;    //translucent image for reloading
    private Image detonate;             //explosion imge
    private boolean hit;                //nuke hit status
    private boolean nukeReady;          //nuke ready to fire
    private boolean fired;              //nuke has been fired
    private boolean detonation;         //boolean check to stop explosion timer executing more than once
    
    private File launchSound;           //for launch sound file
    private AudioInputStream aisLaunch;
    private Clip launch;
    private boolean launchSoundCheck;
    
    private File detonateSound;         //for detonation sound file
    private AudioInputStream aisDetonate;
    private Clip explosion;
      
    /**
     * constructor for firing nuke
     * @param ss 
     */
    
    public Nuke(SpaceShip ss) {
        
        super(ss);
        nukeReady = true;               
        init();
    }
    
    /**
     * constructor for the nuke drop
     * @param x
     * @param y 
     * @param ss 
     */
    
    public Nuke(int x, int y, SpaceShip ss){

        super(ss);
        this.x = x;
        this.y = y;
        this.ss = ss;
        init();
        
    }
    
    private void init(){
        
        getImage = "Nuke.png";
        loadNukeAvailableImage();
        loadReloadingNukeImage();
        loadImage(getImage, ITEM_WIDTH - 2, ITEM_HEIGHT -2);
        loadDetonateImage();
        loadSounds();
    }
    

    /**
     * loads item drop image plus nuke ready
     */
    
    protected void loadNukeAvailableImage(){

        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/Ship/UpgradeImages/nukeDrop.png"));
            ImageIcon ii = new ImageIcon(bi);
            nukeAvailable = ii.getImage().getScaledInstance(ITEM_WIDTH, ITEM_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Nuke.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    /**
     * nuke reloading image
     */
    
    private void loadReloadingNukeImage(){ 
        
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/Ship/UpgradeImages/nukeDropReloading.png"));
            ImageIcon ii = new ImageIcon(bi);
            nukeDropReloading = ii.getImage().getScaledInstance(ITEM_WIDTH, ITEM_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Nuke.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    /**
     * nuke detonating image
     */
    
    private void loadDetonateImage(){
         
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/Ship/UpgradeImages/nukeDetonate.png"));
            ImageIcon ii = new ImageIcon(bi);
            detonate = ii.getImage().getScaledInstance(NUKE_DETONATE_WIDTH, NUKE_DETONATE_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Nuke.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * loads the nuke launching and explosion sounds
     */
    
    private void loadSounds(){
        
        try {
            
            launchSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/craftMove.wav");
            aisLaunch = AudioSystem.getAudioInputStream(launchSound.toURI().toURL());
            detonateSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/lazer.wav");
            aisDetonate = AudioSystem.getAudioInputStream(detonateSound.toURI().toURL());  
      
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);       
        }       
    }
    
    /**
     * sound of nuke launching
     */
    
    private void launchSound(){
     
        try {
            
            detonateSound = new File("src/Ship/Sounds/launch.wav");
            aisDetonate = AudioSystem.getAudioInputStream(detonateSound.toURI().toURL());  
            launch = AudioSystem.getClip();
            launch.open(aisDetonate);
            launch.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    /**
     * sound of nuke detonating
     */
    
    private void detonateSound(){
     
        try {
            
            launchSound = new File("src/Ship/Sounds/detonate.wav");
            aisLaunch = AudioSystem.getAudioInputStream(launchSound.toURI().toURL());       

            explosion = AudioSystem.getClip();
            explosion.open(aisLaunch);
            explosion.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
        
    /**
     * draws nuke being fired
     * @param g 
     */
    
    public void drawNuke(Graphics2D g){
                
        if(isFired() && !isHit()){
            
            g.drawImage(getImage(), x - (ITEM_WIDTH / 2), y + moveY, null);
            nukeFire();
            checkBounds();
            
            if(!isLaunchSoundCheck()){
                
                launchSound();
                setLaunchSoundCheck(true);
            }
        } 
               
        if(isHit()){
            
            g.drawImage(detonate, getPosX() - (NUKE_DETONATE_WIDTH / 2), getPosY() - (NUKE_DETONATE_HEIGHT / 2), null);
            
            if(!isDetonation()){
                
                setLaunchSoundCheck(false);
                launch.stop();          //stops launch sound on impact
                detonateSound();        //starts detonated sound
                detonateTimer();
                detonated(); 
                reloadNuke();
                setDetonation(true);
            }         
        }
    }
    
    /**
     * nuke movement
     */
    
    private void nukeFire(){
        
        moveY -= 4;
    }
    
        /**
     * check weapon is still in the bounds of the screen
     * @return 
     */
    
    @Override
    public boolean checkBounds(){
        
        if(getPosX() < (0 - getWidth()) || getPosX() > BOARD_WIDTH){
            
            reloadNuke();
            return false;
        }
        if(getPosY() < (0 - getHeight()) || getPosY() > BOARD_HEIGHT){
            
            reloadNuke();
            return false;
            
        }else{
            
            return true;
        }        
    }
                          
    /**
     * timer for the detonation image
     */
    
    private void detonated(){
        
        new javax.swing.Timer(1000, new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
               
                setFired(false);
                setHit(false);                
                setDetonation(false);

                ((Timer)e.getSource()).stop();
            }
        
        }).start();        
    }
    
    /**
     * timer to turn off explosion sound
     */
    private void detonateTimer(){
        
        new javax.swing.Timer(3000, new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
               
                explosion.stop();

                ((Timer)e.getSource()).stop();
            }
        
        }).start();        
    }
    
         
    /**
     * timer so nuke can be fired once only every 30 seconds
     */
    
    private void reloadNuke(){
        
        new javax.swing.Timer(30000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                      
                setNukeReady(true);
                
                ((Timer)e.getSource()).stop();
            }        
        }).start();
    }
        
    /**
     * method to call when firing nuke
     * @param ss 
     */
    
    public void fire(SpaceShip ss){
        
        this.ss = ss;
        this.x = ss.getPosX() + (SPACESHIP_WIDTH / 2);
        this.y = ss.getPosY() + 10;
        moveY = 0;
        this.posY = y + moveY;
        this.posX = x;

        setFired(true);
        setNukeReady(false);
    }
       
    /**
     * gets bounds for fired nuke
     * @return 
     */
    
    public Rectangle getNukeBounds(){
        
       return new Rectangle(x, posY, ITEM_WIDTH -2, ITEM_HEIGHT -2);
    }

        
    public boolean isHit() {
        
        return hit;
    }

    public void setHit(boolean hit) {
        
        this.hit = hit;
    }
    
    public Rectangle getDetonatedBounds(){
        
        return new Rectangle(getPosX() - (NUKE_DETONATE_WIDTH / 2), posY - (NUKE_DETONATE_HEIGHT / 2), NUKE_DETONATE_WIDTH, NUKE_DETONATE_HEIGHT);
    }

    public Image getNukeDropReloading() {
        
        return nukeDropReloading;
    }

    public boolean isFired() {
        
        return fired;
    }

    public void setFired(boolean fired) {
        
        this.fired = fired;
    }

    public boolean isNukeReady() {
        
        return nukeReady;
    }

    public void setNukeReady(boolean nukeReady) {
        
        this.nukeReady = nukeReady;
    }  

    public Image getNukeAvailable() {
        
        return nukeAvailable;
    }

    public boolean isDetonation() {
        
        return detonation;
    }

    public void setDetonation(boolean detonation) {
        
        this.detonation = detonation;
    }

    public boolean isLaunchSoundCheck() {
        
        return launchSoundCheck;
    }

    public void setLaunchSoundCheck(boolean launchSoundCheck) {
        
        this.launchSoundCheck = launchSoundCheck;
    }

}


