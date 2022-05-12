package views;

import java.util.ArrayList;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

import controllers.GameController;
import controllers.GameMenuController;
import controllers.GsonHandler;
import models.Game;
import models.User;
import models.civilization.Civilization;

public class MainMenu extends Menu {

    private static MainMenu instance = new MainMenu();

    @Override
    protected boolean checkCommand(String command) {
        if (command.startsWith("play game")) {
            return startGame(command);
        } else if (command.equals("load game")) {
            return loadGame();
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
        Option<Integer> seed = parser.addIntegerOption('s',"seed");

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

        int seedValue = parser.getOptionValue(seed,0);

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
        GameMenuController.startNewGame(users, seedValue);
        Civilization startPlayer = GameController.getGame().getCurrentPlayer();
        System.out.println("it is " + startPlayer.getUser().getNickname() + "'s turn");
        Menu.setCurrentMenu(GameMenu.getInstance());
        return true;
    }

    private boolean loadGame() {
        Game game = GsonHandler.importGame();
        if(game == null){
            System.out.println("No game to load");
            return false;
        }
        GameMenuController.setGame(game);
        Menu.setCurrentMenu(GameMenu.getInstance());
        return true;
    }
}
