package views;

import java.util.ArrayList;
import java.util.HashMap;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.*;

import controllers.CivilizationController;
import controllers.GameController;
import controllers.GameMenuController;
import controllers.UnitController;
import models.Game;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.units.Settler;
import models.units.Worker;
import views.enums.Color;
import views.enums.Message;

public class GameMenu extends Menu {
    private static GameMenu instance = new GameMenu();
    private final int BOARD_WIDTH=113;
    private final int BOARD_HEIGHT=28;


    public static GameMenu getInstance() {
        return instance;
    }

    @Override 
    protected boolean checkCommand(String command){
        if (command.startsWith("select unit combat")) {
            selectCombatUnit(command);
        } else if (command.startsWith("select unit noncombat")) {
            selectNoncombatUnit(command);
        } else if (command.startsWith("unit moveto")) {
            moveUnit(command);
        } else if (command.startsWith("map show")) {
            showMap(command);
        } else if (command.startsWith("next turn")) {
            nextTurn();
        } else if(command.equals("menu exit")){
            Menu.setCurrentMenu(MainMenu.getInstance());
            return true;
        }else {
            System.out.println("invalid command");
        }
        return true;
    }

    private String[][] makeBoardGrid(int baseI , int baseJ){
        String[][] grid = new String[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                grid[i][j]=" ";
            }
        }

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 4; i++) {
                int tileI = baseI + i - j/2;
                int tileJ = baseJ + j - 5;
                ArrayList<String> hex= fillHexData(tileI, tileJ);
                int m=0,n=0;
                for (int k = 0; k < hex.size(); k++) {
                    int x = 10 * j + n;
                    int y = 6 * i + (j % 2) * 3 + m;
                    if(hex.get(k).equals("\n")){
                        m++;
                        n=0;
                        continue;
                    }
                    if(!hex.get(k).equals(" ") && !(hex.get(k).equals("_")&& !grid[y][x].equals(" ")))
                        grid[y][x] = hex.get(k);

                    n++;
                }
            }
        }    
        return grid;    
    }

    private ArrayList<String> fillHexData(int i, int j) {
        Game game = GameController.getGame();
        String template ="   _______\n"  // 0 - 13
                       + "  /##C#M##\\\n" // 14 - 26
                       + " /##II,JJ##\\\n" // 27 - 39
                       + "/##FFFFFFF##\\\n" // 40 - 52
                       + "\\##RRRRRRR##/\n" // 53 - 65 
                       + " \\##TT#####/\n" // 66 - 78
                       + "  \\_______/";  // 79 - 92
        template = template.replace("II", String.format("%02d", i));
        template = template.replace("JJ", String.format("%02d", j));
        HashMap<Tile,Integer> discoveredTiles = game.getCurrentPlayer().getDiscoveredTiles();
        ArrayList<String> colors;
        Tile tile=null;
        int lastDiscovery=0;
        for (Tile tempTile : discoveredTiles.keySet()) {
            if (tempTile.getCoordinates()[0] == i && tempTile.getCoordinates()[1] == j) {
                tile=tempTile;
                lastDiscovery=discoveredTiles.get(tempTile);
            }
        }
        if(tile!=null){    
            if(tile.getTerrainFeature()!=null)
                template = template.replace("FFFFFFF", tile.getTerrainFeature().getMapSign());
            else
                template = template.replace("FFFFFFF", "#######");
            if(tile.getResource()!=null)
                template = template.replace("RRRRRRR", tile.getResource().getResourceTemplate().getMapSign());
            else
                template = template.replace("RRRRRRR", "#######");
            if(tile.getCivilian() instanceof Worker)
                template = template.replace("C", "W");
            else if(tile.getCivilian() instanceof Settler)
                template = template.replace("C", "S");
            else
                template = template.replace("C", "#");
            template = template.replace("TT", String.format("%02d", lastDiscovery));
            colors= getHexColors(template, i, j);
        }else if (i < game.MAP_HEIGHT && i >= 0 && j < game.MAP_WIDTH && j >= 0) {
            template ="   _______\n"  // 0 - 11
                    + "  /#######\\\n" // 14 - 26
                    + " /##II,JJ##\\\n" // 27 - 39
                    + "/###########\\\n" // 40 - 52
                    + "\\###########/\n" // 53 - 65 
                    + " \\#########/\n" // 66 - 78
                    + "  \\_______/";  // 79 - 92
            template = template.replace("II", String.format("%02d", i));
            template = template.replace("JJ", String.format("%02d", j));
            colors= getOutHexColors(template);
        }else{
            template ="   _______\n"  // 0 - 11
                    + "  /#######\\\n" // 14 - 26
                    + " /#########\\\n" // 27 - 39
                    + "/###########\\\n" // 40 - 52
                    + "\\###########/\n" // 53 - 65 
                    + " \\#########/\n" // 66 - 78
                    + "  \\_______/";  // 79 - 92
            colors= getOutHexColors(template);
        }   
        return colors;
    }

    private ArrayList<String> getOutHexColors(String template) {
        ArrayList<String> colors=new ArrayList<>();
        for (int k = 0; k < template.length(); k++) {
            colors.add(String.valueOf(template.charAt(k)));       
        }
        return colors;
    }

    ArrayList<String> getHexColors(String template,int i,int j){
        Game game=GameController.getGame();
        Color hexColor = getHexColor(game.getMap()[i][j]);
        ArrayList<String> colors=new ArrayList<>();

        for (int k = 0; k < template.length(); k++) {
            switch(template.charAt(k)){
                case '\n':
                case ' ':
                    colors.add(String.valueOf(template.charAt(k)));
                    break;
                case '/':
                    if(k<50 && game.getMap()[i][j].getRivers().contains(Direction.LEFT))
                        colors.add(Color.BLUE_BACKGROUND_BRIGHT+String.valueOf(template.charAt(k))+Color.RESET);
                    else if(game.getMap()[i][j].getRivers().contains(Direction.RIGHT))   
                        colors.add(Color.BLUE_BACKGROUND_BRIGHT+String.valueOf(template.charAt(k))+Color.RESET);
                    else
                        colors.add(String.valueOf(template.charAt(k)));
                    break;
                case '\\':
                    if(k<50 && game.getMap()[i][j].getRivers().contains(Direction.UP_RIGHT))
                        colors.add(Color.BLUE_BACKGROUND_BRIGHT+String.valueOf(template.charAt(k))+Color.RESET);
                    else if(game.getMap()[i][j].getRivers().contains(Direction.DOWN_LEFT))
                        colors.add(Color.BLUE_BACKGROUND_BRIGHT+String.valueOf(template.charAt(k))+Color.RESET);        
                    else
                        colors.add(String.valueOf(template.charAt(k)));
                    break;
                case '_':
                    if(k<50 && game.getMap()[i][j].getRivers().contains(Direction.UP))
                        colors.add(Color.BLUE_BACKGROUND_BRIGHT+String.valueOf(template.charAt(k))+Color.RESET);
                    else if(game.getMap()[i][j].getRivers().contains(Direction.DOWN))
                        colors.add(Color.BLUE_BACKGROUND_BRIGHT+String.valueOf(template.charAt(k))+Color.RESET);    
                    else if(k<50)
                        colors.add(String.valueOf(template.charAt(k)));
                    else 
                        colors.add(hexColor+String.valueOf(template.charAt(k))+Color.RESET);
                    break;
                default:
                    colors.add(hexColor+String.valueOf(template.charAt(k))+Color.RESET);
                    break;
            }
        }
        return colors;
    }

    private void drawBoard(int baseI, int baseJ) {
        String[][] grid= makeBoardGrid(baseI , baseJ);
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BOARD_HEIGHT; i++) {
            for(int j = 0; j < BOARD_WIDTH; j++) {
                builder.append(grid[i][j].replace("#", " "));
            }
            builder.append('\n');
        }
        builder.setLength(builder.length()-1);
        System.out.println("turn number: "+GameController.getGame().getTurnNumber());
        System.out.println(builder.toString());
    }

    private Color getHexColor(Tile tile) {
        return tile.getTerrain().getColor();
    }

    private void showMap(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> tileI = parser.addIntegerOption('i', "tileI");
        Option<Integer> tileJ = parser.addIntegerOption('j', "tileJ");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        Integer tileIValue =  parser.getOptionValue(tileI);
        Integer tileJValue =  parser.getOptionValue(tileJ);
        if(tileJValue==null || tileIValue==null) {
            System.out.println("invalid command");
            return;
        }
        CivilizationController.updateDiscoveredTiles();
        drawBoard(tileIValue, tileJValue);
     }

    private void selectCombatUnit(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> unitI = parser.addIntegerOption('i', "unitI");
        Option<Integer> unitJ = parser.addIntegerOption('j', "unitJ");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        Integer unitIValue = parser.getOptionValue(unitI);
        Integer unitJValue = parser.getOptionValue(unitJ);
        if(unitIValue==null || unitJValue==null) {
            System.out.println("Invalid command");
            return;
        }
        Message result = UnitController.selectCombatUnit(unitIValue, unitJValue);

        switch (result) {
            case INVALID_POSITION:
                System.out.println("invalid position");
                break;
            case NO_COMBAT_UNIT:
                System.out.println("no combat unit");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void selectNoncombatUnit(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> unitI = parser.addIntegerOption('i', "unitI");
        Option<Integer> unitJ = parser.addIntegerOption('j', "unitJ");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        Integer unitIValue = parser.getOptionValue(unitI);
        Integer unitJValue = parser.getOptionValue(unitJ);

        if(unitIValue==null || unitJValue==null) {
            System.out.println("Invalid command");
            return;
        }

        Message result = UnitController.selectNonCombatUnit(unitIValue, unitJValue);
        switch (result) {
            case INVALID_POSITION:
                System.out.println("Invalid position");
                break;
            case NO_NONCOMBAT_UNIT:
                System.out.println("No noncombat unit");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;  
        }
    }

    private void moveUnit(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> tileI = parser.addIntegerOption('i', "tileI");
        Option<Integer> tileJ = parser.addIntegerOption('j', "tileJ");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        Integer tileIValue = parser.getOptionValue(tileI);
        Integer tileJValue = parser.getOptionValue(tileJ);

        if(tileIValue==null || tileJValue==null) {
            System.out.println("invalid command");
            return;
        }

        Message result = UnitController.moveUnitToTarget(tileIValue, tileJValue);
        switch (result) {
            case INVALID_POSITION:
                System.out.println("invalid positon");
                break;
            case NO_SELECTED_UNIT:
                System.out.println("no selected unit");
                break;
            case NO_PERMISSION:
                System.out.println("no permission");
                break;
            case SAME_TILE:
                System.out.println("same tile");
                break;
            case NOT_ACCESSIBLE_TILE:
                System.out.println("no accessible tile");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void nextTurn() {
        Message result = GameMenuController.nextTurn();

        switch (result) {
            case FAILURE:
                System.out.println("something is wrong");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

}
