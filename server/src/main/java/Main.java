import controllers.NetworkController;
import controllers.XMLHandler;

import java.io.IOException;

public class Main {
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        try {
            XMLHandler.importDataFromDatabase();
            NetworkController.getInstance().initializeServer(SERVER_PORT);
            NetworkController.getInstance().listenForClients();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
