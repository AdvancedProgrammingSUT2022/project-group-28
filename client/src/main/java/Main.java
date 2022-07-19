import controllers.NetworkController;
import views.App;

import java.io.IOException;

public class Main{
    public static void main(String[] args) {
        try {
            NetworkController.getInstance().connect("localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        App.run(Main.class.getResource(""));
    }

}
