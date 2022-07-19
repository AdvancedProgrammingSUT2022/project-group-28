package controllers;

import models.ClientRequest;
import models.ServerResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    private String userToken = null;

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

    public String getUserToken() { return userToken; }

    public void setUserToken(String userToken) { this.userToken = userToken; }
}
