package views;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;
import controllers.ProfileMenuController;
import views.enums.Message;

public class ProfileMenu extends Menu {
    private static ProfileMenu instance = new ProfileMenu();

    public static ProfileMenu getInstance() {
        return instance;
    }

    @Override
    protected boolean checkCommand(String command) {
        if (command.startsWith("profile change") && command.contains("-p")) {
            changePassword(command);
        } else if (command.startsWith("profile change") && command.contains("-n")) {
            changeNickname(command);
        }  else if (command.equals("menu show-current")) {
            System.out.println("Profile Menu");
        } else if (command.equals("menu exit")) {
            Menu.setCurrentMenu(MainMenu.getInstance());
            return true;
        } else {
            System.out.println("Invalid command");
        }
        return false;
    }

    private void changeNickname(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<String> nickname = parser.addStringOption('n', "nickname");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        String nicknameValue = (String) parser.getOptionValue(nickname);

        if (nicknameValue == null) {
            System.out.println("Invalid command");
            return;
        }

        Message message = ProfileMenuController.changeNickname(nicknameValue);
        printChangeNicknameMessage(message, nicknameValue);

    }

    private void printChangeNicknameMessage(Message message, String nickname) {
        switch (message) {
            case SUCCESS:
                System.out.println("nickname changed successfully!");
                break;
            case CHANGE_NICKNAME_ERROR:
                System.out.println("user with nickname " + nickname + " already exists");
            default:
                break;
        }

    }

    private void changePassword(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> password = parser.addBooleanOption('p', "password");
        Option<String> currentPassword = parser.addStringOption('c', "current");
        Option<String> newPassword = parser.addStringOption('n', "new");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        boolean passwordValue = (Boolean) parser.getOptionValue(password);
        String currentPasswordValue = (String) parser.getOptionValue(currentPassword);
        String newPasswordValue = (String) parser.getOptionValue(newPassword);

        if (passwordValue == false || currentPasswordValue == null || newPasswordValue == null) {
            System.out.println("Invalid command");
            return;
        }

        Message message = ProfileMenuController.changePassword(currentPasswordValue, newPasswordValue);
        printChangePasswordMessage(message);
    }

    private void printChangePasswordMessage(Message message) {
        switch (message) {
            case SUCCESS:
                System.out.println("password changed successfully!");
                break;
            case INCORRECT_PASSWORD:
                System.out.println("current password is invalid");
                break;
            case REPETITIOUS_PASSWORD:
                System.out.println("please enter a new password");
                break;
            default:
                break;
        }
    }


}
