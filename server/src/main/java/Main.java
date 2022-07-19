import controllers.GsonHandler;
import controllers.NetworkController;

import java.io.IOException;

public class Main {
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        try {
            GsonHandler.importDataOfUser();
            NetworkController.getInstance().initializeServer(SERVER_PORT);
            NetworkController.getInstance().listenForClients();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
