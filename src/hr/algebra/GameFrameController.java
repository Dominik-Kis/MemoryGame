/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.model.GameBoard;
import hr.algebra.model.Player;
import hr.algebra.model.Tile;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.SerializationUtils;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class GameFrameController{
    
    @FXML
    private AnchorPane parent;
    
    @FXML
    private AnchorPane MemoryField;
    
    @FXML
    private Button BackToMenu;
    
    @FXML
    private Text playerScore1;
    
    @FXML
    private Text playerScore2;
    
    @FXML
    private StackPane spPlayer1;
    
    @FXML
    private StackPane spPlayer2;
    
    @FXML
    private Button Save;
    
    /**
     * Initializes the controller class.
     */
    
    public static Map<String, Text> playerScore = new HashMap<>();
    public static Map<String, StackPane> spPlayer = new HashMap<>();
    
    public void initialize() {
        
        
        playerScore.put("1", playerScore1);
        playerScore.put("2", playerScore2);
        
        spPlayer.put("1", spPlayer1);
        spPlayer.put("2", spPlayer2);
        /*
        List<Tile> tiles = GameBoard.getTiles();
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            tile.setTranslateX(50 * (i % 4));
            tile.setTranslateY(50 * (i / 4));
            MemoryField.getChildren().add(tile);
            
        }
        */
    }
    
    public void initializeStart(){
        Player p1 = new Player("1", 0);
        Player p2 = new Player("2", 0);
        
        
        p2.changeVisible();
        
        GameBoard.getInstance().setPlaying(p1);
        
        GameBoard.getInstance().createGameBoard(MemoryField, p1, p2);
        GameBoard.setSave(Save);
    }
    
    public void initializeLoad(){
        
        GameBoard.getInstance().createGameBoard(MemoryField);
        GameBoard.setSave(Save);
    }

    @FXML
    private void serialize() {
        try {
            File file = FileUtils.saveFileDialog(Save.getScene().getWindow(), "ser");
            if (file != null) {
                SerializationUtils.write(GameBoard.getInstance(), file.getAbsolutePath());
            }
        } catch (IOException ex) {
            Logger.getLogger(GameFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void BackToMenuOnClick() throws IOException{
        Tile.resetSelected();
        
        FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MemoryGameApplication.getMainStage().setScene(scene);
    }
    
    public void SaveEnable(){
        Save.setDisable(false);
    }
    
    public void SaveDisable(){
        Save.setDisable(true);
    }
    
}
