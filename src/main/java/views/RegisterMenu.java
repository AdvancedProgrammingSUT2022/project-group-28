package views;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

import controllers.GsonHandler;
import controllers.RegisterMenuController;
import models.User;
import views.enums.Message;

public class RegisterMenu extends Menu {

    private static RegisterMenu instance = new RegisterMenu();

    public static RegisterMenu getInstance() {
        return instance;
    }

    @Override
    protected boolean checkCommand(String command) {
        if (command.startsWith("user login")) {
            if (login(command)) {
                MainMenu.setCurrentMenu(MainMenu.getInstance());
                return true;
            }
        } else if (command.startsWith("user create")) {
            register(command);
        } else if (command.equals("menu show-current")) {
            System.out.println("Login Menu");
        } else if (command.equals("menu exit")) {
            GsonHandler.exportDataOfUser(User.getAllUsers());
            System.exit(0);
        } else {
            System.out.println("Invalid command");
        }
        return false;
    }

    private void register(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<String> username = parser.addStringOption('u', "username");
        Option<String> password = parser.addStringOption('p', "password");
        Option<String> nickname = parser.addStringOption('n', "nickname");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        String usernameValue = (String) parser.getOptionValue(username);
        String passwordValue = (String) parser.getOptionValue(password);
        String nicknameValue = (String) parser.getOptionValue(nickname);

        if (usernameValue == null || passwordValue == null || nicknameValue == null) {
            System.out.println("Invalid command");
            return;
        }

        Message message = RegisterMenuController.checkUserRegisterData(usernameValue, passwordValue, nicknameValue);
        printRegisterMessage(message, usernameValue, nicknameValue);

    }

    private void printRegisterMessage(Message message, String username, String nickname) {
        switch (message) {
            case SUCCESS:
                System.out.println("user created successfully!");
                break;
            case USERNAME_EXISTS:
                System.out.println("user with username " + username + " already exists");
                break;
            case NICKNAME_EXISTS:
                System.out.println("user with nickname " + nickname + " already exists");
                break;
            default:
                break;
        }
    }


    private boolean login(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<String> username = parser.addStringOption('u', "username");
        Option<String> password = parser.addStringOption('p', "password");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return false;
        }

        String usernameValue = (String) parser.getOptionValue(username);
        String passwordValue = (String) parser.getOptionValue(password);

        if (usernameValue == null || passwordValue == null) {
            System.out.println("Invalid command");
            return false;
        }

        Message message = RegisterMenuController.checkUserLoginData(usernameValue, passwordValue);
        return printLoginMessage(message, usernameValue);
    }

    private boolean printLoginMessage(Message message, String username) {
        switch (message) {
            case SUCCESS:
                System.out.println("user logged in successfully!");
                RegisterMenuController.setLoggedInUser(username);
                return true;
            case LOGIN_ERROR:
                System.out.println("Username and password didn’t match!");
                break;
            default:
                break;
        }
        return false;
    }
}
