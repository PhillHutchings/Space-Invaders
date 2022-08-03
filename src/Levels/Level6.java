 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import ItemDrops.LifeDrop;
import ItemDrops.ShieldDrop;
import Ship.SpaceShip;
import SpaceInvaders.Board;
import SpaceInvaders.Enemy.PixelShip.PixelShip;
import java.awt.Graphics2D;

/**
 *
 * @author hutch
 */
public class Level6 {
    
    private SpaceShip ss;
    private Board board;
    private PixelShip pixelGen1;
    private PixelShip pixelGen2;
    private PixelShip pixelGen3;
    private ShieldDrop shieldDrop;
    private LifeDrop lifeDrop;
    private boolean init;
    
    public Level6(SpaceShip ss, Board board){
        
        this.ss = ss;
        this.board = board;   
    }
    
    private void init(){
  
        pixelGen1 = new PixelShip(ss);
        pixelGen2 = new PixelShip(ss);
        pixelGen3 = new PixelShip(ss);
        lifeDrop = new LifeDrop(pixelGen1.getShip().get(pixelGen1.getCenterShip()), ss);
        shieldDrop = new ShieldDrop(pixelGen2.getShip().get(pixelGen2.getCenterShip()), ss);
        board.getMusic().level6();     //level 6 music
    }
    
    public void drawLevel6(Graphics2D g){
        
        if(!isInit()){
            
            init();
            setInits(true);
            
        }else{
            
           pixelGen1.drawPixelShip(g);
           
           if(pixelGen1.getShip().get(pixelGen1.getCenterShip()).isDestroyed()){
               
                lifeDrop.draw(g); 
                pixelGen2.drawPixelShip(g);
                         
                if(pixelGen2.getShip().get(pixelGen2.getCenterShip()).isDestroyed()){
               
                    shieldDrop.draw(g);
                    pixelGen3.drawPixelShip(g);
               
                    if(pixelGen3.getShip().get(pixelGen3.getCenterShip()).isDestroyed()){
               
                        board.setLevel(7);
                        
                        //GAME COMPLETED
               
                    }
                }
           }
           
           ss.draw(g);
        }
        
    }

    public boolean isInit() {
        
        return init;
    }

    public void setInits(boolean inits) {
        
        init = inits;
    }
    
    
}
