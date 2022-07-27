package views;

import controllers.NetworkController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Chat;
import models.ChatMessage;
import models.User;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.util.ArrayList;
import java.util.Date;

public class ChatPage extends PageController {

    private static final ImagePattern editPattern = new ImagePattern(new Image(App.class.getResource("../assets/image/" +
            "ui_icon/edit.png").toExternalForm()));
    private static final ImagePattern deletePattern = new ImagePattern(new Image(App.class.getResource("../assets/image/" +
                    "ui_icon/red_cross.png").toExternalForm()));

    private Chat currentChat = null;
    private int toEditMessageIndex = 0;

    private Thread pageUpdater;

    private enum SendMode {
        EDIT,
        SEND
    }

    private SendMode sendMode = SendMode.SEND;

    @FXML
    private VBox availableChats;
    @FXML
    private VBox chatContent;
    @FXML
    private TextField messageField;
    @FXML
    private Rectangle sendButton;



    @FXML
    private void initialize() {
        sendButton.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/ui_icon/send.png").toExternalForm())));

        App.updateUserInfo();
        createPageUpdater();
    }



    @FXML
    private void back() {
        this.onExit();
        pageUpdater.interrupt();
        App.setRoot("mainPage");
    }

    private void createChatsList() {
        User user = App.getCurrentUser();

        availableChats.getChildren().clear();
        for (Chat chat : user.getChats()) {
            User friend;
            if (chat.getUser1().getId() != user.getId()) friend = chat.getUser1();
            else friend = chat.getUser2();

            HBox hBox = new HBox(20);
            hBox.setPrefWidth(400);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getStyleClass().add("chat_info");

            hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ChatPage.this.currentChat = chat;

                }
            });

            Circle avatar = new Circle(30);
            avatar.setFill(new ImagePattern(friend.getAvatar()));
            hBox.getChildren().add(avatar);

            Text nickname = new Text(friend.getNickname());
            nickname.getStyleClass().add("normal_text");
            hBox.getChildren().add(nickname);


            availableChats.getChildren().add(hBox);
        }
    }

    private void createChatMessagesBox() {
        if (currentChat != null) {
            int friendID;
            if (currentChat.getUser1().getId() != App.getCurrentUser().getId()) friendID = currentChat.getUser1().getId();
            else friendID = currentChat.getUser2().getId();

            ArrayList<String> data = new ArrayList<>();
            data.add(String.valueOf(friendID));

            ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_CHAT_INFO, data,
                                        NetworkController.getInstance().getUserToken());

            ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
            if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
                chatContent.getChildren().clear();
                currentChat = Chat.fromXML(serverResponse.getData().get(0));

                Integer messageIndex = 0;
                        Date dsf = new Date();
                for (ChatMessage chatMessage : currentChat.getChatMessages()) {
                    HBox hBox = new HBox();
                    hBox.setPrefWidth(1100);

                    HBox messageBox = new HBox(20);
                    messageBox.setMinWidth(500);
                    messageBox.getStyleClass().add("chat_message");

                    Text sender = new Text(chatMessage.getSender().getNickname());
                    sender.getStyleClass().add("normal_text");

                    Text message = new Text(chatMessage.getMessage());
                    message.getStyleClass().add("normal_text");

                    Text lastModified = new Text("[" + chatMessage.getLastModified().toString() + "]");

                    Circle avatar = new Circle(25);
                    avatar.setFill(new ImagePattern(chatMessage.getSender().getAvatar()));

                    if (chatMessage.getSender().getId() == App.getCurrentUser().getId()) {
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        messageBox.setAlignment(Pos.CENTER_LEFT);
                        messageBox.getChildren().add(avatar);
                        messageBox.getChildren().add(lastModified);
                        messageBox.getChildren().add(message);

                        Integer newMessageIndex = new Integer(messageIndex);

                        Rectangle edit = new Rectangle(20, 20);
                        edit.setFill(editPattern);
                        edit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                editMessage(newMessageIndex);
                            }
                        });
                        messageBox.getChildren().add(edit);

                        Rectangle delete = new Rectangle(20, 20);
                        delete.setFill(deletePattern);
                        delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                deleteMessage(newMessageIndex);
                            }
                        });
                        messageBox.getChildren().add(delete);

                    } else {
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                        messageBox.setAlignment(Pos.CENTER_RIGHT);
                        messageBox.getChildren().add(message);
                        Text friend = new Text(" | " + chatMessage.getSender().getNickname());
                        messageBox.getChildren().add(friend);
                        messageBox.getChildren().add(lastModified);
                        messageBox.getChildren().add(avatar);
                    }
                    hBox.getChildren().add(messageBox);
                    chatContent.getChildren().add(hBox);

                    messageIndex++;
                }
            }
        } else chatContent.getChildren().clear();
    }

    @FXML
    private void sendMessage() {
        if (currentChat != null) {
            int friendID;
            if (currentChat.getUser1().getId() != App.getCurrentUser().getId()) friendID = currentChat.getUser1().getId();
            else friendID = currentChat.getUser2().getId();

            ArrayList<String> data = new ArrayList<>();

            data.add(String.valueOf(friendID));
            String message = messageField.getText();
            if (message.length() == 0) return;

            if (sendMode == SendMode.SEND) {
                data.add(message);
                ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.SEND_MESSAGE, data,
                                            NetworkController.getInstance().getUserToken());

                ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

                if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
                    createChatMessagesBox();
                }
            } else {
                data.add(String.valueOf(toEditMessageIndex));
                data.add(message);
                ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.EDIT_MESSAGE, data,
                                            NetworkController.getInstance().getUserToken());

                ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
                if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
                    createChatMessagesBox();
                }

                sendMode = SendMode.SEND;
            }
            messageField.setText("");
        }
    }

    private void editMessage(Integer messageIndex) {
        String oldMessage = currentChat.getChatMessages().get(messageIndex).getMessage();

        messageField.setText(oldMessage);
        sendMode = SendMode.EDIT;

        toEditMessageIndex = messageIndex;
    }

    private void deleteMessage(int messageIndex) {
        ArrayList<String> data = new ArrayList<>();

        int friendID;
        if (currentChat.getUser1().getId() != App.getCurrentUser().getId()) friendID = currentChat.getUser1().getId();
        else friendID = currentChat.getUser2().getId();

        data.add(String.valueOf(friendID));
        data.add(String.valueOf(messageIndex));

        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.REMOVE_MESSAGE, data,
                                    NetworkController.getInstance().getUserToken());

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            createChatMessagesBox();
        }
    }

    private void createPageUpdater() {
        pageUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        App.updateUserInfo();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                createChatsList();
                                createChatMessagesBox();
                            }
                        });
                        Thread.sleep(1000);
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
