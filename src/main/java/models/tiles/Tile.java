package models.tiles;

import models.tiles.enums.ImprovementTemplate;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.*;

public class Tile {
    Terrain terrain;
    TerrainFeature terrainFeature;
    Military military;
    Civilian civilian;
    Resource resource;
    ImprovementTemplate improvement;
    Project project;
    // TODO: add Ruin, City
    public Tile(Terrain terrain, TerrainFeature terrainFeature, Resource resource) {
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
        this.military = null;
        this.civilian = null;
        this.resource = resource;
        this.improvement = null;
        this.project = null;
    }
}

