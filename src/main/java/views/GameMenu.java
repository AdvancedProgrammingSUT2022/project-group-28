package views;

import models.Game;
import models.tiles.Tile;
import models.units.Worker;

public class GameMenu extends Menu {
    private static GameMenu instance = new GameMenu();
    public static Game game;

    public static GameMenu getInstance() {
        return instance;
    }

    @Override 
    protected boolean checkCommand(String command){
        drawBoard();
        return true;
    }

    private char[][] makeBoardGrid(){
        Tile[][] map = game.getMap();
        char[][] grid = new char[23][93];
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 93; j++) {
                grid[i][j]=' ';
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                String hex= fillHexData(map[i][j]);
                String[] lines = hex.split("\n");
                for (int m = 0; m < lines.length; m++) {
                    String content = lines[m];
                    for (int n = 0; n < content.length(); n++) {
                        int x = 9*j + n;
                        int y = 4*i + (j % 2) * 2 + m;
                        
                        // Only override empty spaces
                        if (grid[y][x] == ' ') {
                            grid[y][x]=content.charAt(n);
                        }
                    }
                }
            }
        }    
        return grid;    
    }

    private String fillHexData(Tile tile) {
        String template ="  _______  \n"  // 0 - 11
                       + " /  C M  \\ \n" // 11 - 22
                       + "/  XX,YY  \\\n" // 22 - 33
                       + "\\  FF RR  /\n" // 33 - 44
                       + " \\_______/ ";  // 44 - 55
        template = template.replace("XX", String.format("%2d", tile.getCoordinates()[0]));
        template = template.replace("YY", String.format("%2d", tile.getCoordinates()[1]));
        if(tile.getTerrainFeature()!=null)
            template = template.replace("FF", tile.getTerrainFeature().getName().substring(0, 2));
        else
            template = template.replace("FF", "  ");
        if(tile.getCivilian() instanceof Worker)
            template = template.replace("C", "W");
        else
            template = template.replace("C", "S");
        return template;
    }

    private void drawBoard() {
        char[][] grid= makeBoardGrid();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 23; i++) {
            for(int j = 0; j < 93; j++) {
                builder.append(grid[i][j]);
            }
            builder.append('\n');
        }
        System.out.println(builder.toString());
    }

}
