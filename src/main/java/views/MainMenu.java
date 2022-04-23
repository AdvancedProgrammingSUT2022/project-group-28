package views;

import java.util.ArrayList;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

public class MainMenu extends Menu {

    private static MainMenu instance = new MainMenu();

    @Override
    protected boolean checkCommand(String command) {
        if (command.startsWith("play game")) {
            startGame(command);
        } else if (command.equals("menu show-current")) {
            System.out.println("Main Menu");
        } else if (command.equals("menu exit")) {
            Menu.setCurrentMenu(RegisterMenu.getInstance());
            return true;
        } else if (command.startsWith("menu enter profile")){   
            ProfileMenu.setCurrentMenu(ProfileMenu.getInstance());
            return true;
        } else {
            System.out.println("Invalid command");
        }
        return false;
    }

    public static MainMenu getInstance() {
        return instance;
    }

    private boolean startGame(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<String> playerName = parser.addStringOption('p',"player");

        ArrayList<String> players = new ArrayList<>();

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return false;
        }

        while (true) {
            String playerNameValue = parser.getOptionValue(playerName);
            if (playerNameValue == null) {
                break;
            }
            else {
                players.add(playerNameValue);
            }
        }

        // TODO: start game
        return false;

    }
    
}
