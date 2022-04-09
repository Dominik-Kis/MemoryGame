/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.online;

import hr.algebra.model.Player;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.SerializationUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class GameFrameOnlineController{
    
    @FXML
    private AnchorPane parent;
    
    @FXML
    private GridPane MemoryField;
    
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
    
    private Text selectedValue = null;
    
    public static Map<String, Text> playerScore = new HashMap<>();
    public static Map<String, StackPane> spPlayer = new HashMap<>();
    
    public void initialize() {
        
        playerScore.put("1", playerScore1);
        playerScore.put("2", playerScore2);
        
        spPlayer.put("1", spPlayer1);
        spPlayer.put("2", spPlayer2);
        
        GameBoardNew.changeGameBoard(new GameBoardNew());
        GameBoardNew.getInstance().generateMemory();
        List<TileNew> tiles = GameBoardNew.getInstance().getTiles();
        
        ObservableList<Node> children = MemoryField.getChildren();
        for (int i = 0; i < children.size(); i++) {
            StackPane pane = (StackPane)children.get(i);
            ObservableList<Node> _value = pane.getChildren();
            Text value = (Text)_value.get(0);
            value.setText(tiles.get(i).getValue());
            
            System.out.println(value.getText());
            
            value.setVisible(true);
            value.setOpacity(0);
            
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
        
        
        changeVisible(spPlayer2);
        
        GameBoardNew.getInstance().setActivePlayer(p1);
        
        GameBoardNew.getInstance().loadPlayers(p1, p2);
    }
    
    public void initializeLoad(){
        
        GameBoardNew.getInstance();
    }
    
    @FXML
    public void handleOnMouseClicked(MouseEvent event){
        Node source = (Node)event.getSource();
        Integer colIndex = (GridPane.getColumnIndex(source) == null) ?  0 : (GridPane.getColumnIndex(source));
        Integer colRow = (GridPane.getRowIndex(source) == null) ? 0 : (GridPane.getRowIndex(source));
        
        
        StackPane pane = (StackPane)event.getSource();
        //pane.setStyle("-fx-background-color: black");
        ObservableList<Node> _value = (ObservableList<Node>)pane.getChildren();
        Text value = (Text)_value.get(0);
        
        
        TileNew tile = GameBoardNew.getInstance().getTiles().get((colRow * 4) + colIndex);
        TileNew selected = GameBoardNew.getInstance().getSelected();
        
        if (tile.getIsOpen())
                return;
        if (selected == null) {
            selectedValue = value;
            GameBoardNew.getInstance().setSelected(tile);
            tile.setIsOpen(true);
            open(value,() -> {});
        }
        else {
            tile.setIsOpen(true);
            open(value, () ->{
            if (!tile.getValue().equals(selected.getValue())) {
               selected.setIsOpen(false);
               close(selectedValue);
               tile.setIsOpen(false);
               close(value);
               //changePlayer()
               GameBoardNew.getInstance().swapPlayer();
               changeVisible(spPlayer1);
               changeVisible(spPlayer2);
               GameBoardNew.getInstance().setSelected(null);
               //GameBoardNew.getSave().setDisable(false);
            }
            else{
                //give point
                GameBoardNew.getInstance().getActivePlayer().givePoint();
                int point = GameBoardNew.getInstance().getActivePlayer().getScore();
                String points = Integer.toString(point);
                playerScore.get(GameBoardNew.getInstance().getActivePlayer().getID()).setText(points);
                
                //GameBoardNew.getInstance().setCount(GameBoard.getInstance().getCount()+1);
                //if (GameBoardNew.getInstance().getCount() == 4*4/2) {
                //    GameBoardNew.getInstance().showPlayers();
                //}
                GameBoardNew.getInstance().setSelected(null);
                selectedValue = null;
            }
            });
        }
        
        System.out.println(colRow + ":" + colIndex);
        }
   
        
    public void open(Text text,Runnable action){
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
        ft.setToValue(1);
        ft.setOnFinished(e -> action.run());
        ft.play();
    } 

    private void close(Text text){
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
        ft.setToValue(0);
        ft.play();
    }
    
        @FXML
    private void serialize() {
        try {
            File file = FileUtils.saveFileDialog(Save.getScene().getWindow(), "ser");
            if (file != null) {
                SerializationUtils.write(GameBoardNew.getInstance(), file.getAbsolutePath());
            }
        } catch (IOException ex) {
            Logger.getLogger(GameFrameOnlineController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changeVisible(StackPane spPlayer) {
        boolean v = spPlayer.visibleProperty().getValue();
        spPlayer.setVisible(!v);
    }
    
    
}
