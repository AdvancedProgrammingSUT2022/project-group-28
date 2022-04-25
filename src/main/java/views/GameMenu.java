package views;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.*;
import controllers.GameController;
import controllers.UnitController;
import models.Game;
import models.tiles.Tile;
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
        }
        return true;
    }

    private String[][] makeBoardGrid(int baseI , int baseJ){
        Game game = GameController.getGame();
        Tile[][] map = game.getMap();
        System.out.println(map[0][1].getTerrain().getName());
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
                String hex= fillHexData(tileI, tileJ);
                String[] lines = hex.split("\n");
                for (int m = 0; m < lines.length; m++) {
                    String content = lines[m];
                    for (int n = 0; n < content.length(); n++) {
                        int x = 10 * j + n;
                        int y = 6 * i + (j % 2) * 3 + m;
                        
                        // Only override empty spaces
                        if (content.charAt(n)!=' ' && content.charAt(n)!='/' && content.charAt(n)!='\\' && (i!=0 || m!=0))
                            if (tileI < game.MAP_HEIGHT && tileI >= 0 && tileJ < game.MAP_WIDTH && tileJ >= 0)
                                grid[y][x]=getHexColor(map[tileI][tileJ])+String.valueOf(content.charAt(n))+"\u001B[0m";
                            else grid[y][x] = Color.RED_BACKGROUND + String.valueOf(content.charAt(n))+"\u001B[0m";
                        else if (content.charAt(n)!=' ')
                             grid[y][x]=String.valueOf(content.charAt(n));
                    }
                }
            }
        }    
        return grid;    
    }

    private String fillHexData(int i, int j) {
        Game game = GameController.getGame();
        String template ="   _______\n"  // 0 - 13
                       + "  /##C#M##\\\n" // 14 - 26
                       + " /##II,JJ##\\\n" // 27 - 39
                       + "/##FFFFFFF##\\\n" // 40 - 52
                       + "\\###########/\n" // 53 - 65 
                       + " \\#####RR##/\n" // 66 - 78
                       + "  \\_______/";  // 79 - 92
        template = template.replace("II", String.format("%02d", i));
        template = template.replace("JJ", String.format("%02d", j));

        if (i < game.MAP_HEIGHT && i >= 0 && j < game.MAP_WIDTH && j >= 0) {
            Tile tile = game.getMap()[i][j];
            if(tile.getTerrainFeature()!=null)
                template = template.replace("FF", tile.getTerrainFeature().getName().substring(0, 2));
            else
                template = template.replace("FF", "--");
            if(tile.getCivilian() instanceof Worker)
                template = template.replace("C", "W");
            else if(tile.getCivilian() instanceof Settler)
                template = template.replace("C", "S");
            else
                template = template.replace("C", "-");
        }
        return template;
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

        int tileIValue = (int) parser.getOptionValue(tileI);
        int tileJValue = (int) parser.getOptionValue(tileJ);
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

        int unitIValue = (int) parser.getOptionValue(unitI);
        int unitJValue = (int) parser.getOptionValue(unitJ);

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

        int unitIValue = (int) parser.getOptionValue(unitI);
        int unitJValue = (int) parser.getOptionValue(unitJ);

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

        int tileIValue = (int) parser.getOptionValue(tileI);
        int tileJValue =  (int) parser.getOptionValue(tileJ);

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
            case SUCCESS:
                System.out.println("success");
                break;
        }
    }

}
