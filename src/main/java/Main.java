import views.Menu;

public class Main {
    public static void main(String[] args) {
        while(true){
            Menu.getCurrentMenu().run();
        }
    }
}
