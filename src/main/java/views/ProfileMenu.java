package views;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

public class ProfileMenu extends Menu {
    private static ProfileMenu instance = new ProfileMenu();

    public static ProfileMenu getInstance() {
        return instance;
    }

    @Override
    protected boolean checkCommand(String command) {
        if (command.startsWith("profile change") && command.contains("-n")) {
            changeNickname(command);
        }else if (command.startsWith("profile change") && command.contains("-p")) {
            changePassword(command);
        } else if (command.equals("menu show-current")) {
            System.out.println("Main Menu");
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
        Option<String> nickname = parser.addStringOption('n',"nickname");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        String nicknameValue = (String) parser.getOptionValue(nickname);

        if(nicknameValue == null){
            System.out.println("Invalid command");
            return;
        }

        // TODO : change nickname
    }

    private void changePassword(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> password = parser.addBooleanOption('p',"password");
        Option<String> currentPassword = parser.addStringOption('p',"password");
        Option<String> newPassword = parser.addStringOption('n',"new");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        boolean passwordValue = (Boolean) parser.getOptionValue(password);
        String currentPasswordValue = (String) parser.getOptionValue(currentPassword);
        String newPasswordValue = (String) parser.getOptionValue(newPassword);

        if(passwordValue == false || currentPasswordValue == null || newPasswordValue == null){
            System.out.println("Invalid command");
            return;
        }

        // TODO : change nickname
    }

    

    
}
