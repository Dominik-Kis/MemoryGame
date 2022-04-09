/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class OnlineMenuController implements Initializable {

    @FXML
    private Button Connect;
    
    @FXML
    private Button Host;
    
    @FXML
    private Button BackToMenu;
    
    
    
    @FXML
    public void BackToMenuOnClick() throws IOException{

    FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("MainMenu.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    MemoryGameApplication.getMainStage().setScene(scene);
    }
    
    @FXML
    public void JoinOnClick() throws IOException{

    //FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("Networking/GameFrameOnline.fxml"));
    FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("Networking/GameFrameOnline.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    MemoryGameApplication.getMainStage().setScene(scene);
    }
    
    @FXML
    public void HostOnClick() throws IOException{

    FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("Networking/GameFrameHost.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    MemoryGameApplication.getMainStage().setScene(scene);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
