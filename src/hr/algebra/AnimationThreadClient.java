/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.Networking.GameFrameOnlineController;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 *
 * @author GamerGruft
 */
public class AnimationThreadClient implements Runnable{

    private static boolean uTijeku = false;
    
    private static String Color;
    
    public static void setColor(String color) {
        Color = color;
    }
    
    @Override
    public void run() {
        while (true) {

            Random random = new Random();

            int index = random.nextInt(4*4);

            animate(index);

            try {
                Thread.sleep(random.nextInt(200));
            } catch (InterruptedException ex) {
                Logger.getLogger(AnimationThreadClient.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized void animate(int index) {
        while (uTijeku == true) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AnimationThreadClient.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        
        uTijeku = true;
        
        ObservableList<Node> _value = (ObservableList<Node>)GameFrameOnlineController.getMemoryField().getChildren();
        StackPane value = (StackPane)_value.get(index);
        Random random = new Random();
        if (random.nextBoolean()) {
            value.setStyle("-fx-background-color: " + Color);
        }
        else{
            value.setStyle("-fx-background-color: white");
        }
        
        uTijeku = false;
        
        notifyAll();
    }
    
}
