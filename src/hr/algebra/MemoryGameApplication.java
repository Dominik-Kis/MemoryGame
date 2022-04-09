/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author domin
 */
public class MemoryGameApplication extends Application {
    
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlFiles = getClass().getResource("GameFrame.fxml");
        URL fxmlFile = getClass().getResource("MainMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        
        fxmlLoader.load();
        
        /*
        GameFrameController a = fxmlLoader.getController();
        a.test();
        */
        Scene scene = new Scene(fxmlLoader.getRoot());
        mainStage = stage;
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
