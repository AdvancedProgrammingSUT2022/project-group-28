import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import views.Menu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

class OldMain {
    public static void main(String[] args) {
        while(true){
            Menu.getCurrentMenu().run();
        }
    }
}

public class Main extends Application {

    public static Scene currentScene;

    @Override
    public void start(Stage stage) throws IOException {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        
        Scene scene = new Scene(loadFXML("startPage"));
        stage.setScene(scene);
        stage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setRoot() {

    }

    public static void main(String[] args) {
        launch();
        OldMain.main(args);
    }

}
