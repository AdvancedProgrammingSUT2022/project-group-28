package controllers;

import models.User;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkController {
    
    private static NetworkController instance;

    private String IPAddress;

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

    private User currentUser = null;
    private String userToken = null;
    private boolean online = false;

    public void connect(String IPAddress, int port) throws IOException {
        this.IPAddress = IPAddress;
        this.socket = new Socket(IPAddress, port);
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
    }

    public synchronized ServerResponse sendRequest(ClientRequest clientRequest) {
        try {
            this.dataOutputStream.writeUTF(clientRequest.toJson());
            this.dataOutputStream.flush();
            String response = this.dataInputStream.readUTF();
            return ServerResponse.fromJson(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startUpdateListener(String IPAddress, int port) throws IOException {
        System.out.println(IPAddress + " " + port);
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

    public void logout(){
        ArrayList<String> data = new ArrayList<>();
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.LOGOUT, data,
                                    NetworkController.getInstance().getUserToken());

        NetworkController.getInstance().sendRequest(clientRequest);
    }

    public User getCurrentUser() { return currentUser; }
    
    public String getUserToken() { return userToken; }
    
    public boolean isOnline() { return online; }
    
    public void setCurrentUser(User user) { this.currentUser = user; }
    
    public void setUserToken(String userToken) { this.userToken = userToken; }

    public void setOnline(boolean online) { this.online = online; }
    
    public String getIPAddress() {
        return IPAddress;
    }
    
    
}
