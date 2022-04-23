package models.tiles;

import models.tiles.enums.ImprovementTemplate;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.*;

public class Tile {
    Terrain terrain;
    TerrainFeature terrainFeature;
    Military military = null;
    Civilian civilian = null;
    Resource resource = null;
    ImprovementTemplate improvement = null;
    Project project = null;
    // TODO: add Ruin, City
    public Tile(Terrain terrain, TerrainFeature terrainFeature, Resource resource) {
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
    }
}

