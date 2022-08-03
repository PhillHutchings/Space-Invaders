/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders;

import Levels.GameFinished;
import Levels.GameOver;
import Levels.GameScreen;
import Levels.Level1;
import Levels.Level2;
import Levels.Level3;
import Levels.Level4;
import Levels.Level5;
import Levels.Level6;
import Levels.Music.Music;
import Ship.BasicShip;
import Ship.FighterShip;
import Ship.SpaceShip;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author hutch
 */
public class Board extends JPanel implements commons{
    
    private Stars[] stars;
    private SpaceShip ss;
     
    private int level;
    private int life;
    
    private Random random; 
    
    private Music music;
    private GameScreen gameScreen;
    private Level1 level1;
    private Level2 level2;
    private Level3 level3;
    private Level4 level4;
    private Level5 level5;
    private Level6 level6;
    private GameOver gameOver;
    private GameFinished finished;
    
    private LoadClonerImages li = new LoadClonerImages();
    
    public Board(){
        
        setLayout(null);           
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        
        initiateLevels();
    }
    
    /**
     * initiates game levels
     */
    
    private void initiateLevels(){

        resetLife();

        ss = new BasicShip(this);       
        ss.enableKeys(this); 
        
        music = new Music(); 
        stars = new Stars[30];
        random = new Random(); 
        
        gameScreen = new GameScreen(this);
        
        level1 = new Level1(ss, this);
        level2 = new Level2(ss, this);
        level3 = new Level3(ss, this);
        level4 = new Level4(ss, this);
    
        gameOver = new GameOver(ss, this);  
        finished = new GameFinished(ss, this);
               
        InitiateStars();

    }
  
    /**
     * creates random stars in array
     */
    
    private void InitiateStars(){
        
        for(int i = 0; i < 30; i++){
            
            int x = random.ints(0, BOARD_WIDTH).findFirst().getAsInt();
            int y = random.ints(0, BOARD_HEIGHT).findFirst().getAsInt();
            int size = random.ints(1, 4).findFirst().getAsInt();
            
            stars[i] = new Stars(x, y, size, Color.WHITE);
        }        
    }
    
    /**
     * adds new star when star falls out of screen 
    */
    
    private void newStar(int s){
        
        int x = random.ints(1, 599).findFirst().getAsInt();
        int y = random.ints(-900, -1).findFirst().getAsInt();
        int size = random.ints(1, 3).findFirst().getAsInt();

        stars[s] = new Stars(x, y, size, Color.WHITE);
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;        
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        
        if(level == 0){
                
            drawStars(g2d);
            gameScreen.draw(g2d);
            
        } 
        
        if(level == 1){
            

            drawStars(g2d);
            
            level1.drawLevel1(g2d);
        }
        
        if(level == 2){

            
            level2.drawLevel2(g2d);    
            
            if(!level2.getAbandonedShip().isInPosition()){
                
                drawStars(g2d);
                
            }else{
                
                drawStarsStill(g2d);
            }           
              
        }
        
        if(level == 3){

            drawStars(g2d);
            
            level3.drawLevel3(g2d);
            
            if(level3.getmCraft().isPlayingMessage() && !level3.getmCraft().isAddMessageArea()){    //mysterycraft first encounter message  cleaned code up later by passing the board through level constructors
      
                add(level3.getmCraft().speechBubble);
                level3.getmCraft().setAddMessageArea(true);

            }
            if(!level3.getmCraft().isPlayingMessage() && level3.getmCraft().isAddMessageArea()){
     
                remove(level3.getmCraft().speechBubble);
                level3.getmCraft().setAddMessageArea(false);
            }
        }
        
        if(level == 4){
            
            if(!level4.isPlanetInPosition()){
                
                drawStars(g2d);
            }else{
                
                drawStarsStill(g2d);
            }
            
            
            level4.drawLevel4(g2d);
        }
        
        if(level == 5){

            drawStars(g2d);
            
            level5.drawLevel5(g2d);
            
        }
        
        if(level == 6){
            
            drawStars(g2d);
            
            level6.drawLevel6(g2d);
            
        }
      
        if(level == 7){

            drawStarsStill(g2d);
            finished.draw(g2d);
        }
        
        if(level == 8){
               
            drawStarsStill(g2d);
            gameOver.draw(g2d);
        }
    }

    /**
     * draws stars moving
     * @param g 
     */
    
    private void drawStars(Graphics2D g){
        
        for(int i = 0; i < stars.length; i++ ){
            
            if(stars[i].getPos() >= 0){
                
                stars[i].draw(g);           //only paint if in screen
            }
            
            stars[i].move();
            
            if(stars[i].getPos() >= BOARD_HEIGHT){      //replace after leaving the screen
                
                newStar(i);
            }
        }    
    }
          
    /**
     * draws stars stationary
     * @param g 
     */
    
    private void drawStarsStill(Graphics2D g){
        
        for(int i = 0; i < stars.length; i++ ){
            
            if(stars[i].getPos() >= 0){
                
                stars[i].draw(g);           //only paint if in screen
            }            
        }    
    }            

    public int getLevel() {
        
        return level;
    }

    public void setLevel(int level) {
        
        this.level = level;
    }

    public int getLife() {
        
        return life;
    }

    public void addLife() {
        
        life++;
    }
 
    public void loseLife() {
        
        life--;
    }
    
    public void resetLife(){
        
        life = 3;
    }
    

    public SpaceShip getSs() {
        
        return ss;
    }
    
    /**
     * for alien cloner level 5 pre-loaded images for death and rebirth scene
     * @return 
     */
    
    public ArrayList<ImageIcon> getDeathImageList(){
        
        return li.getDeathImages();
    }
    
    /**
     * method for upgrading the ship to fighter ship in level 4
     * this method is needed in this way because the health bars and shield bars will not be in sync with each other
     * tried many ways to do this but this is the only one that worked
     */

    public void upgradeShip() {
        
        this.ss = new FighterShip(this);
     
        level5 = new Level5(ss, this);
        level6 = new Level6(ss, this);
        gameOver = new GameOver(ss, this);  
        finished = new GameFinished(ss, this);
    }

    /**
     * restarts game
     */
    
    public void restartGame(){

        resetLife();

        ss = new BasicShip(this);
        ss.enableKeys(this); 
        
        music = new Music(); 
        stars = new Stars[30];
        random = new Random(); 
        
        gameScreen = new GameScreen(this);
        
        level1 = new Level1(ss, this);
        level2 = new Level2(ss, this);
        level3 = new Level3(ss, this);
        level4 = new Level4(ss, this);
    
        gameOver = new GameOver(ss, this);  
        finished = new GameFinished(ss, this);
               
        InitiateStars();

    }

    public Music getMusic() {
        
        return music;
    }
}
