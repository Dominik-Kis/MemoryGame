/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.GameFrameController;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author domin
 */
public class Player implements Externalizable{
    
    private static final long serialVersionUID = 1L;
    
    private String ID;
    private int score = 0;
    boolean visible = true;
    
    public Player(){
    }
    
    public Player(String ID, int score){
        this.ID = ID;
        this.score = score;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }
    
    public void scoreRelode(){
        GameFrameController.playerScore.get(ID).setText(Integer.toString(this.score));
    }
    
    public void setID(String ID){
        this.ID = ID;
    }
    
    public String getID(){
        return ID;
    }
    
    public void updateScore(int score){
        this.score = (score + 1);
        GameFrameController.playerScore.get(ID).setText(Integer.toString(this.score));
    }
    
    public void givePoint(){
        this.score = score + 1;
    }
    
    public void changeVisible(){
        GameFrameController.spPlayer.get(ID).setVisible(
                visible = !getVisible());
        
    }
    
    public boolean getVisible(){
        return GameFrameController.spPlayer.get(ID).visibleProperty().getValue();
    }
    
    public void setVis(boolean visible){
        this.visible = visible;
    }
    
    public boolean getVis(){
        return visible;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(score);
        out.writeUTF(ID);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        score = in.readInt();
        ID = in.readUTF();
    }
}
