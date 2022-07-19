package controllers;

import models.ClientRequest;
import models.ServerResponse;
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
                return handleRegister(data);
            case LOGIN:
                return handleLogin(data);
            default:
                return null;
        }
    }

    private ServerResponse handleRegister(ArrayList<String> data) {
        Message message = RegisterMenuController.checkUserRegisterData(data.get(0), data.get(1), data.get(2));
        ArrayList<String> toSend = new ArrayList<>();
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

    private ServerResponse handleLogin(ArrayList<String> data) {
        Message message = RegisterMenuController.checkUserLoginData(data.get(0), data.get(1));
        ArrayList<String> toSend = new ArrayList<>();
        if (message == Message.SUCCESS) {
            String token = UUID.randomUUID().toString();
            toSend.add(token);
            User user = User.getUserByUsername(data.get(0));
            NetworkController.getInstance().getLoggedInUsers().put(token, user);
            System.out.println("hi " + user.getUsername());
            return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
        } else {
            return new ServerResponse(ServerResponse.Response.LOGIN_ERROR, toSend);
        }
    }
}
