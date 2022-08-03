/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship.Health;

import Ship.BasicShip;
import Ship.SpaceShip;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */
public class Shield implements commons{
    
    private SpaceShip ss;
    private Image dropImage;
    private Image engagedImage;
    private int x;
    private int y;
    private int moveY;
    private int posY;
    private boolean collected;
    private boolean engaged;
    private HealthBar shieldHealthBar;

    /**
     * shield drop
     * @param x
     * @param y
     * @param ss 
     */
    
    public Shield(int x, int y, SpaceShip ss) {
        
        this.x = x;
        this.y = y;
        this.ss = ss;
        init();
    }
    
    /**
     * shield engaged 
     * @param ss 
     * @param shieldHealthBar
     */
    public Shield(SpaceShip ss, HealthBar shieldHealthBar){
        
        this.ss = ss;
        this.shieldHealthBar = shieldHealthBar;
        setEngaged(true);
        init();
    }
    
    private void init(){
        
        loadDropImage();
        loadEngagedImage();
        
    }

    private void loadDropImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("/Ship/UpgradeImages/shield.png"));
        
        dropImage = ii.getImage().getScaledInstance(ITEM_WIDTH, ITEM_HEIGHT, Image.SCALE_SMOOTH);
    }
    
    private void loadEngagedImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("/Ship/UpgradeImages/shieldEngaged.png"));
        
        engagedImage = ii.getImage().getScaledInstance(SHIELD_WIDTH, SHIELD_HEIGHT, Image.SCALE_SMOOTH);
    }
   
    /**
     * item being dropped
     * @param g  
     */
    
    public void drop(Graphics2D g){
       
        g.drawImage(dropImage, x, y + moveY, ITEM_WIDTH, ITEM_HEIGHT, null);
        posY = y + moveY;
        
        move();
        
        if(isCollected()){
                        
            collected();

        }
    }
    
    
    private void move(){
        
        moveY += 2;
    }

    public boolean isCollected() {
        
        return getBounds().intersects(ss.getBounds());
    }

    public void setCollected(boolean collected) {
        
        this.collected = collected;
    }

    public boolean isInBounds() {
        
        return posY < BOARD_HEIGHT + ITEM_HEIGHT;
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(x, posY, ITEM_WIDTH, ITEM_HEIGHT);
    }
    
    public boolean getCollected(){
        
        return collected;
    }
    
    public void collected(){
        
        setCollected(true);
        ss.addShield();
    }
    
    public Rectangle getEngagedBounds(){
        
        return new Rectangle(ss.getPosX() - 15, ss.getPosY() - 10, SHIELD_WIDTH, SHIELD_HEIGHT);
    }
    
    public void hitDamage(int damage){
        
        shieldHealthBar.setValue(shieldHealthBar.getValue() - damage);
        
        if(shieldHealthBar.getValue() <= 0){
            
             setEngaged(false);
        }
    }

    public int getHealth() {
        
        return shieldHealthBar.getValue();
    }

    public HealthBar getShieldHealthBar() {
        
        return shieldHealthBar;
    }
    
    public boolean isEngaged() {
        
        return engaged;
    }

    public void setEngaged(boolean engaged) {
        
        if(engaged){
            
            ss.getBoard().add(shieldHealthBar);
            
        }else{
            
            ss.getBoard().remove(shieldHealthBar);
        }
        
        shieldHealthBar.setVisible(engaged);
        
        this.engaged = engaged;
    }

    public Image getEngagedImage() {
        
        return engagedImage;
    }
    
    
}
