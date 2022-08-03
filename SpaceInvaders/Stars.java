/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author hutch
 */
public class Stars {
    
    private int x;
    private int y;
    private int size;
    private int starMoveY = 0;
    private Color color;
    private int pos;


    public Stars(int x, int y, int size, Color color) {
        
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    
    public void draw(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(getColor());
        
        g2d.fillRect(x, y + starMoveY, size, size);
        
    }

    public int getStarMoveY() {
        return starMoveY;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        
        return color;
    }
    
    public void move(){
        
        starMoveY ++ ;
        pos = y + starMoveY;
    }
    
    public int getPos() {
       
        return pos;
    }

    public void setY(int y) {
        
        this.y = y;
        starMoveY = 0;
    }
    
    
}
