package views;

import java.util.Scanner;

import models.User;

public abstract class Menu {
    protected User loggedInUser;
    protected final Scanner scanner = new Scanner(System.in);
    private static Menu currentMenu = RegisterMenu.getInstance();

    public void run(){
        while (true){
            String line = scanner.nextLine();
            if(checkCommand(line)){
                break;
            }
        }
    }

    public static Menu getCurrentMenu() {
        return currentMenu;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        Menu.currentMenu = currentMenu;
    }

    protected abstract boolean checkCommand(String command);

    public final User getLoggedInUser() {
        return loggedInUser;
    }

    public final void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
