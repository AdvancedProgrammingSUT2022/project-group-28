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
        App.run(Main.class.getResource(""));
        OldMain.main(args);
    }

}
