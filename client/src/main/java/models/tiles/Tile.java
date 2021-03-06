package models.tiles;

import models.civilization.City;
import models.civilization.Civilization;
import models.tiles.enums.Direction;
import models.tiles.enums.ImprovementTemplate;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.*;

import java.util.ArrayList;

public class Tile {
    private final int[] coordinates;
    private final Terrain terrain;
    private TerrainFeature terrainFeature;
    private final Resource resource;
    private ArrayList<Direction> rivers;

    private boolean working; // Citizens

    private ImprovementTemplate improvement;
    private ImprovementTemplate nextImprovement;
    private Project project;
    private boolean roadConstructed = false;
    private boolean railRoadConstructed = false;

    private City city;

    private Civilization civilization;

    private Military military;
    private Civilian civilian;

    public Tile(int i, int j, Terrain terrain, TerrainFeature terrainFeature, Resource resource, ArrayList<Direction> rivers) {
        this.coordinates = new int[]{i , j};
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
        this.resource = resource;
        this.rivers = rivers;
    }

    //copy constructor
    public Tile(Tile tile) {
        this.coordinates = tile.getCoordinates();
        this.terrain = tile.getTerrain();
        this.terrainFeature = tile.getTerrainFeature();
        this.resource = tile.getResource();
        this.rivers = tile.getRivers();
        this.city=tile.getCity();
        this.military=tile.getMilitary();
        this.civilian=tile.getCivilian();
        this.project=tile.getProject();
        this.improvement = tile.getImprovement();
        this.working = tile.isWorking();
        this.roadConstructed = tile.isRoadConstructed();
        this.railRoadConstructed = tile.isRailRoadConstructed();
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public TerrainFeature getTerrainFeature() {
        return terrainFeature;
    }

    public Resource getResource() {
        return resource;
    }

    public ArrayList<Direction> getRivers() {
        return rivers;
    }

    public ImprovementTemplate getImprovement() {
        return improvement;
    }

    public ImprovementTemplate getNextImprovement() { return  nextImprovement; }

    public Project getProject() {
        return project;
    }

    public City getCity() {
        return city;
    }

    public Civilization getCivilization() { return civilization; }

    public Military getMilitary() {
        return military;
    }

    public Civilian getCivilian() {
        return civilian;
    }

    public boolean isWorking() { return working; }

    public boolean isRoadConstructed() { return roadConstructed; }

    public boolean isRailRoadConstructed() { return railRoadConstructed; }

    public void setTerrainFeature(TerrainFeature terrainFeature) { this.terrainFeature = terrainFeature; }

    public void setImprovement(ImprovementTemplate improvement) { this.improvement = improvement; }

    public void setNextImprovement(ImprovementTemplate nextImprovement) { this.nextImprovement = nextImprovement; }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setCivilization(Civilization civilization) { this.civilization = civilization; }

    public void setMilitary(Military military) {
        this.military = military;
    }

    public void setCivilian(Civilian civilian) {
        this.civilian = civilian;
    }

    public void setRailRoadConstructed(boolean railRoadConstructed) { this.railRoadConstructed = railRoadConstructed; }

    public boolean isAccessible() {
        if (terrainFeature != null) return terrain.isAccessible() && terrainFeature.isAccessible();
        return terrain.isAccessible();
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public void setRoadConstructed(boolean roadConstructed) { this.roadConstructed = roadConstructed; }

    public void freeUnit(Unit unit) {
        if (unit instanceof Military) military = null;
        if (unit instanceof Civilian) civilian = null;
    }

    public void addUnit(Unit unit) {
        if (unit instanceof Military) military = (Military) unit;
        if (unit instanceof Civilian) civilian = (Civilian) unit;
    }

    @Override
    public String toString(){
        return "(" + coordinates[0] + ", " + coordinates[1] + ")";
    }
}

