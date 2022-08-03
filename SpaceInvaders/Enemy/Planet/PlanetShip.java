/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.Planet;

import Ship.SpaceShip;
import SpaceInvaders.Sprite;
import SpaceInvaders.commons;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author hutch
 */
public class PlanetShip extends Sprite implements commons {

    private final String getImage = "Planet/PlanetShips.png";
    private final String getDestroyedImage = "Planet/PlanetShipDestroyed.png";
    private ArrayList<PlanetShipMissile> missiles;
    private boolean modify;         //experiment to stop concurrent mod ex -- works perfectly... so far
    private ScheduledExecutorService fireMissilesEx;   
    private boolean adjustToLand;
    private boolean endScene;
     
    
    public PlanetShip(SpaceShip ss, int x, int y, int width, int height) {
        
        super(ss, x, y, width, height);
        init();
    }

    private void init(){
        
        loadImage(getImage);
        loadDestroyedImage(getDestroyedImage, width + 30, height + 30);
        
        setHealth("Horizontal", 1500, 1500, 100, 20, 400, 5, Color.BLACK, Color.BLUE);
        missiles = new ArrayList<>();
        fireMissiles();
        
        message = new String[13];
        initiateMessage();

        moveX = 1;
        moveY = 1;
    }
    
    public void draw(Graphics2D g){
                  
        drawMissiles(g);
        
        if(!isDestroyed()){
                                        //always turns to point at player

            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();            
            at.rotate(Math.atan2(getRotY(), getRotX()) + Math.toRadians(90), getCentreX(), getCentreY());
            g.setTransform(at);
            g.drawImage(getImage(), getPosX(), getPosY(), null);
            g.setTransform(old);

            move();
            
        }else{
            
            stopFiring();
            g.drawImage(getImage(), getPosX(), getPosY(), null);
            g.drawImage(getDestroyedImage(), getPosX() - 10, getPosY() - (height - 10), null);
        }              
    }
    
    private void move(){
            
        //bounces off edges like ball in pong
        
        if(y + moveY <= 0){
            
            moveY = 1;
        }
        
        if(y + moveY >= BOARD_HEIGHT - height){
            
            moveY = -1;
        }
        
        if(x + moveX <= 0){
            
            moveX = 1;
        }
        
        if(x + moveX >= BOARD_WIDTH - width){
            
            moveX = -1;
        }
        
        x = x + moveX;
        y = y + moveY;
        
        posX = x;
        posY = y;
        
    }
    
    private void drawMissiles(Graphics2D g){
      
        if(!modify){
            
            for(PlanetShipMissile missile : missiles){

                missile.draw(g);
            }
        }
    }
    
    private void fireMissiles(){
        
        fireMissilesEx = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {

            modify = true;

            missiles.add(new PlanetShipMissile(thisShip(), ss));
            modify = false;
            
        };
        
        fireMissilesEx.scheduleWithFixedDelay(task, 1000, 3000, TimeUnit.MILLISECONDS);
    }
    
    private PlanetShip thisShip(){
        
        return this;
    }

    public ArrayList<PlanetShipMissile> getMissiles() {
        
        return missiles;
    }

    public void stopFiring(){
        
        fireMissilesEx.shutdown();
    }
    
    /**
     * initiates message after defeat
     */
    
    private void initiateMessage(){
        
        message[0] = "No Please Stop!";
        message[1] = "Dont Destroy Us!";
        message[2] = "Wait.. Your Not Going To Destroy Us?";
        message[3] = "<html>Im Sorry I Thought<br> You Were With Those Evil Aliens.</html>";
        message[4] = "<html>Well Thats A Relief,<br> Im Sorry For Attacking You.</html>";
        message[5] = "You Are A Great Pilot.";
        message[6] = "Even With That Out-Dated Ship.";
        message[7] = "Sorry, But It Is.";
        message[8] = "You Beat Those Aliens With It?";
        message[9] = "Wow..";
        message[10] = "<html>Imagine What You Could Do<br> With A Ship Like Mine.</html>";
        message[11] = "<html>You Could Defeat Those Aliens<br> For Good!</html>";
        message[12] = "<html>Come Down To My PLanet<br> And We Will Gift You A New Ship</html>";

    }
    
    /**
     * plays message after defeat
     */
    
    public void plaYMessage(){
              
        setPlayMessage(true);
        ss.getBoard().getMusic().dialogueAmbient();
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
          
            if(line == message.length){
                
                setPlayMessage(false);
                setEndScene(true);
                setAdjustToLand(true);
                removeHealthBar();
                
                ss.removeHealthBar();     //removes to clean up board for landing scene
                        
                if(ss.getShield() != null){

                    ss.getShield().setEngaged(false);
                }
                
                ex.shutdown();
                
            }else{
                
                updateMessage(message, line++);
            }
        };
        
        ex.scheduleWithFixedDelay(task, 1000, message[line].length() * 200, TimeUnit.MILLISECONDS);
    }
    
    /**
     * adjust the ships positions land onto the planet
     */
    
    public void adjustSpaceShipAndPlanetShipPosition(){
        
        if(getPosX() != (BOARD_WIDTH / 2) - (PLANET_SHIP_WIDTH / 2) || getPosY() != (BOARD_HEIGHT / 2) - (PLANET_SHIP_HEIGHT / 2) || !ss.isShipReset()){
            
            if(getPosX() > (BOARD_WIDTH / 2) - (PLANET_SHIP_WIDTH / 2)){

                moveX--;
            }

            if(getPosX() < (BOARD_WIDTH / 2) - (PLANET_SHIP_WIDTH / 2)){

                moveX++;
            }

            if(getPosY() > (BOARD_HEIGHT / 2) - (PLANET_SHIP_HEIGHT / 2)){

                moveY--;
            }

            if(getPosY() < (BOARD_HEIGHT / 2) - (PLANET_SHIP_HEIGHT / 2)){

                moveY++;
            } 
            
            ss.resetPosiiton();
            
        }   
    }

    public boolean isAdjustToLand() {
        
        return adjustToLand;
    }

    public void setAdjustToLand(boolean adjustToLand) {
        
        this.adjustToLand = adjustToLand;
    }

    public boolean isEndScene() {
        
        return endScene;
    }

    public void setEndScene(boolean endScene) {
        
        this.endScene = endScene;
    }
    
    
}
