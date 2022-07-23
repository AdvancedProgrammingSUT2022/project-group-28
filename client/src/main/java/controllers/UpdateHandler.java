package controllers;

import javafx.application.Platform;
import models.Game;
import models.User;
import models.network.ServerUpdate;
import views.App;
import views.AttendGameRequestPage;

import views.GameMediator;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;

public class UpdateHandler extends Thread {
    private DataInputStream updateInputStream;
    public UpdateHandler(DataInputStream updateInputStream) {
        this.updateInputStream = updateInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ServerUpdate serverUpdate = ServerUpdate.fromJson(this.updateInputStream.readUTF());
                handleUpdates(serverUpdate);
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUpdates(ServerUpdate serverUpdate) {
        switch (serverUpdate.getUpdate()) {
            case ATTEND_GAME_REQUEST:
                handleAttendGameRequest(serverUpdate);
                break;
            case SET_INITIAL_GAME:
                handleSetInitialGame(serverUpdate);
            default:
                break;
        }
    }

    private void handleAttendGameRequest(ServerUpdate serverUpdate) {
        AttendGameRequestPage.setSender(User.fromXML(serverUpdate.getData().get(0)));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameMediator.getInstance().openAttendGameRequestPage();
            }
        });
    }

    private void handleSetInitialGame(ServerUpdate serverUpdate) {
        Game game = Game.decode(serverUpdate.getData().get(0));
        GameMenuController.setGame(game);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                App.setRoot("gamePage");
            }
        });
    }
}
