package views;

import controllers.CivilizationController;
import controllers.GameController;
import controllers.GameMenuController;
import controllers.NetworkController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.Game;
import models.civilization.Civilization;
import models.network.ClientRequest;
import models.network.ServerResponse;
import models.tiles.Tile;
import views.components.Hex;

import java.util.ArrayList;
import java.util.HashMap;

public class TVShowPage extends PageController{
    private static TVShowPage instance;

    private static int firstUserID;

    @FXML
    private Pane gameContent;

    private boolean rightKey = false, leftKey = false, topKey = false, bottomKey = false;
    private boolean rightMouse = false, leftMouse = false, topMouse = false, bottomMouse = false;

    private int offsetI=0, offsetJ=0;

    private int baseI, baseJ;

    private Thread pageUpdater;

    @FXML
    private void initialize() {
        instance = this;

        Civilization civilization = GameController.getGame().getCurrentPlayer();

        baseI = civilization.getUnits().get(0).getTile().getCoordinates()[0];
        baseJ = civilization.getUnits().get(0).getTile().getCoordinates()[1];
        createMap(true);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        createPageUpdater();
    }

    public void createMap(boolean fogOfWar) {
        Game game = GameController.getGame();
        CivilizationController.updateDiscoveredTiles();
        this.gameContent.getChildren().clear();

        HashMap<Tile,Integer> discoveredTiles = game.getCurrentPlayer().getDiscoveredTiles();
        if (fogOfWar){
            for (int i = 0; i < game.MAP_WIDTH ; i++) {
                for (int j = 0; j < game.MAP_HEIGHT ; j++) {
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
            for (int i = 0; i < game.MAP_WIDTH ; i++) {
                for (int j = 0; j < game.MAP_HEIGHT ; j++) {
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
        Tile mapTile = GameController.getGame().getMap()[tileI][tileJ];
        Hex hex = new Hex(mapTile, discoveryTurn, hexX, hexY);
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
        return tileJ * 259.8076 + tileI * 129.9038 - baseJ * 240 - baseI * 120 + offsetI;
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

    @FXML
    private void mouseClicked(MouseEvent mouseEvent) {
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

    public void createPageUpdater() {
        pageUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ArrayList<String> data = new ArrayList<>();
                        data.add(String.valueOf(firstUserID));

                        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_ONLINE_GAME, data,
                                                    NetworkController.getInstance().getUserToken());

                        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
                        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
                            Game game = Game.decode(serverResponse.getData().get(0));
                            GameMenuController.setGame(game);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    createMap(true);
                                }
                            });
                        }
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        pageUpdater.start();
    }

    @FXML
    private void back() {
        pageUpdater.interrupt();
        App.setRoot("tvPage");
    }

    public static void setFirstUserID(int firstUserID) { TVShowPage.firstUserID = firstUserID; }

    public static TVShowPage getInstance() {
        return instance;
    }

    public Pane getGameContent() {
        return gameContent;
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
