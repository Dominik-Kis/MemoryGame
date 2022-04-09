package hr.algebra.online;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author domin
 */
public class TileNew implements Serializable{
    
    private String value;
    private boolean isOpen = false;
    private int index;
    
    public TileNew(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
    
    public boolean getIsOpen(){
        return isOpen;
    }
    
    public void setIsOpen(boolean open){
        isOpen = open;
    }
    
    public void setIndex(int index){
        this.index = index;
    }
    
    public int getIndex(){
        return index;
    }
}
