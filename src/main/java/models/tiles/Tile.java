package models.tiles;

import models.civilization.City;
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

    public void setProject(Project project) {
        this.project = project;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setMilitary(Military military) {
        this.military = military;
    }

    public void setCivilian(Civilian civilian) {
        this.civilian = civilian;
    }

    public boolean isAccessible() {
        if (terrainFeature != null) return terrain.isAccessible() && terrainFeature.isAccessible();
        return terrain.isAccessible();
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

