/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.AbandonedShip;

import SpaceInvaders.commons;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author hutch
 */
public class AlienLazer implements commons{
    
    private int x;
    private int y;
    private int posY;
    private int moveY;
    private Alien alien;
    private boolean live;

    public AlienLazer(Alien alien) {
        
        this.alien = alien;
        this.x = alien.getPosX() + (ALIEN_WIDTH / 2);
        this.y = alien.getPosY() + ALIEN_HEIGHT;
        this.live = true;
    }
    
    public void drawLazer(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.BLUE);
        
        g2d.fillRect(x, y + moveY, 4, 10);

        posY = y + moveY;

        lazerMove();
        
        if(!inBounds()){
            
            setLive(false);
        }
    }
     
    private void lazerMove(){
        
        moveY +=4;
    }

    public boolean isLive() {
        
        return live;
    }

    public void setLive(boolean live) {
        
        this.live = live;
    }

    public int getPosY() {
        
        return posY;
    }

    private boolean inBounds(){
        
        return getPosY() < BOARD_HEIGHT; 
    }
    
    public Rectangle getBounds(){
        
        return new Rectangle(x, getPosY(), 4, 10);
    }
    
}
