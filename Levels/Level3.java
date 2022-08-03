/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import Ship.SpaceShip;
import Ship.Weapons.Weapon;
import SpaceInvaders.Board;
import SpaceInvaders.Enemy.MysteryCraft.MysteryCraft;
import SpaceInvaders.Enemy.MysteryCraft.MysteryCraftMissile;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author hutch
 */
public class Level3 implements commons{
    
    private final SpaceShip ss;
    private MysteryCraft mCraft;
    private boolean enterCraft;
    private final Board board;
    private boolean inits;
   
    public Level3(SpaceShip ss, Board board) {
        
        this.ss = ss;
        this.board = board;
        
    }
    
    private void init(){
        
        board.getMusic().dialogueAmbient();
        mCraft = new MysteryCraft(ss, board);
        
    }

    public void drawLevel3(Graphics2D g){
      
                    
        if(!isInits()){
            
            init();
            ss.disableKeys(board);
            mysterycraftTimer();
            setInits(true);
        }
        
        if(!mCraft.isDefeated()){

            if(enterCraft && mCraft.isEnterScene()){            //drawing the enter scene for the mystery craft

                mCraft.drawEnterMysteryCraft(g);
            }

            if(mCraft.isFightScene()){             //drawing the fight scene

                mCraft.drawFightScene(g);
                collision();
            }
            
        }else{
            
            mCraft.drawExitScene(g);
        }
                
        ss.draw(g);
        
    }
    
    public void mysterycraftTimer(){
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {

          enterCraft = true; 
          mCraft.mysterycraftEnterMovement();
          
          ex.shutdown();
        };      
        
        ex.scheduleWithFixedDelay(task, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    public MysteryCraft getmCraft() {
        
        return mCraft;
    }
    
    /**
     * collision detection
     */
    
    private void collision(){
        
        for(Weapon missile : ss.getMissiles()){
            
            if(missile.IsLive()){
                
                if(missile.getBounds().intersects(mCraft.getBounds())){
                    
                    mCraft.missileDamage();
                    missile.hit();
                    
                    if(mCraft.getHealthBar().getValue() <= 0){
                    
                        mCraft.setDefeated(true);
                        
                        if(!mCraft.isDefeatMessage()){
                            
                            board.remove(mCraft.getHealthBar());
                            mCraft.playDefeatMessage();
                            mCraft.setDefeatMessage(true);
                        }     
                    }
                }
            }
        }
        
        if(ss.getNuke() != null && ss.getNuke().isFired()){
            
            if(ss.getNuke().getNukeBounds().intersects(mCraft.getBounds()) && !ss.getNuke().isHit()){
                
                mCraft.nukeDamage();
                ss.getNuke().setHit(true);
                
                if(mCraft.getHealthBar().getValue() <= 0){
                    
                    mCraft.setDefeated(true);
                    
                    if(!mCraft.isDefeatMessage()){
                            
                        board.remove(mCraft.getHealthBar());
                        mCraft.playDefeatMessage();
                        mCraft.setDefeatMessage(true);
                    }
                }
            }
        }
        
        for(MysteryCraftMissile missile : mCraft.getMissiles()){
            
            if(missile.IsLive()){
                
                if(missile.getBounds().intersects(ss.getBounds())){
                    
                    missile.setLive(false);
                    ss.hitDamage(10);
                    
                }
            }
        }
        
        if(mCraft.isFireLazer() && mCraft.getLazerBounds().intersects(ss.getBounds())){
            
            ss.hitDamage(2);
             
        }
    }
        
    public boolean isInits() {
        
        return inits;
    }

    public void setInits(boolean inits) {
        
        this.inits = inits;
    }
    
    
}
