package controllers;

import models.ServerUpdate;
import models.User;
import models.WaitingGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NetworkController {
    private static NetworkController instance;
    private NetworkController() {}
    public static NetworkController getInstance() {
        if (instance == null) instance = new NetworkController();
        return instance;
    }

    private ServerSocket serverSocket;

    private HashMap<String, User> loggedInUsers = new HashMap<>();
    private ArrayList<WaitingGame> waitingGames = new ArrayList<>();

    public void initializeServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

        public void listenForClients() throws IOException {
       while (true) {
           Socket socket = serverSocket.accept();
           new SocketHandler(socket).start();
       }
    }

    public void sendUserUpdate(User user, ServerUpdate serverUpdate) throws IOException {
        user.getUpdateOutputStream().writeUTF(serverUpdate.toJson());
    }

    public HashMap<String, User> getLoggedInUsers() { return loggedInUsers; }

    public ArrayList<WaitingGame> getWaitingGames() { return waitingGames; }
}
