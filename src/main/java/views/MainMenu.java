package views;

import java.util.ArrayList;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

import models.Game;
import models.User;

public class MainMenu extends Menu {

    private static MainMenu instance = new MainMenu();

    @Override
    protected boolean checkCommand(String command) {
        if (command.startsWith("play game")) {
            return startGame(command);
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

        ArrayList<String> playerNames = new ArrayList<>();

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
                playerNames.add(playerNameValue);
            }
        }

        ArrayList<User> users = new ArrayList<>();
        User thisUser;
        for (String username : playerNames) {
            if((thisUser=User.getUserByUsername(username)) == null) {
                System.out.println("User " + username + " does not exist");
                return false;
            }else{
                users.add(thisUser);
            }
        }
        if(users.contains(loggedInUser)){
            System.out.println("You cannot play with yourself");
            return false;
        }
        users.add(loggedInUser);
        if(users.size() < 2) {
            System.out.println("You need at least two players to play");
            return false;
        }
        GameMenu.game=new Game(users, 0);
        Menu.setCurrentMenu(GameMenu.getInstance());
        return true;
    }
    
}
