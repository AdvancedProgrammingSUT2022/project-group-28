package models.civilization.enums;

import models.civilization.BuildingInterface;
import models.civilization.City;
import models.civilization.Technology;


public enum BuildingTemplate implements BuildingInterface {
    BARRACKS("Barracks", 80, 1, 0, 1, null) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    GRANARY("Granary", 100, 1, 0,  1, null) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    LIBRARY("Library", 80, 1, 2, 1, null) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    MONUMENT("Monument", 60, 1, 0, 1, null) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    WALLS("Walls", 100, 1, 0, 1, null) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    WATER_MILL("Water mill", 120, 2, 0, 1, null) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    };

    // TODO: Add all buildings
    // TODO: Add required techs

    private String name;
    private int cost;
    private int maintenance;
    private int slots;
    private int eraNumber;
    private Technology requiredTechnology;
    BuildingTemplate (String name, int cost, int maintenance, int slots, int eraNumber, Technology requiredTechnology) {
        this.name = name;
        this.cost = cost;
        this.maintenance = maintenance;
        this.slots = slots;
        this.eraNumber = eraNumber;
        this.requiredTechnology = requiredTechnology;
    }
}
