package controllers;

import models.ClientRequest;
import models.ServerResponse;
import models.ServerUpdate;
import models.User;
import views.enums.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;

public class SocketHandler extends Thread {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientRequest clientRequest = ClientRequest.fromJson(this.dataInputStream.readUTF());
                ServerResponse serverResponse = handleRequest(clientRequest);
                this.dataOutputStream.writeUTF(serverResponse.toJson());
                this.dataOutputStream.flush();

                if (clientRequest.getRequest() == ClientRequest.Request.START_LISTEN) break;
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ServerResponse handleRequest(ClientRequest clientRequest) {
        ArrayList<String> data = clientRequest.getData();
        switch (clientRequest.getRequest()) {
            case REGISTER:
                return handleRegister(clientRequest);
            case LOGIN:
                return handleLogin(clientRequest);
            case START_LISTEN:
                return handleStartListen(clientRequest);
            case GET_USER_INFO:
                return handleGetUserInfo(clientRequest);
            case REQUEST_FRIENDSHIP:
                return handleFriendship(clientRequest);
            case ACCEPT_FRIENDSHIP:
                return handleAcceptFriendship(clientRequest);
            default:
                return null;
        }
    }

    private ServerResponse handleRegister(ClientRequest clientRequest) {
        ArrayList<String> data = clientRequest.getData();
        ArrayList<String> toSend = new ArrayList<>();

        Message message = RegisterMenuController.checkUserRegisterData(data.get(0), data.get(1), data.get(2));
        switch (message) {
            case USERNAME_EXISTS:
                return new ServerResponse(ServerResponse.Response.USERNAME_EXISTS, toSend);
            case NICKNAME_EXISTS:
                return new ServerResponse(ServerResponse.Response.NICKNAME_EXISTS, toSend);
            case SUCCESS:
                return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
            default:
                return null;
        }
    }

    private ServerResponse handleLogin(ClientRequest clientRequest) {
        ArrayList<String> data = clientRequest.getData();
        ArrayList<String> toSend = new ArrayList<>();

        Message message = RegisterMenuController.checkUserLoginData(data.get(0), data.get(1));
        if (message == Message.SUCCESS) {
            String token = UUID.randomUUID().toString();
            User user = User.getUserByUsername(data.get(0));
            NetworkController.getInstance().getLoggedInUsers().put(token, user);

            toSend.add(token);
            toSend.add(user.toXML());

            return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
        } else {
            return new ServerResponse(ServerResponse.Response.LOGIN_ERROR, toSend);
        }
    }

    private ServerResponse handleStartListen(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        if (user.getUpdateSocket() != null) {
            return new ServerResponse(ServerResponse.Response.IS_LISTENING, toSend);
        }

        user.setUpdateSocket(this.socket);
        user.setUpdateInputStream(this.dataInputStream);
        user.setUpdateOutputStream(this.dataOutputStream);

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleGetUserInfo(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        toSend.add(user.toXML());
        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleFriendship(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        User friend = User.getUserByNickname(clientRequest.getData().get(0));
        if (friend == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_NICKNAME, toSend);
        }

        if (user.getFriends().contains(friend)) {
            return new ServerResponse(ServerResponse.Response.ALREADY_FRIEND, toSend);
        }

        for (String token : NetworkController.getInstance().getLoggedInUsers().keySet()) {
            if (NetworkController.getInstance().getLoggedInUsers().get(token).equals(friend)) {
                ArrayList<String> updateData = new ArrayList<>();
                updateData.add(user.getNickname());
                ServerUpdate serverUpdate = new ServerUpdate(ServerUpdate.Update.FRIENDSHIP_REQUEST, updateData);
                try {
                    NetworkController.getInstance().sendUserUpdate(friend, serverUpdate);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
            }
        }

        return new ServerResponse(ServerResponse.Response.IS_OFFLINE, toSend);
    }

    private ServerResponse handleAcceptFriendship(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        User friend = User.getUserByNickname(clientRequest.getData().get(0));
        if (friend == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_NICKNAME, toSend);
        }

        user.acceptFriendship(friend);
        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }
}
