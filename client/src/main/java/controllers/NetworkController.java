package controllers;

import models.ClientRequest;
import models.ServerResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkController {
    private static NetworkController instance;
    private NetworkController() {}
    public static NetworkController getInstance() {
        if (instance == null) instance = new NetworkController();
        return instance;
    }

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private Socket updateSocket;
    private DataInputStream updateInputStream;
    private DataOutputStream updateOutputStream;

    private String userToken = null;
    private boolean online = false;

    public void connect(String IPAddress, int port) throws IOException {
        this.socket = new Socket(IPAddress, port);
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
    }

    public ServerResponse sendRequest(ClientRequest clientRequest) {
        try {
            this.dataOutputStream.writeUTF(clientRequest.toJson());
            String response = this.dataInputStream.readUTF();
            return ServerResponse.fromJson(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startUpdateListener(String IPAddress, int port) throws IOException {
        this.updateSocket = new Socket(IPAddress, port);
        this.updateInputStream = new DataInputStream(this.updateSocket.getInputStream());
        this.updateOutputStream = new DataOutputStream(this.updateSocket.getOutputStream());

        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.START_LISTEN, new ArrayList<>(), this.userToken);
        this.updateOutputStream.writeUTF(clientRequest.toJson());

        ServerResponse serverResponse = ServerResponse.fromJson(this.updateInputStream.readUTF());

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            new UpdateHandler(this.updateInputStream).start();
        }
    }

    public String getUserToken() { return userToken; }

    public boolean isOnline() { return online; }

    public void setUserToken(String userToken) { this.userToken = userToken; }

    public void setOnline(boolean online) { this.online = online; }
}
