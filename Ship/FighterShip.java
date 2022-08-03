/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ship;

import Ship.Weapons.FighterMissile;
import SpaceInvaders.Board;
import SpaceInvaders.commons;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 *
 * @author hutch
 */
public class FighterShip extends SpaceShip implements commons, MouseMotionListener, MouseListener{
    
    private final String shipImage;
    
    public FighterShip(Board board) {
        
        super(board);
        this.board = board;
        
        shipImage = "fighterShip.png";       
        init();
        
    }

    /**
     * init
     */
    
    private void init(){
               
        loadImage(shipImage, 0, 0); 
        setHealth(400,0,0, Color.CYAN, 0, 0);
        setShieldHealth(400,0,0, Color.CYAN, -20, 0);
        setMaxMissiles(4);
        fighterShipCursor();
        board.addLife();
        addShield();
        
        board.addMouseMotionListener(this);
        board.addMouseListener(this); 
    }
    
    /**
     * cross hair cursor for fighter ship
     */
    
    private void fighterShipCursor(){
        
        board.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
            new ImageIcon(getClass().getResource("/MiscImages/crosshairsWhite.png")).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH),
            new Point(0,0),"custom cursor"));
    }

    /**
     *
     * @param g
     */
    @Override
    public void draw(Graphics2D g){
        
        drawLife(g);
        drawMissiles(g);
        
        AffineTransform at = new AffineTransform();
        AffineTransform old = g.getTransform();        
        at.rotate(Math.atan2(getAngleY(), getAngleX()) - Math.toRadians(90), getCentreX(), getCentreY());
        g.setTransform(at); 
        drawShieldEngaged(g);
        g.drawImage(getImage(), getPosX(), getPosY(), null);               
        g.setTransform(old);
        

    }


    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * angle to face
     * @param e 
     */
    
    @Override
    public void mouseMoved(MouseEvent e) {
        
        setTurnX(e.getX());
        setTurnY(e.getY());

    }
                         
    /**
     * action keys to enable 
     * @param board
     */
    
    @Override
    public void enableKeys(Board board){

        board.addMouseListener(this);
        
        AbstractAction up = new AbstractAction(){               //up

            @Override
            public void actionPerformed(ActionEvent e) {
                                                                //using distance to stop ship doing donuts when reaching mouse point
                if(getDistance() > 50){    
                    
                    moveX -= (int) Math.round(6 * Math.cos(Math.atan2(getAngleY(), getAngleX())));
                    moveY -= (int) Math.round(6 * Math.sin(Math.atan2(getAngleY(), getAngleX())));
                }
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("W"), "W");
        board.getActionMap().put("W", up);
        
        AbstractAction down = new AbstractAction(){                //down

            @Override
            public void actionPerformed(ActionEvent e) {

                if(getDistance() > 50){    
                    
                    moveX += (int) Math.round(6 * Math.cos(Math.atan2(getAngleY(), getAngleX())));
                    moveY += (int) Math.round(6 * Math.sin(Math.atan2(getAngleY(), getAngleX())));
                }
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("S"), "S");
        board.getActionMap().put("S", down);
        
        AbstractAction left = new AbstractAction(){                 //left

            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(getDistance() > 50){ 
                    
                    moveX -= (int) Math.round(6 * Math.cos(Math.atan2(getAngleY(), getAngleX()) - Math.toRadians(90)));
                    moveY -= (int) Math.round(6 * Math.sin(Math.atan2(getAngleY(), getAngleX()) - Math.toRadians(90)));
                }
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("A"), "A");
        board.getActionMap().put("A", left);
        
        AbstractAction right = new AbstractAction(){                //right

            @Override
            public void actionPerformed(ActionEvent e) {

                  if(getDistance() > 50){ 
                    
                    moveX += (int) Math.round(6 * Math.cos(Math.atan2(getAngleY(), getAngleX()) - Math.toRadians(90)));
                    moveY += (int) Math.round(6 * Math.sin(Math.atan2(getAngleY(), getAngleX()) - Math.toRadians(90)));
                }
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("D"), "D");
        board.getActionMap().put("D", right);
              
        AbstractAction fireNuke = new AbstractAction(){                //fire nuke

            @Override
            public void actionPerformed(ActionEvent e) {                
    
                if(nuke != null &&  nuke.isNukeReady()){

                    nuke.fire(getThis());

                }    
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true), "fireNuke");
        board.getActionMap().put("fireNuke", fireNuke);
    }
    
            
    /**
     * action keys to disabled
     * @param board
     */
    
    @Override
    public void disableKeys(Board board){
        
        board.removeMouseListener(this);
        
        AbstractAction up = new AbstractAction(){               //up

            @Override
            public void actionPerformed(ActionEvent e) {

             
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("W"), "none");
        board.getActionMap().put("W", up);
        
        AbstractAction down = new AbstractAction(){                //down

            @Override
            public void actionPerformed(ActionEvent e) {

  
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("S"), "none");
        board.getActionMap().put("S", down);
        
        AbstractAction left = new AbstractAction(){                 //left

            @Override
            public void actionPerformed(ActionEvent e) {

               
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("A"), "none");
        board.getActionMap().put("A", left);
        
        AbstractAction right = new AbstractAction(){                //right

            @Override
            public void actionPerformed(ActionEvent e) {

             
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("D"), "none");
        board.getActionMap().put("D", right);
        
        
        AbstractAction fireNuke = new AbstractAction(){                //fire nuke

            @Override
            public void actionPerformed(ActionEvent e) {                
    
                if(nuke != null &&  nuke.isNukeReady()){

                    nuke.fire(getThis());

                }    
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true), "none");
        board.getActionMap().put("fireNuke", fireNuke);
    }

    @Override
    public void mouseClicked(MouseEvent e) {  
    }

    @Override
    public void mousePressed(MouseEvent e) {
      
        if(e.getButton() == MouseEvent.BUTTON1){
            
            if(liveMissileCount()){

                fireSound();
                addNewMissile(new FighterMissile(getThis()));
            }
        }
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
        
}
