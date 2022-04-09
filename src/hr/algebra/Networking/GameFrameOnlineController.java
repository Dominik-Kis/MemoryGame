/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.Networking;

import hr.algebra.AnimationThreadClient;
import hr.algebra.model.Player;
import hr.algebra.online.GameBoardNew;
import hr.algebra.online.TileNew;
import hr.algebra.rmi.ChatClient;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class GameFrameOnlineController implements Initializable {

    private final String TIME_FORMAT = "HH:mm:ss";
    private static final String MESSAGE_FORMAT = "%s (%s): %s";
    private final String CLIENT_NAME = "Client";
    private static final int MESSAGE_LENGTH = 78;
    private static final int FONT_SIZE = 15;
    private static GridPane memoryField;

    private ObservableList<Node> messages;

    private ChatClient chatClient;

    @FXML
    private TextField tfMessage;

    @FXML
    private ScrollPane spContainer;

    @FXML
    private VBox vbMessages;

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
    
    private Client client;
    
    private GameBoardNew copy;
    
    public static Map<String, Text> playerScore = new HashMap<>();
    public static Map<String, StackPane> spPlayer = new HashMap<>();
    
    @FXML
    private void send(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    @FXML
    private void sendMessage() {
        if (tfMessage.getText().trim().length() > 0) {
            chatClient.sendMessage(tfMessage.getText().trim());
            addMessage(tfMessage.getText().trim(), CLIENT_NAME, Color.BLACK);
            tfMessage.clear();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        memoryField = MemoryField;
        client = new Client(this);
        client.setDaemon(true);
        
        initializeGame();
        
        chatClient = new ChatClient(this);
        messages = FXCollections.observableArrayList();
        Bindings.bindContentBidirectional(messages, vbMessages.getChildren());
        tfMessage.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.length() >= MESSAGE_LENGTH) {
                        ((StringProperty) observable).setValue(oldValue);
                    }
                }
        );
        
        client.start();
        
    }

    public void postMessage(String message, String name, Color color) {
        Platform.runLater(() -> addMessage(message, name, color));
    }

    private void addMessage(String message, String name, Color color) {
        Label label = new Label();
        label.setFont(new Font(FONT_SIZE));
        label.setTextFill(color);
        label.setText(String.format(MESSAGE_FORMAT, LocalTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT)), name, message));
        messages.add(label);
        moveScrollPane();
    }

    private void moveScrollPane() {
        spContainer.applyCss();
        spContainer.layout();
        spContainer.setVvalue(1D);
    }
    
    
    public void initializeGame(){
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
        
        GameBoardNew.getInstance().setPlayer(p2);
        
        GameBoardNew.getInstance().setNextPlayer(p1);
        
        changeVisible(spPlayer2);
        
        GameBoardNew.getInstance().setActivePlayer(p1);
        
        GameBoardNew.getInstance().loadPlayers(p1, p2);
    }
    
    public void initializeLoad(){
        
        GameBoardNew.getInstance();
    }
    
    @FXML
    public void handleOnMouseClicked(MouseEvent event){
        if (!GameBoardNew.getInstance().getActivePlayer().getID().equals(GameBoardNew.getInstance().getPlayer().getID())) {
            return;
        }
        Node source = (Node)event.getSource();
        Integer colIndex = (GridPane.getColumnIndex(source) == null) ?  0 : (GridPane.getColumnIndex(source));
        Integer colRow = (GridPane.getRowIndex(source) == null) ? 0 : (GridPane.getRowIndex(source));
        
        
        StackPane pane = (StackPane)event.getSource();
        //pane.setStyle("-fx-background-color: black");
        ObservableList<Node> _value = (ObservableList<Node>)pane.getChildren();
        Text value = (Text)_value.get(0);
        
        
        TileNew tile = GameBoardNew.getInstance().getTiles().get((colRow * 4) + colIndex);
        TileNew selected = GameBoardNew.getInstance().getSelected();
        
        if (tile.getIsOpen()){
                return;
        }
        
        GameBoardNew.getInstance().setPressed(tile);
        GameBoardNew.getInstance().setIndex((colRow * 4) + colIndex);
        
        copy = GameBoardNew.getInstance().copy();
        
        
        
        TileLogic(tile, (colRow * 4) + colIndex);
        
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
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameFrameHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
        text.setStyle("-fx-opacity: 0.1");
    }
    
    private void changeVisible(StackPane spPlayer) {
        boolean v = spPlayer.visibleProperty().getValue();
        spPlayer.setVisible(!v);
    }
    
    
    public void TileLogic(TileNew tile, int i) {
        
        
        TileNew selected = GameBoardNew.getInstance().getSelected();
        
        
        ObservableList<Node> children = MemoryField.getChildren();
        StackPane pane = (StackPane)children.get(i);
        ObservableList<Node> _value = pane.getChildren();
        Text value = (Text)_value.get(0);
        
        boolean ok = (GameBoardNew.getInstance().getActivePlayer().getID().equals(GameBoardNew.getInstance().getPlayer().getID()));
        
        if (selected == null) {
            
            selectedValue = value;
            GameBoardNew.getInstance().setSelected(tile);
            tile.setIsOpen(true);
            open(value);   
            
            if (ok) {
            copy.setNextPlayer(GameBoardNew.getInstance().getActivePlayer());
            client.trigger(copy);
            GameBoardNew.getInstance().setNextPlayer(GameBoardNew.getInstance().getActivePlayer());
            }
        }
        else {
            tile.setIsOpen(true);
            open(value);
            if (!tile.getValue().equals(selected.getValue())) {
               selected.setIsOpen(false);
                new Thread(() -> {
                    closeDelay(value);
                }).start();
                new Thread(() -> {
                    closeDelay(selectedValue);
                }).start();
               tile.setIsOpen(false);
               //changePlayer()
               GameBoardNew.getInstance().swapPlayer();
               
               System.err.println(GameBoardNew.getInstance().getActivePlayer().getID());
               
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
                
                GameBoardNew.getInstance().setCount(GameBoardNew.getInstance().getCount()+1);
                if (GameBoardNew.getInstance().getCount() == 4*4/2) {
                    spPlayer1.setVisible(true);
                    spPlayer2.setVisible(true);
                    ExecutorService executorService = Executors.newCachedThreadPool();
                    if (GameBoardNew.getInstance().getPlayer1().getScore() > GameBoardNew.getInstance().getPlayer2().getScore()) {
                        AnimationThreadClient.setColor("dodgerblue");
                    }
                    else if (GameBoardNew.getInstance().getPlayer1().getScore() < GameBoardNew.getInstance().getPlayer2().getScore()) {
                        AnimationThreadClient.setColor("#ff971f");
                    }
                    else{
                        AnimationThreadClient.setColor("Black");
                    }
                    for (int j = 0; j < 10; j++) {
                        executorService.execute(new AnimationThreadClient());
                    }
                }
                GameBoardNew.getInstance().setSelected(null);
                selectedValue = null;
            }
        if (ok) {
            copy.setNextPlayer(GameBoardNew.getInstance().getActivePlayer());
            client.trigger(copy);
            GameBoardNew.getInstance().setNextPlayer(GameBoardNew.getInstance().getActivePlayer());
        }
            
        }

        
    }

    void Load() {
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
    }
            
    public static GridPane getMemoryField(){
        return memoryField;
    }
}
