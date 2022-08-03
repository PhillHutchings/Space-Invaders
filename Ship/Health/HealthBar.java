/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship.Health;

import SpaceInvaders.commons;
import java.awt.Color;
import javax.swing.JProgressBar;

/**
 *
 * @author hutch
 */
public class HealthBar extends JProgressBar implements commons{
    
    private int maxValue;
    private int width;
    private int height;
    private Color color;
    private int x;
    private int y;

    public HealthBar(int maxValue, int width, int height, Color color, int x, int y) {
        
        super(HEALTH_ORIENT);
        this.maxValue = maxValue;
        this.width = width;
        this.height = height;
        this.color = color;
        this.x = x;
        this.y = y;
        
        init();
    }

    private void init(){
        
        setMaximum(maxValue);
        setValue(maxValue);
        setBounds(HEALTH_X + x, HEALTH_Y + y, HEALTH_WIDTH + width, HEALTH_HEIGHT + height);
        setBackground(Color.BLACK);
        setForeground(color);
    }
       
    
}
