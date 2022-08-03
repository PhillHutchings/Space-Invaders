/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders.Enemy.MysteryCraft;

import Levels.Level2;
import Ship.SpaceShip;
import SpaceInvaders.Board;
import SpaceInvaders.Sprite;
import SpaceInvaders.commons;
import static SpaceInvaders.commons.BOARD_HEIGHT;
import static SpaceInvaders.commons.BOARD_WIDTH;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author hutch
 */
public class MysteryCraft implements commons{
    
    private Image mysteryCraft;
    private Image lazerEnd;
    private Image logo;
    private final SpaceShip ss;
    private final Board board;
    private MysteryCraft mc;
    private JProgressBar healthBar;
    private int x;
    private int y;
    private int moveX;
    private int moveY;
    private int posX;
    private int posY;
    private boolean enterScene;
    private boolean fightScene;
    private boolean moveLeft;
    private boolean turn;
    private boolean stop;
    private final int degreeTurn = 90;
    private int loopTimes;
    public JLabel speechBubble;
    private boolean playingMessage;
    private String[] message;
    private String[] defeatedMessage;
    private boolean addMessageArea;
    private int line;
    private boolean callAttack;
    private boolean attackCalled;
    private Sprite sprite;
    private double radian;

    private ArrayList<MysteryCraftMissile> missiles;
    
    private ScheduledExecutorService rainFire;          //circle action
    private ScheduledExecutorService fightCircleAction;
    private boolean inPosition;                     //check mystery craft is in centre of screen before circle action comences
    
    ScheduledExecutorService lazerStrafeEX;         //lazer strafe action executor

    private boolean lazerStrafeRight;           //to check when ship moves into position to start strafe, so moving right can occur
    private boolean fireLazer;              //check to fire lazer

    ScheduledExecutorService trackFireEx;       //tracking fire action
    ScheduledExecutorService armMissilesEx;     //arming missiles in tracking fire action
    private boolean trackingMissiles;           //check for slowing down missile fire
    
    ExecutorService fightSequence;   //full fight sequence executor
    
    private boolean defeated;
    private boolean defeatMessage;
    
    private File moveSound;            //sounds of ship moving
    private AudioInputStream aisMove;
    private File lazerSound;            //sounds of ship lazer straffe
    private AudioInputStream aisLazer;
    private Clip lazer;
    private File missileSound;            //sounds of ship lazer straffe
    private AudioInputStream aisMissile;

    /**
     * first encounter constructor
     * @param ss
     * @param board 
     */
    
    public MysteryCraft(SpaceShip ss, Board board){
        
        this.ss = ss;
        this.board = board;
        mc = this;       
        enterScene = true;
        init();       
    }
    
    /**
     * called in support constructor
     * @param ss
     * @param board
     * @param sprite
     * @param callAttack 
     */
    
    public MysteryCraft(SpaceShip ss, Board board, Sprite sprite, boolean callAttack){
        
        this.ss = ss;
        this.board = board;
        this.sprite = sprite;
        this.callAttack = callAttack; 
        x = BOARD_WIDTH + MYSTERYCRAFT_WIDTH;
        y = BOARD_HEIGHT / 2;
        posX = x + moveX;
        posY = y + moveY;
        
        mysteryCraftLogo();
        loadMysteryCraft();
        loadMysteryCraftLaserEnd();
        callMysteryCraftEnable();
    }
    
    private void init(){
        
        loadMysteryCraft();
        loadMysteryCraftLaserEnd();
        loadSounds();
        
        x = BOARD_WIDTH + MYSTERYCRAFT_WIDTH;
        y = BOARD_HEIGHT - 200;
        posX = x + moveX;
        posY = y + moveY;
        moveLeft = true;
        loopTimes = 0;
        
        healthBar = new JProgressBar(SwingConstants.HORIZONTAL);
        healthBar.setMaximum(1000);
        healthBar.setValue(1000);
        healthBar.setBounds(100, 20, 400, 5);
        healthBar.setForeground(Color.GREEN);
        healthBar.setBackground(Color.BLACK);
       
        message = new String[10];
        defeatedMessage = new String[6];
        
        speechBubble = new JLabel("", JLabel.CENTER);
        speechBubble.setBackground(Color.BLACK);
        speechBubble.setBounds((BOARD_WIDTH / 2) - 200,(BOARD_HEIGHT - 400), 400, 150);
        speechBubble.setFont(new Font("OCR A Extended", Font.BOLD, 18));
        speechBubble.setForeground(Color.GREEN);
        
        missiles = new ArrayList<>();
        
    }
    
    /**
     * mystery craft image
     */
    
    private void loadMysteryCraft(){
        
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/MysteryCraft/mysteryCraft.png"));
            ImageIcon ii = new ImageIcon(bi);
            mysteryCraft = ii.getImage().getScaledInstance(MYSTERYCRAFT_WIDTH, MYSTERYCRAFT_HEIGHT,   Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(MysteryCraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * mystery craft laser end
     */
    
    private void loadMysteryCraftLaserEnd(){
        
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/MysteryCraft/lazerEnd.png"));
            ImageIcon ii = new ImageIcon(bi);
            lazerEnd = ii.getImage().getScaledInstance(MYSTERYCRAFT_WIDTH + 10, MYSTERYCRAFT_HEIGHT + 10,   Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(MysteryCraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * image of the mystery craft logo for calling
     */
    
    private void mysteryCraftLogo(){
        
        try {
            
            BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/SpaceInvaders/Enemy/MysteryCraft/MysteryCraftLogo.png"));
            ImageIcon ii = new ImageIcon(bi);
            logo = ii.getImage().getScaledInstance(ITEM_HEIGHT, ITEM_HEIGHT,  Image.SCALE_SMOOTH);
            
        } catch (IOException ex) {
            
            Logger.getLogger(MysteryCraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * draws mystery craft logo when its ready to be called
     * @param g 
     */
    
    public void drawLogo(Graphics2D g){
        
        g.drawImage(logo, MYSTERYCRAFT_READY_X , MYSTERYCRAFT_READY_Y, null);
    }
        
    /**
     * drawing the first time encountering the mystery craft in level 3
     * @param g 
     */
    
    public void drawEnterMysteryCraft(Graphics2D g){

        if(!stop){      //check mystery craft is not doing entryScene, stopping being the end.
        
            if(turn){               //turns to face direction of travel

                AffineTransform af = g.getTransform();
                g.rotate(Math.toRadians(-degreeTurn), posX + (MYSTERYCRAFT_WIDTH / 2), posY + (MYSTERYCRAFT_HEIGHT / 2));
                g.drawImage(mysteryCraft, posX, posY, null);
                g.setTransform(af);                      
            }

            if(!turn){                  //turns to face direction of travel

                AffineTransform af = g.getTransform();
                g.rotate(Math.toRadians(degreeTurn), posX + (MYSTERYCRAFT_WIDTH / 2), posY + (MYSTERYCRAFT_HEIGHT / 2));
                g.drawImage(mysteryCraft, posX, posY, null);
                g.setTransform(af);

            }     

        }else{                      //turns to face direction of player

                AffineTransform af = g.getTransform();
                g.rotate(Math.toRadians(0), posX + (MYSTERYCRAFT_WIDTH / 2), posY + (MYSTERYCRAFT_HEIGHT / 2));
                g.drawImage(mysteryCraft, posX, posY, null);
                g.setTransform(af);

            } 
        
        posX = x + moveX;
        posY = y + moveY;
    }
    
    /**
     * first whoosh sound of craft entering
     * cannot do it within the mysterycraftEnterMovement() method
     */
    private void firstMoveSound(){
        
        new javax.swing.Timer(3000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                moveSound();
                ((Timer)e.getSource()).stop();
            }         
        }).start();
    }
    
    /**
     * first time encountering movement
     */
    
    public void mysterycraftEnterMovement(){
        
        firstMoveSound();
                
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {

            if(loopTimes < 3){          //how many times to across the screen
                
                if(posX > -200 && moveLeft){     //moves left

                    moveX -= 10;

                    if(posX <= -180 && moveLeft){ 

                        moveSound();

                        moveLeft = false;
                        turn = true;
                        loopTimes++;
                        y -= 200;
                    }              
                }

                if(posX < BOARD_WIDTH + 200 && !moveLeft){            //moves right

                    moveX += 10;

                    if(posX >= BOARD_WIDTH + 180 && !moveLeft){

                        moveSound();
                        
                        moveLeft = true;
                        turn = false;
                        loopTimes++;
                        y -= 200;
                    }     
                }            
            }else{
                
                if(posX < (BOARD_WIDTH / 2) - (MYSTERYCRAFT_WIDTH / 2)){
                    
                    moveX+=10;
                    
                }else{
                    
                    stop = true;                     
                    ss.resetPosiiton();
                    
                    if(ss.isShipReset()){       //once ship is in starting position message begins

                        craftEnterMessage();
                        setPlayingMessage(true);
                        playEnterMessage();
                        ex.shutdown();
                    }
                } 
            }
            
            posX = x + moveX;
            posY = y + moveY;
        };
        
        ex.scheduleAtFixedRate(task, 3000, 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * plays message in first encounter
     */
    
    private void playEnterMessage(){
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
            
            if(line == (message.length)){

            setPlayingMessage(false); 
            setEnterScene(false);
            setFightScene(true);
            
    //re-enables keys and nuke
            ss.enableKeys(board);
            
            fightSequence();                    //starts fight sequence
            board.add(healthBar);
            
            line = 0;
            
            ex.shutdown();
            
            board.getMusic().level3();     //starts the level 3 music

            }else{

                updateMessage(message, line++);

            }        
        };    
        
        ex.scheduleAtFixedRate(task, 1000, message[line].length() * 150, TimeUnit.MILLISECONDS);
    }
    
    /**
     * updates new message line to text area
     * @param line 
     */
    
    private void updateMessage(String[] text, int line){
        
        speechBubble.setText(text[line]);
    }
   
    /**
     * craft message to player on first encounter
     */
    
    private void craftEnterMessage(){
        
        message[0] = "Hey There Traveller!";
        message[1] = "<html>That Was Some Nice Shooting <br/>Back There</html>";
        message[2] = "I Was Watching You Move";
        message[3] = "Not Too Bad Are You.";
        message[4] = "<html>But.. <br/>They Were Week And Slow</html>";
        message[5] = "<html>Id Like To See You Go Up Against <br/>"
                + "A Real Challenge <br/>"
                + "Like Me!</html>";
        message[6] = "Im The Fastest There Is";
        message[7] = "<html>Your Gunna Have To <br/>Step Up Your Game!</html>";
        message[8] = "<html>Beat Me And You Just Might Earn <br/>"
                + "My Respect </html>";
        message[9] = "Now Its Time To Fight!";

    }
    
    /**
     * draws first encounter fight scene
     * @param g 
     */
    
    public void drawFightScene(Graphics2D g){
        
        if(!isDefeated()){
            
            fireMissile(g);

            if(fireLazer){

                fireLazer(g);
            }

            g.drawImage(mysteryCraft, posX, posY, null);
                        
        }
    }
    
    /**
     * mystery crafts action cycle
     */
    
    private void fightSequence(){
        
        fightSequence = Executors.newSingleThreadExecutor();
       
        Runnable task = () -> {
               
            if(!isDefeated()){
                    
                fullCircleAction();          //fight circle first action

                do{
                    //allow fight circle action to finish

                }while(!fightCircleAction.isShutdown());               

                trackingFireAction();            //tracking fire

                do{
                    //allow tracking fire action to finish

                }while(!trackFireEx.isShutdown());

                lazerStrafeAction();

                do{
                    //allow lazer strafe action to finish

                }while(!lazerStrafeEX.isShutdown());

                trackingFireAction();            //tracking fire

                do{
                    //allow tracking fire action to finish

                }while(!trackFireEx.isShutdown());

                fightSequence.shutdown();
                reDoFightSequence();     //redo unitll fights end
           
            }else{
                
                fightSequence.shutdown();
            }                       
        };   
       
       fightSequence.execute(task);
    }
    
    /**
     * recall fight sequence
     */
    
    private void reDoFightSequence(){
        
        fightSequence(); 
    }
    
    /**
     * Circle fight sequence
     */
    
    private void fullCircleAction(){
        
        fightSceneMovementCircle();             
        circleRainFire();

    }
    
    /**
     * tracking fire sequence
     */
    
    private void trackingFireAction(){
        
        trackingFire();
        trackingFireTimer();
    }
    
    /**
     * lazerStrafe sequence
     */
    
    private void lazerStrafeAction(){
        
        lazerStrafe();
    }
    
    /**
     * fight scene movements for the first encounter CIRCLE
     * @param g 
     */
    
    private void fightSceneMovementCircle(){
        
        fightCircleAction = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            
            if(!isDefeated()){    
                
                if(getPosX() == (BOARD_WIDTH/ 2) - (MYSTERYCRAFT_WIDTH / 2) && !inPosition){

                    inPosition = true;
                }

                if(getPosX() > (BOARD_WIDTH/ 2) - (MYSTERYCRAFT_WIDTH / 2) && !inPosition) {

                    moveX --;
                    posX = x + moveX;

                }  

                if(getPosX() < (BOARD_WIDTH/ 2) - (MYSTERYCRAFT_WIDTH / 2) && !inPosition) {

                    moveX ++;
                    posX = x + moveX;

                } 

                if(inPosition){

                    moveX += Math.round(4 * Math.cos(Math.toRadians(radian)));
                    moveY += Math.round(4 * Math.sin(Math.toRadians(radian)));

                    radian++;
                    
                    posX = x + moveX;
                    posY = y + moveY;
                    
                }   

                if(radian == 720){          //two complete circles then done

                    inPosition = false;
                    fightCircleAction.shutdown();
                    rainFire.shutdown();
                    radian = 0;
                }
            }else{
                
                fightCircleAction.shutdown();
            }
        };
        
        fightCircleAction.scheduleWithFixedDelay(task, 1000, 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * adds new missiles to fire while doing circle movement 
     */
    
    private void circleRainFire(){
        
        rainFire = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            
            if(!isDefeated()){  
                
                missileSound();
                missiles.add(new MysteryCraftMissile(getThis()));
                
            }else{
                
                rainFire.shutdown();
            }
        };
        
        rainFire.scheduleWithFixedDelay(task, 1000, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * laser strafe action
     * @param g 
     */
    
    private void lazerStrafe(){
 
        lazerStrafeEX = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
            
            if(!isDefeated()){                
                
                if(getPosX() >= 100 && !lazerStrafeRight){

                    moveX -= 2;  

                    if(getPosX() == 100){

                        lazerStrafeRight = true;
                        lazerSound();
                    }

                }else{

                    fireLazer = true;
                    moveX++;

                    if(getPosX() == BOARD_WIDTH - 150){                   

                        lazerStrafeRight = false;
                        fireLazer = false;
                        lazerStrafeEX.shutdown();
                    }                  
                }  

                posX = x + moveX;
                posY = y + moveY;

            }else{
                
                lazerStrafeEX.shutdown();
            }
        };
        
        lazerStrafeEX.scheduleAtFixedRate(task, 1000, 10, TimeUnit.MILLISECONDS);
    }
    
    /**
     * laser for first encounter fight   board length laser
     * @param g 
     */
    
    private void fireLazer(Graphics2D g){
        
        g.setColor(Color.GREEN); 
        g.fillRect(getPosX() + (MYSTERYCRAFT_WIDTH / 2) - 4, getPosY() + (MYSTERYCRAFT_HEIGHT / 2), 3, BOARD_HEIGHT - 200);
        g.drawImage(lazerEnd, getPosX() - 7, BOARD_HEIGHT - 100, null);
        
    }
    
    /**
     * timer for the tracking fire action
     */
    
    private void trackingFireTimer(){
        
        new javax.swing.Timer(10000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               
                trackFireEx.shutdown();
                ((Timer)e.getSource()).stop();
            } 
            
        }).start();
    }
    /**
     * tracks player movement then fires
     */
    
    private void trackingFire(){
        
        trackFireEx = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
            
            if(!isDefeated()){     
                
                if(getPosX() < ss.getPosX()){                

                    moveX += 1;
                    posX = x + moveX;

                }

                if(getPosX() > ss.getPosX()){  

                    moveX -= 1;
                    posX = x + moveX;

                }

                if(getPosX() == ss.getPosX() && !trackingMissiles){

                        armMissiles();
                        trackingMissiles = true;
                }   
            }else{
                
                trackFireEx.shutdown();
            }
        };
        
        trackFireEx.scheduleAtFixedRate(task, 1000, 10, TimeUnit.MILLISECONDS);
    }
        
    /**
     * adds new missiles to fire during tracking fire action
     */
    
    private void armMissiles(){
        
        armMissilesEx = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            if(!isDefeated()){    
               
                if(liveMissileCount()){

                    missileSound();
                    missiles.add(new MysteryCraftMissile(getThis()));
               }

               if(!liveMissileCount()){

                   trackingMissiles = false;
                   armMissilesEx .shutdown();
               }
            }else{
                
                armMissilesEx.shutdown();
            }
        };
        
        armMissilesEx.scheduleWithFixedDelay(task, 1000, 500, TimeUnit.MILLISECONDS);
    }
    
    /**
     * makes sure no more than 3 missiles can be fires at once
     * @return 
     */
    
    private boolean liveMissileCount(){
        
        int count = 0;
        
        for(MysteryCraftMissile missile : missiles){
                  
            if(missile.IsLive()){

                count++;
            }
        }     
        
        return count < 3;
    }
    
    /**
     * fires missiles
     * @param g 
     */
    
    private void fireMissile(Graphics2D g){
        
        for(MysteryCraftMissile missile : missiles){
            
            missile.draw(g);
        }        
    }
    
        
    /**
     * craft message to player on first encounter after defeat
     */
    
    private void craftDefeatedMessage(){
        
        defeatedMessage[0] = "Damn Your Good!";
        defeatedMessage[1] = "<html>You Have Earned My Respect!<br/>Good Shooting!</html>";
        defeatedMessage[2] = "If You Ever Need A Hand";
        defeatedMessage[3] = "And Im Not Too Far Away";
        defeatedMessage[4] = "Call Me";
        defeatedMessage[5] = "Till Next Time!";

    }
    
    /**
     * plays message on defeat of mystery craft
     */
    
    public void playDefeatMessage(){

        board.getMusic().dialogueAmbient();
        craftDefeatedMessage();
        board.add(speechBubble);
        updateMessage(defeatedMessage, line++);
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
          
            if(line == (defeatedMessage.length)){
            
                setDefeatMessage(false);
                board.remove(speechBubble);
                ex.shutdown();

            }else{ 

                updateMessage(defeatedMessage, line++);

            }        
        };    
        
        ex.scheduleAtFixedRate(task, 1000, defeatedMessage[line].length() * 100, TimeUnit.MILLISECONDS);
    }
    
    /**
     * drawing the exit scene after defeat
     * @param g 
     */
    
    public void drawExitScene(Graphics2D g){
               
        g.drawImage(mysteryCraft , getPosX(), getPosY(), null);
               
        if(!isDefeatMessage()){            //waits till defeat message has finished before exiting
                   
            exitMovement(g);
        }
    }
    
    /**
     * movement out of screen when defeat message is done
    */
    
    private void exitMovement(Graphics2D g){
        
        
        if(getPosY() < BOARD_HEIGHT){
            
            moveY += 6;

            posY = y + moveY;
            
        }else{
           
            board.setLevel(4);
        
        }
    }
    
    /**
     * when player calls in help form mystery craft
     * locks on to chosen sprite and fires lazer until sprite destroyed
     * @param g 
     */
    
    public void calledInAttack(Graphics2D g){
        
        drawLogo(g);
        boolean firing = false;
        
        if(isCallAttack()){
            
            if(!firing){
                
                lazerSound();       //so sound only fires once
                firing = true;
            }
            
            moveX -= 4;
            g.setColor(Color.GREEN);

            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(degreeTurn), posX + (MYSTERYCRAFT_WIDTH / 2), posY + (MYSTERYCRAFT_HEIGHT / 2));
            g.setTransform(at);
            g.drawImage(mysteryCraft, posX, posY, null);
            g.setTransform(old); 

            if(!sprite.isDestroyed()){          //only fire if sprite not destroyed

                g.drawLine(getPosX() + 10, getPosY() + (MYSTERYCRAFT_HEIGHT / 2), sprite.getPosX() + (sprite.getWidth() / 2), sprite.getPosY() + (sprite.getHeight()/2));
                g.drawImage(lazerEnd,  sprite.getPosX() + (sprite.getWidth() / 2) - lazerEnd.getWidth(null) / 2, sprite.getPosY() + (sprite.getHeight() / 2) - lazerEnd.getHeight(null) / 2, null);
            }

            if(posX <= 0 - MYSTERYCRAFT_WIDTH || sprite.isDestroyed()){

                lazer.stop();
                setCallAttack(false);
                
            }

            callAttackCollision();

            posX = x + moveX;
            posY = y + moveY;
        }
    }

    public boolean isCallAttack() {
        
        return callAttack;
    }

    public void setCallAttack(boolean callAttack) {
        
        this.callAttack = callAttack;
    }
    
    /**
     * damage done by call in attack
     */
    private void callAttackCollision(){
        
        sprite.craftDamage(2);
    }
     
    /**
     * key binder to call mystery craft to attack enemy
     */
    
    private void callMysteryCraftEnable(){
              
        AbstractAction call = new AbstractAction(){               //up

            @Override
            public void actionPerformed(ActionEvent e) {
                    
                    setCallAttack(true);
                    
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke("M"), "call");
        board.getActionMap().put("call", call);
    }
    
    /**
     * pre loads the sounds for the mystery craft
     */  
    
    private void loadSounds(){
        
        try {
            
            moveSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/craftMove.wav");
            aisMove = AudioSystem.getAudioInputStream(moveSound.toURI().toURL());
            lazerSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/lazer.wav");
            aisLazer = AudioSystem.getAudioInputStream(lazerSound.toURI().toURL());  
            missileSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/craftMissile.wav");
            aisMissile = AudioSystem.getAudioInputStream(missileSound.toURI().toURL());   
       
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException | IOException ex) {
            
            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);       
        }       
    }
    
    /**
     * plays the moving sound
     */
    
    private void moveSound(){
     
        try {
            
            moveSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/craftMove.wav");
            aisMove = AudioSystem.getAudioInputStream(moveSound.toURI().toURL());          

            Clip clip = AudioSystem.getClip();
            clip.open(aisMove);
            clip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * plays the laser sound
     */
    
    private void lazerSound(){
     
        try {
            
            lazerSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/lazer.wav");
            aisLazer = AudioSystem.getAudioInputStream(lazerSound.toURI().toURL());          

            lazer = AudioSystem.getClip();
            lazer.open(aisLazer);
            
            FloatControl fc =  (FloatControl) lazer.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = 0.1f;
            
            fc.setValue(40f * (float) Math.log10(volume));
            
            lazer.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
         
    /**
     * plays the missile sound
     */
    
    private void missileSound(){
     
        try {
            
            missileSound = new File("src/SpaceInvaders/Enemy/MysteryCraft/craftMissile.wav");
            aisMissile = AudioSystem.getAudioInputStream(missileSound.toURI().toURL());          

            Clip clip = AudioSystem.getClip();
            clip.open(aisMissile);
            clip.start();

        } catch (LineUnavailableException | IOException ex) {

            Logger.getLogger(Level2.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (UnsupportedAudioFileException ex) {
            
            Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isAttackCalled() {
        
        return attackCalled;
    }

    public void setAttackCalled(boolean attackCalled) {
        
        this.attackCalled = attackCalled;
    }
       
    /**
     * damage done by player missiles
     */
    
    public void missileDamage(){
        
        healthBar.setValue(healthBar.getValue() - 10);
    }
       
    /**
     * damage done by player nuke
     */
    
    public void nukeDamage(){
        
        healthBar.setValue(healthBar.getValue() - 120);
    }

    /**
     * get mystery craft bounds
     * @return 
     */
    
    public Rectangle getBounds(){
    
        return new Rectangle(getPosX(), getPosY(), MYSTERYCRAFT_WIDTH, MYSTERYCRAFT_HEIGHT);
    }
    
    /**
     * gets bounds of laser
     * @return 
     */
    
    public Rectangle getLazerBounds(){

        return new Rectangle(getPosX() + (MYSTERYCRAFT_WIDTH / 2) - 4, getPosY() + (MYSTERYCRAFT_HEIGHT / 2), 3, BOARD_HEIGHT - 200);

    }
    
    public int getPosX() {
        
        return posX;
    }

    public int getPosY() {
        
        return posY;
    }   

    public boolean isEnterScene() {
        
        return enterScene;
    }

    public void setEnterScene(boolean enterScene) {
        
        this.enterScene = enterScene;
    }

    public boolean isPlayingMessage() {
        
        return playingMessage;
    }

    public void setPlayingMessage(boolean playingMessage) {
        
        this.playingMessage = playingMessage;
    }

    public boolean isAddMessageArea() {
        
        return addMessageArea;
    }

    public void setAddMessageArea(boolean addMessageArea) {
        
        this.addMessageArea = addMessageArea;
    }

    public boolean isFightScene() {
        
        return fightScene;
    }

    public void setFightScene(boolean fightScene) {
        
        this.fightScene = fightScene;
    }

    public MysteryCraft getThis() {
        
        return mc;
    }

    public ArrayList<MysteryCraftMissile> getMissiles() {
        
        return missiles;
    }

    public boolean isFireLazer() {      //for level 3 collision()
        
        return fireLazer;
    }

    public boolean isDefeated() {
        
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        
        this.defeated = defeated;
    }

    public JProgressBar getHealthBar() {
       
        return healthBar;
    } 

    public boolean isDefeatMessage() {
        
        return defeatMessage;
    }

    public void setDefeatMessage(boolean defeatMessage) {
        
        this.defeatMessage = defeatMessage;
    }
    
    
}
