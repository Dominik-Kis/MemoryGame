/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.online;

import hr.algebra.model.Player;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author domin
 */
public class GameBoardNew implements Serializable{
     
    private static Player player;
    private Player nextPlayer;
    
    private List<TileNew> tiles;
    private TileNew selected;
    private TileNew pressed;
    private Player p1;
    private Player p2;
    private Player activePlayer;
    private int count = 0;
    
    private static GameBoardNew INSTANCE;       
    public static GameBoardNew getInstance() {
        return INSTANCE;
    }
    private int index;
    
    public void generateMemory(){

        tiles = new ArrayList<>();
        char c = 'A';
        
        for (int i = 0; i < 4*4/2; i++) {
            tiles.add(new TileNew(String.valueOf(c)));
            tiles.add(new TileNew(String.valueOf(c)));
            c++;
        }
        
        Collections.shuffle(tiles);
        
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setIndex(i);
        }
    }
    
    public static void changeGameBoard(GameBoardNew test) {
        INSTANCE = test;
    }
    
    public void setTiles(List<TileNew> tiles){
        this.tiles = tiles;
    }
    
    public List<TileNew> getTiles(){
        return tiles;
    }
    
    public TileNew getSelected(){
        return selected;
    }
    
    public void setSelected(TileNew tile){
        selected = tile;
    }
    
    public Player getActivePlayer(){
        return activePlayer;
    }
    
    public void setActivePlayer(Player p){
        activePlayer = p;
    }

    public void loadPlayers(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public void setPlayer(Player player){
        GameBoardNew.player = player;
    }

    public void swapPlayer() {
        if (activePlayer.getID() == p1.getID()) {
            activePlayer = p2;
        }
        else if (activePlayer.getID() == p2.getID()) {
            activePlayer = p1;
        }
    }
    
    public TileNew getPressed(){
        return pressed;
    }
    
    public void setPressed(TileNew pressed){
        this.pressed = pressed;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getIndex(){
        return index;
    }

    public GameBoardNew copy(){
        
        Object object = null;
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(INSTANCE);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            object = new ObjectInputStream(bais).readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameBoardNew.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameBoardNew.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (GameBoardNew)object;
    }
    
    public Player getNextPlayer(){
        return nextPlayer;
    }
    
    public void setNextPlayer(Player nextPlayer){
        this.nextPlayer = nextPlayer;
    }
    
    public int getCount(){
        return count;
    }
    
    public void setCount(int count){
        this.count = count;
    }
    
    public Player getPlayer1(){
        return p1;
    }
    
    public void setPlayer1(Player player){
        p1 = player;
    }
    
    public Player getPlayer2(){
        return p2;
    }
    
    public void setPlayer2(Player player){
        p2 = player;
    }
}
