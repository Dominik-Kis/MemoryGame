/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dom;

import hr.algebra.AnimationThread;
import hr.algebra.MemoryGameApplication;
import hr.algebra.Networking.Server;
import hr.algebra.model.Player;
import hr.algebra.online.GameBoardNew;
import hr.algebra.online.TileNew;
import hr.algebra.rmi.ChatServer;
import hr.algebra.utilities.MessageUtils;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author GamerGruft
 */
public class ReplayController implements Initializable {
    
    private static GridPane memoryField;
    
    ObservableList<GameBoardNew> boards;
    int index = 0;
    int indexMax;
    ArrayList<GameBoardNew> list = new ArrayList<GameBoardNew>();
    
    @FXML
    private AnchorPane parent;
    
    @FXML
    private GridPane MemoryField;
    
    @FXML
    private Button BackToMenu;
    
    @FXML
    private Button btnBackward;
        
    @FXML
    private Button btnForward;
    
    @FXML
    private Text playerScore1;
    
    @FXML
    private Text playerScore2;
    
    @FXML
    private StackPane spPlayer1;
    
    @FXML
    private StackPane spPlayer2;
    
    private Text selectedValue = null;
    
    public static Map<String, Text> playerScore = new HashMap<>();
    public static Map<String, StackPane> spPlayer = new HashMap<>();
    
    @FXML
    public void BackToMenuOnClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MemoryGameApplication.getMainStage().setScene(scene);
    }
        
    @FXML
    public void BackwardOnClick(){
        if (index != 0) {
        index--;
        load();
        }
    }
    
    @FXML
    public void ForwardOnClick(){
        if (index != indexMax) {
        index++;
        load();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loadDOM();
        indexMax = boards.size() - 1;
        checkButtons();
        initializeGame();
        
    }
    
         
     public void initializeGame(){
        playerScore.put("1", playerScore1);
        playerScore.put("2", playerScore2);
        
        spPlayer.put("1", spPlayer1);
        spPlayer.put("2", spPlayer2);
        
        GameBoardNew.changeGameBoard(boards.get(0));
        List<TileNew> tiles = GameBoardNew.getInstance().getTiles();
        
        ObservableList<Node> children = MemoryField.getChildren();
        for (int i = 0; i < children.size(); i++) {
            StackPane pane = (StackPane)children.get(i);
            ObservableList<Node> _value = pane.getChildren();
            Text value = (Text)_value.get(0);
            value.setText(tiles.get(i).getValue());
            
            System.out.println(value.getText());
            
            value.setVisible(true);
            
            value.setStyle("-fx-opacity: 0");
            
            if (tiles.get(i).getIsOpen()) {
            value.setStyle("-fx-opacity: 1");
            }
        }
        
        
        
        initializeStart();
    }
     
    public void initializeStart(){
        Player p1 = new Player("1", 0);
        Player p2 = new Player("2", 0);
        
        GameBoardNew.getInstance().setPlayer(p1);
        
        GameBoardNew.getInstance().setNextPlayer(p1);
        
        changeVisible(spPlayer2);
        
        GameBoardNew.getInstance().setActivePlayer(p1);
        
        GameBoardNew.getInstance().loadPlayers(p1, p2);
    }
   
        
    public void open(Text text){
        //FadeTransition ft = new FadeTransition(Duration.seconds(0.1), text);
        //ft.setToValue(1);
        //ft.setOnFinished(e -> action.run());
        //ft.play();
        
        text.setStyle("-fx-opacity: 1");
    } 

    private void close(Text text){
        //FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
        //ft.setToValue(0);
        //ft.play();
        
        text.setStyle("-fx-opacity: 0.1");
    }
    
    private void closeDelay(Text text){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReplayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        text.setStyle("-fx-opacity: 0.1");
    }
    
    private void changeVisible(StackPane spPlayer) {
        boolean v = spPlayer.visibleProperty().getValue();
        spPlayer.setVisible(!v);
    }

    private void loadBoard(){
        GameBoardNew.changeGameBoard(boards.get(index));
    }
    
    private void loadDOM() {
        boards = DOMUtils.loadBoards();
    }

    private void checkButtons() {
        if (index == 0) {
            btnBackward.setDisable(true);
        }
        else{
            btnBackward.setDisable(false);
        }
        if (index == indexMax) {
            btnBackward.setDisable(true);
        }
        else{
            btnBackward.setDisable(false);
        }
    }

    private void load() {
        GameBoardNew.changeGameBoard(boards.get(index));
        reload();
    }

    private void reload() {
        
        List<TileNew> tiles = GameBoardNew.getInstance().getTiles();
        
        ObservableList<Node> children = MemoryField.getChildren();
        for (int i = 0; i < children.size(); i++) {
            StackPane pane = (StackPane)children.get(i);
            ObservableList<Node> _value = pane.getChildren();
            Text value = (Text)_value.get(0);
            value.setText(tiles.get(i).getValue());
            
            System.out.println(value.getText());
            
            value.setVisible(true);
            
            value.setStyle("-fx-opacity: 0");
            
            if (tiles.get(i).getIsOpen()) {
            value.setStyle("-fx-opacity: 1");
            }
        }
        
        loadPlayers();
        
    }

    private void loadPlayers() {
        playerScore1.setText(Integer.toString(GameBoardNew.getInstance().getPlayer1().getScore()));
        playerScore2.setText(Integer.toString(GameBoardNew.getInstance().getPlayer2().getScore()));
        if (GameBoardNew.getInstance().getActivePlayer().getID().equals(GameBoardNew.getInstance().getPlayer1().getID())) {
            spPlayer1.setVisible(true);
            spPlayer2.setVisible(false);
        }
        else if(GameBoardNew.getInstance().getActivePlayer().getID().equals(GameBoardNew.getInstance().getPlayer2().getID())){
            spPlayer2.setVisible(true);
            spPlayer1.setVisible(false);
        }
        else{
            spPlayer1.setVisible(true);
            spPlayer2.setVisible(true);
        }
    }
}
