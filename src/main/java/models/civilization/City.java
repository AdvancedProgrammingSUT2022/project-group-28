package models.civilization;

import models.tiles.Tile;

import java.util.ArrayList;

public class City {
    private final String NAME;
    private Civilization civilization;
    private Tile tile;
    private ArrayList<Tile> tiles;

    private int population;
    private int citizens;
    private int strength;
    private int hitPoint;

    private int foodBalance;
    private int productionBalance;

    private ArrayList<Building> buildings;

    public City(Civilization civilization, Tile tile) {
        this.NAME = "hi";
        this.civilization = civilization;
        this.tile = tile;
        // TODO: A lot of things
    }

}
