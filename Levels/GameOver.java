/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import Ship.SpaceShip;
import SpaceInvaders.Board;
import SpaceInvaders.commons;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import static SpaceInvaders.commons.BOARD_WIDTH;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

/**
 *
 * @author hutch
 */
public class GameOver implements commons, MouseMotionListener, MouseListener{
    
    private SpaceShip ss;
    private final Board board;
    private Image gameOverText;
    private Image gameOverCoin;
    private boolean clearScreen;
    private int mouseX;
    private int mouseY;
    private Cursor insert;
    private Rectangle pointer;
    private File coinSlot;
    private AudioInputStream aisCoin;

    public GameOver(SpaceShip ss, Board board) {
        
        this.ss = ss;
        this.board = board;
        loadText();
        loadCoin();
        loadCoinSlotSound();
        insert = Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(getClass().getResource("/MiscImages/insert-coin.png")).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH),
                new Point(0,0),"custom cursor");
    }
    
    /**
     * loads game over text
     */
    
    private void loadText(){
        
        try {
            
            BufferedImage buf = ImageIO.read(getClass().getResourceAsStream("/MiscImages/gameOverText.png"));
            ImageIcon ii = new ImageIcon(buf);
            gameOverText = ii.getImage().getScaledInstance(GOTEXT_WIDTH, GOTEXT_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(GameOver.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
           
    /**
     * loads game over coin
     */
    
    private void loadCoin(){
        
        try {
            
            BufferedImage buf = ImageIO.read(getClass().getResourceAsStream("/MiscImages/coin.png"));
            ImageIcon ii = new ImageIcon(buf);
            gameOverCoin = ii.getImage().getScaledInstance(COIN_WIDTH , COIN_HEIGHT, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(GameOver.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * loads sound of coin going in machine
     */
    
    private void loadCoinSlotSound(){
        
        try {
            
            coinSlot = new File("src/MiscImages/coinSlot.wav");
            aisCoin = AudioSystem.getAudioInputStream(coinSlot.toURI().toURL());
            
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(GameOver.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(GameOver.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
     * plays the coin sound
     */
    
    private void coinSound(){
     
        try {
                        
            Clip coinClip= AudioSystem.getClip();
            coinClip.open(aisCoin);
            coinClip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    /**
     * clears board and adds listeners
     * @param g 
     */
    
    private void clearBoard(Graphics2D g){
                
        board.getMusic().gameOver();
        board.removeAll();
        ss.disableKeys(board);
        board.addMouseMotionListener(this);
        board.addMouseListener(this);
    }
    
    /**
     *
     * @param g
     */
    
    public void draw(Graphics2D g){
        
        if(!isClearScreen()){
         
            clearBoard(g);
            setClearScreen(true);
            
        }
        
        g.drawImage(gameOverText, (BOARD_WIDTH / 2) - (GOTEXT_WIDTH / 2), (200) - (GOTEXT_HEIGHT / 2), null);
        g.drawImage(gameOverCoin, (BOARD_WIDTH / 2) - (COIN_WIDTH / 2), (BOARD_HEIGHT / 2) - (COIN_HEIGHT / 2), null);
        
        checkMousePosition();
    }
    
    /**
     * checks mouse position to change cursor when over coin
     */
    private void checkMousePosition(){
        
        pointer = new Rectangle(mouseX, mouseY, 10,10);
        
        if(coinBounds().intersects(pointer)){
            
            board.setCursor(insert);
            
        }else{
            
            board.setCursor(Cursor.getDefaultCursor());
        }
        
    }
    
    /**
     * bounds of the coin image
     * @return 
     */
    
    private Rectangle coinBounds(){
        
        return new Rectangle((BOARD_WIDTH / 2) - (COIN_WIDTH / 2), (BOARD_HEIGHT / 2) - (COIN_HEIGHT / 2), COIN_WIDTH, COIN_HEIGHT);
    }

    public boolean isClearScreen() {
        
        return clearScreen;
    }

    public void setClearScreen(boolean clearScreen) {
        
        this.clearScreen = clearScreen;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
     
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
        mouseX = e.getX();
        mouseY = e.getY();

    }

    /**
     * if coin clicked on game resets
     * @param e 
     */
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if(pointer.intersects(coinBounds())){
            
            coinSound();
            board.removeMouseMotionListener(getThis());
            board.removeMouseListener(getThis()); 
            board.setCursor(Cursor.getDefaultCursor());
            board.restartGame();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }

    private GameOver getThis(){
        
        return this;
    }
}
