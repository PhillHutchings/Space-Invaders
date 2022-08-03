/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

/**
 *
 * @author hutch
 */
public class Main extends JFrame implements commons{
    
    private Board board;
    
    public Main(){
        
       init();
    }
    
    private void init(){
        
        board = new Board();
        add(board);
        
        setTitle("Space Invaders");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        Game();
        
    }
    
    public final void Game(){
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
        Runnable task = () -> {
           
            board.repaint();
        };
        
        ex.scheduleAtFixedRate(task, 1000, 10, TimeUnit.MILLISECONDS);
    }
          
    public static void main(String[] args){
        
        java.awt.EventQueue.invokeLater(() -> {
        
            new Main().setVisible(true);
        });
    }
}
