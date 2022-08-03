/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.MysteryCraft;

import SpaceInvaders.commons;
import static SpaceInvaders.commons.MISSILE_HEIGHT;
import static SpaceInvaders.commons.MISSILE_WIDTH;
import static SpaceInvaders.commons.MYSTERYCRAFT_WIDTH;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author hutch
 */
public class MysteryCraftMissile implements commons {
    
    private int x;
    private int y;
    private int moveY;
    private int posX;
    private int posY;   
    private boolean hitTarget;
    private boolean live;
    private MysteryCraft mCraft;

    public MysteryCraftMissile(MysteryCraft mCraft) {
        
        this.mCraft = mCraft;
        this.x = mCraft.getPosX() + (MYSTERYCRAFT_WIDTH  / 2);
        this.y = mCraft.getPosY() + (MYSTERYCRAFT_HEIGHT - 10);
        this.posY = y + moveY;
        this.posX = x;
        this.live = true;
    }
    
    
    public void draw(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.GREEN);
   
        if(IsLive()){    
            
            g2d.fillRect(x, y + moveY, MISSILE_WIDTH, MISSILE_HEIGHT);
            posY = y + moveY;
            fireMissile();
        }
        
        if(!inBoard()){

            setLive(false);
        }       
    }
    
    private void fireMissile(){
        
        moveY += 6;
    }

    public int getPosX() {
        
        return posX;
    }

    public int getPosY() {
        
        return posY;
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(posX, posY, MISSILE_WIDTH, MISSILE_HEIGHT);
    }
   
    private boolean inBoard(){
        
        return getPosY() < BOARD_HEIGHT;
    }

    public boolean IsLive(){
        
        return live;
    }

    public void setLive(boolean live) {
        
        this.live = live;
    }
}
