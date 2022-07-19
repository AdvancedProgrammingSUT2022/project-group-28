import controllers.NetworkController;
import views.App;
import views.Menu;

import java.io.IOException;

class OldMain {
    public static void main(String[] args) {
        while(true){
            Menu.getCurrentMenu().run();
        }
    }
}

public class Main{
    public static void main(String[] args) {
        try {
            NetworkController.getInstance().connect("localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        new Thread(new Runnable(){
            @Override
            public void run() {
                OldMain.main(args);
            }
        }).start();
        App.run(Main.class.getResource(""));
    }

}
