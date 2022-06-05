package views;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import controllers.GsonHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.User;

public class App extends Application{
    
    public static Scene currentScene;
    public static URI resource;

    private static User currentUser;

    
    @Override
    public void start(Stage stage) throws IOException {
        
        currentScene = new Scene(loadFXML("loginPage"));
        stage.setScene(currentScene);
        stage.getIcons().add(new Image(resource.resolve("assets/image/icon.png").toString()));
        stage.setTitle("Civilization");
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                GsonHandler.exportDataOfUser(User.getAllUsers());
            }
        });
        currentScene.getRoot().requestFocus();
    }
    
    public static Parent loadFXML(String fxml){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(resource.resolve("fxml/" + fxml + ".fxml").toURL());
            return fxmlLoader.load();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setRoot(String fxml){
        currentScene.setRoot(loadFXML(fxml));
        currentScene.getRoot().requestFocus();
    }
    
    public static void run(URL resource) {
        try {
            App.resource = resource.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        launch();
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

}
