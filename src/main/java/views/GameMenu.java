package views;

import models.Game;
import models.tiles.Tile;
import models.units.Settler;
import models.units.Worker;
import views.enums.Color;

public class GameMenu extends Menu {
    private static GameMenu instance = new GameMenu();
    public static Game game;
    private final int BOARD_WIDTH=113;
    private final int BOARD_HEIGHT=34;


    public static GameMenu getInstance() {
        return instance;
    }

    @Override 
    protected boolean checkCommand(String command){
        drawBoard();
        return true;
    }

    private String[][] makeBoardGrid(){
        Tile[][] map = game.getMap();
        String[][] grid = new String[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                grid[i][j]=" ";
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                String hex= fillHexData(map[i][j]);
                String[] lines = hex.split("\n");
                for (int m = 0; m < lines.length; m++) {
                    String content = lines[m];
                    for (int n = 0; n < content.length(); n++) {
                        int x = 10*j + n;
                        int y = 6 * i + (j % 2) * 3 + m;
                        
                        // Only override empty spaces
                        if (content.charAt(n)!=' ' && content.charAt(n)!='/' && content.charAt(n)!='\\' && (i!=0 || m!=0)) 
                            grid[y][x]="\u001B[41m"+String.valueOf(content.charAt(n))+"\u001B[0m";
                        else if (content.charAt(n)!=' ')
                             grid[y][x]=String.valueOf(content.charAt(n));
                    }
                }
            }
        }    
        return grid;    
    }

    private String fillHexData(Tile tile) {
        String template ="   _______\n"  // 0 - 13
                       + "  /##C#M##\\\n" // 14 - 26
                       + " /##XX,YY##\\\n" // 27 - 39
                       + "/###########\\\n" // 40 - 52
                       + "\\###########/\n" // 53 - 65 
                       + " \\##FF#RR##/\n" // 66 - 78
                       + "  \\_______/";  // 79 - 92
        template = template.replace("XX", String.format("%02d", tile.getCoordinates()[0]));
        template = template.replace("YY", String.format("%02d", tile.getCoordinates()[1]));
        if(tile.getTerrainFeature()!=null)
            template = template.replace("FF", tile.getTerrainFeature().getName().substring(0, 2));
        else
            template = template.replace("FF", "  ");
        if(tile.getCivilian() instanceof Worker)
            template = template.replace("C", "W");
        else if(tile.getCivilian() instanceof Settler)
            template = template.replace("C", "S");
        else
            template = template.replace("C", " ");
        return template;
    }

    private void drawBoard() {
        String[][] grid= makeBoardGrid();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BOARD_HEIGHT; i++) {
            for(int j = 0; j < BOARD_WIDTH; j++) {
                builder.append(grid[i][j]);
            }
            builder.append('\n');
        }
        builder.setLength(builder.length()-1);
        System.out.println(builder.toString());
    }

    private Color getHexColor(Tile tile) {
        return Color.RED_BACKGROUND;
    }
}
