/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship;

import Levels.Level2;
import Ship.Health.HealthBar;
import Ship.Health.Shield;
import Ship.Weapons.Missiles;
import Ship.Weapons.Nuke;
import Ship.Weapons.Weapon;
import SpaceInvaders.Board;
import SpaceInvaders.commons;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import static SpaceInvaders.commons.BOARD_WIDTH;
import static SpaceInvaders.commons.SHIELD_HEIGHT;
import static SpaceInvaders.commons.SHIELD_WIDTH;
import static SpaceInvaders.commons.SPACESHIP_HEIGHT;
import static SpaceInvaders.commons.SPACESHIP_WIDTH;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class SpaceShip implements commons{
    
    protected Image image;  //spaceship image
    protected int width;    // += SPACESHIP_WIDTH 
    protected int height;   // += SPACESHIP_HEIGHT 
    protected int moveX;    //change to move on x axis
    protected int moveY;    //change to move on y axis
    protected int posX;     //x coord of start position + moveX
    protected int posY;     //y coord of start position + moveX
    protected int turnX;    //angles of mouse coords
    protected int turnY;    //angles of mouse coords
    protected int angleX;   //angle between mouse coords and player
    protected int angleY;   //angle between mouse coords and player
    protected int centreX;  //center of spaceShip in x coord
    protected int centreY;  //center of spaceShip in y coord
    protected int distance; //distance between spaceship and mouse position
    
    protected int life;
    protected Image hearts;
    
    protected ArrayList<Weapon> missiles;
    protected int maxMissiles;
    protected Nuke nuke;
    protected Shield shield;
    protected Board board;
    protected HealthBar health;
    protected HealthBar shieldHealth;
    
    protected File hitSound;            //sounds for player hit
    protected AudioInputStream aisHit;
    
    protected File fireSound;            //sounds for player firing
    protected AudioInputStream aisFire;
    
    protected File loseLifeSound;            //sounds for player firing
    protected AudioInputStream aisLoseLife;
    
    private boolean hitTime;            //out of memory error occurs sometimes with pixel ship level 6 when play and ship overlap, attempt to reduce the call rate of hit method

    public SpaceShip(Board board) {
        
        this.board = board;
        init();
    }
    
    private void init(){
               
        missiles = new ArrayList<>();
        loadLifeImage();
        loadSounds();
    }
    
    /**
     * set image
     * @param getImage 
     * @param width 
     * @param height 
    */
    
    protected void loadImage(String getImage, int width, int height){
        
        this.width = SPACESHIP_WIDTH + width;
        this.height = SPACESHIP_HEIGHT + height;
        
        try {

            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/Ship/" + getImage));
            ImageIcon ii = new ImageIcon(bi);
            image = ii.getImage().getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    /**
     * set healthBar
     * @param maxValue
     * @param x
     * @param y
     * @param width
     * @param color
     * @param height 
     */
    
    protected void setHealth(int maxValue, int width, int height, Color color, int x, int y){
        
        health = new HealthBar(maxValue, width, height, color, x, y);
        addHealthBar();
    }
     
    /**
     * set Shield healthBar
     * @param maxValue
     * @param x
     * @param y
     * @param width
     * @param color
     * @param height 
     */
    
    protected void setShieldHealth(int maxValue, int width, int height, Color color, int x, int y){
        
        shieldHealth = new HealthBar(maxValue, width, height, color, x, y);
        
    }
    
    /**
     * loads the heart image for life
     */
    
    private void loadLifeImage(){
        
        try {
            
            BufferedImage buf = ImageIO.read(getClass().getResourceAsStream("/Ship/UpgradeImages/heart.png"));
            ImageIcon ii = new ImageIcon(buf);
            hearts = ii.getImage().getScaledInstance(ITEM_WIDTH, ITEM_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }


    public SpaceShip getThis(){
        
        return this;
    }

    public Image getImage() {
        
        return image;
    }

    public Board getBoard() {
        
        return board;
    }

    public int getPosX() {
        
        posX = SPACESHIP_START_X + moveX;
        
        return posX;
    }

    public int getPosY() {
        
        posY = SPACESHIP_START_Y + moveY;
        
        return posY;
    }

    public void setMoveX(int moveX) {
        
        this.moveX += moveX;
    }

    public void setMoveY(int moveY) {
        
        this.moveY += moveY;
    }

    public int getTurnX() {
        
        return turnX;
    }

    public void setTurnX(int turnX) {
        
        this.turnX = turnX;
    }

    public int getTurnY() {
        
        return turnY;
    }

    public void setTurnY(int turnY) {
        
        this.turnY = turnY;
    }

    public int getAngleX() {
        
        return getPosX() - getTurnX();
    }

    public int getAngleY() {
        
        return getPosY() - getTurnY();
    }

    public int getWidth() {
        
        return width;
    }

    public int getHeight() {
        
        return height;
    }
    
    public int getCentreX() {
        
        centreX = getPosX() + (getWidth() / 2) - 2;
        
        return centreX;
    }

    public int getCentreY() {
        
        centreY = getPosY() + (getHeight() / 2);
        
        return centreY;
    }

    /**
     * distance between play position and mouse position
     * @return 
     */
    
    public int getDistance() {
        
        double pointY = Math.abs(getAngleY());
        double pointX = Math.abs(getAngleX());
        
        distance = (int) Math.hypot(pointX, pointY);
        
        return distance;
    }
    
    public int getHealth(){
        
        return health.getValue();
    }
        
    /**
     * handles ship damage
     * @param damage 
     */
    
    public void hitDamage(int damage){
        
        if(shield != null && shield.isEngaged()){
            
            shield.hitDamage(damage);
            hitSound();
            
        }else{

            health.setValue((health.getValue() - damage)); 
            hitSound();
        }
        
        if(health.getValue() <= 0){
            
            if(life > 0){
                
                loseLifeSound();
                board.loseLife();
                health.setValue(health.getMaximum()); 
                
            }else{
                
                board.setLevel(8);  //level seven in game over screen
            }
            
        }
    }
        
    public void addHealthBar(){
        
        board.add(health);
    }
    
    public void removeHealthBar(){
        
        board.remove(health);
    }
    
    public void addShield(){
        
        shieldHealth.setValue(shieldHealth.getMaximum());   //for when a second shield is colllected, otherwise new shield will have zero health
        shield = new Shield(this, shieldHealth);
        health.setValue(health.getMaximum()); 
    }

    public Shield getShield() {
        
        return shield;
    }

    public ArrayList<Weapon> getMissiles() {
        
        return missiles;
    }
           
    public void addNewMissile(Weapon missile){
        
        missiles.add(missile);
    }
    
    public void setMaxMissiles(int maxMissiles) {
        
        this.maxMissiles = maxMissiles;
    }
       
    /**
     * makes sure no more than maxMissiles can be fires at once
     * @return 
     */
    
    public boolean liveMissileCount(){
        
        int count = 0;
        
        for(Weapon missile : missiles){
                  
            if(missile.IsLive()){

                count++;
            }
        } 
   
        return count < maxMissiles;
    }

    public Nuke getNuke() {
        
        return nuke;
    }

    public void addNuke() {
        
        nuke = new Nuke(this);
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(getPosX(), getPosY(), width, height);
    }  

    
    /**
     * drawing methods
     * @param g 
     */
    
    public void draw(Graphics2D g){
        
        drawLife(g);
        drawNukeReady(g);
        drawNuke(g);
        drawMissiles(g);
        g.drawImage(image, getPosX(), getPosY(), null);
        drawShieldEngaged(g);
                       
    }
    
    /**
     * draws the lives
     * @param g 
     */
    
    protected void drawLife(Graphics2D g){
        
        life = board.getLife();
        int heartX = HEART_X;
        int heartY = HEART_Y;
        
        for(int i = 0 ; i < life; i++){
            
            g.drawImage(hearts, heartX, heartY, null);
            heartY -= ITEM_HEIGHT;
        }
        
    }
        
    /**
     * draws missiles
     * @param g 
     */
    
    protected void drawMissiles(Graphics2D g){
        
        for(Weapon missile : missiles){
            
            if(missile.IsLive()){
                
                missile.draw(g);
            }
        }
    }
               
    /**
     * shield on ship
     * @param g 
     */
    
    protected void drawShieldEngaged(Graphics2D g){

        if(shield != null && shield.isEngaged()){
          
            g.drawImage(shield.getEngagedImage(), getPosX() - 15, getPosY() - 10, SHIELD_WIDTH, SHIELD_HEIGHT, null);
            
        }
    }
    
    /**
     * draws nuke
     * @param g 
     */
    
    protected void drawNuke(Graphics2D g){
        
        if(nuke != null && nuke.isFired()){
            
            nuke.drawNuke(g);
        }
    }
    
    /**
     * draws nuke symbol when nuke ready to fire
     * @param g 
     */
    
    protected void drawNukeReady(Graphics2D g){
                   
        if(nuke != null){

            if(nuke.isNukeReady()){

                g.drawImage(nuke.getNukeAvailable(), NUKE_READY_X , NUKE_READY_Y, null);

            }else{

                g.drawImage(nuke.getNukeDropReloading(), NUKE_READY_X , NUKE_READY_Y, null);
            }
        }
    } 
    
                
    /**
     * loads sounds for player so they fire first time otherwise they wont play on first run
     * reload from their respective methods to stop the reloading of all sounds for specific sound to be reloaded
     * 
     */
    
    private void loadSounds(){
        
        try {
            
            hitSound = new File("src/Ship/Sounds/playerHit.wav");
            aisHit = AudioSystem.getAudioInputStream(hitSound.toURI().toURL());
            fireSound = new File("src/Ship/Sounds/playerFire.wav");
            aisFire = AudioSystem.getAudioInputStream(fireSound.toURI().toURL());
            loseLifeSound = new File("src/Ship/Sounds/loseLife.wav");
            aisLoseLife = AudioSystem.getAudioInputStream(loseLifeSound.toURI().toURL());
       
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);       
        }       
    }
    
    /**
     * plays player hit sound
     * 
     */
    
    protected void hitSound(){

        if(!isHitTime()){
            
            try {

                hitSound = new File("src/Ship/Sounds/playerHit.wav");
                aisHit = AudioSystem.getAudioInputStream(hitSound.toURI().toURL());          

                Clip clip = AudioSystem.getClip();
                clip.open(aisHit);

                FloatControl fc =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float volume = 0.1f;

                fc.setValue(20f * (float) Math.log10(volume));
                clip.start();


            } catch (LineUnavailableException | IOException ex) {

                Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);

            } catch (UnsupportedAudioFileException ex) {

                Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        
        setHitTime(true);
        hitTimer();
    }
   
    /**
     * timer to stop over calling resulting in a oom error
     */
    
    private void hitTimer(){
        
        new javax.swing.Timer(500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                setHitTime(false);
                ((Timer)e.getSource()).stop();
            }           
        }).start();
    }
    
    /**
     * plays player fire sound
     */
    
    protected void fireSound(){
                   
        try {

            fireSound = new File("src/Ship/Sounds/playerFire.wav");
            aisFire = AudioSystem.getAudioInputStream(fireSound.toURI().toURL());
            
            Clip clip = AudioSystem.getClip();
            clip.open(aisFire);
            
            FloatControl fc =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = 0.1f;
            
            fc.setValue(20f * (float) Math.log10(volume));
            
            clip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
       
    /**
     * plays player lose life sound
     */
    
    protected void loseLifeSound(){
                  
        try {
            
            loseLifeSound = new File("src/Ship/Sounds/loseLife.wav");
            aisLoseLife = AudioSystem.getAudioInputStream(loseLifeSound.toURI().toURL());
            
            Clip clip = AudioSystem.getClip();
            clip.open(aisLoseLife);
            clip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
                  
    /**
     * action keys to enable 
     * @param board
     */
    
    public void enableKeys(Board board){

        AbstractAction up = new AbstractAction(){               //up
   
            @Override
            public void actionPerformed(ActionEvent e) {

                if(getPosY() > 0) setMoveY(-6);
               
            }
        };

        board.getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        board.getActionMap().put("up", up);
        
        AbstractAction down = new AbstractAction(){                //down

            @Override
            public void actionPerformed(ActionEvent e) {

                if(getPosY() < BOARD_HEIGHT - height) setMoveY(6);
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        board.getActionMap().put("down", down);
        
        AbstractAction left = new AbstractAction(){                 //left

            @Override
            public void actionPerformed(ActionEvent e) {

                if(getPosX() > 0) setMoveX(-6);
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        board.getActionMap().put("left", left);
        
        AbstractAction right = new AbstractAction(){                //right

            @Override
            public void actionPerformed(ActionEvent e) {

                if(getPosX() < BOARD_WIDTH - width) setMoveX(6);
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        board.getActionMap().put("right", right);
        
        AbstractAction fire = new AbstractAction(){                //fire missile

            @Override
            public void actionPerformed(ActionEvent e) {

                if(liveMissileCount()){
                    
                    addNewMissile(new Missiles(getThis()));
                    fireSound();
                }
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "fire");
        board.getActionMap().put("fire", fire);
        
        AbstractAction fireNuke = new AbstractAction(){                //fire nuke

            @Override
            public void actionPerformed(ActionEvent e) {                
    
                if(nuke != null &&  nuke.isNukeReady()){

                    nuke.fire(getThis());

                }    
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true), "fireNuke");
        board.getActionMap().put("fireNuke", fireNuke);
    }
    
            
    /**
     * action keys to disabled
     * @param board
     */
    
    public void disableKeys(Board board){
        
        AbstractAction up = new AbstractAction(){               //up

            @Override
            public void actionPerformed(ActionEvent e) {

                 if(getPosY() > 0) setMoveY(-6);
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");
        board.getActionMap().put("up", up);
        
        AbstractAction down = new AbstractAction(){                //down

            @Override
            public void actionPerformed(ActionEvent e) {

               if(getPosY() < BOARD_HEIGHT - height) setMoveY(6);
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "none");
        board.getActionMap().put("down", down);
        
        AbstractAction left = new AbstractAction(){                 //left

            @Override
            public void actionPerformed(ActionEvent e) {

                 if(getPosX() > 0) setMoveX(-6);
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "none");
        board.getActionMap().put("left", left);
        
        AbstractAction right = new AbstractAction(){                //right

            @Override
            public void actionPerformed(ActionEvent e) {

                 if(getPosX() < BOARD_WIDTH - width) setMoveX(6);
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "none");
        board.getActionMap().put("right", right);
        
        AbstractAction fire = new AbstractAction(){                //fire missile

            @Override
            public void actionPerformed(ActionEvent e) {

                if(liveMissileCount()){
                    
                    addNewMissile(new Missiles(getThis()));
                }
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "none");
        board.getActionMap().put("fire", fire);
        
        AbstractAction fireNuke = new AbstractAction(){                //fire nuke

            @Override
            public void actionPerformed(ActionEvent e) {                
    
                if(nuke != null &&  nuke.isNukeReady()){

                    nuke.fire(getThis());

                }    
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true), "none");
        board.getActionMap().put("fireNuke", fireNuke);
    }
        
    /**
     * resets spaceship to starting position
     */
    
    public void resetPosiiton(){
        
        //x position
        
        if(getPosX() < SPACESHIP_START_X){
            
            setMoveX(1);
            
        }else if(getPosX() > SPACESHIP_START_X){
            
            setMoveX(-1);
            
        }else{
            
            moveX = 0;
        }
        
        //y position
        
        if(getPosY() < SPACESHIP_START_Y){
            
            setMoveY(1);
            
        }else if(getPosY() > SPACESHIP_START_Y){
            
            setMoveY(-1);
            
        }else{
            
            moveY = 0;
        }       
    }
    
    /**
     * resets spaceship to starting position with timer
     */
    
    public void resetPosiitonTimer(){
        
                
        new javax.swing.Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
//x position
        
                disableKeys(board);
        
                if(getPosX() < SPACESHIP_START_X){

                    setMoveX(1);

                }else if(getPosX() > SPACESHIP_START_X){

                    setMoveX(-1);

                }else{

                    moveX = 0;
                }

                //y position

                if(getPosY() < SPACESHIP_START_Y){

                    setMoveY(1);

                }else if(getPosY() > SPACESHIP_START_Y){

                    setMoveY(-1);

                }else{

                    moveY = 0;
                } 
                
                if(isShipReset()){
                    
                    ((Timer)e.getSource()).start();
                }
            }
        }).start();
    }
        
    /**
     * checks spaceship reset to start position
     * @return 
     */
    
    public boolean isShipReset(){
        
        return getPosX() == SPACESHIP_START_X && getPosY() == SPACESHIP_START_Y;
    }
    
    public HealthBar getHealthBar(){
        
        return health;
    }

    public int getLife() {
        
        return life;
    }

    public boolean isHitTime() {
        
        return hitTime;
    }

    public void setHitTime(boolean hitTime) {
        
        this.hitTime = hitTime;
    }
    
    
}
