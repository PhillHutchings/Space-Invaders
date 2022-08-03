/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceInvaders;

import SpaceInvaders.Enemy.AlienCloner.AlienCloner;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */
public final class LoadClonerImages {
    
    private final ArrayList<ImageIcon> deathImages;

    public LoadClonerImages() {
        
        deathImages = new ArrayList<>();
        loadDeathImages();
    }
     
    /**
    * loads the death sequence images 
    */
   
   public void loadDeathImages(){
             
        File folder = new File(System.getProperty("user.dir") + "/src/SpaceInvaders/Enemy/AlienCloner/deathImages");
        File[] images = folder.listFiles();

        for (File image1 : images) {
            try {
                
                BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir") + "/src/SpaceInvaders/Enemy/AlienCloner/deathImages/" + image1.getName()));
                ImageIcon ii = new ImageIcon(bi);
                deathImages.add(ii);
                
            } catch (IOException ex) {
                
                Logger.getLogger(AlienCloner.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
   }

    public ArrayList<ImageIcon> getDeathImages() {
        
        return deathImages;
    }
   
   
}
