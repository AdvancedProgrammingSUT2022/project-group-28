package views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{
    
    public static Scene currentScene;
    public static URI resource;

    @Override
    public void start(Stage stage) throws IOException {

        currentScene = new Scene(loadFXML("startPage"));
        stage.setScene(currentScene);
        stage.getIcons().add(new Image(resource.resolve("assets/image/icon.png").toString()));

        stage.show();
        currentScene.getRoot().requestFocus();
    }

    public static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(resource.resolve("fxml/" + fxml + ".fxml").toURL());
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setRoot(String fxml) {
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
}
