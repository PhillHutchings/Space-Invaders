/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import ItemDrops.LifeDrop;
import Ship.SpaceShip;
import SpaceInvaders.Board;
import SpaceInvaders.Enemy.AlienCloner.AlienCloner;
import SpaceInvaders.Enemy.MysteryCraft.MysteryCraft;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class Level5 implements commons{
    
    private SpaceShip ss;
    private final Board board;
    private AlienCloner cloner;
    private MysteryCraft craft;
    private boolean inits;          
    private LifeDrop lifeDrop;
    private boolean end;
    private Image finalRound;

    public Level5(SpaceShip ss, Board board) {
         
        this.ss = ss;
        this.board = board;

    }
    
    private void init(){

        ss.getBoard().getMusic().level5();
        ss.getMissiles().clear();
        cloner = new AlienCloner(ss, ALIEN_CLONER_START_X_1, ALIEN_CLONER_START_Y_1, ALIEN_CLONER_WIDTH_1, ALIEN_CLONER_HEIGHT_1);
        loadFinalRoundLogo();       
    }
    
    /**
     * final round log to show at end of level for a time before starting level 6
     */
    
    private void loadFinalRoundLogo(){
        
        try {
            
            BufferedImage buf = ImageIO.read(getClass().getResourceAsStream("/MiscImages/finalRound.png"));
            ImageIcon ii = new ImageIcon(buf);
            finalRound = ii.getImage().getScaledInstance(400, 100, Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Level5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void drawLevel5(Graphics2D g){
       
        if(!isInits()){   //initiates when drawn first
            
            init();
            setInits(true);
        }
        
        if(!isEnd()){
            
            if(lifeDrop != null){

                lifeDrop.draw(g);
            }

            drawCloners(g);
            ss.draw(g);
            rebirth(cloner);

            if(craft != null){

                craft.calledInAttack(g);        //attack when called

                if(craft.getPosX() <= 0){

                    craft = null;
                }
            }    
        }else{
            
            ss.draw(g);
            g.drawImage(finalRound, (BOARD_WIDTH /2) - (finalRound.getWidth(null) / 2), (BOARD_HEIGHT / 2) - 200, null);
        }
    }
    
    private void drawCloners(Graphics2D g){
        
        cloner.draw(g);

    }    

    public boolean isInits() {
        
        return inits;
    }

    public void setInits(boolean inits) {
        
        this.inits = inits;
    }

    public AlienCloner getAlienCloner() {
        
        return cloner;
    }
    
    /**
     * checks and adds new cloner
     * @param clone 
     */
    
    private void rebirth(AlienCloner clone){
          
        if(clone.isRebirth()){
            
            int width = clone.getWidth();
            
            switch(width){
                
                case ALIEN_CLONER_WIDTH_1:      //second cloner appears
               
                    cloner = new AlienCloner(ss, ALIEN_CLONER_START_X_2, ALIEN_CLONER_START_Y_2, ALIEN_CLONER_WIDTH_2, ALIEN_CLONER_HEIGHT_2);
                    craft = new MysteryCraft(ss, board, cloner, false);         //sets up mystery craft to be used aginst second cloner
        
                break;
                
                case ALIEN_CLONER_WIDTH_2:          //third cloner appears
                    
                    lifeDrop = new LifeDrop(cloner, ss);
                    
                    cloner = new AlienCloner(ss, ALIEN_CLONER_START_X_3, ALIEN_CLONER_START_Y_3, ALIEN_CLONER_WIDTH_3, ALIEN_CLONER_HEIGHT_3);
                    
                        if(craft != null && !craft.isAttackCalled()){           //if mystery craft not used for second cloner it gets updated to be used on the third cloner
                            
                            craft = new MysteryCraft(ss, board, cloner, false);
                        }
                    
                break;
                
                case ALIEN_CLONER_WIDTH_3:          //third cloner dies new level
                                                      
                    endSceneTimer();
                    setEnd(true);   // end scene title comes up and starts countdown to final fight
                    
                break;
            }
        }
    }
    
    /**
     * timer until start of final level
     */
    
    private void endSceneTimer(){
        
        new javax.swing.Timer(5000, new ActionListener(){
            @Override
            
            public void actionPerformed(ActionEvent e) {
                
                board.setLevel(6);
                ((Timer)e.getSource()).stop();
            }     
        }).start();      
    }
    

    public boolean isEnd() {
        
        return end;
    }

    public void setEnd(boolean end) {
        
        this.end = end;
    }
    
    
}
