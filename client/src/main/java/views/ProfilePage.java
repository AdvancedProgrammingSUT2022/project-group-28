package views;

import java.io.File;
import java.util.ArrayList;

import controllers.NetworkController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;
import models.network.ClientRequest;
import models.FriendshipRequest;
import models.network.ServerResponse;

public class ProfilePage extends PageController{
    private Thread pageUpdater;

    @FXML
    private ImageView picture,right,left;
    @FXML
    private VBox friendshipRequestsContainer;

    private int imagePointer;

    public void initialize(){
        User currentUser = App.getCurrentUser();
        picture.setImage(currentUser.getAvatar());
        this.imagePointer = currentUser.getProfilePicNumber();
        if(imagePointer==7) left.setVisible(false);
        else if(imagePointer==1) right.setVisible(false);

        createFriendshipRequestsList();
        createPageUpdater();
    }

    public void back(){
        pageUpdater.interrupt();
        App.setRoot("mainPage");
    }

    public void chooseAvatar(){
        User currentUser = App.getCurrentUser();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open avatar picture");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*jpg"));
        File file = fileChooser.showOpenDialog((Stage)picture.getScene().getWindow());
        if (file!=null){
            picture.setImage(new Image(file.toURI().toString()));
            imagePointer = -1;
            currentUser.setProfilePicNumber(-1);
            currentUser.setAvatar(picture.getImage());
        }
    }

    public void leftPic(){
        User currentUser = App.getCurrentUser();

        if(imagePointer == -1) imagePointer = 1;
        if (imagePointer==7)return;
        else {
            imagePointer++;
            if(imagePointer==7) left.setVisible(false);
            picture.setImage(new Image(App.resource.resolve("assets/image/profilePictures/" + imagePointer + ".png").toString()));
            currentUser.setProfilePicNumber(imagePointer);

            ArrayList<String> data = new ArrayList<>();
            data.add(String.valueOf(imagePointer));
            ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.CHANGE_AVATAR , data,
                    NetworkController.getInstance().getUserToken());
            NetworkController.getInstance().sendRequest(clientRequest);

            right.setVisible(true);
        }
    }

    public void rightPic(){
        User currentUser = App.getCurrentUser();

        if(imagePointer < 1) imagePointer = 1;
        if (imagePointer==1)return;
        else {
            imagePointer--;
            if(imagePointer==1) right.setVisible(false);
            picture.setImage(new Image(App.resource.resolve("assets/image/profilePictures/" + imagePointer + ".png").toString()));
            currentUser.setProfilePicNumber(imagePointer);

            ArrayList<String> data = new ArrayList<>();
            data.add(String.valueOf(imagePointer));
            ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.CHANGE_AVATAR, data,
                                        NetworkController.getInstance().getUserToken());
            NetworkController.getInstance().sendRequest(clientRequest);

            left.setVisible(true);
        }
    }

    public void changePassword(){
        // TODO: handle server

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(picture.getScene().getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("changePassword"), 788, 488);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    public void changeNickname(){
        // TODO: handle server
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(picture.getScene().getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("changeNickname"), 788, 260);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    private void createFriendshipRequestsList() {
        String token = NetworkController.getInstance().getUserToken();
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_USER_INFO,
                new ArrayList<>(), token);

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            App.setCurrentUser(User.fromXML(serverResponse.getData().get(0)));
        }

        User currentUser = App.getCurrentUser();
        friendshipRequestsContainer.getChildren().clear();

        for (FriendshipRequest friendshipRequest : currentUser.getFriendshipRequests()) {
            VBox vBox = new VBox(10);
            vBox.setPrefWidth(450);
            vBox.setAlignment(Pos.CENTER);
            vBox.getStyleClass().add("friendship_info");

            Text text = new Text(friendshipRequest.getSender().getNickname() + " requests " +
                                 friendshipRequest.getReceiver().getNickname());
            text.getStyleClass().add("normal_text");
            vBox.getChildren().add(text);

            Text state = new Text("State: " + friendshipRequest.getState().toString());
            state.getStyleClass().add("normal_text");
            vBox.getChildren().add(state);

            if (friendshipRequest.getState() == FriendshipRequest.State.WAITING &&
                friendshipRequest.getSender().getId() != currentUser.getId()) {
                Button accept = new Button("Accept");
                accept.getStyleClass().add("normal_button");
                accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add(friendshipRequest.getReceiver().getNickname());
                        ClientRequest clientRequest1 = new ClientRequest(ClientRequest.Request.ACCEPT_FRIENDSHIP,
                                                            data, NetworkController.getInstance().getUserToken());
                        ServerResponse serverResponse1 = NetworkController.getInstance().sendRequest(clientRequest1);

                        if (serverResponse1.getResponse() == ServerResponse.Response.SUCCESS) {
                            createFriendshipRequestsList();
                        }
                    }
                });
                vBox.getChildren().add(accept);

                Button reject = new Button("Reject");
                reject.getStyleClass().add("normal_button");
                reject.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add(friendshipRequest.getReceiver().getNickname());
                        ClientRequest clientRequest1 = new ClientRequest(ClientRequest.Request.REJECT_FRIENDSHIP,
                                data, NetworkController.getInstance().getUserToken());
                        ServerResponse serverResponse1 = NetworkController.getInstance().sendRequest(clientRequest1);
                        if (serverResponse1.getResponse() == ServerResponse.Response.SUCCESS) {
                            createFriendshipRequestsList();
                        }
                    }
                });
                vBox.getChildren().add(reject);
            }
            friendshipRequestsContainer.getChildren().add(vBox);
        }
    }

    private void createPageUpdater() {
        pageUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                createFriendshipRequestsList();
                            }
                        });
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        pageUpdater.start();
    }
}
