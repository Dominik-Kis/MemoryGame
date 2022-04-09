/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.Networking;

import hr.algebra.online.GameBoardNew;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author domin
 */
public class Server extends Thread{

    private static final String PROPERTIES_FILE = "socket.properties";
    private static final String CLIENT_PORT = "CLIENT_PORT";
    private static final String GROUP = "GROUP";
    private static final Properties PROPERTIES = new Properties();
    private ServerSocket server;
    private static Socket client;
    private final GameFrameHostController controller;
    
    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final LinkedBlockingDeque<GameBoardNew> suspects = new LinkedBlockingDeque<>();

    public Server(GameFrameHostController controller){
        this.controller = controller;
    }
    
    public void trigger(GameBoardNew suspect) {
        suspects.add(suspect);
    }
    
    @Override
    public void run() {
        // we use new Socket for each client call
        try {
            server = new ServerSocket(Integer.valueOf(PROPERTIES.getProperty(CLIENT_PORT)));
            client = server.accept();
            ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(client.getInputStream());
            System.err.println("Streams open");
            while (true) {

                if (!suspects.isEmpty()) {
                    
                    System.err.println("sending");
                    os.writeObject(suspects.getFirst());
                    suspects.clear();
                }
                else if(!GameBoardNew.getInstance().getNextPlayer().getID().equals(GameBoardNew.getInstance().getPlayer().getID()) && suspects.isEmpty() ){
                    System.err.println("getting");
                    GameBoardNew g = (GameBoardNew)is.readObject();
                    System.err.println("gotten");
                    if (g != null) {
                        GameBoardNew.changeGameBoard(g);
                        controller.TileLogic(GameBoardNew.getInstance().getPressed(), GameBoardNew.getInstance().getIndex());
                    }
                }

                
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
