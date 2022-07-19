package views;

import java.util.Scanner;

import models.User;

public abstract class Menu {
    protected static User loggedInUser;
    private static Menu currentMenu = RegisterMenu.getInstance();
    protected final Scanner scanner = new Scanner(System.in);

    public static Menu getCurrentMenu() {
        return currentMenu;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        Menu.currentMenu = currentMenu;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        Menu.loggedInUser = loggedInUser;
    }

    public void run() {
        Menu oldMenu = currentMenu;
        while (true) {
            String line = scanner.nextLine();
            if (checkCommand(line) || oldMenu != currentMenu) {
                break;
            }
        }
    }

    protected abstract boolean checkCommand(String command);
}
