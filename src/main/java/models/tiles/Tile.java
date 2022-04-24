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
    private final TerrainFeature terrainFeature;
    private final Resource resource;
    private ArrayList<Direction> rivers;

    private ImprovementTemplate improvement;
    private Project project;

    private City city;

    private Military military;
    private Civilian civilian;

    public Tile(int i, int j, Terrain terrain, TerrainFeature terrainFeature, Resource resource, ArrayList<Direction> rivers) {
        this.coordinates = new int[]{i , j};
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
        this.resource = resource;
        this.rivers = rivers;
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

    public Project getProject() {
        return project;
    }

    public City getCity() {
        return city;
    }

    public Military getMilitary() {
        return military;
    }

    public Civilian getCivilian() {
        return civilian;
    }

    public boolean isAccessible() {
        return terrain.isAccessible() && terrainFeature.isAccessible();
    }

    public void freeUnit(Unit unit) {
        if (unit instanceof Military) military = null;
        if (unit instanceof Civilian) civilian = null;
    }

    public void addUnit(Unit unit) {
        if (unit instanceof Military) military = (Military) unit;
        if (unit instanceof Civilian) civilian = (Civilian) unit;
    }
}

