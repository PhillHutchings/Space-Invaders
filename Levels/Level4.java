/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import Ship.SpaceShip;
import Ship.FighterShip;
import Ship.Weapons.Weapon;
import SpaceInvaders.Board;
import SpaceInvaders.Enemy.Planet.PlanetShip;
import SpaceInvaders.Enemy.Planet.PlanetShipMissile;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class Level4 implements commons{
   
    private SpaceShip ss;
    private final Board board;
    private Image planet;               //the planet image
    private Image messageBox;               //message box saying proximty alert
    private Image messageBoxControls;               //message box saying new space ship controls
    private BufferedImage planetEntry;          //end picture when landing on planet
    private BufferedImage planetEntryARGB;      //
    private int planetX;
    private int planetY;
    private boolean planetInPosition;   //for when the planet gets to the final position to start other methods
    private boolean message;            //for message
    private PlanetShip planetShip;      //planets ship that comes to fight
    private int closerX;                //planet x width for expansion during landing scene
    private int closerY;                //planet y height for expansion during landing scene
    private int amp;                    //alpha value for planet entry picture
    private boolean enterPlanetScreen;          //for when the fight is over and the action of the entering and leaving the planet
    private boolean fadeIn;                 //for the end pic fade in and out
    private boolean controlBox;             //for when the new controls of the spaceship are shown
    private boolean inits;
    
    private File alarmSound;                //alarm sound for proximity alert
    private AudioInputStream aisAlarm;
    private Clip alarm;
    
    public Level4(SpaceShip ss, Board board) {
        
        this.ss = ss;
        this.board = board;
  
    }
    
    private void init(){
        
        planetX = (BOARD_WIDTH / 2) - (PLANET_WIDTH /2);
        planetY = 0 - (PLANET_HEIGHT * 2);
        loadPlanet();
        planetAproach();
        loadPlanetMessageBox();
        loadPlanetMessageBoxControls();
        loadPlanetEntry();
        loadSounds();
    }
    
    /**
     * load planet image
     */
    
    private void loadPlanet(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("/MiscImages/planet.png"));
        planet = ii.getImage().getScaledInstance(PLANET_WIDTH, PLANET_HEIGHT, Image.SCALE_SMOOTH);
    }
    
    /**
     * load planet message box
     */
    
    private void loadPlanetMessageBox(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("/MiscImages/planetMessageBox.png"));
        messageBox = ii.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        
    }
    
     /**
     * load planet message box for new controls
     */
    
    private void loadPlanetMessageBoxControls(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("/MiscImages/fighterShipControlBox.png"));
        messageBoxControls = ii.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        
    }
           
    /**
     * load planetEntry image
     */
    
    private void loadPlanetEntry(){
 
        try {
            
            planetEntry = ImageIO.read(new File(System.getProperty("user.dir") + "/src/SpaceInvaders/Enemy/Planet/planetEntry.jpg"));
            
            planetEntryARGB = new BufferedImage(planetEntry.getWidth(), BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        } catch (IOException ex) {
            
            Logger.getLogger(Level4.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
   
    public void drawLevel4(Graphics2D g){
        
        if(!isInits()){
            
            init();
            setInits(true);
        }
                
        drawPlanet(g);
        
        if(isMessage()){                //message box at start proximity alert
            
            g.drawImage(messageBox, (BOARD_WIDTH / 2) - (messageBox.getWidth(null) / 2), (BOARD_HEIGHT / 2) - (messageBox.getHeight(null) / 2), null);
        }
        
        if(planetShip != null){

            if(planetShip.isPlayMessage()){           //message played when planet ship defeated

               if(!planetShip.isAdjustToLand() && !planetShip.isEndScene()){        //end scene boolean to stop re do after planet landing planetShipadjustToLand() set to false again
                
                   planetShip.adjustSpaceShipAndPlanetShipPosition();          //adjusts ships to position to origanl positions
               }
            }
        }         
    
        ss.draw(g);
        
        if(planetShip != null){
        
            planetShip.draw(g); 
        }
        
        if(planetShip != null && !planetShip.isDestroyed()){
            
            collision();

        }
                  
        if(isEnterPlanetScreen()){          //end scene entering planet city pic fades in and out
            
            planetEntryScrren(g);
        }
        
        if(isControlBox()){
            
            g.drawImage(messageBoxControls, (BOARD_WIDTH / 2) - (messageBoxControls.getWidth(null) / 2), (BOARD_HEIGHT / 2) - (messageBoxControls.getHeight(null) / 2), null);
        }
    }
 
    /**
     * planet moving into position
     */
    
    public void planetAproach(){
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
            
            planetY++;
            
            if(planetY >= (BOARD_HEIGHT / 2) - (PLANET_HEIGHT / 2)){

                setPlanetInPosition(true);  
                setMessage(true);
                messageTimer();
                alarmSound();
                ex.shutdown();
            }                      
        };
        
        ex.scheduleAtFixedRate(task, 3000, 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * timer for message box
     */
    
    private void messageTimer(){
        
        new javax.swing.Timer(3000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                alarm.stop();            //stops alarm... clip too long
                setMessage(false);
                board.getMusic().level4();
                planetShip = new PlanetShip(ss, 300, 300, PLANET_SHIP_WIDTH, PLANET_SHIP_HEIGHT);
                planetShip.addHealthBar();
                ((Timer)e.getSource()).stop();
            }       
        }).start();        
    }   
        
    /**
     * pre loads the sounds for proximity alert
     */  
    
    private void loadSounds(){
        
        try {
            
            alarmSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/craftMove.wav");
            aisAlarm = AudioSystem.getAudioInputStream(alarmSound.toURI().toURL());
              
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);       
        }       
    }
    
    /**
     * plays the alarm sound
     */
    
    private void alarmSound(){
     
        try {
            
            alarmSound = new File("src/SpaceInvaders/Enemy/Planet/planetAlarm.wav");
            aisAlarm = AudioSystem.getAudioInputStream(alarmSound.toURI().toURL());          

            alarm = AudioSystem.getClip();
            alarm.open(aisAlarm);
            
            float volume = 0.1f;
            FloatControl fc =  (FloatControl) alarm .getControl(FloatControl.Type.MASTER_GAIN);   
            fc.setValue(20f * (float) Math.log10(volume));
            
            alarm.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * draws planet
     * @param g 
     */
    
    private void drawPlanet(Graphics2D g){
        
        if(planetShip != null && planetShip.isAdjustToLand()){
            
            landOnPlanet(g);

        }else{
            
            g.drawImage(planet, planetX, planetY, null);
        }
    }
    
    /**
     * planet rises up to players to simulate landing
     * @param g 
     */
    
    private void landOnPlanet(Graphics2D g){
        
        if(planetShip.isAdjustToLand()){    
            
            if(!fadeIn){        //planet coming in closer .. grows

                    g.drawImage(planet, (BOARD_WIDTH / 2) - ((PLANET_WIDTH + closerX) / 2) , (BOARD_HEIGHT / 2) - ((PLANET_HEIGHT + closerY) / 2), PLANET_WIDTH + closerX, PLANET_HEIGHT + closerY, null);

                    closerX+=2;
                    closerY+=2;

                    setEnterPlanetScreen(true);

            }else{          //after picture of planet city starts to fade out planet .. shrinks

                g.drawImage(planet, (BOARD_WIDTH / 2) - ((PLANET_WIDTH + closerX) / 2) , (BOARD_HEIGHT / 2) - ((PLANET_HEIGHT + closerY) / 2), PLANET_WIDTH + closerX, PLANET_HEIGHT + closerY, null);

                closerX-=2;
                closerY-=2;

                if(closerX == 0 || closerY == 0){

                    planetShip.setAdjustToLand(false);
                    
                }
            }
        }
    }
    
    /**
     * fading in and out picture when landing on planet
     * @param g 
     */
    
    private void planetEntryScrren(Graphics2D g){
                
        if(!fadeIn){            //fade in
            
            amp += 3;

            Graphics2D fader = planetEntryARGB.createGraphics();
            fader.drawImage(planetEntry, null, 0, 0);

            RescaleOp ro = new RescaleOp(new float[]{1f, 1f, 1f, (float) amp / 500}, new float[]{0, 0, 0, 0}, null);
            BufferedImage filter = ro.filter(planetEntryARGB, null);

            g.drawImage(filter, 0, 0, null);

            if(amp >= 700){         //acts as a timer
                
               
                board.upgradeShip();
                ss = board.getSs();
                planetShip = null;                  //planetship dosent leave planet with you
                fadeIn = true;
            }
            
        }else{          //fade back out
            
            amp -= 3;

            Graphics2D fader = planetEntryARGB.createGraphics();
            fader.drawImage(planetEntry, null, 0, 0);

            RescaleOp ro = new RescaleOp(new float[]{1f, 1f, 1f, (float) amp / 500}, new float[]{0, 0, 0, 0}, null);
            BufferedImage filter = ro.filter(planetEntryARGB, null);

            g.drawImage(filter, 0, 0, null);
            
            if(amp <= 1){
                
                setEnterPlanetScreen(false);     // ends this scene
                setControlBox(true);            // boolean to show the new controls for new ship
                controlBoxTimer();
            }
        }
    }
    
    /**
     * timer to stop showing the control box timer
     */
    
    private void controlBoxTimer(){
        
        new javax.swing.Timer(4000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                setControlBox(false); 
                moveOnToNextLevel();
                ((Timer)e.getSource()).stop();
            }
        }).start();
    }
    
    /**
     * planet moves to next level
     */
    
    private void moveOnToNextLevel(){
        
        setPlanetInPosition(false);          //starts stars moving again
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
          
            planetY++;
            
            if(planetY > BOARD_HEIGHT){
                     
                ss.enableKeys(board);
                board.setLevel(5);  
                ex.shutdown();
            }
        };
        
        ex.scheduleAtFixedRate(task, 1000, 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * collision detection
     */
    
    private void collision(){
        
        for(Weapon missile : ss.getMissiles()){           //space ship missiles
            
            if(missile.IsLive()){
                
                if(missile.getBounds().intersects(planetShip.getBounds())){
                    
                    planetShip.missileDamage(30);
                    
                    missile.hit();
                    
                    if(planetShip.getHealth() <= 0){
                        
                        planetShip.setDestroyed(true);
                        planetShip.stopFiring();
                        ss.disableKeys(board);
                        
                        if(!planetShip.isPlayMessage()){
                            planetShip.plaYMessage();
                        }
                        //NEXTLEVEL
                    }
                }
            }
        }
        
        if(planetShip != null){         //planet ship missiles
            
            for(PlanetShipMissile missile : planetShip.getMissiles()){

                if(missile.isLive() && missile.getBounds().intersects(ss.getBounds())){

                    ss.hitDamage(10); 
                    missile.Hit();
                }
            }
        }
 
        if(ss.getNuke() != null && ss.getNuke().isFired()){             //spaceship nuke
            
            if(!ss.getNuke().isHit() && ss.getNuke().getNukeBounds().intersects(planetShip.getBounds())){
                
                planetShip.nukeDamage(150);
                ss.getNuke().setHit(true);
                
                if(planetShip.getHealth() <= 0){
                    
                    planetShip.setDestroyed(true);
                    planetShip.stopFiring();
                    ss.disableKeys(board);
                    
                    if(!planetShip.isPlayMessage()){
                            planetShip.plaYMessage();
                        }
                    //NEXTLEVEL
                }
            }
        }
    }

    public boolean isPlanetInPosition() {
        
        return planetInPosition;
        
    }

    public void setPlanetInPosition(boolean planetInPosition) {
        
        this.planetInPosition = planetInPosition;
    }

    public boolean isMessage() {
        
        return message;
    }

    public void setMessage(boolean message) {
        
        this.message = message;
    }

    public boolean isEnterPlanetScreen() {
        
        return enterPlanetScreen;
    }

    public void setEnterPlanetScreen(boolean enterPlanetScreen) {
        
        this.enterPlanetScreen = enterPlanetScreen;
    }

    public boolean isControlBox() {
        
        return controlBox;
    }

    public void setControlBox(boolean controlBox) {
        
        this.controlBox = controlBox;
    }

    public boolean isInits() {
        
        return inits;
    }

    public void setInits(boolean inits) {
        
        this.inits = inits;
    }


    
    
}
