/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import Ship.SpaceShip;
import Ship.Weapons.Weapon;
import SpaceInvaders.Board;
import SpaceInvaders.Enemy.Asteroids.Asteroids;
import SpaceInvaders.commons;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author hutch
 */
public class Level1 implements commons{
    
    private Asteroids[] asteroids;
    private SpaceShip ss;
    private Board board;
    private Random random;   
    private int asteroidCount;
    private boolean inits;
    
    public Level1(SpaceShip ss, Board board) {
        
        this.ss = ss;
        this.board = board;
   
    }
    
    private void init(){
        
        asteroids = new Asteroids[50];
        random = new Random();
        initiateAsteroids();
        asteroidShower();
        board.getMusic().level1();

    }
    
    public void drawLevel1(Graphics2D g){
        
        if(!isInits()){
            
            init();
            setInits(true);
        }
        
        ss.draw(g);
        drawAsteroids(g);
        
        collision();
        
        if(checkDestroyed() == (asteroids.length - 1)){
            
            board.setLevel(2);
        }
                             
    }
            
    /**
     * creates asteroid array
     */
    
    private void initiateAsteroids(){
         
        for(int i = 0; i < asteroids.length; i++){
            
            asteroids[i] = new Asteroids(false, false);
        }
    }
    
    
    /**
     * draws asteroids
     * @param g 
     */
        
    private void drawAsteroids(Graphics2D g){
        
        for(int i = 0; i < asteroidCount; i++){
            
            asteroids[i].drawAsteroid(g);
            
        }   
    }
   
    /**
     * sets the shower in increments
     */
    
    private void asteroidShower(){        
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
              
            int numOfAsteroids = random.nextInt(4);         //added amount of asteroids to draw
                
                int checkCount = asteroidCount += numOfAsteroids;       //checks added asteroids to draw dont exceed asteroids[] length
                
                if(checkCount < (asteroids.length -1)){
                    
                    asteroidCount += numOfAsteroids;
                    
                }else {
                    
                    int lastAsteroids = (asteroids.length -1) - asteroidCount;      //if number exceeds just add the last remainng asteroids 
                    
                    asteroidCount += lastAsteroids;
                    
                    ex.shutdown();
                }     
        };
        
        ex.scheduleAtFixedRate(task, 4000, nextwave(), TimeUnit.MILLISECONDS);
    }
    
    /**
     * checks how many asteroids are destroyed
     * @return 
     */
    
    private int checkDestroyed(){
        
        int count = 0;
        
        for(Asteroids asteroid : asteroids){
            
            if(!asteroid.isLive()){
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * next wave of asteroids
     * @return 
     */
    
    private long nextwave(){
        
        return random.ints(4000,10000).findFirst().getAsInt();
    }
           
    /**
     * collision detection
     */
    
    private void collision(){
                   
        //missile hitting asteroid
            
        for(Weapon missile : ss.getMissiles()){ 
            
            for(Asteroids asteroid: asteroids){
                
                if(asteroid.getBounds().intersects(missile.getBounds())  && missile.IsLive() && asteroid.isLive()){
                    
                    asteroid.asteroidHit();
                    missile.hit();
                    
                    if(asteroid.getHitsTillDestroyed() == 0){
                                               
                        asteroid.setDestroyed(true);
                        asteroid.asteroidDestroyedSound();
                    }
                }
            }
        }
         
         //asteroid hitting spaceship
        
        for(Asteroids asteroid: asteroids){
            
            if(asteroid != null){
             
                if(asteroid.getBounds().intersects(ss.getBounds()) && asteroid.isLive()){

                    ss.hitDamage(10);
                    asteroid.setImpact(true);
                    asteroid.asteroidDestroyedSound();

                }
            }
        }  
    }

    public boolean isInits() {
        
        return inits;
    }

    public void setInits(boolean inits) {
        
        this.inits = inits;
    }
    
    
}
