/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders;

import javax.swing.SwingConstants;

/**
 *
 * @author hutch
 */
public interface commons {
    
    
   //Board
    
    float MUSIC_VOLUME = 0.1f;
    int BOARD_HEIGHT = 900;
    int BOARD_WIDTH = 600;
    
    //Spaceship
    
    int MISSILE_WIDTH = 2;
    int MISSILE_HEIGHT = 15;
    
    int SPACESHIP_WIDTH = 50;
    int SPACESHIP_HEIGHT = 80;
    int SPACESHIP_START_X = (BOARD_WIDTH / 2) - (SPACESHIP_WIDTH / 2);
    int SPACESHIP_START_Y = BOARD_HEIGHT - (SPACESHIP_HEIGHT * 2);
    
    int HEALTH_X = BOARD_WIDTH - 35;
    int HEALTH_Y = BOARD_HEIGHT - 200;
    int HEALTH_WIDTH = 10;
    int HEALTH_HEIGHT = 140;
    int HEALTH_ORIENT = SwingConstants.VERTICAL;
    
    int HEART_X = 10;
    int HEART_Y = BOARD_HEIGHT - 90;
    
    //gamescreen
    
    int LOGO_WIDTH = 400;
    int LOGO_HEIGHT = 250;
    int LOGO_X  = (BOARD_WIDTH / 2) - (LOGO_WIDTH / 2);
    int LOGO_Y = 70;
   
    //level 2 
    
    int ABANDONED_SHIP_WIDTH = 250;
    int ABANDONED_SHIP_HEIGHT = 190;    
    int ABANDONED_SHIP_X = ((BOARD_WIDTH /2) - (ABANDONED_SHIP_WIDTH/ 2));
    int ABANDONED_SHIP_Y = 0 - (ABANDONED_SHIP_HEIGHT * 2);
    int ABANDONED_SHIP_FINAL_Y = ((BOARD_HEIGHT / 2) - ABANDONED_SHIP_HEIGHT);
    
    
    int ALIEN_WIDTH = 100;
    int ALIEN_HEIGHT = 50;
    int ALIEN_MOVE_X = 60;
    int ALIEN_MOVE_Y = ALIEN_HEIGHT / 2;
    
    int ITEM_WIDTH = 30;
    int ITEM_HEIGHT = 30;
    
    int SHIELD_WIDTH = 80;
    int SHIELD_HEIGHT = 100;
    
    int NUKE_DETONATE_WIDTH = 300;
    int NUKE_DETONATE_HEIGHT = 300;
    
    int NUKE_READY_X = HEALTH_X - 60;
    int NUKE_READY_Y = BOARD_HEIGHT - 90;
    
    //level 3
    
    int MYSTERYCRAFT_WIDTH = SPACESHIP_WIDTH + 20;
    int MYSTERYCRAFT_HEIGHT = SPACESHIP_WIDTH + 20;
    
    int MYSTERYCRAFT_READY_X = HEALTH_X - 60;
    int MYSTERYCRAFT_READY_Y = NUKE_READY_Y;
    
    //level 4
    
    int PLANET_WIDTH = 500;
    int PLANET_HEIGHT = 500;
    
    int PLANET_SHIP_WIDTH = 60;
    int PLANET_SHIP_HEIGHT = 90;
    
    //level 5
    
    //first cloner
    
    int ALIEN_CLONER_WIDTH_1 = 200;
    int ALIEN_CLONER_HEIGHT_1 = 250;
    int ALIEN_CLONER_START_X_1 = (BOARD_WIDTH / 2) - (ALIEN_CLONER_WIDTH_1 / 2) ;  
    int ALIEN_CLONER_START_Y_1 = 200;
    
    //second cloner 
    
    int ALIEN_CLONER_WIDTH_2 = 150;
    int ALIEN_CLONER_HEIGHT_2 = 200;
    int ALIEN_CLONER_START_X_2 = 100; 
    int ALIEN_CLONER_START_Y_2 = 200;
    
    //third cloner 
    
    int ALIEN_CLONER_WIDTH_3 = 100;
    int ALIEN_CLONER_HEIGHT_3 = 150;
    int ALIEN_CLONER_START_X_3 = 200; 
    int ALIEN_CLONER_START_X_3B = 400; 
    int ALIEN_CLONER_START_Y_3 = 200;
        
    //LEVEL 6
    
    int PIXEL_SHIP_WIDTH  = 10;
    int PIXEL_SHIP_HEIGHT = 10;
    
    //GAME OVER SCREEN
    
    int GOTEXT_WIDTH = 450;
    int GOTEXT_HEIGHT = 190;
    
    int COIN_WIDTH = 200;
    int COIN_HEIGHT = 200;
    
    //GAME FINISHED SCREEN
        
    int FINISH_TEXT_WIDTH = 300;
    int FINISH_TEXT_HEIGHT = 190;
}
