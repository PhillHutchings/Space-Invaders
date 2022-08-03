/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import Ship.SpaceShip;
import SpaceInvaders.Board;
import SpaceInvaders.commons;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
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
public class GameFinished implements commons{
    
    private Board board;
    private SpaceShip ss;
    private Image logo;
    private Image completed;
    private boolean stamp;
    private boolean clearScreen;
    private File stampSound;
    private AudioInputStream aisStamp;
    
    public GameFinished(SpaceShip ss, Board board){
        
        this.ss = ss;
        this.board = board;
        init();
    }
    
    private void init(){
        
        loadLogo();
        loadCompleted();
        loadStampSound();
    }
   
    /**
     * loads space invaders logo 
     */
    
    private void loadLogo(){
        
        try {
            
            BufferedImage buf = ImageIO.read(getClass().getResourceAsStream("/MiscImages/logo.png"));
            ImageIcon ii = new ImageIcon(buf);
            logo = ii.getImage().getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * loads completed text
     */
    
    private void loadCompleted(){
        
        try {
            
            BufferedImage buf = ImageIO.read(getClass().getResourceAsStream("/MiscImages/completed.png"));
            ImageIcon ii = new ImageIcon(buf);
            completed = ii.getImage().getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
           
    /**
     * loads sound of stamp fo completed text
     */
    
    private void loadStampSound(){
        
        try {
            
            stampSound = new File("src/MiscImages/stamp.wav");
            aisStamp = AudioSystem.getAudioInputStream(stampSound.toURI().toURL());
            
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(GameOver.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(GameOver.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
     * plays the stamp sound
     */
    
    private void stampSound(){
     
        try {
                        
            Clip stampClip = AudioSystem.getClip();
            stampClip.open(aisStamp );
            stampClip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    /**
     * timer till finished text appears
     */
    
    private void textTimer(){ 
        
        new javax.swing.Timer(2000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                    
                stampSound();
                setStamp(true);
                ((Timer)e.getSource()).stop();
            }
        }).start();
    }
    
    /**
     * clears board plays music and starts text timer
     * @param g 
     */
    
    private void clearBoard(Graphics2D g){
        
        board.removeAll();
        ss.disableKeys(board);
        board.getMusic().finished();
        textTimer();

    }
    
    public void draw(Graphics2D g){
        
        if(!isClearScreen()){
         
            clearBoard(g);
            setClearScreen(true);
            
        }

        g.drawImage(logo, LOGO_X, LOGO_Y, null);
        
        if(isStamp()){
            
            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();
            at.rotate(Math.toDegrees(210), (BOARD_WIDTH / 2) ,BOARD_HEIGHT / 2); 
            g.setTransform(at);
            g.drawImage(completed, (BOARD_WIDTH / 2) - (LOGO_WIDTH / 2), (BOARD_HEIGHT / 2) - (LOGO_HEIGHT / 2), null);
            g.setTransform(old);
        }
        
    }
    
    public boolean isClearScreen() {
        
        return clearScreen;
    }

    public void setClearScreen(boolean clearScreen) {
        
        this.clearScreen = clearScreen;
    }

    public boolean isStamp() {
        
        return stamp;
    }

    public void setStamp(boolean stamp) {
        
        this.stamp = stamp;
    }
    
}
