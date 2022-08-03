/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders;

import Levels.Level2;
import Ship.SpaceShip;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import static SpaceInvaders.commons.BOARD_WIDTH;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 *
 * @author hutch
 */
public class Sprite implements commons{
    
    protected Image image;
    protected BufferedImage buffim;
    protected Image destroyedImage;
    protected SpaceShip ss;
    protected Random random;
    protected int x;            //start x
    protected int y;            //start y
    protected int moveX;        //moveable x coord
    protected int moveY;        //moovable y coord
    protected int posX;         //current x position
    protected int posY;         //current y position
    protected int rotX;         //x angle to player
    protected int rotY;         //y  angle to player
    protected int savedRotX;    //x angle savd when rotation stopped
    protected int savedRotY;    //y angle savd when rotation stopped
    protected int centreX;      //x centre of sprite
    protected int centreY;      //y centre of sprite
    protected int width;
    protected int height;  
    protected boolean destroyed;        //check if defeated
    protected boolean inBounds;         //check inside board height and width
    protected JProgressBar health;      //health bar
    protected JLabel speechBubble;      //for dialogue
    protected String[] message;         //message to be played
    protected int line;                 //index of string in message
    protected boolean playMessage;      //check message playing
    protected File messageSound;            //sounds for message changing
    protected AudioInputStream aisMessage;

    public Sprite(SpaceShip ss, int x, int y, int width, int height) {
        
        this.ss = ss;
        this.x = x;
        this.y = y;
        this.rotX = getRotX();
        this.rotY = getRotY();
        this.width = width;
        this.height = height;
        random = new Random();
        init();
        
    }

    private void init(){
        
        speechBubble = new JLabel("", JLabel.CENTER);
        speechBubble.setBackground(Color.BLACK);
        speechBubble.setBounds((BOARD_WIDTH / 2) - 250,(BOARD_HEIGHT - 400), 450, 150);
        speechBubble.setFont(new Font("OCR A Extended", Font.BOLD, 18));
        speechBubble.setForeground(Color.GREEN);
        loadSounds();
    }
    
    /**
     * set image
     * @param getImage 
     */
    
    protected void loadImage(String getImage){
        
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/" + getImage));  
            ImageIcon ii = new ImageIcon(bi);
            image = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            
            
        } catch (IOException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * set destroyed image
     * @param getImage 
     * @param width 
     * @param height 
     */
    
    protected void loadDestroyedImage(String getImage, int width, int height){
        
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/" + getImage));
            ImageIcon ii = new ImageIcon(bi);
            destroyedImage = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * set health bar
     * @param orientation
     * @param max
     * @param setValue
     * @param x
     * @param y
     * @param width
     * @param height
     * @param background
     * @param foreground 
     */
    
    protected void setHealth(String orientation, int max, int setValue, int x, int y, int width, int height, Color background, Color foreground){
        
        switch(orientation){
            
            case "Horizontal":
                
                health = new JProgressBar(SwingConstants.HORIZONTAL);
                break;
                
            case "Vertical":
                
                health = new JProgressBar(SwingConstants.VERTICAL);
                break;
                
            default:
                
                health = new JProgressBar(SwingConstants.HORIZONTAL);
                break;
        }
               
        health.setMaximum(max);
        health.setValue(setValue);
        health.setBounds(x, y, width, height);
        health.setForeground(foreground);
        health.setBackground(background);
              
    }
    
    public void addHealthBar(){
        
        ss.getBoard().add(health);
    }
    
    protected void removeHealthBar(){
        
        ss.getBoard().remove(health);
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(getPosX(), getPosY(), width, height);
    }

    public int getX() {
        
        return x;
    }

    public int getY() {
        
        return y;
    }

    public void setX(int x) {
        
        this.x = x;
    }

    public void setY(int y) {
        
        this.y = y;
    }

    public void setMoveX(int moveX) {  
        
        this.moveX += moveX;
    }

    public void setMoveY(int moveY) {
        
        this.moveY += moveY;
    }

    public int getMoveX() {
        
        return moveX;
    }

    public int getMoveY() {
        
        return moveY;
    }

    public int getPosX() {
        
        posX = x + moveX;
        
        return posX;
    }

    public int getPosY() {
        
        posY = y + moveY;
        
        return posY;
    }

    public final int getRotX() {
        
        rotX = getPosX() - ss.getPosX();
        
        return rotX;
    }

    public final int getRotY() {
        
        rotY = getPosY() - ss.getPosY();
        
        return rotY;
    }

    public int getCentreX() {
        
        centreX = getPosX() + width / 2;
        
        return centreX;
    }

    public int getCentreY() {
        
        centreY = getPosY() + height / 2;
        
        return centreY;
    }
    
    public Image getImage() {
        
        return image;
    }

    public BufferedImage getBuffim() {
        
        return buffim;
    }
    
    public Image getDestroyedImage() {
        
        return destroyedImage;
    }

    public int getWidth() {
        
        return width;
    }

    public int getHeight() {
        
        return height;
    }
    
    public boolean isDestroyed() {
        
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        
        this.destroyed = destroyed;
    }

    public boolean isInBounds() {
        
        return getBounds().intersects(new Rectangle(0, 0, BOARD_WIDTH, BOARD_HEIGHT));
    }

    public int getHealth() {
        
        return health.getValue();
    }
             
    /**
     * damage done by player missiles
     * @param damage
     */
    
    public void missileDamage(int damage){
        
        health.setValue(health.getValue() - damage);
        
        if(getHealth() <= 0){
            
            setDestroyed(true);
            removeHealthBar();
        }
    }
       
    /**
     * damage done by player nuke
     * @param damage
     */
    
    public void nukeDamage(int damage){
        
        health.setValue(health.getValue() - damage);
        
        if(getHealth() <= 0){
            
            setDestroyed(true);
            removeHealthBar(); 
        }        
    }
                 
    /**
     * damage done by mystery craft
     * @param damage
     */
    
    public void craftDamage(int damage){
        
        health.setValue(health.getValue() - damage);
        
        if(getHealth() <= 0){
            
            setDestroyed(true);
            removeHealthBar();
        }
    }
      
    /**
     * updates new message line to text area
     * @param text
     * @param line 
     */
    
    public void updateMessage(String[] text, int line){
        
        speechBubble.setText(text[line]);
        messageSound();
        
    }

            
    /**
     * loads sounds for message
     */
    
    private void loadSounds(){
        
        try {
            
            messageSound = new File("src/MiscImages/messageSound.wav");
            aisMessage = AudioSystem.getAudioInputStream(messageSound.toURI().toURL());
       
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);       
        }       
    }
    
    /**
     * plays message sound
     */
    
    protected void messageSound(){
       
        loadSounds();
            
        try {

            Clip clip = AudioSystem.getClip();
            clip.open(aisMessage);
            clip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public boolean isPlayMessage() {
        
        return playMessage;
    }

    public void setPlayMessage(boolean playMessage) {
        
        if(playMessage){

            ss.getBoard().add(speechBubble);
        
        }else{

            ss.getBoard().remove(speechBubble);
        }
        
        speechBubble.setVisible(playMessage);
        
        this.playMessage = playMessage;
    }

    public int setSavedRotX() {
        
        savedRotX = getRotX();
        
        return savedRotX;
    }

    public int setSavedRotY() {
        
        savedRotY = getRotY();
        
        return savedRotY;
    }

    public int getSavedRotX() {
        
        return savedRotX;
    }

    public int getSavedRotY() {
        
        return savedRotY;
    }

  
}
