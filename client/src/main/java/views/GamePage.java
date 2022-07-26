package views;

import java.util.HashMap;
import java.util.Optional;

import controllers.CivilizationController;
import controllers.GameController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import models.Game;
import models.tiles.Tile;
import views.components.Hex;
import views.components.TileInfo;

public class GamePage extends PageController{
    private static GamePage instance;

    public enum MapState {
        NORMAL,
        ASSIGN_CITIZEN,
        BUY_TILE,
        UNIT_SELECTED,
        CITY_ATTACK;
        //TODO: complete
    }

    @FXML
    private Pane gameContent;
    @FXML
    private Group HUD;

    private TileInfo tileInfo;

    private MapState mapState = MapState.NORMAL;

    private boolean rightKey = false, leftKey = false, topKey = false, bottomKey = false;
    private boolean rightMouse = false, leftMouse = false, topMouse = false, bottomMouse = false;

    private int offsetI=0, offsetJ=0;

    private int baseI, baseJ;


    @FXML
    private void initialize() {
        instance = this;

        tileInfo = new TileInfo();

        HUDController.getInstance().createHUD(HUD);

        // TODO: change base
        baseI = GameController.getGame().getCurrentPlayer().getUnits().get(0).getTile().getCoordinates()[0];
        baseJ = GameController.getGame().getCurrentPlayer().getUnits().get(0).getTile().getCoordinates()[1];
        createMap(true);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        this.updateGamePage();
    }

    public void createMap(boolean fogOfWar) {
        Game game = GameController.getGame();
        CivilizationController.updateDiscoveredTiles();
        this.gameContent.getChildren().clear();

        HashMap<Tile,Integer> discoveredTiles = App.getCurrentUserCivilization().getDiscoveredTiles();
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

    public void createTileInfo(TileInfo tileInfo){
        this.gameContent.getChildren().add(tileInfo);
    }

    public void deleteTileInfo(TileInfo tileInfo){
        if(GamePage.this.gameContent.getChildren().contains(tileInfo)){
            GamePage.this.gameContent.getChildren().remove(tileInfo);
        }
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
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            this.mapState = MapState.NORMAL;
            GameController.getGame().setSelectedCity(null);
            GameController.getGame().setSelectedUnit(null);
            updateGamePage();
        }
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

    public void updateGamePage() {
        this.createMap(true);
        HUDController.getInstance().getUnitInfo().update();
        HUDController.getInstance().getCurrentTechnologyInfo().updateData();
        HUDController.getInstance().updateNextTurnButton();
        HUDController.getInstance().getMiniMap().updateMap();
        HUDController.getInstance().getInfoBar().update();

    }

    public boolean destroyOrAttachCity(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Destroy or Attach City");
        String s = "city is conquered. do you want to destroy it or attach it?";
        alert.setContentText(s);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.initStyle(StageStyle.TRANSPARENT);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Destroy");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Attach");

        Optional<ButtonType> result = alert.showAndWait();
        
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            return true;
        }
        return false;
    }

    public static GamePage getInstance() {
        return instance;
    }


    public Pane getGameContent() {
        return gameContent;
    }

    public Group getHUD() {
        return HUD;
    }

    public MapState getMapState() { return mapState; }

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

    public void setMapState(MapState mapState) { this.mapState = mapState; }
}
