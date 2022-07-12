import views.App;
import views.Menu;

class OldMain {
    public static void main(String[] args) {
        while(true){
            Menu.getCurrentMenu().run();
        }
    }
}

public class Main{
    public static void main(String[] args) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                OldMain.main(args);
            }
        }).start();
        App.run(Main.class.getResource(""));
    }

}
