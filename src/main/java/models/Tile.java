package models;

import models.enums.*;
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
    public Tile(Terrain terrain, TerrainFeature terrainFeature, Resource resource, Military military, Civilian civilian) {
        this.terrain = terrain;
        this.terrainFeature = terrainFeature;
        this.military = military;
        this.civilian = civilian;
        this.resource = resource;
        this.improvement = null;
        this.project = null;
    }
}

