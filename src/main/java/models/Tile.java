package models;

import models.enums.*;
import models.units.*;

public class Tile {
    Terrain terrain;
    TerrainFeature terrainFeature;
    Military military;
    Civilian civilian;
    Resource resource;
    // TODO: add Ruin, Resource, City, Improvement
    public Tile(Terrain terrain, TerrainFeature terrainFeature, Resource resource) {
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
        this.military = null;
        this.civilian = null;
        this.resource = resource;
    }
}

