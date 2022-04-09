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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author domin
 */
public class GameBoard implements Externalizable{
    
    private static final long serialVersionUID = 1L;
    
    public GameBoard() {        
    }
    
    private static GameBoard INSTANCE;

    public static GameBoard getInstance() {
        return INSTANCE;
    }
    
    public static void changeGameBoard(GameBoard test) {
        INSTANCE = test;
    }
    
    private static Button save;
    
    public static void setSave(Button s){
        save = s;
    }
    
    public static Button getSave(){
        return save;
    }
    
    private List<Tile> tiles;
    private int x = 0;
    private int y = 0;
    
    private int count = 0;
    
    private Player player1 = null;
    private Player player2 = null;
    
    private Player playing = null;
    
    public void generateMemory(int x, int y){
        
        this.x = x;
        this.y = y;
        
        List<Tile> tiles = new ArrayList<>();
        char c = 'A';
        
        for (int i = 0; i < (x * y / 2); i++) {
            tiles.add(new Tile(String.valueOf(c), x));
            tiles.add(new Tile(String.valueOf(c), x));
            c++;
        }
        
        Collections.shuffle(tiles);
        
        this.tiles = tiles;
    }
    
    public void createGameBoard(AnchorPane GameBoard){
        
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            tile.setTranslateX(600 / x * (i % x));
            tile.setTranslateY(600 / x * (i / x));
            GameBoard.getChildren().add(tile); 
        }
            if (playing.getID().equals(player1.getID())) {
                player2.changeVisible();   
            }
            else{
                player1.changeVisible();
            }
            player1.scoreRelode();
            player2.scoreRelode();
    }
    
    public void createGameBoard(AnchorPane GameBoard, Player p1, Player p2){
        
        player1 = p1;
        player2 = p2;
        
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            tile.setTranslateX(600 / x * (i % x));
            tile.setTranslateY(600 / x * (i / x));
            GameBoard.getChildren().add(tile);
            
        }
    }
    
    /*
    public static List<Tile> getTiles(){
        return tiles;
    }
    */
    
    public void swapPlayer(){
        playing.changeVisible();
        
        if (playing == player1) {
            playing = player2;
        } else if(playing == player2) {
            playing = player1;
        }
        
        playing.changeVisible();
    }
    

    void pointPlayer() {
        
        playing.updateScore(playing.getScore());
    }

    void showPlayers() {
        if (!player1.getVisible()) {
            player1.changeVisible();
        }
        if (!player2.getVisible()) {
            player2.changeVisible();
        }
    }
    
    public void setPlaying(Player playing){
        this.playing = playing;
    }

    public void setCount(int count){
        this.count = count;
    }
    
    public int getCount(){
        return this.count;
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(tiles.size());
        for (Tile tile : tiles) {
            out.writeObject(tile);
        }
    
        out.writeInt(count);

        out.writeObject(player1);
        out.writeObject(player2);

        out.writeObject(playing);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readInt();
        this.y = in.readInt();
        int tilesSize = in.readInt();
        tiles = new ArrayList<>();
        for (int i = 0; i < tilesSize; i++) {
            Tile tile = (Tile) in.readObject();
            Tile tile1 = new Tile(tile.getValue(), this.x);
            tile1.setOpen(tile.getOpen());
            tiles.add(tile1);
        }
        this.count = in.readInt();
        
        this.player1 = (Player) in.readObject();
        this.player2 = (Player) in.readObject();
        
        this.playing = (Player) in.readObject();
    }

    
}
