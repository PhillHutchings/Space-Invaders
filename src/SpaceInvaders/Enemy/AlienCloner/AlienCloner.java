/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.AlienCloner;

import Levels.Level2;
import Ship.SpaceShip;
import Ship.Weapons.Weapon;
import SpaceInvaders.Sprite;
import SpaceInvaders.commons;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.time.Duration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author hutch
 */
public class AlienCloner extends Sprite implements commons{

    private String getImage;                //cloner main image
    private Image movingImage;              //blurred image to show movement
    private boolean forming;                //check ship arriving in bubble
    private boolean appearing;              //check ship is appearing
    private JLabel formingFrame;            //label where bubble gif appears
    private int setHealth;                  //so each size ship has different max health
    private boolean fightScene;             //check so weapons can be drawn without throwing null pointer
    private HomingMissile homingMissile;    //homing missiles fired
    ExecutorService clonerActions;          //full sequence of ship actions excutor
    private boolean moving;                 //check for moving, different graphic shown
    private boolean missileFire;            //check missile fire action
    
    private boolean lazerPincer;            //check lazer pincer action
    
    private LazerPoint lazerPoint1;         //first lazer on  gun one start point
    private LazerPoint lazerPoint1Finish;   //first lazer on  gun one  finish point
    private int lazerGun1PosX;              //first lazer gun one x coord
    private int lazerGun1PosY;              //first lazer gun one y coord
    private int lazerX1;                    //first lazer start x coord
    private int lazerY1;                    //first lazer start y coord
    private int lazerX1Finish;              //first lazer finish x coord
    private int lazerY1Finish;              //first lazer finish y coord  
    private int lazer1Move;                 //lazer straffe movement
    
    private LazerPoint lazerPoint2;         //second lazer on gun two start point
    private LazerPoint lazerPoint2Finish;   //first lazer on  gun two finish point
    private int lazerGun2PosX;              //first lazer gun two x coord
    private int lazerGun2PosY;              //first lazer gun two y coord
    private int lazerX2;                    //second lazer start x coord
    private int lazerY2;                    //second lazer start x coord
    private int lazerX2Finish;              //second lazer finish x coord
    private int lazerY2Finish;              //second lazer finish y coord
    private int lazerLength;                //lazer length
    private int lazer2Move;                 //lazer straffe movement
    
    private LazerLine lazerLine1;           //get bounds for lazer one
    private LazerLine lazerLine2;           //get bounds for lazer two
    
    private ArrayList<Image> deathImages;   //images used when dieing reversed for next cloner entry
    private int imageNumber;                //index of death image
    private boolean death;                  //check for death to play death images
    private boolean rebirth;                //checked in level 5 to spawn new cloner
    private boolean enter;                  //for smaller cloners to enter in as biggest died
    private boolean smaller;                //check for cloners that come after the first.. different entry
    
    private File lazerSound;                //lazer pincer sound
    private AudioInputStream aisLazer;
    private Clip lazer;
    
    private File deathSound;                //death sound
    private AudioInputStream aisDeath;
    private Clip deathClip;
    
    private File reBirthSound;                //rebirth sound
    private AudioInputStream aisReBirth;
    private Clip reBirthClip;
    
    private File formingSound;                //forming sound
    private AudioInputStream aisForming;
    private Clip formingClip;
    
    private File movingSound;                //moving sound
    private AudioInputStream aisMoving;
    private Clip movingClip;
    
    
    public AlienCloner(SpaceShip ss, int x, int y, int width, int height) {
        
        super(ss, x, y, width, height);
        init();
        
    }
    
    private void init(){
        
        if(width < ALIEN_CLONER_WIDTH_1){           //initiate for different entry if not first cloner not bubble, reverse death images
            
            setEnter(true);
            setSmaller(true);
        }
        
        deathImages = new ArrayList<>();
        lazerLength = 350; 
        lazer1Move = 200;
        lazer2Move = 200;
        lazerLine1 = new LazerLine();
        lazerLine2 = new LazerLine();
                        
        formingFrame = new JLabel(); 
        getImage = ("AlienCloner/cloner.png");
        setHealth =  width * 10;        //so smaller cloners get less health
        setHealth("Horizontal", setHealth, setHealth, 100, 20, 400, 5, Color.BLACK, Color.ORANGE);
        
        loadDeathImages();
        loadMovingImage();

        loadImage(getImage);
        
        setForming(true);
        formCloner();       
        clonerActions();
        loadSounds();    
        
        
    }
    
    /**
     * image when ship moves
     */
    
    private void loadMovingImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("/SpaceInvaders/Enemy/AlienCloner/clonerMove.png"));
        movingImage = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    
    /**
     * entering scene gif..bubble
     */
    
    private void formCloner(){
          
        URL url = getClass().getResource("/SpaceInvaders/Enemy/AlienCloner/giphy.gif");     
        ImageIcon ii = new ImageIcon(url);         
        formingFrame.setBounds((BOARD_WIDTH / 2) - (ii.getIconWidth() / 2), y - (height / 2), ii.getIconWidth(), ii.getIconHeight());
        formingFrame.setIcon(ii);           

    } 
                
    /**
     * pre loads the sounds for cloner
     */  
    
    private void loadSounds(){
        
        try {
            
            lazerSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerLazer.wav");
            aisLazer = AudioSystem.getAudioInputStream(lazerSound.toURI().toURL());  
            formingSound = new File("src/SpaceInvaders/Enemy/AlienCloner/formingSound.wav");
            aisForming = AudioSystem.getAudioInputStream(formingSound.toURI().toURL()); 
            movingSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerMoveSound.wav");
            aisMoving = AudioSystem.getAudioInputStream(movingSound.toURI().toURL());
            deathSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerDeath.wav");
            aisDeath = AudioSystem.getAudioInputStream(deathSound.toURI().toURL()); 
            reBirthSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerRebirth.wav");
            aisReBirth = AudioSystem.getAudioInputStream(reBirthSound.toURI().toURL());  

        } catch (MalformedURLException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);       
        }       
    }
       
    /**
     * plays the forming sound
     */
    
    private void formingSound(){
     
        try {
            
            formingSound = new File("src/SpaceInvaders/Enemy/AlienCloner/formingSound.wav");
            aisForming = AudioSystem.getAudioInputStream(formingSound.toURI().toURL());          

            formingClip = AudioSystem.getClip();
            formingClip.open(aisForming );
            formingClip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        }catch (UnsupportedAudioFileException ex) {
            
           Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
          
    /**
     * stops forming sound
     */
    
    private void stopFormingSound(){
        
        formingClip.stop();
    }
    
    /**
     * plays the laser sound
     */
    
    private void lazerSound(){
     
        try {
            
            lazerSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerLazer.wav");
            aisLazer = AudioSystem.getAudioInputStream(lazerSound.toURI().toURL());          

            lazer = AudioSystem.getClip();
            lazer.open(aisLazer);
            lazer.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
          
    /**
     * plays the moving sound
     */
    
    private void movingSound(){
     
        try {
            
            movingSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerMoveSound.wav");
            aisMoving = AudioSystem.getAudioInputStream(movingSound.toURI().toURL());          

            movingClip = AudioSystem.getClip();
            movingClip.open(aisMoving);
            movingClip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        }catch (UnsupportedAudioFileException ex) {
            
           Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
        
    /**
     * plays the death sound
     */
    
    private void deathSound(){
     
        try {
            
            deathSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerDeath.wav");
            aisDeath = AudioSystem.getAudioInputStream(deathSound.toURI().toURL());          

            deathClip = AudioSystem.getClip();
            deathClip.open(aisDeath);
            deathClip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * plays the reBirth sound
     */
    
    private void reBirthSound(){
     
        try {
            
            reBirthSound = new File("src/SpaceInvaders/Enemy/AlienCloner/clonerRebirth.wav");
            aisReBirth = AudioSystem.getAudioInputStream(reBirthSound.toURI().toURL());          

            reBirthClip = AudioSystem.getClip();
            reBirthClip.open(aisReBirth);
            reBirthClip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * stops moving sound
     */
    
    private void stopMovingSound(){
        
        movingClip.stop();
    }
    
    /**
     * draw method 
     * @param g 
     */
    
   public void draw(Graphics2D g){

       if(!isEnter()){        //check false if it is the first cloner to appear
           
            if(!isDestroyed()){

                if(isFightScene()){

                     if(isMissileFire()){

                         drawHomingMissile(g);
                     }
                     if(isLazerPincer()){

                         drawLazerPincer(g);
                     }
                 }

                if(isAppearing() && !isMoving() && !isLazerPincer()){

                    AffineTransform old = g.getTransform();
                    AffineTransform at = new AffineTransform();            
                    at.rotate(Math.atan2(getRotY(), getRotX()) + Math.toRadians(90), getCentreX(), getCentreY());
                    g.setTransform(at);
                    g.drawImage(getImage(), getPosX(), getPosY(), null);
                    g.setTransform(old);

                 }

                 if(isMoving()){

                      AffineTransform old = g.getTransform();
                      AffineTransform at = new AffineTransform();            
                      at.rotate(Math.atan2(getRotY(), getRotX()) + Math.toRadians(90), getCentreX(), getCentreY());
                      g.setTransform(at);
                      g.drawImage(movingImage, getPosX(), getPosY(), null);
                      g.setTransform(old);
                 }

                 if(isLazerPincer()){

                      AffineTransform old = g.getTransform();
                      AffineTransform at = new AffineTransform();            
                      at.rotate(Math.atan2(getSavedRotY(), getSavedRotX()) + Math.toRadians(90), getCentreX(), getCentreY());
                      g.setTransform(at);
                      g.drawImage(getImage(), getPosX(), getPosY(), null);
                      g.setTransform(old);
                 }

                collision();

            }else{        

                    drawDeathAndEnter(g);       //death scene
            }
        }else{        

                drawDeathAndEnter(g);           //smaller ship enter scene
            }
    }
       
    

   /**
    * full sequence of ship actions from forming to fighting 
    */
   
   public void clonerActions(){
             
       clonerActions = Executors.newSingleThreadExecutor();       
       
        Runnable task = new Runnable() {
           @Override
            public void run() {
                               
                Instant start;
                long time;
                
                if(!isEnter()){          //first cloner appears in bubble
  
                    start = Instant.now();              

                    do{                                     //time till ship bubble arrives

                        Instant finish = Instant.now();
                        time = Duration.between(start, finish).toMillis();

                    }while(time < 5000);                 

                    formingSound();    //sound of forming bubble
                    ss.getBoard().add(formingFrame);            //adds forming bubble to screen

                    start = Instant.now();              

                    do{                                     //time till ship appears in forming bubble

                        Instant finish = Instant.now();
                        time = Duration.between(start, finish).toMillis();

                    }while(time < 1000);            

                    setAppearing(true); 

                     do{                            //time till forming bubble disappears

                        Instant finish = Instant.now();
                        time = Duration.between(start, finish).toMillis();

                    }while(time < 4000);

                    setForming(false);               
                    ss.getBoard().remove(formingFrame);      //removes bubble
                    addHealthBar();                      //adds health bar

                    do{                         //timer till fight sequence

                        Instant finish = Instant.now();
                        time = Duration.between(start, finish).toMillis();

                    }while(time < 4000);

                    stopFormingSound();             ///stops forming sound
                    
                    homingMissile = new HomingMissile(getThis(), ss);  
                    setFightScene(true);        //enabled for the drawing of weapons
                    ss.enableKeys(ss.getBoard()); 
                    
                    //-----go straight to homing missile action
                    
                    //-------------------------------------------------------------------//for cloners after first one has been destroyed
                    
                }else{             
                    
                    reBirthSound();
                    
                    imageNumber = deathImages.size() - 1;       //start or entry is death images reversed

                    do{
                      
                    start = Instant.now();              

                    do{                                     //timer for reverse switching between death images for entry of new cloner
                        
                        Instant finish = Instant.now();
                        time = Duration.between(start, finish).toMillis();

                    }while(time < 200);

                    imageNumber--;
                    
                    }while(imageNumber > 1);
                    
                    setEnter(false);
                    setAppearing(true); 
                    homingMissile = new HomingMissile(getThis(), ss);  
                    setFightScene(true);        //enabled for the drawing of weapons
                    addHealthBar();                      //adds health bar
                }
                
                //*****************************************************************************//END OF FORMING ENTERANCE
                
               
                //****************************************************************************//START OF FIGHTING ACTION 
                
                //------------ HOMING MISSILE AND MOVEMENT
                
        action: do{
                    
                    setMissileFire(true);
                    int moveCount = 0;           //how many time ship will reloacate

                    do{
                        
                        movingSound();   //plays moving sound
                        setMoving(true);         //for showing moving image

                        y = random.ints(0, BOARD_HEIGHT - (height * 2)).findFirst().getAsInt();          //choose random position relocation
                        x = random.ints(0, BOARD_WIDTH - (width * 2)).findFirst().getAsInt();

                        start = Instant.now();              

                        do{                                     //timer for moving image
                            
                            if(isDestroyed()){      //to catch destroyed mid action
                                setDeath(true);
                                deathSound();
                                break action;
                            }
                            
                            Instant finish = Instant.now();
                            time = Duration.between(start, finish).toMillis();

                        }while(time < 600);

                        setMoving(false);        //for hiding moving image
                        stopMovingSound();       //stops moving sound
                        
                        if(moveCount < 5){      //to allow current missile to play out before starting lazers
                            
                           if(!homingMissile.isLive()){         //if previous missile fired has not hit or has gone out of bounds missile is reset

                               homingMissile.reloadHomingMissile();
                           }
                        }
                        
                        start = Instant.now();              

                        do{                                     //timer for moving image
                            
                            if(isDestroyed()){      //to catch destroyed mid action
                                setDeath(true);
                                deathSound();
                                break action;
                            }
                            
                            Instant finish = Instant.now();
                            time = Duration.between(start, finish).toMillis();

                        }while(time < 3000);

                        moveCount++;

                    }while(moveCount < 5 || homingMissile.isLive() && !isDestroyed());          //move n times before moving on to the next fight sequence, if missile live continue till not
                    
                    setMissileFire(false);
                    setSavedRotY();                  //saves the rotation so ship remains pointed in last position
                    setSavedRotX();

                    //----------------------------------------------------------- END OF HOMING MISSILE AND MOVEMENT

                    //-------------- START OF LAZER PINCER

                    //gets the position of the end of the ship gun, tranlates the coords to final position when ship has turned to face player and rotaion is saved then laser start 
                    //position will be the new coords essential for drawing lazer in right place everytime

                //lazer gun one start coord

                    lazerGun1PosX = getPosX() + (width / 2) - (width / 2) * 19 / 100;
                    lazerGun1PosY = getPosY() + (height - 30);

                    double[] pt = {lazerGun1PosX , lazerGun1PosY};
                    AffineTransform.getRotateInstance(Math.toRadians(Math.toDegrees(Math.atan2(getSavedRotY(), getSavedRotX()) + Math.toRadians(90))),  getCentreX(), getCentreY()) 
                      .transform(pt, 0, pt, 0, 1);
                    int newX = Math.round((int)(pt[0] + 0.5));
                    int newY = Math.round((int)(pt[1] + 0.5));
                    lazerPoint1 = new LazerPoint(newX, newY);       //initiates new points
                    lazerX1 = (int)lazerPoint1.getX();
                    lazerY1 = (int)lazerPoint1.getY();

                //lazer gun two start coord

                    lazerGun2PosX = getPosX() + (width / 2) + (width / 2) * 19 / 100;
                    lazerGun2PosY = getPosY() + (height - 30);

                    double[] pt2 = {lazerGun2PosX, lazerGun2PosY};
                    AffineTransform.getRotateInstance(Math.toRadians(Math.toDegrees(Math.atan2(getSavedRotY(), getSavedRotX()) + Math.toRadians(90))),  getCentreX(), getCentreY()) 
                      .transform(pt2, 0, pt2, 0, 1);
                    int newX2 = Math.round((int)(pt2[0] + 0.5));
                    int newY2 = Math.round((int)(pt2[1] + 0.5));
                    lazerPoint2 = new LazerPoint(newX2, newY2);       //initiates new points
                    lazerX2 = (int)lazerPoint2.getX();
                    lazerY2 = (int)lazerPoint2.getY();
                    
                    if(isDestroyed()){      //to catch destroyed mid action
                        setDeath(true);
                        deathSound();
                        break action;
                    }

            //lazer pincer movement

            lazerSound();                       //sound of lazer pincer
            
                    do{  

                    //lazer gun one finish coord

                        double[] ptf1 = {lazerGun1PosX - lazer1Move--, lazerGun1PosY + lazerLength};
                        AffineTransform.getRotateInstance(Math.toRadians(Math.toDegrees(Math.atan2(getSavedRotY(), getSavedRotX()) + Math.toRadians(90))), getCentreX(), getCentreY()) 
                          .transform(ptf1, 0, ptf1, 0, 1);
                        int newXF1 = Math.round((int)(ptf1[0] + 0.5));
                        int newYF1 = Math.round((int)(ptf1[1] + 0.5));
                        lazerPoint1Finish = new LazerPoint(newXF1, newYF1);       //initiates new points
                        lazerX1Finish = (int)lazerPoint1Finish.getX();
                        lazerY1Finish = (int)lazerPoint1Finish.getY();

                    //lazer gun two finish coord

                        double[] ptf2 = {lazerGun2PosX + lazer2Move--, lazerGun2PosY + lazerLength};
                        AffineTransform.getRotateInstance(Math.toRadians(Math.toDegrees(Math.atan2(getSavedRotY(), getSavedRotX()) + Math.toRadians(90))), getCentreX(), getCentreY()) 
                          .transform(ptf2, 0, ptf2, 0, 1);
                        int newXF2 = Math.round((int)(ptf2[0] + 0.5));
                        int newYF2 = Math.round((int)(ptf2[1] + 0.5));
                        lazerPoint2Finish = new LazerPoint(newXF2, newYF2);       //initiates new points
                        lazerX2Finish = (int)lazerPoint2Finish.getX();
                        lazerY2Finish = (int)lazerPoint2Finish.getY();

                        setLazerPincer(true);               //start drawing lazer movment
  
                        start = Instant.now();              

                        do{                                     //timer for lazer pincer move increment
                            
                            if(isDestroyed()){      //to catch destroyed mid action                                
                                setDeath(true);
                                deathSound();
                                lazer.stop(); 
                                break action;
                            }
                            
                            Instant finish = Instant.now();
                            time = Duration.between(start, finish).toMillis();

                        }while(time < 10);

                    }while(!lazerPoint1Finish.equals(lazerPoint2Finish) && !isDestroyed());       //ends when two lazers meet

                    lazer1Move = 200;      //reset lazers
                    lazer2Move = 200;
                    setLazerPincer(false);  
                    lazer.stop();           //stops lazer pincer sound
                    
                    //---------------------------------------------------------- END OF LAZER PINCER
                
                }while(!isDestroyed());

                setSavedRotY();                  //saves the rotation so ship remains pointed in last position
                setSavedRotX();
                setDeath(true);         //starts drawing death images 

                imageNumber = 0;        //set to zero because of other cloners entrance where they use images reversed to enter so will be set to death images size
                
                do{
                      
                    start = Instant.now();              

                   do{                                     //timer for switching between death images
                       Instant finish = Instant.now();
                       time = Duration.between(start, finish).toMillis();

                   }while(time < 200);

                   imageNumber++; 
                    
                }while(imageNumber < deathImages.size() - 1);

                setDeath(false);            //stops drawing death images
                setRebirth(true);
                               
                clonerActions.shutdown();
            }
       };
       
       clonerActions.execute(task);
   }

   /**
    * draw the homing missiles
    * @param g 
    */
   
   private void drawHomingMissile(Graphics2D g){
 
        homingMissile.draw(g);
       
   }
   
   /**
    * draw laser pincer move
    * @param g 
    */
   private void drawLazerPincer(Graphics2D g){
            
        g.setColor(Color.red);

        g.drawLine(lazerX1, lazerY1, lazerX1Finish, lazerY1Finish);

        g.drawLine(lazerX2, lazerY2, lazerX2Finish, lazerY2Finish);

    //for getting bounds of lazers
    
        lazerLine1.setLine(lazerX1, lazerY1, lazerX1Finish, lazerY1Finish);
        lazerLine2.setLine(lazerX2, lazerY2, lazerX2Finish, lazerY2Finish);
   
   }
   
   private void loadDeathImages(){
       
       ArrayList<ImageIcon> iis = ss.getBoard().getDeathImageList();
       
       iis.stream().map(i -> i.getImage()).forEach(images -> deathImages.add(images.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
   }

   /**
    * draws death images
    * @param g 
    */
   
   private void drawDeathAndEnter(Graphics2D g){ 
       
       if(!isEnter()){          //death scene

            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();            
            at.rotate(Math.atan2(getSavedRotY(), getSavedRotX()) + Math.toRadians(90), getCentreX(), getCentreY());
            g.setTransform(at);
            g.drawImage(deathImages.get(imageNumber), getPosX(), getPosY(), null);
            g.setTransform(old);
            
       }else{           //enter scene
           
            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();            
            at.rotate(Math.atan2(getRotY(), getRotX()) + Math.toRadians(90), getCentreX(), getCentreY());
            g.setTransform(at);
            g.drawImage(deathImages.get(imageNumber), getPosX(), getPosY(), null);
            g.setTransform(old);
           
       }
   }
   
   
    public boolean isForming() {
        
        return forming;
    }

    public void setForming(boolean forming) {
        
        this.forming = forming;
    }

    public boolean isAppearing() {
        
        return appearing;
    }

    public void setAppearing(boolean appearing) {
        
        this.appearing = appearing;
    }

    public boolean isFightScene() {
        
        return fightScene;
    }

    public void setFightScene(boolean fightScene) {
        
        this.fightScene = fightScene;
    }

    public HomingMissile getHomingMissile() {
       
        return homingMissile;
    }
   
    /**
     * overridden  to make hit box smaller
     * @return 
     */
    @Override
    public Rectangle getBounds(){
        
        return new Rectangle(getPosX() + (width / 2) - 20, getPosY(), 40, height);
    }

    public boolean isMoving() {
        
        return moving;
    }

    public void setMoving(boolean moving) {
        
        this.moving = moving;
    }
   
    public AlienCloner getThis(){
        
        return this;
    }

    public boolean isMissileFire() {
        
        return missileFire;
    }

    public void setMissileFire(boolean missileFire) {
        
        this.missileFire = missileFire;
    }

    public boolean isLazerPincer() {
        
        return lazerPincer;
    }

    public void setLazerPincer(boolean lazerPincer) {
        
        this.lazerPincer = lazerPincer;
    }

    public boolean isDeath() {
        
        return death;
    }

    public void setDeath(boolean death) {
        
        this.death = death;
    }
    
    public boolean isRebirth() {
        
        return rebirth;
    }

    public void setRebirth(boolean rebirth) {
        
        this.rebirth = rebirth;
    }

    public boolean isEnter() {
        
        return enter;
    }

    public void setEnter(boolean enter) {
        
        this.enter = enter;
    }

    public boolean isSmaller() {
        
        return smaller;
    }

    public void setSmaller(boolean smaller) {
        
        this.smaller = smaller;
    }
 
    /**
     * to get coords for lazer lines
     */
    
    class LazerPoint extends Point{

        public LazerPoint(int x, int y) {
           
            super(x, y);
        }    
    }
    
    public LazerLine getLazerLine1() {
        
        return lazerLine1;
    }

    public LazerLine getLazerLine2() {
        
        return lazerLine2;
    }

    public class LazerLine extends Line2D {

        public double x1;
        public double y1;
        public double x2;
        public double y2;
        
        @Override
        public double getX1() {
            
            return x1;
        }

        @Override
        public double getY1() {
           
            return y1;
        }

        @Override
        public Point2D getP1() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double getX2() {
           
            return x2;
        }

        @Override
        public double getY2() {
            
            return y2;
        }

        @Override
        public Point2D getP2() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setLine(double x1, double y1, double x2, double y2) {
            this.x1 = (float) x1;
            this.y1 = (float) y1;
            this.x2 = (float) x2;
            this.y2 = (float) y2;
        }

        @Override
        public Rectangle2D getBounds2D() {
            double x, y, w, h;
            
            if (x1 < x2) {
                x = x1;
                w = x2 - x1;
            } else {
                x = x2;
                w = x1 - x2;
            }
            if (y1 < y2) {
                y = y1;
                h = y2 - y1;
            } else {
                y = y2;
                h = y1 - y2;
            }
            
            return new Rectangle2D.Double(x, y, w, h);
        }
    }
      /**
     * collision detection
     */
    
    private void collision(){
        
        // player missile
        
        for(Weapon missile : ss.getMissiles()){
            
            if(missile.IsLive()){
                 
            //check missile connection with cloner
                if(missile.getBounds().intersects(getBounds()) && !isMoving()){

                    missile.hit();
                    missileDamage(20);

                }       
            }
        }
        
        //check missile connection with homing missile
                            
        for(Weapon missile : ss.getMissiles()){
            
            if(missile.IsLive()){
                
                if(getHomingMissile() != null && getHomingMissile().isLive()){   

                    if(getHomingMissile().getBounds().intersects(missile.getBounds())){

                        getHomingMissile().intercepted();  
                        missile.hit();

                    }
                }               
            }
        }
        
        //cloner homing missile
                
        if(isMissileFire()){

            if(getHomingMissile() != null && getHomingMissile().isLive()){   

                if(getHomingMissile().getBounds().intersects(ss.getBounds())){

                    getHomingMissile().Hit();
                    ss.hitDamage(15);    

                }
            }                      
        }
           
        //cloner lazer pincer       
            
        if(isLazerPincer()){

            if(getLazerLine1() != null && getLazerLine1().intersects(ss.getBounds())){

                ss.hitDamage(1);
            }

            if(getLazerLine2() != null && getLazerLine2().intersects(ss.getBounds())){

                ss.hitDamage(1);
            }
        }     
    }
}
