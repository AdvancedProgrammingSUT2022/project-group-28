package models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.enums.CivilizationNames;
import models.tiles.Resource;
import models.tiles.Tile;
import models.tiles.enums.*;
import models.units.Unit;
import controllers.TileController;
import controllers.units.SettlerController;

public class Game {
    public final int MAP_WIDTH = 50;
    public final int MAP_HEIGHT = 50;

    private Tile[][] map;
    private ArrayList<Civilization> civilizations;

    private Civilization currentPlayer;
    private int turnNumber;

    private Unit selectedUnit = null;
    private City selectedCity = null;

    private int numberOfPlayers;

    
    public Game(ArrayList<User> users, int seed) {
        Random random = new Random(seed);
        
        this.map = generateRandomMap(random);
        
        this.numberOfPlayers = users.size();
        
        this.civilizations = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            Civilization civilization = new Civilization(users.get(i), CivilizationNames.values()[i]);
            while(true){
                int[] thisCoordinates = new int[]{random.nextInt(MAP_WIDTH), random.nextInt(MAP_HEIGHT)};
                boolean isValid = true;
                Tile tile = this.map[thisCoordinates[0]][thisCoordinates[1]];
                if (!tile.getTerrain().isAccessible() ||
                    (tile.getTerrainFeature()!= null && !tile.getTerrainFeature().isAccessible())) continue;
                for(Civilization c : this.civilizations){
                    if(TileController.getDistance(c.getUnits().get(0).getTile(),tile ) < 10){
                        isValid = false;
                        break;
                    }
                }
                if (isValid){
                    SettlerController.createSettler(civilization, tile);
                    this.civilizations.add(civilization);
                    break;
                }
            }
            
        }
        
        this.currentPlayer = this.civilizations.get(0);
        this.turnNumber = 1;
    }
    
    
    private Tile[][] generateRandomMap(Random random) {
        Tile[][] map = new Tile[MAP_HEIGHT][MAP_WIDTH];
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = MAP_WIDTH - 1; j >= 0; j--) {
                ArrayList<Direction> rivers = getRandomRivers(random);
                Terrain terrain = Terrain.generateRandomTerrain(random);
                TerrainFeature terrainFeature = getPossibleTerrainFeature(random, terrain, rivers);
                Resource resource = getPossibleResource(random, terrain, terrainFeature);
                map[i][j] = new Tile(i, j, terrain, terrainFeature, resource, rivers);
                addAdjacentRivers(i, j, map, rivers);
                if(i<2 || j<2 || i>=MAP_HEIGHT || j>=MAP_WIDTH) 
                map[i][j] = new Tile(i, j, Terrain.OCEAN,null,null,new ArrayList<>());
            }
        }
        return map;
    }

    private ArrayList<Direction> getRandomRivers(Random random) {
        ArrayList<Direction> result = new ArrayList<>();
        if (random.nextInt(30) == 0) result.add(Direction.UP);
        if (random.nextInt(30) == 0) result.add(Direction.UP_RIGHT);
        if (random.nextInt(30) == 0) result.add(Direction.RIGHT);
        return result;
    }
    
    private TerrainFeature getPossibleTerrainFeature(Random random, Terrain terrain, ArrayList<Direction> rivers) {
        TerrainFeature terrainFeature = TerrainFeature.generateRandomTerrainFeature(random);
        if (terrainFeature == TerrainFeature.FOOD_PLAIN && rivers.size() == 0) return null;
        else if (terrainFeature == TerrainFeature.OASIS && terrain != Terrain.DESERT) return null;
        else if (terrainFeature == TerrainFeature.ICE && (terrain != Terrain.TUNDRA || 
                                                          terrain != Terrain.MOUNTAIN || 
                                                          terrain != Terrain.OCEAN || 
                                                          terrain != Terrain.SNOW )) return null;
                                                          else if (terrain == Terrain.OCEAN) return null;
                                                          return terrainFeature;
    }
    
    private Resource getPossibleResource(Random random, Terrain terrain, TerrainFeature terrainFeature) {
        ResourceTemplate resourceTemplate = ResourceTemplate.generateRandomResourceTemplate(random);
        if (!resourceTemplate.getPossiblePlaces().contains(terrain) &&
        !resourceTemplate.getPossiblePlaces().contains(terrainFeature)) return null;
        else {
            int count;
            if (resourceTemplate.getType().equals(ResourceType.STRATEGIC)) {
                count = (random.nextInt(3) + 1) * 2;
            } else count = 1;
            return new Resource(resourceTemplate, count);
        }
    }
    
    private void addAdjacentRivers(int i , int j, Tile[][] map, ArrayList<Direction> rivers) {
        if (rivers.contains(Direction.UP) && i - 1 >= 0)
        map[i - 1][j].getRivers().add(Direction.DOWN);
        if (rivers.contains(Direction.RIGHT) && j + 1 < MAP_WIDTH)
        map[i][j + 1].getRivers().add(Direction.LEFT);
        if (rivers.contains(Direction.UP_RIGHT) && i - 1 >= 0 && j + 1 < MAP_WIDTH)
        map[i - 1][j + 1].getRivers().add(Direction.DOWN_LEFT);
    }

    public static Game decode(String coded) {
        try{
            byte[] bytes = Base64.getDecoder().decode(coded);
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
            Scanner scanner = new Scanner(gis, "UTF-8");
            String xml = "";
            xml = scanner.useDelimiter("\\A").next();
            scanner.close();
            XStream xStream = new XStream();
            xStream.addPermission(AnyTypePermission.ANY);
            return (Game) xStream.fromXML(xml);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String encode() {
        try{
            XStream xStream = new XStream();
            xStream.addPermission(AnyTypePermission.ANY);
            String xml = xStream.toXML(this).replaceAll(">[\\s\\n]+<", "><");
            ByteArrayOutputStream obj=new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(obj);
            gzip.write(xml.getBytes("UTF-8"));
            gzip.close();
            return new String(Base64.getEncoder().encode(obj.toByteArray()));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public Tile[][] getMap() { return map; }
    
    public ArrayList<Civilization> getCivilizations() { return civilizations; }
    
    public Civilization getCurrentPlayer() { return currentPlayer; }
    
    public int getTurnNumber() { return turnNumber; }
    
    public void setCurrentPlayer(Civilization currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }
    
    public Unit getSelectedUnit() {
        return selectedUnit;
    }
    
    public City getSelectedCity() { return selectedCity; }
    
    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }
    
    public void setSelectedCity(City selectedCity) { this.selectedCity = selectedCity; }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
