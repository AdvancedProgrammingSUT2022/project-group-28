package controllers;

import javafx.application.Platform;
import models.Game;
import models.Trade;
import models.User;
import models.Trade.Result;
import models.network.ServerUpdate;
import views.*;
import views.components.MessageBox;
import views.components.MessageBox.Type;

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
            case UPDATE_GAME:
                handleUpdateGame(serverUpdate);
                break;
            case INVITE_GAME_REQUEST:
                handleInviteGameRequest(serverUpdate);
                break;
            case IN_GAME_MESSAGE:
                handleInGameMessage(serverUpdate);
                break;
            case TRADE_REQUEST:
                handleTradeRequest(serverUpdate);
                break;
            case TRADE_RESULT:
                handleTradeResult(serverUpdate);
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

    private void handleUpdateGame(ServerUpdate serverUpdate) {
        Game game = Game.decode(serverUpdate.getData().get(0));
        GameMenuController.setGame(game);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GamePage.getInstance().updateGamePage();
            }
        });
    }


    private void handleInviteGameRequest(ServerUpdate serverUpdate) {
        InviteGameRequestPage.setSender(User.fromXML(serverUpdate.getData().get(0)));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameMediator.getInstance().openInviteGameRequestPage();
            }
        });
    }

    private void handleInGameMessage(ServerUpdate serverUpdate) {
        String message = serverUpdate.getData().get(0) + " | " + serverUpdate.getData().get(1);

        MessageBox.Type type;
        if (serverUpdate.getData().get(2).equals("INFO")) {
            type = MessageBox.Type.INFO;
        } else if (serverUpdate.getData().get(2).equals("ALERT")) {
            type = MessageBox.Type.ALERT;
        } else type = MessageBox.Type.WARNING;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HUDController.getInstance().addMessage(message, type);
            }
        });
    }

    private void handleTradeRequest(ServerUpdate serverUpdate){
        String message = serverUpdate.getData().get(1);
        Platform.runLater(new Runnable() {
            @Override 
            public void run(){
                HUDController.getInstance().addMessage(message, Type.INFO,"Accept","Reject");
            }
        });
    }

    private void handleTradeResult(ServerUpdate serverUpdate){
        String sender = serverUpdate.getData().get(0);
        String message = serverUpdate.getData().get(1);
        String result = serverUpdate.getData().get(2);

        for (Trade trade:GameController.getGame().getTrades()){
            if (trade.getMessage().equals(message) && trade.getSeller().getUser().getNickname().equals(sender)){
                if (result.equals("Y")){
                    trade.setResult(Result.ACCEPT);
                    trade.getSeller().addTrade(trade);
                    trade.getCustomer().addTrade(trade);
                }

                GameController.getGame().getTrades().remove(trade);
                break;

            }
        }
    }
}
