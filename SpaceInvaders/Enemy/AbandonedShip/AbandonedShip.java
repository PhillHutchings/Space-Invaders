/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.AbandonedShip;

import ItemDrops.NukeDrop;
import ItemDrops.ShieldDrop;
import Levels.Level2;
import Ship.SpaceShip;
import SpaceInvaders.Sprite;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class AbandonedShip extends Sprite{

    private String getImage;
    private Image debris;
    private Image explosion;
    private int debrisWidth;
    private int debrisHeight;
    private boolean inPosition;
    private boolean explosionCheck;
    private boolean actionScene;
    
    private ShieldDrop shieldDrop;
    private NukeDrop nukeDrop;
    private boolean itemDrop;
    
    private boolean itemsCollected;
    
    private boolean explosionTime;
    private boolean aliensArrive;
    
    private File explosionSound;            //sounds for abandoned ship explode
    private AudioInputStream aisExplosion;
    
    private boolean end;
    private int debrisMove;
    
    public AbandonedShip(SpaceShip ss, int x, int y, int width, int height) {
        
        super(ss, x, y, width, height);

        init();
        
    }
    
    private void init(){
        
        getImage = "AbandonedShip/AbandonedShip.png";
        message = new String[15];
        debrisWidth = 150;
        debrisHeight = 150;
        debrisMove = 1;
        message = new String[15];
        loadSounds();
        loadImage(getImage);
        
        loadExplosionImage();
        loadDebrisImage();
        initiateDrops();
        
        setActionScene(true);
        
    }
    
        /**
     * load abandoned ship image
     */
    
    private void loadExplosionImage(){

        ImageIcon ii = new ImageIcon(getClass().getResource("/MiscImages/explosion.png"));

        explosion = ii.getImage().getScaledInstance(ABANDONED_SHIP_WIDTH + 400, ABANDONED_SHIP_HEIGHT + 200, Image.SCALE_SMOOTH);
         
    }
    
    /**
    * load destroyed abandoned ship debris image
    */
    
    private void loadDebrisImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("/MiscImages/spaceShipDebris.png"));
        
        debris = ii.getImage().getScaledInstance(ABANDONED_SHIP_WIDTH + 350, ABANDONED_SHIP_HEIGHT + 200, Image.SCALE_SMOOTH);
    }
           
    /**
     * draws debris from destroyed abandoned ship
     * @param g 
     */
    
    public void drawDebris(Graphics2D g){
        
        if(!isEnd()){ 
            
            if(debrisWidth < 400){              //debris animation exanding after explosion

                g.drawImage(debris, getPosX() + 80 - (debrisWidth / 2), getPosY() + 80 - (debrisHeight / 2), debrisWidth+=7, debrisHeight+=3, null);

            }else{

                g.drawImage(debris, getPosX() + 80 - (debrisWidth / 2), getPosY() + 80 - (debrisHeight / 2), debrisWidth, debrisHeight, null);

            }
        
        }else{        //used for when the ship moves at the end of the level the debris moves with stars

            g.drawImage(debris, getPosX() + 80 - (debrisWidth / 2), getPosY() + 80 - (debrisHeight / 2) + debrisMove++, debrisWidth, debrisHeight, null);
        }      
    }
            
    /**
     * move abandoned ship
     */
    
    private void move(){
    
        if(!isInPosition()){        //while abandoned ship is not in position
            
            if(getPosY() > ((BOARD_HEIGHT / 2) - (ABANDONED_SHIP_HEIGHT / 2))){

                //stop moving
                ss.disableKeys(ss.getBoard());
                ss.resetPosiiton();

                if(ss.isShipReset()){       //checks ship back to start position

                    if(!isPlayMessage()){  //checks if message playing
                        
                        setInPosition(true);        //confirms abandoned ship in position  

                        shipMessage();      //initiates message
                        playMessage();      //plays message

                    }
                }            
            }else{

                moveY ++;
            }
        }
    }
    
    /**
     * abandoned ship position
     * @return 
     */
    
    public boolean isInPosition() {
        
        return inPosition;
    }

    public void setInPosition(boolean inPosition) {
        
        this.inPosition = inPosition;
    }
    
    /**
     * ship message to player
     */
    
    private void shipMessage(){
        
        message[0] = "**SENSORS DETECTING INBOUND SHIP**";
        message[1] = "... ... ...";
        message[2] = "Hello?";
        message[3] = "<html>Can You Hear Me? <br/>"
                + "This Is The Captain Of The New Hope</html>";
        message[4] = "<html>Thank God I Thought Id </br> Die Alone Out Here!</html>";
        message[5] = "<html>We Have Been Stranded Here For 3 Weeks <br/>"
                + "Are Ship Was Disabled In Battle Against An <br/>"
                + "UnKnown Enemy, I Am The Only Survivor!</html>";
        message[6] = "Can you Please Help Me?";
        message[7] = "Oh Thank You!";
        message[8] = "<html>I Have Have Some High Tech Weapons And Shield <br/>"
                + "You Can Have Them As Soon As You Get Me Back To <br/>"
                + "My Home Planet As A Thank You!</html>";
        message[9] = "<html>Now If You Just Dock Into The Loading Bay I ... <br/>"
                + "... Oh No!</html>";
        message[10] = "**SENSORS DETECTING INBOUND SHIP**";
        message[11] = "THERE BACK!!!";
        message[12] = "**SENSORS DETECTING INBOUND MISSILE**";
        message[13] = "<html>OH NOOO, Theres No Time! ... <br/>"
                + "Here Take The Items, You Will Need Them To Fight <br/>"
                + "Them Off!</html>";
        message[14] = "GoodBye Stranger";

    }
    
    /**
    *thread to play message
    */
    
    private void playMessage(){

        setPlayMessage(true);     //begins playing message
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
     
            Runnable Task = () -> {
                
                if(line == (message.length)){
   
                    initiateDrops();
                    setPlayMessage(false);      
                    setItemDrop(true);
                    
                    ex.shutdown();
                    
                }else{
            
                    updateMessage(message, line++);
                    
                }
            };
            
        ex.scheduleWithFixedDelay(Task, 1000, message[line].length() * 150, TimeUnit.MILLISECONDS);
       
    }
    
        /**
     * initiates the items dropped
     */
    
    private void initiateDrops(){

        nukeDrop = new NukeDrop(this,ss);
        shieldDrop =  new ShieldDrop(this,ss);
        
        
    }
             
    /**
     * draws item drop
     * @param g 
     */
    
    public void drawItemDrop(Graphics2D g){
        
        if(isItemDrop()){
        
            nukeDrop.draw(g);           //nuke first drop
            
            if(nukeDrop.isCollected()){      
                 
                shieldDrop.draw(g); 
            
                if(shieldDrop.isCollected()){       //shield second to drop

                    setItemDrop(false);
                    setItemsCollected(true);
                    
                }    
            }       
        }   
    }      
        
    /**
     * loads sounds for the level
     */
    
    private void loadSounds(){
        
        try {
            
            explosionSound = new File("src/SpaceInvaders/Enemy/AbandonedShip/abandonedShipExplode.wav");
            aisExplosion = AudioSystem.getAudioInputStream(explosionSound.toURI().toURL());
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        }       
    }
    
    /**
     * plays explosion sound
     */
    
    private void explosionSound(){
        
        try {
            
            Clip clip = AudioSystem.getClip();
            clip.open(aisExplosion);       
            clip.start();
            
        } catch (LineUnavailableException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * draw abandoned ship
     * @param g 
     */
    
    public void drawAbandonedShip(Graphics2D g){
        
        if(!isDestroyed()){
            
            g.drawImage(getImage(), getPosX(), getPosY(), null);
            move();     //moving abandoned ship
        }
            
        if(isExplosionTime()){      //abandoned ship explodes
            
            g.drawImage(getImage(), getPosX(), getPosY(), null);   
            g.drawImage(explosion,  getPosX() - 160, getPosY() - 130, null);
            
            if(!explosionCheck){    //to stop multiple executions
                
                ss.getBoard().getMusic().level2();   //starts playing level2 music for entry of aliens
                explosion();
                explosionSound();
                explosionCheck = true;
            }
        }       
    }
           
    /**
     * explosion timer
     */
    
    private void explosion(){
        
        new javax.swing.Timer(1000, new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                ss.getMissiles().clear();    // just to keep the content low
                
                setExplosionTime(false);     
                setActionScene(false);
                setAliensArrive(true);
                
                ss.enableKeys(ss.getBoard());
                
                ((Timer)e.getSource()).stop();
            }
            
        }).start();
    }
    
    public boolean isItemsCollected() {
        
        return itemsCollected;
    }

    public void setItemsCollected(boolean itemsCollected) {
        
        this.itemsCollected = itemsCollected;
    }       
    
    public boolean isItemDrop() {
        
        return itemDrop;
    }

    public void setItemDrop(boolean itemDrop) {
        
        this.itemDrop = itemDrop;
    }
   

    public boolean isExplosionTime() {
        
        return explosionTime;
    }

    public void setExplosionTime(boolean explosionTime) {
        
        this.explosionTime = explosionTime;
    }

    public boolean isActionScene() {
        
        return actionScene;
    }

    public void setActionScene(boolean actionScene) {
        
        this.actionScene = actionScene;
    }

    public boolean isAliensArrive() {
        
        return aliensArrive;
    }

    public void setAliensArrive(boolean aliensArrive) {
        
        this.aliensArrive = aliensArrive;
    }

    public boolean isEnd() {
        
        return end;
    }

    public void setEnd(boolean end) {
        
        this.end = end;
    }

    
}
