package models;

import models.enums.*;
import models.units.*;


public class Tile {
    Terrain terrain;
    TerrainFeature terrainFeature;
    Military military;
    Civilian civilian;
    // TODO: add Ruin, Resource, City, Improvement
    public Tile(Terrain terrain, TerrainFeature terrainFeature) {
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
        this.military = null;
        this.civilian = null;
    }
}
