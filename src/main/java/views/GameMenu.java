package views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.*;

import controllers.*;
import controllers.units.SettlerController;
import controllers.units.UnitController;
import controllers.units.WorkerController;
import models.Constructable;
import models.Game;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.Technology;
import models.civilization.enums.BuildingTemplate;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.tiles.enums.ImprovementTemplate;
import models.units.Ranged;
import models.units.Settler;
import models.units.Unit;
import models.units.Worker;
import models.units.enums.UnitState;
import models.units.enums.UnitTemplate;
import views.enums.*;
import views.notifications.CivilizationNotification;
import views.notifications.GameNotification;

public class GameMenu extends Menu {
    private static GameMenu instance = new GameMenu();
    private final int BOARD_WIDTH=113;
    private final int BOARD_HEIGHT=28;

    @Override
    protected boolean checkCommand(String command){
        if (command.startsWith("select unit combat")) {
            selectCombatUnit(command);
        } else if (command.startsWith("select unit noncombat")) {
            selectNoncombatUnit(command);
        } else if (command.startsWith("unit moveto")) {
            moveUnit(command);
        }else if (command.startsWith("study technology")) {
            studyTechnology();
        } else if (command.startsWith("unit found city")) {
            foundCity();
        } else if (command.startsWith("unit build")) {
            buildImprovement();
        } else if (command.startsWith("unit sleep")) {
            unitSleep();
        } else if (command.startsWith("unit alert")) {
            unitAlert();
        } else if (command.startsWith("unit wake")) {
            unitWake();
        } else if (command.startsWith("unit fortify")) {
            unitFortify();
        } else if (command.startsWith("unit heal")) {
            unitHeal();
        } else if (command.startsWith("unit pillage")) {
            unitPillage();
        } else if (command.startsWith("select city")) {
            selectCity(command);
        } else if (command.startsWith("city assign citizen")) {
            assignCitizen(command);
        } else if (command.startsWith("city free citizen")) {
            freeCitizen(command);
        } else if (command.startsWith("map show")) {
            showMap(command);
        } else if (command.startsWith("next turn")) {
            nextTurn();
        } else if (command.startsWith("city buy tile")) {
            buyTile();
        } else if (command.startsWith("city construct")) {
            cityConstruct();
        } else if(command.equals("city info")){
            showCityInfo();
        } else if(command.equals("civilization info")){
            showCivilizationInfo();
        } else if(command.equals("research panel")){
            showResearchPanel();
        } else if(command.equals("cities panel")){
            showCitiesPanel();
        } else if(command.equals("units panel")){
            showUnitsPanel();
        } else if(command.equals("economic overview")){
            showEconomicOverview();
        }else if(command.equals("military overview")){
            showMilitaryOverview();
        }else if(command.startsWith("cheat increase gold")){
            increaseGold(command);
        } else if (command.startsWith("cheat next turn")) {
            cheatNextTurn(command);
        } else if (command.startsWith("cheat move unit")) {
            cheatMoveUnit(command);
        } else if (command.startsWith("cheat next player")) {
            cheatNextPlayer();
        } else if(command.equals("city buy unit")){
            buyUnit();
        } else if(command.startsWith("unit attack")){
            unitAttack(command);
        } else if(command.equals("unit info")){
            showUnitInfo();
        } else if(command.equals("unit prepare")){
            prepareUnit();
        } else if(command.equals("cheat heal unit")) {
            cheatHealUnit();
        } else if(command.equals("cheat recharge mp")) {
            cheatRechargeMP();
        } else if(command.equals("save game")){
            saveGame();
        } else if (command.startsWith("notification info")) {
            notificationInfo(command);
        } else if (command.startsWith("tile info")) {
            tileInfo(command);
        } else if (command.startsWith("city attack")) {
            cityAttack(command);
        } else if (command.startsWith("cheat get technology")) {
            cheatGetTechnology();
        } else if (command.startsWith("unit delete")) {
            deleteUnit();
        } else if (command.startsWith("unit free")) {
            freeUnit();
        } else if (command.startsWith("menu show-current")) {
            System.out.println("this is game menu");
        } else if(command.equals("menu exit")){
            Menu.setCurrentMenu(MainMenu.getInstance());
            return true;            
        } else {
            System.out.println("invalid command");
        }
        return true;
    }

    public static GameMenu getInstance() {
        return instance;
    }

    private String[][] makeBoardGrid(int baseI , int baseJ, boolean fogOfWar){
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
                ArrayList<String> hex= fillHexData(tileI, tileJ, fogOfWar);
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

    private ArrayList<String> fillHexData(int i, int j, boolean fogOfWar) {
        Game game = GameController.getGame();
        String template ="   _______\n"  // 0 - 13
                       + "  /#C#QQQ#\\\n" // 14 - 26
                       + " /##II,JJ##\\\n" // 27 - 39
                       + "/#FFF#W#VVV#\\\n" // 40 - 52
                       + "\\##RRRoiiiD#/\n" // 53 - 65
                       + " \\#TTT#MMM#/\n" // 66 - 78
                       + "  \\_______/";  // 79 - 92
        template = template.replace("II", String.format("%02d", i));
        template = template.replace("JJ", String.format("%02d", j));
        HashMap<Tile,Integer> discoveredTiles = game.getCurrentPlayer().getDiscoveredTiles();
        ArrayList<String> colors;
        Tile tile=null;
        int lastDiscovery=0;
        if(fogOfWar){
            for (Tile tempTile : discoveredTiles.keySet()) {
                if (tempTile.getCoordinates()[0] == i && tempTile.getCoordinates()[1] == j) {
                    tile=tempTile;
                    lastDiscovery=discoveredTiles.get(tempTile);
                }
            }
        }else{
            tile=game.getMap()[i][j];
            lastDiscovery = game.getTurnNumber();
        }
        if(tile!=null){    
            if(tile.getTerrainFeature()!=null)
                template = template.replace("FFF", tile.getTerrainFeature().getMapSign());
            else
                template = template.replace("FFF", "###");

            if (tile.getCity() != null)
                template = template.replace("QQQ", tile.getCity().getNAME());
            else template = template.replace("QQQ", "###");

            if (tile.getProject() != null) {
                template = template.replace("iii", tile.getProject().getImprovement().getName().substring(0, 3));
                template = template.replace("D", "-");
            } else if (tile.getImprovement() != null) {
                template = template.replace("iii", tile.getImprovement().getName().substring(0, 3));
                template = template.replace("D", "+");
            } else template = template.replace("iiiD", "####");

            if (tile.isRoadConstructed()) {
                template = template.replace("o", "*");
            } else template = template.replace("o", "#");

            if (tile.isWorking())
                template = template.replace("W", "*");
            else template = template.replace("W", "#");

            if(tile.getCivilian() instanceof Worker)
                template = template.replace("C", "W");
            else if(tile.getCivilian() instanceof Settler)
                template = template.replace("C", "S");
            else
                template = template.replace("C", "#");

            if(tile.getMilitary()!=null)
                template = template.replace("MMM", tile.getMilitary().getUnitTemplate().getName().substring(0, 3).toLowerCase());
            else
                template = template.replace("MMM", "###");

            if(TileController.getTileCivilization(tile)!=null)
                template = template.replace("VVV", TileController.getTileCivilization(tile).getCivilizationNames().getMapSign());
            else
                template = template.replace("VVV", "###");
            template = template.replace("TTT", String.format("%03d", lastDiscovery%1000));
            template = template.replace("RRR", GameMenuController.getTileShowableResource(tile, game.getCurrentPlayer(),!fogOfWar));
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

    private void drawBoard(int baseI, int baseJ, boolean fogOfWar) {
        String[][] grid= makeBoardGrid(baseI , baseJ,fogOfWar);
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
        Option<Boolean> cheat = parser.addBooleanOption('c', "cheat");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        Integer tileIValue =  parser.getOptionValue(tileI);
        Integer tileJValue =  parser.getOptionValue(tileJ);
        Boolean cheatValue =  !parser.getOptionValue(cheat, false);

        if(tileJValue==null || tileIValue==null) {
            System.out.println("invalid command");
            return;
        }
        CivilizationController.updateDiscoveredTiles();
        drawBoard(tileIValue, tileJValue, cheatValue);
        while (true) {
            System.out.print("\n> ");
            String command2 = scanner.nextLine();
            if(command2.equals("q"))
                return;
            else if(command2.equals("w")) 
                tileIValue=Math.max(tileIValue - 1, 0);
            else if(command2.equals("a")) 
                tileJValue=Math.max(tileJValue - 1, 0);
            else if(command2.equals("s")) 
                tileIValue=Math.min(tileIValue + 1, 100);
            else if(command2.equals("d"))
                tileJValue=Math.min(tileJValue + 1, 100);
            else {
                System.out.println("invalid command");
                continue;
            }
            drawBoard(tileIValue, tileJValue, cheatValue);
        }
     }

    private void selectCombatUnit(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> unitI = parser.addIntegerOption('i', "unitI");
        Option<Integer> unitJ = parser.addIntegerOption('j', "unitJ");
        Option<Boolean> cheatMode = parser.addBooleanOption('c', "cheat");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        Integer unitIValue = parser.getOptionValue(unitI);
        Integer unitJValue = parser.getOptionValue(unitJ);
        Boolean cheatModeValue = parser.getOptionValue(cheatMode, false);
        if(unitIValue==null || unitJValue==null) {
            System.out.println("Invalid command");
            return;
        }
        UnitMessage result = UnitController.selectCombatUnit(unitIValue, unitJValue, cheatModeValue);

        switch (result) {
            case INVALID_POSITION:
                System.out.println("invalid position");
                break;
            case NOT_COMBAT_UNIT:
                System.out.println("no combat unit");
                break;
            case NO_PERMISSION:
                System.out.println("you just can select your units");
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
        Option<Boolean> cheatMode = parser.addBooleanOption('c', "cheat");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Invalid command");
            return;
        }

        Integer unitIValue = parser.getOptionValue(unitI);
        Integer unitJValue = parser.getOptionValue(unitJ);
        Boolean cheatModeValue = parser.getOptionValue(cheatMode, false);

        if(unitIValue==null || unitJValue==null) {
            System.out.println("Invalid command");
            return;
        }

        UnitMessage result = UnitController.selectNonCombatUnit(unitIValue, unitJValue, cheatModeValue);
        switch (result) {
            case INVALID_POSITION:
                System.out.println("Invalid position");
                break;
            case NOT_NONCOMBAT_UNIT:
                System.out.println("No noncombat unit");
                break;
            case NO_PERMISSION:
                System.out.println("you just can select your units");
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

        UnitMessage result = UnitController.moveUnitToTarget(tileIValue, tileJValue);
        switch (result) {
            case INVALID_POSITION:
                System.out.println("invalid position");
                break;
            case NO_SELECTED_UNIT:
                System.out.println("no selected unit");
                break;
            case NOT_PLAYERS_TURN:
                System.out.println("no permission");
                break;
            case SAME_TARGET_TILE:
                System.out.println("same tile");
                break;
            case NOT_ACCESSIBLE_TILE:
                System.out.println("no accessible tile");
                break;
            case FULL_TARGET_TILE:
                System.out.println("target is full");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void studyTechnology(){
        CivilizationMessage message = TechnologyController.checkTechnologyStudyPossible();
        switch (message){
            case CHANGE_CURRENT_STUDY_TECHNOLOGY:
                System.out.println("you have a current study technology...\nChange technology to study:");
                printListOfTechnology();
                break;
            case SHOW_LIST:
                System.out.println("Choose a technology to study:");
                printListOfTechnology();
            default:
                break;
        }

    }

    private void printListOfTechnology(){
        String out = TechnologyController.printPossibleTechnology();
        System.out.println(out);
        chooseNumber();
    }

    private void chooseNumber(){
        System.out.println("Enter the number of technology:");
        while (true){
            String input = scanner.nextLine().trim();
            CivilizationMessage message = TechnologyController.checkNumber(input);
            switch (message){
                case SUCCESS:
                    System.out.println("Your study on "
                            + TechnologyController.getGame().getCurrentPlayer().getCurrentStudyTechnology().getTechnologyTemplate().getName()
                            + " technology began");

                    return;
                case OUT_OF_RANGE:
                    System.out.println("your number is out of range");
                    break;
                case INVALID_INPUT:
                    System.out.println("invalid number");
                    break;
                default:
                    break;
            }
        }
    }

    private void nextTurn() {
        GameNotification nextTurnCheck = GameMenuController.nextTurn();

        if (nextTurnCheck.getNotificationTemplate() != CivilizationNotification.SUCCESS) {
            System.out.println(nextTurnCheck);

            switch ((CivilizationNotification)nextTurnCheck.getNotificationTemplate()) {
                case FREE_UNIT:
                    int unitI = Integer.parseInt(nextTurnCheck.getData().get(0));
                    int unitJ = Integer.parseInt(nextTurnCheck.getData().get(1));
                    CivilizationController.updateDiscoveredTiles();
                    drawBoard(unitI, unitJ, true);
                    break;
                case NO_CONSTRUCTION:
                    String cityName = nextTurnCheck.getData().get(0);
                    CityController.selectCityByName(cityName, false);
                    Tile cityTile = GameController.getGame().getSelectedCity().getTile();
                    int cityI = cityTile.getCoordinates()[0];
                    int cityJ = cityTile.getCoordinates()[1];
                    CivilizationController.updateDiscoveredTiles();
                    drawBoard(cityI, cityJ, true);
                    break;
                default:
                    break;
            }
        }

        if (nextTurnCheck.getNotificationTemplate() != CivilizationNotification.SUCCESS) return;

        GameMenuController.startNewTurn();

        String nickname = GameController.getGame().getCurrentPlayer().getUser().getNickname();
        System.out.println("it is " + nickname + "'s turn");

        Civilization civilization = GameController.getGame().getCurrentPlayer();
        for (int i = civilization.getGameNotifications().size() - 1; i >= 0 ; i--) {
            GameNotification gameNotification = civilization.getGameNotifications().get(i);
            if (gameNotification.isRead()) break;
            gameNotification.setRead(true);
            System.out.println(gameNotification.toString());
            System.out.println("********************************");
        }

    }

    private void foundCity() {
        UnitMessage message = SettlerController.foundCity();
        switch (message) {
            case NO_SELECTED_UNIT:
                System.out.println("no selected unit");
                break;
            case NOT_PLAYERS_TURN:
                System.out.println("not player's turn");
                break;
            case NOT_SETTLER_UNIT:
                System.out.println("not settler unit");
                break;
            case NEAR_CITY_BOARDERS:
                System.out.println("near city borders");
                break;
            case NEAR_ENEMY_UNITS:
                System.out.println("near enemy units");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    public void selectCity(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<String> cityName = parser.addStringOption('n', "name");
        Option<Integer> cityI = parser.addIntegerOption('i', "positionI");
        Option<Integer> cityJ = parser.addIntegerOption('j', "positionJ");
        Option<Boolean> cheatMode = parser.addBooleanOption('c', "cheat");
        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }
        String cityNameValue = parser.getOptionValue(cityName);
        Integer cityIValue = parser.getOptionValue(cityI);
        Integer cityJValue = parser.getOptionValue(cityJ);
        Boolean cheatModeValue = parser.getOptionValue(cheatMode, false);

        if (cityNameValue != null) {
            CityMessage result = CityController.selectCityByName(cityNameValue, cheatModeValue);
            switch (result) {
                case INVALID_NAME:
                    System.out.println("invalid name");
                    break;
                case NO_PERMISSION:
                    System.out.println("you just can select your city");
                    break;
                case SUCCESS:
                    System.out.println("success");
                    break;
                default:
                    break;
            }
        } else if (cityIValue != null && cityJValue != null) {
            CityMessage result = CityController.selectCityByPosition(cityIValue, cityJValue, cheatModeValue);
            switch (result) {
                case INVALID_POSITION:
                    System.out.println("invalid position");
                    break;
                case NOT_CITY:
                    System.out.println("there is not city there");
                    break;
                case NO_PERMISSION:
                    System.out.println("you just can select your city");
                    break;
                case SUCCESS:
                    System.out.println("success");
                    break;
                default:
                    break;
            }
        } else System.out.println("invalid command");
    }

    public void assignCitizen(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> startI = parser.addIntegerOption('i', "tileI");
        Option<Integer> startJ = parser.addIntegerOption('j', "tileJ");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        int startIValue = parser.getOptionValue(startI);
        int startJValue = parser.getOptionValue(startJ);
        CityMessage result = CityController.assignCitizen(startIValue, startJValue);

        switch (result) {
            case INVALID_POSITION:
                System.out.println("Invalid position");
                break;
            case NO_SELECTED_CITY:
                System.out.println("no selected city");
                break;
            case NO_PERMISSION:
                System.out.println("you don't own the city");
                break;
            case NOT_CITY_TILE:
                System.out.println("tile is not a city tile");
                break;
            case WORKING_TILE:
                System.out.println("the tile already has citizen");
                break;
            case NO_FREE_CITIZEN:
                System.out.println("you don't have any free citizens");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    public void freeCitizen(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> startI = parser.addIntegerOption('i', "tileI");
        Option<Integer> startJ = parser.addIntegerOption('j', "tileJ");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        int startIValue = parser.getOptionValue(startI);
        int startJValue = parser.getOptionValue(startJ);

        CityMessage result = CityController.freeCitizen(startIValue, startJValue);

        switch (result) {
            case INVALID_POSITION:
                System.out.println("Invalid position");
                break;
            case NO_SELECTED_CITY:
                System.out.println("no selected city");
                break;
            case NO_PERMISSION:
                System.out.println("you don't own the city");
                break;
            case NOT_CITY_TILE:
                System.out.println("tile is not a city tile");
                break;
            case NOT_WORKING_TILE:
                System.out.println("the tile has no citizen");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void showCityInfo(){
        if(GameController.getGame().getSelectedCity() == null){
            System.out.println("no selected city");
            return;
        }else{ 
            City city = GameController.getGame().getSelectedCity();
            System.out.println("*****************************");
            System.out.println("City name: " + city.getNAME());
            System.out.println("City owner: " + city.getCivilization().getCivilizationNames());
            System.out.println("City coordinates: " + city.getTile().getCoordinates()[0] + "," + city.getTile().getCoordinates()[1]);
            System.out.println("City population: " + city.getPopulation());
            System.out.println("City number of citizens: " + city.getCitizens());
            System.out.println("City food balance: " + city.getFoodBalance());
            System.out.println("City production balance: " + city.getProductionBalance());
            System.out.println("City growth bucket: " + city.getGrowthBucket());
            System.out.println("City hit point: " + city.getHitPoint());
            System.out.println("*****************************");
        }
    }

    private void buyTile() {
        City city = GameController.getGame().getSelectedCity();
        if (city == null) {
            System.out.println("you have to select a city first");
            return;
        }
        ArrayList<Tile> availableTiles = CityController.getAvailableTilesToBuy(city);


        while (true) {
            for (int i = 0; i < availableTiles.size(); i++) {
                Tile tile = availableTiles.get(i);
                System.out.printf("%d- %d,%d: %d\n", i + 1, tile.getCoordinates()[0],
                        tile.getCoordinates()[1], CityController.getTileValue(city, tile));
            }
            System.out.println("choose a tile to buy: (q for exit)");
            String input = scanner.nextLine();
            if (input.startsWith("q")) return;
            int choice = Integer.parseInt(input);
            if (choice <= 0 || choice > availableTiles.size()) {
                System.out.println("invalid choice");
                continue;
            }
            CityMessage result = CityController.buyTile(city, availableTiles.get(choice - 1));
            switch (result) {
                case NO_PERMISSION:
                    System.out.println("you don't own this city");
                    break;
                case NOT_ENOUGH_GOLD:
                    System.out.println("you don't have enough gold");
                    break;
                case SUCCESS:
                    System.out.println("success");
                    return;
                default:
                    break;
            }
        }
    }

    private void showCivilizationInfo() {
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        System.out.println("**************************************");
        System.out.println("Civilization name: " + civilization.getCivilizationNames().getName());
        if(civilization.getCurrentCapital()!=null)
            System.out.println("Civilization capital: " + civilization.getCurrentCapital().getNAME());
        System.out.println("Civilization gold: " + civilization.getGold());
        System.out.println("Civilization gold balance: " + civilization.getGoldBalance());
        System.out.println("Civilization happiness: " + civilization.getHappiness());
        System.out.println("Civilization science balance: " + civilization.getScienceBalance());
        System.out.print("Civilization cities: ");
        for (City city : civilization.getCities()) {
            System.out.print(city.getNAME()+"\t");
        }
        System.out.println();
        System.out.print("Civilization technologies: ");
        for (Technology technology : civilization.getStudiedTechnologies()) {
            System.out.print(technology.getTechnologyTemplate().getName() + " : " + 
                            (String.format("%.2f",technology.getProgress()/(float)technology.getTechnologyTemplate().getCost()*100) +"%\t" ));
        }
        System.out.println();
        if(civilization.getCurrentStudyTechnology()!=null)
        System.out.println("current studying technology: "+ civilization.getCurrentStudyTechnology().getTechnologyTemplate().getName() + " : " + 
                           (civilization.getCurrentStudyTechnology().getProgress()/(float)civilization.getCurrentStudyTechnology().getTechnologyTemplate().getCost())*100+"%");
        System.out.println("**************************************");
    }

    private void showResearchPanel(){
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        if (civilization.getCurrentStudyTechnology() != null) {
            System.out.println("# your current study : " + civilization.getCurrentStudyTechnology().getTechnologyTemplate().getName()
                    + " \t"
                    + civilization.getCurrentStudyTechnology().getProgress() + " of "
                    + civilization.getCurrentStudyTechnology().getTechnologyTemplate().getCost());
            System.out.println("  If you finish this study you will get :");
            System.out.println("## UNITS :\n" +
                    TechnologyController.extractTheObtainedUnits(civilization.getCurrentStudyTechnology().getTechnologyTemplate()) +
                    "## BUILDINGS :\n" +
                    TechnologyController.extractTheObtainedBuildings(civilization.getCurrentStudyTechnology().getTechnologyTemplate()));
        }
        System.out.println("# Civilization technologies: ");
        for (Technology technology : civilization.getStudiedTechnologies()) {
            System.out.println(" -> " + technology.getTechnologyTemplate().getName() + " : "
                    + technology.getProgress() + " of " + technology.getTechnologyTemplate().getCost());
        }
        if (civilization.getStudiedTechnologies().size() == 0) System.out.println("  nothing");

    }

    private void showCitiesPanel() {
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        if(civilization.getCities().size() != 0){
            System.out.println("your capital : ");
            System.out.println(" -> Population: " + civilization.getCurrentCapital().getPopulation() + "  |  Name: "
                     + civilization.getCurrentCapital().getNAME() + "  |  Combat strength : " + civilization.getCurrentCapital().getCombatStrength());
            System.out.println("Civilization another cities : ");
            for (City city: civilization.getCities()) {
                if(!civilization.getCities().contains(civilization.getCurrentCapital()) && civilization.getCities().size() > 1){
                    System.out.println(" -> " + city.getPopulation() + "  |  " + city.getNAME() + "  |  " + city.getCombatStrength());
                }
                else {
                    System.out.println("nothing :(");
                }
            }
        }
        else {
            System.out.println("nothing :(");
        }

    }

    private void showUnitsPanel(){
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        System.out.println("Your units: ");
        for (Unit unit: civilization.getUnits()) {
            System.out.println(" -> Name: " + unit.getUnitTemplate().getName() +
                    "   |   Move state: " + UnitController.getMoveUnitState(unit.getMovePoint(),unit.getUnitTemplate().getMovementPoint()));
        }
    }
    
    private void showEconomicOverview(){
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        City  capitalCity= civilization.getCurrentCapital();
        //TODO: check science for city ....
        //TODO check get production for city
        if(civilization.getCities().size() != 0){
            System.out.println("your capital : ");
            System.out.println(" -> Population: " + capitalCity.getPopulation() + " | Name: "
                     + capitalCity.getNAME() + " | Combat strength: " + capitalCity.getCombatStrength() +
                    " | Food: " + capitalCity.getFoodStore() + " | Science: " + capitalCity.getPopulation() +
                    " | Gold: " + CityController.getCityGoldBalance(capitalCity) + " | Production: " + capitalCity.getProductionBalance() );
            System.out.println("Civilization another cities : ");
            for (City city: civilization.getCities()) {
                if(!civilization.getCities().contains(capitalCity) && civilization.getCities().size() > 1){
                    System.out.println(" -> Population: " + city.getPopulation() + " | Name: " + city.getNAME() + " | Combat strength: " + city.getCombatStrength() +
                            " | Food: " + city.getFoodStore() + " | Science: " + city.getPopulation() + " | Gold: " + CityController.getCityGoldBalance(city)
                             + " | Production: " + city.getProductionBalance());
                }
                else {
                    System.out.println("nothing :(");
                }
            }
        }
        else {
            System.out.println("nothing :(");
        }

    }

    private void showMilitaryOverview(){
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        System.out.println("Your units: ");
        String range = "";
        for (Unit unit: civilization.getUnits()) {
            if(unit instanceof Ranged){
                range = Integer.toString(((Ranged) unit).getRangedCombatStrength());
            }
            else{
                range = "null" ;
            }
            System.out.println(" -> Name: " + unit.getUnitTemplate().getName() + " | Status: " + unit.getUnitState() +
                    " | Move point: " + unit.getMovePoint()+ " / " + unit.getUnitTemplate().getMovementPoint() +
                    " | Move state: " + UnitController.getMoveUnitState(unit.getMovePoint(),unit.getUnitTemplate().getMovementPoint()) +
                    " | Combat strength: " + unit.getCombatStrength() + " | Ranged combat strength: " + range);
        }
        if(civilization.getUnits().size() == 0){
            System.out.println("nothing :(");
        }

    }
    
    private void increaseGold(String command){
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> amount = parser.addIntegerOption('a', "amount");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        Integer goldValue = parser.getOptionValue(amount);

        if(goldValue == null){
            System.out.println("invalid command");
            return;
        }

        Civilization civilization = GameController.getGame().getCurrentPlayer();
        civilization.setGold(civilization.getGold() + goldValue);

        System.out.println("gold increased successfully");
    }

    private void buyUnit(){
        Game game = GameController.getGame();
        if(game.getSelectedCity()==null){
            System.out.println("you have to select a city first");
            return;
        }
        LinkedHashMap<UnitTemplate,String> availableUnitTemplates = CivilizationController.getAvailableUnitTemplates(game.getCurrentPlayer(), game.getSelectedCity().getTile());
        
        for (int i = 0; i < availableUnitTemplates.keySet().size(); i++) {
            UnitTemplate unitTemplate = (UnitTemplate) availableUnitTemplates.keySet().toArray()[i];
            String message = availableUnitTemplates.get(unitTemplate);
            if (message==null)
                System.out.printf("%d- %s: %d\n", i+1, unitTemplate.getName(), unitTemplate.getCost());
            else
                System.out.printf(Color.BLACK_BRIGHT + "%d- %s: %d  -  %s\n" + Color.RESET, i+1, unitTemplate.getName(),unitTemplate.getCost(), message);
        }
        while(true){
            System.out.println("choose a unit to buy: (q for exit)");
            System.out.print("> ");
            String input = scanner.nextLine();
            if(input.equals("q")) return;
            if(!input.matches("\\d+")) {
                System.out.println("invalid command");
                continue;
            }
            int choice = Integer.parseInt(input);
            if(choice<=0 || choice>availableUnitTemplates.size()){
                System.out.println("invalid choice");
                continue;
            }
            UnitTemplate unitTemplate = (UnitTemplate) availableUnitTemplates.keySet().toArray()[choice-1];
            if (availableUnitTemplates.get(unitTemplate)!=null){
                System.out.println("you can't buy this unit" + availableUnitTemplates.get(unitTemplate));
                continue;
            }
            CivilizationController.buyUnit(game.getCurrentPlayer(), game.getSelectedCity().getTile(), unitTemplate);
            System.out.println("purchase successful.");
            return;
        }
    }

    public void buildImprovement() {
        UnitMessage checkToBuild = WorkerController.checkImprovementIsPossibleToBuild();
        switch (checkToBuild) {
            case NO_SELECTED_UNIT:
                System.out.println("no selected unit");
                break;
            case NOT_WORKER_UNIT:
                System.out.println("not unit worker");
                break;
            case NO_MOVE_POINT:
                System.out.println("you don't have enough move point");
                break;
            case CITY_TILE:
                System.out.println("you can't build improvement on city tile");
                break;
            case NOT_PLAYER_TILE:
                System.out.println("not player tile");
                break;
            case SUCCESS:
                Worker worker = (Worker) GameController.getGame().getSelectedUnit();
                ArrayList<ImprovementTemplate> possibleImprovements = TileController.getTilePossibleImprovements(worker.getTile());
                while (true) {
                    for (int i = 0; i < possibleImprovements.size(); i++) {
                        ImprovementTemplate improvement = possibleImprovements.get(i);
                        System.out.println(i + 1 + "-" + improvement.getName() + ": " + improvement.getTurnCost());
                    }
                    System.out.println("(q for exit)");
                    String input = scanner.nextLine();
                    if (input.startsWith("q")) break;
                    if (!input.matches("\\d+")) {
                        System.out.println("invalid input");
                        continue;
                    }
                    int choice = Integer.parseInt(input);
                    if (choice <= 0 || choice > possibleImprovements.size()) {
                        System.out.println("invalid number");
                        continue;
                    }
                    ImprovementTemplate selectedImprovement = possibleImprovements.get(choice - 1);
                    WorkerController.startImprovement(worker, selectedImprovement);
                    System.out.println("Success");
                    break;
                }
                break;
            default:
                break;
        }
    }

    private void cheatNextTurn(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> count = parser.addIntegerOption('c', "count");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        Integer countValue = parser.getOptionValue(count);
        if (countValue == null) {
            System.out.println("invalid command");
            return;
        }
        CheatController instance = CheatController.getInstance();
        instance.nextTurnCheat(GameController.getGame(), countValue);
        System.out.println("success");
    }

    private void cityConstruct() {
        City city = GameController.getGame().getSelectedCity();
        if (city == null) {
            System.out.println("no selected city");
            return;
        }
        LinkedHashMap<Constructable, CityMessage> allConstructions = CityController.getConstructableConstructions(city);
        ArrayList<Constructable> constructionTemplates = new ArrayList<>(allConstructions.keySet());
        for (int i = 0; i < constructionTemplates.size(); i++) {
            Constructable constructable = constructionTemplates.get(i);
            if (constructable instanceof UnitTemplate) {
                UnitTemplate unitTemplate = (UnitTemplate) constructable;
                switch (allConstructions.get(constructable)) {
                    case REQUIRED_TECHNOLOGY:
                        String string = Color.BLACK_BRIGHT.toString() + (i + 1) + "- " + unitTemplate.getName() +
                                "| requires tech: ";
                        if (unitTemplate.getRequiredTechnology() != null)
                            string = string + unitTemplate.getRequiredTechnology().getName();
                        System.out.println(string);
                        break;
                    case REQUIRED_RESOURCE:
                        System.out.println(Color.BLACK_BRIGHT.toString() + (i + 1) + "- " + unitTemplate.getName() +
                                "| requires resource: " + unitTemplate.getRequiredResource().getName());
                        break;
                    case CITY_NOT_GREW:
                        System.out.println(Color.BLACK_BRIGHT.toString() + (i + 1) + "- " + unitTemplate.getName() +
                                "| city is not grown");
                        break;
                    case UNHAPPY_PEOPLE:
                        System.out.println(Color.BLACK_BRIGHT.toString() + (i + 1) + "- " + unitTemplate.getName() +
                                "| unhappy people");
                        break;
                    case SUCCESS:
                        System.out.println( (i + 1) + "- " + unitTemplate.getName());
                        break;
                    default:
                        break;
                }

                System.out.printf("%s", Color.RESET.toString());
            } else if (constructable instanceof BuildingTemplate) {

            }
        }

        System.out.println("(q for exit)");

        while (true) {
            String input = scanner.nextLine();
            if (input.startsWith("q")) break;
            if (!input.matches("\\-?\\d+")) {
                System.out.println("invalid input");
                continue;
            }
            int choice = Integer.parseInt(input);
            if (choice <= 0 || choice > constructionTemplates.size()) {
                System.out.println("invalid choice");
                continue;
            }
            Constructable construction = constructionTemplates.get(choice - 1);
            CityController.startConstructing(city, construction);
            System.out.println("success");
            break;
        }

    }

    public void unitAttack(String command){
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
        
        if(tileJValue==null || tileIValue==null) {
            System.out.println("invalid command");
            return;
        }
        
        CombatMessage message = CombatController.unitAttack(tileIValue, tileJValue, this);
        combatMessagePrinter(message);
    }
    
    private void combatMessagePrinter(CombatMessage message) {
        switch (message) {
            case INVALID_POSITION:
                System.out.println("invalid position");
                break;
            case NO_COMBATABLE:
                System.out.println("no combatable unit in destination tile.");
                break;
            case NOT_VISIBLE_TILE:
                System.out.println("destination tile is not visible.");
                break;
            case NO_UNIT_SELECTED:
                System.out.println("please select a unit first.");
                break;
            case OUT_OF_RANGE:
                System.out.println("destination tile is out of unit range.");
                break;
            case CANNOT_ATTACK_YOURSELF:
                System.out.println("you cannot attack yourself.");
                break;
            case SUCCESS:
                System.out.println("attack successfully.");
                break;
            case NEEDS_MELEE_UNIT:
                System.out.println("only melee units can conquer or destroy a city.");
                break;
            case NO_MOVE_POINT:
                System.out.println("you don't have enough move points.");
                break;
            case NOT_PREPARED:
                System.out.println("you have not prepared your siege unit.");
                break;
            case CITY_ATTACHED:
                System.out.println("city attached to your civilization.");
                break;
            case CITY_DESTROYED:
                System.out.println("city destroyed.");
                break;
            case CANNOT_ATTACK_TWICE:
                System.out.println("you cannot attack twice in one turn.");
                break;
            case NO_CITY_SELECTED:
                System.out.println("no city selected.");
                break;
        }
    }

    public void cheatMoveUnit(String command) {
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

        if(tileJValue==null || tileIValue==null) {
            System.out.println("invalid command");
            return;
        }

        UnitMessage result = CheatController.getInstance().moveUnitCheat(GameController.getGame(), tileIValue, tileJValue);
        switch (result) {
            case NO_SELECTED_UNIT:
                System.out.println("no selected unit");
                break;
            case INVALID_POSITION:
                System.out.println("invalid positions");
                break;
            case NOT_ACCESSIBLE_TILE:
                System.out.println("not accessible tile");
                break;
            case SAME_TARGET_TILE:
                System.out.println("same target tile");
                break;
            case FULL_TARGET_TILE:
                System.out.println("full target tile");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void showUnitInfo() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit == null) {
            System.out.println("no selected unit");
            return;
        }else{
            System.out.println("*****************************");
            System.out.println("unit name: " + unit.getUnitTemplate().getName());
            System.out.println("unit type: " + unit.getUnitTemplate().getUnitType());
            System.out.println("unit mp: " + unit.getMovePoint());
            System.out.println("unit move target: " + ((unit.getMoveTarget()!=null)?(unit.getMoveTarget().getCoordinates()[0] + " , " + unit.getMoveTarget().getCoordinates()[1]):"none"));
            System.out.println("unit health: " + unit.getHealth());
            System.out.println("unit coordinates: " + unit.getTile().getCoordinates()[0] + " , " + unit.getTile().getCoordinates()[1]);
            System.out.println("unit raw combat strength: " + unit.getCombatStrength());
            System.out.println("unit combat strength: " + CombatController.getCombatStrength(unit, false));
            if(unit instanceof Ranged){
                System.out.println("unit range: " + ((Ranged) unit).getUnitTemplate().getRange());
                System.out.println("unit raw ranged combat strength: " + ((Ranged) unit).getRangedCombatStrength());
                System.out.println("unit ranged combat strength: " + CombatController.getCombatStrength((Ranged) unit, true));
            }
            System.out.println("unit state :" + unit.getUnitState());
            System.out.println("*****************************");
        }
    }

    private void prepareUnit(){
        switch(UnitController.prepareUnit()){
            case SUCCESS:
                System.out.println("unit prepared");
                break;
            case NO_SELECTED_UNIT:
                System.out.println("no selected unit");
                break;
            case NO_SIEGE_UNIT:
                System.out.println("unit is not siege unit");
                break;
            default:
                break;   
        }

    }

    public boolean destroyOrAttachCity(){
        System.out.println("city is conquered. do you want to destroy it or attach it?");
        System.out.println("1. destroy");
        System.out.println("2. attach");
        while(true){
            System.out.print(">");
            String line = scanner.nextLine();
            if(line.equals("1"))return true;
            else if(line.equals("2"))return false;
            else System.out.println("invalid command");
        }
    }

    private void cheatNextPlayer() {
        CheatController instance = CheatController.getInstance();
        instance.nextPlayerCheat(GameController.getGame());
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        System.out.println("success");

        System.out.println("it is " + civilization.getUser().getNickname() + "'s turn");

        for (int i = civilization.getGameNotifications().size() - 1; i >= 0 ; i--) {
            GameNotification gameNotification = civilization.getGameNotifications().get(i);
            if (gameNotification.isRead()) break;
            gameNotification.setRead(true);
            System.out.println(gameNotification.toString());
            System.out.println("*********************");
        }
    }

    private void cheatHealUnit(){
        Unit unit = GameController.getGame().getSelectedUnit();
        if(unit==null){
            System.out.println("no selected unit");
            return;
        }
        else unit.setHealth(10);
        System.out.println("successfully healed");
    }

    private void cheatRechargeMP(){
        Unit unit = GameController.getGame().getSelectedUnit();
        if(unit==null){
            System.out.println("no selected unit");
            return;
        }
        else unit.setMovePoint(unit.getUnitTemplate().getMovementPoint());
        System.out.println("successfully recharged");
    }

    private void notificationInfo(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> count = parser.addIntegerOption('c', "count");
        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        Integer countValue = parser.getOptionValue(count, 5);
        if (countValue == null) {
            System.out.println("Invalid command");
        }
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        for (int i = civilization.getGameNotifications().size() - 1; i >= 0; i--) {
            GameNotification gameNotification = civilization.getGameNotifications().get(i);
            gameNotification.setRead(true);
            if (countValue > 0) {
                System.out.println("Turn " + gameNotification.getTurnNumber() + ":");
                System.out.println(gameNotification);
                System.out.println("****************************");
            }
            countValue--;
        }
    }

    private void saveGame() {
        if(GsonHandler.saveGame(GameController.getGame()))
            System.out.println("game saved.");
        else System.out.println("failed to save game.");
    }

    private void unitSleep() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit == null) {
            System.out.println("There is no selected unit");
            return;
        }
        UnitMessage result = UnitController.sleepUnit(unit);
        switch (result) {
            case IS_SLEPT:
                System.out.println("unit already slept");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void unitAlert() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit == null) {
            System.out.println("There is no selected unit");
            return;
        }

        UnitMessage result = UnitController.alertUnit(unit);
        switch (result) {
            case NOT_COMBAT_UNIT:
                System.out.println("noncombat unit can not alert");
                break;
            case IS_ALERT:
                System.out.println("unit already alert");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void unitWake() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit == null) {
            System.out.println("There is no selected unit");
            return;
        }

        UnitMessage result = UnitController.wakeUnit(unit);
        switch (result) {
            case IS_AWAKE:
                System.out.println("Unit is awake already");
                break;
            case SUCCESS:
                System.out.println("Success");
                break;
            default:
                break;
        }
    }

    private void tileInfo(String command) {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> tileI = parser.addIntegerOption('i', "tileI");
        Option<Integer> tileJ = parser.addIntegerOption('j', "tileJ");
        Option<Boolean> cheat = parser.addBooleanOption('c', "cheat");

        try {
            parser.parse(command.split(" "));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("invalid command");
            return;
        }

        Integer tileIValue =  parser.getOptionValue(tileI);
        Integer tileJValue =  parser.getOptionValue(tileJ);
        Boolean cheatValue =  !parser.getOptionValue(cheat, false);

        if(tileJValue==null || tileIValue==null) {
            System.out.println("invalid command");
            return;
        }
        Game game = GameController.getGame();
        if (tileIValue < 0 || tileIValue >= game.MAP_HEIGHT || tileJValue < 0 || tileJValue >= game.MAP_WIDTH){
            System.out.println("invalid position");
            return;
        }
        CivilizationController.updateDiscoveredTiles();
        Tile tile = null;
        int lastSeen = 0;
        if(cheatValue){
            for (Map.Entry<Tile,Integer> entry : game.getCurrentPlayer().getDiscoveredTiles().entrySet()) {
                if(tileIValue==entry.getKey().getCoordinates()[0] &&
                tileJValue==entry.getKey().getCoordinates()[1]){
                    tile = entry.getKey();
                    lastSeen = entry.getValue();
                    break;
                }  
            }
        }else{
            tile = game.getMap()[tileIValue][tileJValue];
            lastSeen = game.getTurnNumber();
        }
        if(tile == null) {
            System.out.println("tile is not visible");
            return;
        }

        System.out.println("*****************************");
        System.out.println("tile coordinates: " + tile.getCoordinates()[0] + "," + tile.getCoordinates()[1]);
        System.out.println("tile last discovery: " + lastSeen);
        System.out.println("tile terrain: " + tile.getTerrain().getName());
        System.out.println("tile terrain feature: " + ((tile.getTerrainFeature()!=null)?tile.getTerrainFeature().getName():"none"));
        System.out.println("tile resource: " + ((tile.getResource()!=null)?tile.getResource().getResourceTemplate().getName():"none"));
        System.out.println("tile improvement: " + ((tile.getImprovement()!=null)?tile.getImprovement().getName():"none"));
        System.out.println("tile next improvement: " + ((tile.getNextImprovement()!=null)?tile.getNextImprovement().getName():"none"));;
        System.out.println("tile project: " + ((tile.getProject()!=null)?(tile.getProject().getImprovement() + " , spent turns :"+ tile.getProject().getSpentTurns()):"none"));
        System.out.println("tile civilization: " + ((tile.getCivilization()!=null)?tile.getCivilization().getCivilizationNames():"none"));
        System.out.println("tile military unit: " + ((tile.getMilitary()!=null)?tile.getMilitary().getUnitTemplate().getName() + " , health : "+ tile.getMilitary().getHealth():"none"));
        System.out.println("tile civilian unit: " + ((tile.getCivilian()!=null)?tile.getCivilian().getUnitTemplate().getName() + " , health : "+ tile.getCivilian().getHealth():"none"));
        System.out.println("tile city: " + ((tile.getCity()!=null)?tile.getCity().getNAME()+" , hit point : "+tile.getCity().getHitPoint():"none"));
        System.out.println("*****************************");
    }

    public void cityAttack(String command){
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
        
        if(tileJValue==null || tileIValue==null) {
            System.out.println("invalid command");
            return;
        }
        
        CombatMessage message = CombatController.cityAttack(tileIValue, tileJValue);
        combatMessagePrinter(message);
    }

    private void unitFortify() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit == null) {
            System.out.println("no selected unit");
            return;
        }
        UnitMessage result = UnitController.fortifyUnit(unit);
        switch (result) {
            case NOT_COMBAT_UNIT:
                System.out.println("not combat unit");
                break;
            case IS_FORTIFYING:
                System.out.println("unit already fortifying");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void unitHeal() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit == null) {
            System.out.println("no selected unit");
            return;
        }
        UnitMessage result = UnitController.healUnit(unit);
        switch (result) {
            case FULL_HEALTH:
                System.out.println("unit health is full");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void unitPillage() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit == null) {
            System.out.println("no selected unit");
            return;
        }

        UnitMessage result = CombatController.pillageTile(unit);
        switch (result) {
            case NOT_COMBAT_UNIT:
                System.out.println("not combat unit");
                break;
            case NO_MOVE_POINT:
                System.out.println("unit don't have move point");
                break;
            case NO_IMPROVEMENT:
                System.out.println("there is no improvement on tile");
                break;
            case SAME_SIDE:
                System.out.println("you can not pillage your improvements");
                break;
            case SUCCESS:
                System.out.println("success");
                break;
            default:
                break;
        }
    }

    private void cheatGetTechnology() {
        System.out.println(TechnologyController.printAllRemainingTechnology());
        System.out.println("Enter the number of technology:");
        while (true){
            String input = scanner.nextLine().trim();
            CivilizationMessage message = TechnologyController.checkCheatNumber(input);
            switch (message){
                case SUCCESS:
                    System.out.println("success");
                    return;
                case OUT_OF_RANGE:
                    System.out.println("your number is out of range");
                    break;
                case INVALID_INPUT:
                    System.out.println("invalid number");
                    break;
                default:
                    break;
            }
        }

    }

    private void deleteUnit(){
        Unit unit = GameController.getGame().getSelectedUnit();
        if(unit == null){
            System.out.println("no selected unit");
            return;
        }
        unit.destroy();
        System.out.println("unit deleted");

    }

    private void freeUnit(){
        Unit unit = GameController.getGame().getSelectedUnit();
        if(unit == null){
            System.out.println("no selected unit");
            return;
        }
        unit.setUnitState(UnitState.FREE);
        unit.setMoveTarget(null);
        System.out.println("unit deleted");

    }
}
