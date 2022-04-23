package models.tiles;

import models.TileOrRiver;
import models.civilization.Civilization;
import models.tiles.enums.ImprovementTemplate;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.*;

public class Tile implements TileOrRiver {
    Terrain terrain;
    TerrainFeature terrainFeature;
    Military military = null;
    Civilian civilian = null;
    Resource resource = null;
    ImprovementTemplate improvement = null;
    Project project = null;

    Civilization civilization;
    // TODO: add Ruin, City
    public Tile(Terrain terrain, TerrainFeature terrainFeature, Resource resource) {
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public TerrainFeature getTerrainFeature() {
        return terrainFeature;
    }

    public Military getMilitary() {
        return military;
    }

    public Civilian getCivilian() {
        return civilian;
    }

    public Resource getResource() {
        return resource;
    }

    public ImprovementTemplate getImprovement() {
        return improvement;
    }

    public Project getProject() {
        return project;
    }

    public Civilization getCivilization() {
        return civilization;
    }
}

