/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.model.GameBoard;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ReflectionUtils;
import hr.algebra.utilities.SerializationUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * FXML Controller class
 *
 * @author domin
 */

public class MainMenuController implements Initializable {
    
    private final String packageLocation = ".\\src";
    private final String subPackageLocation = "";
    
    private static final String[] LEVELS = {"4x4","6x6", "20x20"};
    private static final String SPLITER = "x";
    
    @FXML
    private Button Start;
    
    @FXML
    private Button Load;
    
    @FXML
    private Button Exit;
    
    @FXML
    private Button Online;
    
    @FXML
    private ComboBox LevelSelect;
    
    
    @FXML
    public void StartOnClick() throws IOException{
        String[] dimensions = LEVELS[LevelSelect.getSelectionModel().getSelectedIndex()].split(SPLITER);
        GameBoard.changeGameBoard(new GameBoard());
        GameBoard.getInstance().generateMemory(Integer.parseInt(dimensions[0]),Integer.parseInt(dimensions[1]));
        FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("GameFrame.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GameFrameController ctrl = fxmlLoader.getController();
        ctrl.initializeStart();
        MemoryGameApplication.getMainStage().setScene(scene);
    }    
    
    @FXML
    public void ReplayOnClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("dom/Replay.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MemoryGameApplication.getMainStage().setScene(scene);
    }
    
    @FXML
    public void ExitOnClick(){
        Platform.exit();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LevelSelect.getItems().addAll(LEVELS);
        LevelSelect.setStyle("-fx-font: 24px \"System\"; -fx-font-weight: bold;");
        LevelSelect.getSelectionModel().selectFirst();
        LevelSelect.getSelectionModel().getSelectedIndex();
    }
    
    @FXML
    public void OnlineOnClick() throws IOException{
    FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("OnlineMenu.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    MemoryGameApplication.getMainStage().setScene(scene);
    }
    
    @FXML
    public void deserialize() {
        File file = FileUtils.uploadFileDialog(Load.getScene().getWindow(), "ser");
        if (file != null) {
            try {
                // we do not need a returned instance of Repository, because it is a Singleton
                // also, we do not need to refresh the form, because of the observable pattern
                
                
                GameBoard board =(GameBoard)SerializationUtils.read(file.getAbsolutePath());
                
                GameBoard.changeGameBoard(board);
                
                FXMLLoader fxmlLoader = new FXMLLoader(MemoryGameApplication.class.getResource("GameFrame.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                GameFrameController ctrl = fxmlLoader.getController();
                MemoryGameApplication.getMainStage().setScene(scene);
                ctrl.initializeLoad();
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GameFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void Document(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        ReflectionUtils.readAllFromSourcePackage(packageLocation, subPackageLocation, sb);
        
        try (FileWriter zapisivac = new FileWriter("dokumentacija.html")) {
            zapisivac.write(sb.toString());

        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
