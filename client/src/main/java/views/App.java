package views;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import controllers.GameController;
import controllers.GsonHandler;
import controllers.NetworkController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Game;
import models.User;
import models.civilization.Civilization;
import models.network.ClientRequest;
import models.network.ServerResponse;

public class App extends Application{
    // TODO: private
    public static Scene currentScene;
    public static URI resource;

    private static User currentUser;

    // TODO: check all on exit


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

                ClientRequest leaveGameRequest = new ClientRequest(ClientRequest.Request.LEAVE_GAME, new ArrayList<>(),
                                                NetworkController.getInstance().getUserToken());
                NetworkController.getInstance().sendRequest(leaveGameRequest);


                NetworkController.getInstance().logout();

                System.exit(0);
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

    public static void updateUserInfo() {
        String token = NetworkController.getInstance().getUserToken();
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_USER_INFO,
                new ArrayList<>(), token);

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            App.setCurrentUser(User.fromXML(serverResponse.getData().get(0)));
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static Civilization getCurrentUserCivilization() {
        Game game = GameController.getGame();
        for (Civilization civilization : game.getCivilizations()) {
            if (civilization.getUser().getId() == currentUser.getId()) return civilization;
        }

        return null;
    }

}
