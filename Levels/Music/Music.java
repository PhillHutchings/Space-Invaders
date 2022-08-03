/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels.Music;

import SpaceInvaders.Board;
import SpaceInvaders.commons;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author hutch
 */
public class Music implements commons{
    
    private Clip start;
    private Clip diaolgueAmbient;
    private Clip level1;
    private Clip level2;
    private Clip level3;
    private Clip level4;
    private Clip level5;
    private Clip level6;    
    private Clip finished;
    private Clip gameOver;
    
    
    float volume = MUSIC_VOLUME ;
    
    /**
     * stops all currently running music
     * called before each new music chosen
     */
    
    public void diasbleAllMusic(){
        
        if(start != null) start.stop();
        if(diaolgueAmbient != null) diaolgueAmbient.stop();
        if(level1 != null) level1.stop();
        if(level2 != null) level2.stop();
        if(level3 != null) level3.stop();
        if(level4 != null) level4.stop();
        if(level5 != null) level5.stop();
        if(level6 != null) level6.stop();       
        if(finished != null) finished.stop();
        if(gameOver != null) gameOver.stop();
    }
    
    /**
     * start music
     */
    
    public void start(){
            
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/start.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            start = AudioSystem.getClip();
            
            start.open(aisGame);
            
            FloatControl fc =  (FloatControl) start.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            start.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
    
    /**
     * plays ambient music for dialogue scenes
     */
    
    public void dialogueAmbient(){
            
        diasbleAllMusic();
        
        try {
        
            File gameMusic = new File("src/Levels/Music/dialogueAmbient.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            diaolgueAmbient = AudioSystem.getClip();
            
            diaolgueAmbient.open(aisGame);
            
            FloatControl fc =  (FloatControl) diaolgueAmbient.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(10f * (float) Math.log10(volume));
                
            diaolgueAmbient.loop(Clip.LOOP_CONTINUOUSLY);                                

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }   
    }
    
    /**
     * level 1 music
     */
    
    public void level1(){
            
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/level1.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            level1 = AudioSystem.getClip();
            
            level1.open(aisGame);
            
            FloatControl fc =  (FloatControl) level1.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            level1.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
        
    /**
     * level 2 music
     */
    
    public void level2(){
            
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/level2.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            level2 = AudioSystem.getClip();
            
            level2.open(aisGame);
            
            FloatControl fc =  (FloatControl) level2.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            level2.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
      
    /**
     * level 3 music
     */
    
    public void level3(){
            
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/level3.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            level3 = AudioSystem.getClip();
            
            level3.open(aisGame);
            
            FloatControl fc =  (FloatControl) level3.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            level3.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
            
    /**
     * level 4 music
     */
    
    public void level4(){
            
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/level4.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            level4 = AudioSystem.getClip();
            
            level4.open(aisGame);
            
            FloatControl fc =  (FloatControl) level4.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            level4.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
            
    /**
     * level 5 music
     */
    
    public void level5(){
            
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/level5.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            level5 = AudioSystem.getClip();
            
            level5.open(aisGame);
            
            FloatControl fc =  (FloatControl) level5.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            level5.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
    
    /**
     * level 6 music
     */
    
    public void level6(){
             
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/level6.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            level6 = AudioSystem.getClip();
            
            level6.open(aisGame);
            
            FloatControl fc =  (FloatControl) level6.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            level6.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }   
    
    /**
     * level 1 music
     */
    
    public void finished(){
            
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/finished.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            finished = AudioSystem.getClip();
            
            finished.open(aisGame);
            
            FloatControl fc =  (FloatControl) finished.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            finished.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
    
    /**
     * game over music
     */
    
    public void gameOver(){
             
        diasbleAllMusic();
        
        try {
    
            File gameMusic = new File("src/Levels/Music/gameOver.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            level6 = AudioSystem.getClip();
            
            level6.open(aisGame);
            
            FloatControl fc =  (FloatControl) level6.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(20f * (float) Math.log10(volume));
             
            level6.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }       
    }
}
