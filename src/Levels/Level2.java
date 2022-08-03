/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import Ship.SpaceShip;
import Ship.Weapons.Weapon;
import SpaceInvaders.Board;
import SpaceInvaders.Enemy.AbandonedShip.AbandonedShip;
import SpaceInvaders.Enemy.AbandonedShip.Alien;
import SpaceInvaders.Enemy.AbandonedShip.AlienLazer;
import SpaceInvaders.commons;
import static SpaceInvaders.commons.ALIEN_MOVE_X;
import static SpaceInvaders.commons.ALIEN_MOVE_Y;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;
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
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class Level2 implements commons{
    
    private SpaceShip ss;
    private AbandonedShip as;
    private Board board;

    private int alienMissileY;
    private int alienMissileFire;

    private Random random;
    private Alien[] aliens;
    private ArrayList<AlienLazer> lazers;
    private int destroyedAliens;
    
    private File lazerSound;            //sounds for alien lazer
    private AudioInputStream aisLazer;
       
    private boolean inits;
     
    private boolean nextLevel;

    public Level2(SpaceShip ss, Board board) {
        
        this.ss = ss;   
        this.board = board;

    }
    
    private void init(){
             
        board.getMusic().dialogueAmbient();   
        as = new AbandonedShip(ss, ABANDONED_SHIP_X , ABANDONED_SHIP_Y, ABANDONED_SHIP_WIDTH, ABANDONED_SHIP_HEIGHT);

        random = new Random();
        aliens = new Alien[28];
        lazers = new ArrayList<>();
 
        initiateAliens();
        loadSounds();
    }

    
    
    /**
     * drawing level 2
     * @param g 
     */
    
    public void drawLevel2(Graphics2D g){

                
        if(!isInits()){
            
            init();
            setInits(true);
        }
        
        if(as.isDestroyed()){
            
            as.drawDebris(g);      //destroyed abandoned ship debris , here to behind ship when it explodes
        }
        
        if(as.isActionScene()){
            
            drawAlienMissile(g);        //alien missile that destroys the abandoned ship     
            as.drawItemDrop(g);        //item drop after cutscene
            as.drawAbandonedShip(g);  // ship moves into position            
              
        }
             
        if(as.isAliensArrive()){
            
            drawAliens(g);
            alienLazerLoad();
            drawLazers(g);
            
        }

        ss.draw(g);
               
        collision();
        
    }
    

    /**
     * alien missile destroying abandoned ship
     * @param g 
     */
    
    private void alienMissile(Graphics2D g){
        
        g.setColor(Color.BLUE);
        
        if(!as.isDestroyed()){
            
            g.fillRect(BOARD_WIDTH / 2 - 5, alienMissileY + alienMissileFire -30, 10, 20);
            alienFire();
        }
    }
     
    /**
     * alien missile move
     */
    private void alienFire(){
        
        alienMissileFire +=2;
    }
        
    /**
     * drawing the alien missile that destroys the abandoned ship
     * @param g 
     */
    
    private void drawAlienMissile(Graphics2D g){
        
        if(as.isItemsCollected()){
            
            alienMissile(g);
    //when missile hits abandoned ship it blows up
            if(alienMissileY + alienMissileFire >= as.getPosY() + (as.getHeight() / 2)){
                
                as.setDestroyed(true); 
                
                if(!as.isExplosionTime()){
                    
                    AlienMove();
                    as.setExplosionTime(true);
                }
            }
        }      
    } 
        
    /**
     * creates alien array
     */
    
    private void initiateAliens(){
        
        int x = 50;
        int y = -50;
        
        for(int i = 0; i < aliens.length; i++){
             
            if(i % 4 == 0 && i != 0){
                
                x = 50;
                y -= ALIEN_HEIGHT + 20;
            }
            
            aliens[i] = new Alien(x, y, false);
            x += ALIEN_WIDTH + 20;
        }        
    }
        
    /**
     * draws aliens
     * @param g 
     */
    
    private void drawAliens(Graphics2D g){
        
        for (Alien alien : aliens) {
            
             alien.draw(g);
        }
    }
    
    /**
     * initiates the alien lasers
     */    
    
    private void alienLazerLoad(){
        
        int count = 0;
        
        for(AlienLazer lazer : lazers){
            
            if(lazer.isLive()){
                
                count++;
                
            }
        }
        
        if(count < 5){          //checks no more than five lazers fired at one time
            
            for(Alien alien: aliens){
                
                if(!alien.isDestroyed() && alien.inBoard() && count < 5){       //chooses alien visible on board
                    
                    int arm = random.ints(1, 8).findFirst().getAsInt();     //random number
                    
                    if(arm == 2){       //if 2 this gives a little delay on firing so they dont all fire at once
                        
                        lazers.add(new AlienLazer(alien));          //adds new lazer to arraylist with chosen alien coords
                        alienLazerSound();                  //alien lazer firing sound
                        count++;        //increases count
                        
                    }
                }
            }
        }      
    }
    
    /**
     * loads sounds for the level
     */
    
    private void loadSounds(){
        
        try {
            
            lazerSound = new File("src/SpaceInvaders/Enemy/AbandonedShip/alienLazer.wav");
            aisLazer = AudioSystem.getAudioInputStream(lazerSound.toURI().toURL());
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        }       
    }
    
    /**
     * plays alien laser sound
     */
    
    private void alienLazerSound(){
        
        loadSounds();
                
        try {
            
            Clip clip = AudioSystem.getClip();
            clip.open(aisLazer);       
            clip.start();
            
        } catch (LineUnavailableException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * draws alien lasers
     * @param g 
     */
    
    private void drawLazers(Graphics2D g){
        
        for(AlienLazer lazer : lazers){
            
            if(lazer.isLive()){
                
                lazer.drawLazer(g);
            }
        }
    }    
           
    /**
     * alien movement
     */
    
    public final void AlienMove(){
                
        new javax.swing.Timer(200, new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
                               
                boolean bottom = false;
                
                for (Alien alien : aliens) {        //checks if an alien is at the bottom
                    
                    if (alien.getPosY() >= 500 && !alien.isDestroyed()) {
                        
                        bottom = true;                        
                        break;
                    }    
                }
  
                for (Alien alien1 : aliens) {
                    
                    if (bottom) {                   //check if an alien has reached bottom to stop aliens moving down further 
                        
                        for (Alien alien : aliens) {            //moves aliens back and forth while an alien is at the bottom
                            
                            if (alien.isMoveRight()) {
                                
                                alien.setMoveX(5);
                                
                                if (alien.getMoveX() == ALIEN_MOVE_X) {
                                    
                                    alien.setMoveRight(false);
                                }  
                                
                            }else if (!alien.isMoveRight()) {
                                
                                alien.setMoveX(-5);
                                
                                if (alien.getMoveX() == 0) {
                                    
                                    alien.setMoveRight(true);
                                }
                            }
                        }
                    } else {            //moves aliens 20 to right then down then 20 to left then down .. repeat till get to bottom
                        
                        if (alien1.isMoveRight()) {
                            
                            alien1.setMoveX(5);
                            
                            if (alien1.getMoveX() == ALIEN_MOVE_X) {
                                
                                alien1.setMoveY(ALIEN_MOVE_Y);
                                alien1.setMoveRight(false);
                            }
                            
                        } else if (!alien1.isMoveRight()) { 
                            
                            alien1.setMoveX(-5);
                            
                            if (alien1.getMoveX() == 0) {
                                
                                alien1.setMoveY(ALIEN_MOVE_Y);
                                alien1.setMoveRight(true);
                            }
                        }
                    }
                }
                
               if(board.getLevel() == 3){
                   
                   ((Timer)e.getSource()).stop();
               }
            }
        }).start();
    }
    
   
    /**
     * collision detection
     */
    
    private void collision(){
    
        //missile hitting alien spaceship
        
         for(Weapon missile : ss.getMissiles()){
            
             if(missile.IsLive()){
                 
                for (Alien alien : aliens) {

                    if(!alien.isDestroyed()){       //check alien not already destroyed and missile is live

                        if(missile.getBounds().intersects(alien.getBounds())){

                            missile.hit();
                            alien.hit();

                            if(alien.getHitPoints() == 0){

                                destroyedAliens++;
                                alien.setDestroyed(true);
                            }
                        }
                    }
                }    
             }
         }
           
    // alien lazer hitting spaceship

        for(AlienLazer lazer : lazers){

            if(lazer.isLive()){

                if(lazer.getBounds().intersects(ss.getBounds())){

                    lazer.setLive(false);
                    ss.hitDamage(10);
                         
                }
            }
        }
        
        //nuke collision
        
        if(ss.getNuke() != null && ss.getNuke().isFired()){     
            
            for(Alien alien : aliens){

                if(alien.getBounds().intersects(ss.getNuke().getNukeBounds()) && !alien.isDestroyed()){
                    
                    ss.getNuke().setHit(true);

                    for(Alien alien2 : aliens){

                        if(ss.getNuke().getDetonatedBounds().contains(alien2.getBounds()) && !alien2.isDestroyed()){

                            alien2.hit();
                            alien2.hit();
                            alien2.hit();
                            alien2.hit();

                             if(alien2.getHitPoints() <= 0){

                                destroyedAliens++;
                                alien2.setDestroyed(true);
                               
                            }
                        }

                        if(alien2.getBounds().intersects(ss.getNuke().getDetonatedBounds()) && !alien2.isDestroyed()){

                            alien2.hit();
                            alien2.hit();
                            alien2.hit();

                            if(alien2.getHitPoints() <= 0){

                                destroyedAliens++;
                                alien2.setDestroyed(true);
                     
                            }                       
                        }                                    
                    }
                    
                    break;
                }
            }
        }
        
        //check if spaceship touches alien
                
        for(Alien alien :aliens){
            
            if(ss.getBounds().intersects(alien.getBounds()) && !alien.isDestroyed()){
                
                 ss.hitDamage(20);
                
            }
        }
        
        //check for all aliens destroyed 

        if(destroyedAliens == aliens.length){

            as.setInPosition(false);           //for the stars to move again
            as.setAliensArrive(false);
            as.setEnd(true);            //debris moves with stars 
            
            if(!nextLevel){
                
                nextLevel();
                nextLevel = true;
            } 
        }
    }
    
    /**
     * initiates next level
     */
    
    private void nextLevel(){
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
            
            board.setLevel(3);
            ex.shutdown();
        };
        
        ex.scheduleWithFixedDelay(task, 10000, 1000, TimeUnit.MILLISECONDS);
    }

    public AbandonedShip getAbandonedShip() {
        
        return as;
    }

    public void setAs(AbandonedShip as) {
        
        this.as = as;
    }
    
    public boolean isInits() {
        
        return inits;
    }

    public void setInits(boolean inits) {
        
        this.inits = inits;
    }
    

  
}
