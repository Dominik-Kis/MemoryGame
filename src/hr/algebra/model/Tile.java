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
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author domin
 */
public class Tile extends StackPane implements Externalizable{
    
    private static final long serialVersionUID = 1L;
    
    private static Tile selected = null;
    private Text text = new Text();
    private boolean isOpen = false;
    
    public static void resetSelected(){
        selected = null;
    }
    
    public Tile(){
    }
    
    public Tile(String value, int x){
        Rectangle border = new Rectangle(600 / x,600 /x);
        border.setFill(null);
        border.setStroke(Color.BLACK);
        
        text.setText(value);
        text.setFont(Font.font(600 / x * 0.7));
        text.setOpacity(0);
        
        setAlignment(Pos.CENTER);
        getChildren().addAll(border, text);
        
        setOnMouseClicked(event -> {
            if (isOpen)
                return;
            if (selected == null) {
                selected = this;
                open(() -> {});
                GameBoard.getSave().setDisable(true);
            }
            else {
                open(() -> {
                if (!hasSameValue(selected)) {
                   selected.close();
                   this.close();
                   //changePlayer()
                   GameBoard.getInstance().swapPlayer();
                    selected = null;
                   GameBoard.getSave().setDisable(false);
                }
                else{
                    //give point
                    GameBoard.getInstance().pointPlayer();
                    GameBoard.getInstance().setCount(GameBoard.getInstance().getCount()+1);
                    GameBoard.getSave().setDisable(false);
                    if (GameBoard.getInstance().getCount() == x*x/2) {
                        GameBoard.getInstance().showPlayers();
                    }
                    selected = null;
                }
                });
            }
        });
    }
    
   
    
    public void open(Runnable action){
        isOpen = true;
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
        ft.setToValue(1);
        ft.setOnFinished(e -> action.run());
        ft.play();
    } 
    
    public void close(){
        isOpen = false;
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
        ft.setToValue(0);
        ft.play();
    }
    
    public boolean hasSameValue(Tile other) {
        return text.getText().equals(other.text.getText());
    }
    
    public String getValue(){
        return this.text.getText();
    }
    
    public boolean getOpen(){
        return this.isOpen;
    }
    
    public void setOpen(boolean b){
        this.isOpen = b;
        if (b) {
            open(() -> {});
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(isOpen);
        out.writeUTF(text.getText());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        isOpen = in.readBoolean();
        text.setText(in.readUTF());
    }
    
}
