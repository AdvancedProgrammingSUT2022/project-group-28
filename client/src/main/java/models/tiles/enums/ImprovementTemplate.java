package models.tiles.enums;

import models.civilization.enums.TechnologyTemplate;
import models.tiles.Project;
import models.tiles.TerrainOrTerrainFeature;
import models.tiles.Tile;

import java.util.ArrayList;
import java.util.Arrays;

public enum ImprovementTemplate {
    ROAD("Road", null, "road", 3, 0, 0, 0, TechnologyTemplate.THE_WHEEL) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (TerrainFeature.ICE.equals(tile.getTerrainFeature())) return false;
            if (tile.isRoadConstructed()) return false;
            return true;
        }
        @Override
        public void startImprovement(Tile tile) { tile.setProject(new Project(ROAD)); }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setRoadConstructed(true);
        }
    },
    RAILROAD("Railroad", null, "railroad", 3, 0, 0, 0, TechnologyTemplate.STEAM_POWER) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (TerrainFeature.ICE.equals(tile.getTerrainFeature())) return false;
            if (tile.isRailRoadConstructed()) return false;
            return true;
        }
        @Override
        public void startImprovement(Tile tile) { tile.setProject(new Project(RAILROAD));}

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setRoadConstructed(true);
        }
    },
    CAMP("Camp", "camp", "camp", 6, 0, 0, 0, TechnologyTemplate.TRAPPING) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            ArrayList<TerrainOrTerrainFeature> possiblePlaces = new ArrayList<>(Arrays.asList(Terrain.TUNDRA, Terrain.PLAIN,
                    Terrain.HILL, TerrainFeature.FOREST));
            if (!possiblePlaces.contains(tile.getTerrain()) && !possiblePlaces.contains(tile.getTerrainFeature()))
                return false;
            return true;
        }
        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(CAMP));
            tile.setNextImprovement(null);
        }
        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(CAMP);
        }
    },
    FARM("Farm", "farm","farm", 6, 1, 0, 0, TechnologyTemplate.AGRICULTURE) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            ArrayList<TerrainOrTerrainFeature> possiblePlaces = new ArrayList<>(Arrays.asList(Terrain.PLAIN, Terrain.DESERT,
                    Terrain.GRASSLAND));
            if (!possiblePlaces.contains(tile.getTerrain())) return false;
            return true;
        }
        @Override
        public void startImprovement(Tile tile) {
            if (TerrainFeature.FOREST.equals(tile.getTerrainFeature())) {
                tile.setProject(new Project(REMOVE_FOREST));
                tile.setNextImprovement(FARM);
            } else if (TerrainFeature.JUNGLE.equals(tile.getTerrainFeature())) {
                tile.setProject(new Project(REMOVE_JUNGLE));
                tile.setNextImprovement(FARM);
            } else if (TerrainFeature.MARSH.equals(tile.getTerrainFeature())) {
                tile.setProject(new Project(REMOVE_MARSH));
                tile.setNextImprovement(FARM);
            } else {
                tile.setProject(new Project(FARM));
                tile.setNextImprovement(null);
            }
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(FARM);
        }

    },
    LUMBERMILL("Lumbermill", "lumbermill","lumbermill", 6, 0, 1, 0, TechnologyTemplate.CONSTRUCTION) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (!TerrainFeature.FOREST.equals(tile.getTerrainFeature())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(LUMBERMILL));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(LUMBERMILL);
        }
    },
    MINE("Mine", "mine", "mine", 6, 0, 1, 0, TechnologyTemplate.MINING) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            ArrayList<TerrainOrTerrainFeature> possiblePlace = new ArrayList<>(Arrays.asList(Terrain.PLAIN, Terrain.DESERT,
                    Terrain.GRASSLAND, Terrain.TUNDRA, Terrain.SNOW, Terrain.HILL, TerrainFeature.FOREST,
                    TerrainFeature.JUNGLE, TerrainFeature.MARSH));
            if (!possiblePlace.contains(tile.getTerrain()) && !possiblePlace.contains(tile.getTerrainFeature()))
                return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            if (TerrainFeature.FOREST.equals(tile.getTerrainFeature())) {
                tile.setProject(new Project(REMOVE_FOREST));
                tile.setNextImprovement(MINE);
            } else if (TerrainFeature.JUNGLE.equals(tile.getTerrainFeature())) {
                tile.setProject(new Project(REMOVE_JUNGLE));
                tile.setNextImprovement(MINE);
            } else if (TerrainFeature.MARSH.equals(tile.getTerrainFeature())) {
                tile.setProject(new Project(REMOVE_MARSH));
                tile.setNextImprovement(MINE);
            } else {
                tile.setProject(new Project(MINE));
                tile.setNextImprovement(null);
            }
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(MINE);
        }
    },
    PASTURE("Pasture", "pasture", "pasture", 7, 0, 0, 0, TechnologyTemplate.ANIMAL_HUSBANDRY) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (tile.getResource() == null) return false;
            ArrayList<ResourceTemplate> possibleResources = new ArrayList<>(Arrays.asList(ResourceTemplate.HORSE,
                    ResourceTemplate.COW, ResourceTemplate.SHEEP));
            if (!possibleResources.contains(tile.getResource().getResourceTemplate())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(PASTURE));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(PASTURE);
        }
    },
    PLANTATION("Plantation", "plantation", "plantation", 5, 0, 0, 0, TechnologyTemplate.CALENDAR) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (tile.getResource() == null) return false;
            ArrayList<ResourceTemplate> possibleResources = new ArrayList<>(Arrays.asList(ResourceTemplate.BANANA,
                    ResourceTemplate.COTTON, ResourceTemplate.DYE, ResourceTemplate.INCENSE, ResourceTemplate.SILK,
                    ResourceTemplate.SUGAR));
            if (!possibleResources.contains(tile.getResource().getResourceTemplate())) return false;
            return true;

        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(PLANTATION));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(PLANTATION);
        }
    },
    QUARRY("Quarry", "quarry", "quarry", 7, 0, 0, 0, TechnologyTemplate.MASONRY) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (tile.getResource() == null) return false;
            if (!ResourceTemplate.MARBLE.equals(tile.getResource().getResourceTemplate())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(QUARRY));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(QUARRY);
        }
    },
    TRADING_POST("Trading post", "trading_post", "trading_post", 8, 0, 0, 1, TechnologyTemplate.TRAPPING) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            ArrayList<TerrainOrTerrainFeature> possiblePlaces = new ArrayList<>(Arrays.asList(Terrain.PLAIN, Terrain.DESERT,
                    Terrain.GRASSLAND, Terrain.TUNDRA));
            if (!possiblePlaces.contains(tile.getTerrain())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(TRADING_POST));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(TRADING_POST);
        }
    },
    FACTORY("Factory", "factory", "factory",10, 0, 2, 0, TechnologyTemplate.ENGINEERING) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            ArrayList<TerrainOrTerrainFeature> possiblePlaces = new ArrayList<>(Arrays.asList(Terrain.PLAIN, Terrain.DESERT,
                    Terrain.GRASSLAND, Terrain.TUNDRA, Terrain.SNOW));
            if (!possiblePlaces.contains(tile.getTerrain())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(FACTORY));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setImprovement(FACTORY);
        }
    },
    REMOVE_JUNGLE("Remove jungle", null, "axe",7, 0, 0, 0, TechnologyTemplate.BRONZE_WORKING) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (!TerrainFeature.JUNGLE.equals(tile.getTerrainFeature())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(REMOVE_JUNGLE));
        }

        @Override
        public void completeImprovement(Tile tile) {
            if (tile.getNextImprovement() != null) tile.setProject(new Project(tile.getNextImprovement()));
            else tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setTerrainFeature(null);
        }
    },
    REMOVE_FOREST("Remove forest", null, "axe", 4, 0, 0, 0, TechnologyTemplate.BRONZE_WORKING) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (!TerrainFeature.FOREST.equals(tile.getTerrainFeature())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(REMOVE_FOREST));
        }

        @Override
        public void completeImprovement(Tile tile) {
            if (tile.getNextImprovement() != null) tile.setProject(new Project(tile.getNextImprovement()));
            else tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setTerrainFeature(null);
        }

    },
    REMOVE_MARSH("Remove marsh", null, "axe", 6, 0, 0, 0, TechnologyTemplate.BRONZE_WORKING) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (!TerrainFeature.MARSH.equals(tile.getTerrainFeature())) return false;
            return true;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(REMOVE_MARSH));
        }

        @Override
        public void completeImprovement(Tile tile) {
            if (tile.getNextImprovement() != null) tile.setProject(new Project(tile.getNextImprovement()));
            else tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setTerrainFeature(null);
        }
    },
    REMOVE_ROAD("Remove road", null, "axe", 3, 0, 0, 0, TechnologyTemplate.THE_WHEEL) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (tile.isRoadConstructed()) return true;
            return false;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(REMOVE_ROAD));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setRoadConstructed(false);
        }
    },
    REMOVE_RAILROAD("Remove railroad", null, "axe", 3, 0, 0, 0, TechnologyTemplate.STEAM_POWER) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (tile.isRailRoadConstructed()) return true;
            return false;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.setProject(new Project(REMOVE_RAILROAD));
            tile.setNextImprovement(null);
        }

        @Override
        public void completeImprovement(Tile tile) {
            tile.setProject(null);
            tile.setNextImprovement(null);
            tile.setRailRoadConstructed(false);
        }
    },
    REPAIR("Repair", null, "hammer", 3, 0, 0, 0, TechnologyTemplate.AGRICULTURE) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (tile.getProject() != null && tile.getProject().isBroken()) return true;
            return false;
        }

        @Override
        public void startImprovement(Tile tile) {
            tile.getProject().setBroken(false);
            tile.setNextImprovement(null);
        }
    },
    CONTINUE("Continue", null, null,0, 0, 0, 0, TechnologyTemplate.AGRICULTURE) {
        @Override
        public boolean isPossiblePlaceToBuild(Tile tile) {
            if (tile.getProject() != null) return true;
            return false;
        }
    };

    public boolean isPossiblePlaceToBuild(Tile tile) {
        return true;
    }

    public void startImprovement(Tile tile) { }

    public void completeImprovement(Tile tile) { }
    private final String name;
    private final String filename;
    private final String iconName;
    private final int turnCost;
    private final int food;
    private final int production;
    private final int gold;
    private final TechnologyTemplate requiredTechnology;

    ImprovementTemplate(String name, String filename, String iconName, int turnCost, int food, int production, int gold, TechnologyTemplate requiredTechnology) {
        this.name = name;
        this.filename = filename;
        this.iconName = iconName;
        this.turnCost = turnCost;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.requiredTechnology = requiredTechnology;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public String getIconName() {
        return iconName;
    }

    public int getTurnCost() {
        return turnCost;
    }

    public int getFood() {
        return food;
    }

    public int getProduction() {
        return production;
    }

    public int getGold() {
        return gold;
    }

    public TechnologyTemplate getRequiredTechnology() {
        return requiredTechnology;
    }
}
