package views;

import java.util.HashMap;

import controllers.CivilizationController;
import controllers.GameController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.Game;
import models.tiles.Tile;
import views.components.Hex;

public class GamePage extends PageController{
    private static GamePage instance;

    @FXML
    private Pane gameContent;
    @FXML
    private Pane HUD;


    private boolean rightKey = false, leftKey = false, topKey = false, bottomKey = false;
    private boolean rightMouse = false, leftMouse = false, topMouse = false, bottomMouse = false;

    private int offsetI=0, offsetJ=0;

    private int baseI = 30, baseJ = 30;


    @FXML
    private void initialize() {
        instance = this;

        HUDController.getInstance().createHUD(HUD);
        createMap(true);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void createMap(boolean fogOfWar) {
        Game game = GameController.getGame();
        CivilizationController.updateDiscoveredTiles();
        this.gameContent.getChildren().clear();
        HashMap<Tile,Integer> discoveredTiles = game.getCurrentPlayer().getDiscoveredTiles();
        if (fogOfWar){
            for (int i = 0; i < 100 ; i++) {
                for (int j = 0; j < 100 ; j++) {
                    boolean found = false;
                    for (Tile tile : discoveredTiles.keySet()) {
                        if (tile.getCoordinates()[0] == i && tile.getCoordinates()[1] == j) {
                            createHex(tile, discoveredTiles.get(tile));
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        createFogOfWarHex(i, j);
                    }
                }
            }
        }else{
            int turnNumber = game.getTurnNumber();
            for (int i = 0; i < 100 ; i++) {
                for (int j = 0; j < 100 ; j++) {
                    Tile tile = GameController.getGame().getMap()[i][j];
                    createHex(tile, turnNumber);
                }
            }
        }
            
    }

    private void createHex(Tile tile, int discoveryTurn){
        int[] coordinates = tile.getCoordinates();
        int tileI = coordinates[0];
        int tileJ = coordinates[1];
        double hexX = getHexX(tileI, tileJ);
        double hexY = getHexY(tileI, tileJ);

        if (hexX < -200 || hexX >= 1800 || hexY <- 200 || hexY >= 1100)
                return;
        Hex hex = new Hex(tile, discoveryTurn, hexX, hexY);
        //hex.setMouseTransparent(true);
        this.gameContent.getChildren().add(hex);
    }

    private void createFogOfWarHex(int tileI , int tileJ){
        double hexX = getHexX(tileI, tileJ);
        double hexY = getHexY(tileI, tileJ);
        if (hexX < -200 || hexX >= 1800 || hexY <- 200 || hexY >= 1100)
                return;
        Hex hex = new Hex(hexX, hexY,tileI,tileJ);
        this.gameContent.getChildren().add(hex);
    }

    public double getHexX(int tileI,int tileJ){ 
        return tileJ * 259.8076 + tileI * 129.9038 - baseJ * 360 + offsetI; 
    }

    public double getHexY(int tileI,int tileJ){
        return tileI * 225 - baseI * 210 + offsetJ;
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode().getName().equals("Up")){
            topKey = true;
        } else if(keyEvent.getCode().getName().equals("Down")){
            bottomKey = true;
        } else if(keyEvent.getCode().getName().equals("Left")){
            leftKey = true;
        } else if(keyEvent.getCode().getName().equals("Right")){
            rightKey = true;
        }
    }

    @FXML
    private void keyReleased(KeyEvent keyEvent){
        if(keyEvent.getCode().getName().equals("Up")){
            topKey = false;
        } else if(keyEvent.getCode().getName().equals("Down")){
            bottomKey = false;
        } else if(keyEvent.getCode().getName().equals("Left")){
            leftKey = false;
        } else if(keyEvent.getCode().getName().equals("Right")){
            rightKey = false;
        }
    }

    @FXML
    private void mouseMoved(MouseEvent mouseEvent){
        leftMouse = (mouseEvent.getX()>20)?false:true;
        topMouse = (mouseEvent.getY()>20)?false:true;
        bottomMouse = (mouseEvent.getY()<880)?false:true;
        rightMouse = (mouseEvent.getX()<1580)?false:true;
    }

    private void update() {
        if(rightKey || rightMouse) {
            if (getHexX(99, 99)>600) {
                offsetI -= 5;
                createMap(true);
            }
        }
        if(leftKey || leftMouse){
            if (getHexX(0, 0)<1000) {
                offsetI+=5;
                createMap(true);
            }
        }
        if(topKey || topMouse) {
            if (getHexY(0, 50)<300) {
                offsetJ += 5;
                createMap(true);
            }
        }
        if(bottomKey || bottomMouse){
            if (getHexY(99, 50)>600) {
                offsetJ-=5;
                createMap(true);
            }
        }
    }

    public static GamePage getInstance() {
        return instance;
    }



    public int getBaseI() {
        return baseI;
    }

    public int getBaseJ() {
        return baseJ;
    }

    public int getOffsetI() {
        return offsetI;
    }

    public int getOffsetJ() {
        return offsetJ;
    }

    public void setOffsetI(int offsetI) {
        this.offsetI = offsetI;
    }

    public void setOffsetJ(int offsetJ) {
        this.offsetJ = offsetJ;
    }

    public void setBaseI(int baseI) {
        this.baseI = baseI;
    }

    public void setBaseJ(int baseJ) {
        this.baseJ = baseJ;
    }
}
