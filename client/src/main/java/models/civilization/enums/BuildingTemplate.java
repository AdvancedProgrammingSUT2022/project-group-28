package models.civilization.enums;

import controllers.CivilizationController;
import controllers.TechnologyController;
import models.Constructable;
import models.civilization.BuildingEffect;
import models.civilization.City;
import models.tiles.Tile;
import models.tiles.enums.ImprovementTemplate;
import models.tiles.enums.ResourceTemplate;
import models.tiles.enums.Terrain;
import views.enums.CityMessage;

import java.util.ArrayList;


public enum BuildingTemplate implements Constructable {
    BARRACKS("Barracks", "barracks", 80, 1, 0, 1, TechnologyTemplate.BRONZE_WORKING),
    GRANARY("Granary", "granary", 100, 1, 0,  1, TechnologyTemplate.POTTERY) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(2, 0, 0, 0, 0);
        }
    },
    LIBRARY("Library","library", 80, 1, 2, 1, TechnologyTemplate.WRITING) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, 0, city.getPopulation() * 2, 0);
        }
    },
    MONUMENT("Monument", "monument", 60, 1, 0, 1, null),
    WALLS("Walls", "walls", 100, 1, 0, 1, TechnologyTemplate.MASONRY) {
        @Override
        public void instantEffect(City city) {
            city.setStrength(city.getStrength() + 5);
        }
    },
    WATER_MILL("Water mill", "water_mill", 120, 2, 0, 1, TechnologyTemplate.THE_WHEEL) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (city.getTile().getRivers().size() == 0) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(2, 0, 0, 0, 0);
        }
    },
    ARMORY("Armory", "armory", 130 , 3 , 0, 2, TechnologyTemplate.IRON_WORKING) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(BARRACKS)) return false;
            return true;
        }
    },
    BURIAL_TOMB("Burial Tomb", "burial_tomb", 120 , 0 , 0, 2, TechnologyTemplate.PHILOSOPHY) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0 ,0,0,0,2);
        }
    },
    CIRCUS("Circus", "circus", 150 , 3 , 0, 2, TechnologyTemplate.HORSEBACK_RIDING) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, 0,0, 3);
        }
    },
    COLOSSEUM("Colosseum", "colosseum", 150 , 3 , 0, 2, TechnologyTemplate.CONSTRUCTION) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0,0,0,4);
        }
    },
    COURTHOUSE("Courthouse", "courthouse",200 , 5 , 0, 2, TechnologyTemplate.MATHEMATICS) {
        @Override
        public void instantEffect(City city) {
            // TODO: add
        }
    },
    STABLE("Stable", "stable", 100 , 1 , 0, 2, TechnologyTemplate.HORSEBACK_RIDING) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            for (Tile tile : city.getTiles()) {
                if (tile.getResource() != null) {
                    if (tile.getResource().getResourceTemplate().equals(ResourceTemplate.HORSE) &&
                        ResourceTemplate.HORSE.getRequiredImprovement().equals(tile.getImprovement()))
                        return true;
                }
            }
            return false;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, city.getProductionBalance()/4, 0, 0, 0);
        }
    },
    TEMPLE("Temple", "temple", 120 , 2 , 0, 2, TechnologyTemplate.PHILOSOPHY) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(MONUMENT)) return false;
            return true;
        }
    },
    CASTLE("Castle", "castle", 200 , 3 , 0, 3, TechnologyTemplate.CHIVALRY) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(WALLS)) return false;
            return true;
        }

        @Override
        public void instantEffect(City city) {
            city.setStrength(city.getStrength() + 7);
        }
    },
    FORGE("Forge", "forge",150 , 2 , 0, 3, TechnologyTemplate.METAL_CASTING) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if(!super.isAvailableToBuild(city)) return false;
            for (Tile tile : city.getTiles()) {
                if (tile.getResource() != null && tile.getResource().getResourceTemplate().equals(ResourceTemplate.IRON))
                    return true;
            }
            return false;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, (int)(city.getProductionBalance() * 0.15), 0, 0, 0);
        }
    },
    GARDEN("Garden", "garden", 120 , 2 , 0, 3, TechnologyTemplate.THEOLOGY) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (city.getTile().getRivers().size() != 0) return true;
            for (Tile tile : city.getTiles()) {
                if (tile.getTerrain().equals(Terrain.OCEAN)) return true;
            }
            return false;
        }
    },
    MARKET("Market", "market",120 , 0 , 0, 3, TechnologyTemplate.CURRENCY) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, 0, city.getCivilization().getGoldBalance()/4,0);
        }
    },
    MINT("Mint", "mint",120 , 0 , 0, 3, TechnologyTemplate.CURRENCY) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            int count = 0;
            for (Tile tile : city.getTiles()) {
                if (tile.getResource() != null &&
                    (tile.getResource().getResourceTemplate() == ResourceTemplate.GOLD ||
                    tile.getResource().getResourceTemplate() == ResourceTemplate.SILVER) &&
                    tile.getImprovement() == ImprovementTemplate.MINE) {
                    count++;
                }
            }

            return new BuildingEffect(0, 0, 0, count * 3, 0);
        }
    },
    MONASTERY("Monastery", "monastery", 120 , 2 , 0, 3, TechnologyTemplate.THEOLOGY),
    UNIVERSITY("University", "university", 200 , 3 , 0, 3, TechnologyTemplate.EDUCATION) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(LIBRARY)) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, city.getCivilization().getScienceBalance()/2, 0, 0);
        }
    },
    WORKSHOP("Workshop", "workshop", 100 , 2 , 0, 3, TechnologyTemplate.METAL_CASTING) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, city.getProductionBalance()/5, 0, 0, 0);
        }
    },
    BANK("Bank", "bank", 220 , 0 , 0, 4, TechnologyTemplate.BANKING) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(MARKET)) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, 0, city.getCivilization().getGoldBalance()/4, 0);
        }
    },
    MILITARY_ACADEMY("Military Academy", "military_academy", 350 , 3 , 0, 4, TechnologyTemplate.MILITARY_SCIENCE) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(BARRACKS)) return false;
            return true;
        }
    },
    MUSEUM("Museum", "museum", 350 , 3 , 0, 4, TechnologyTemplate.ARCHAEOLOGY) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(OPERA_HOUSE)) return false;
            return true;
        }
    },
    OPERA_HOUSE("Opera House", "opera_house", 220 , 3 , 0, 4, TechnologyTemplate.ACOUSTICS) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(BURIAL_TOMB)) return false;
            return true;
        }
    },
    PUBLIC_SCHOOL("Public School", "public_school",350 , 3 , 0, 4, TechnologyTemplate.SCIENTIFIC_THEORY) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(UNIVERSITY)) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, city.getCivilization().getScienceBalance()/2, 0,0);
        }
    },
    SATRAPS_COURT("Satrap’s Court", "satraps_court", 220 , 0 , 0, 4, TechnologyTemplate.BANKING) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(MARKET)) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, 0, city.getCivilization().getGoldBalance()/4, 2);
        }
    },
    THEATER("Theater", "theatre", 300 , 5 , 0, 4, TechnologyTemplate.PRINTING_PRESS) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(COLOSSEUM)) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, 0, 0, 4);
        }
    },
    WINDMILL("Windmill", "windmill", 180 , 2 , 0, 4, TechnologyTemplate.ECONOMICS) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (city.getTile().getTerrain().equals(Terrain.HILL)) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, (int)(city.getProductionBalance() * 0.15), 0, 0, 0);
        }
    },
    ARSENAL("Arsenal", "arsenal", 350 , 3 , 0, 5, TechnologyTemplate.RAILROAD) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(MILITARY_ACADEMY)) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, city.getProductionBalance()/5, 0, 0,0);
        }
    },
    BROADCAST_TOWER("Broadcast Tower", "broadcast_tower", 600 , 3 , 0, 5, TechnologyTemplate.RADIO) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(MUSEUM)) return false;
            return true;
        }
    },
    FACTORY("Factory", "factory" , 300 , 3 , 0, 5, TechnologyTemplate.STEAM_POWER) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            CivilizationController.updateResources(city.getCivilization());
            if (city.getCivilization().getResources().get(ResourceTemplate.COAL) == 0) return false;
            return true;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, city.getProductionBalance()/2, 0, 0, 0);
        }
    },
    HOSPITAL("Hospital", "hospital", 400 , 2 , 0, 5, TechnologyTemplate.BIOLOGY) {
        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(-city.getFoodBalance()/2, 0, 0, 0, 0);
        }
    },
    MILITARY_BASE("Military Base", "military_base", 450 , 4 , 0, 5, TechnologyTemplate.TELEGRAPH) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (!city.getBuildings().contains(CASTLE)) return false;
            return true;
        }

        @Override
        public void instantEffect(City city) {
            city.setStrength(city.getStrength() + 12);
        }
    },
    STOCK_EXCHANGE("Stock Exchange", "stock_exchange", 650 , 0 , 0, 5, TechnologyTemplate.ELECTRICITY) {
        @Override
        public boolean isAvailableToBuild(City city) {
            if (!super.isAvailableToBuild(city)) return false;
            if (city.getBuildings().contains(BANK) || city.getBuildings().contains(SATRAPS_COURT)) return true;
            return false;
        }

        @Override
        public BuildingEffect continuousEffect(City city) {
            return new BuildingEffect(0, 0, 0,city.getCivilization().getGoldBalance()/3, 0);
        }
    };

    // TODO: change due every building
    @Override
    public CityMessage checkPossibilityOfConstruction(City city) {
        return CityMessage.SUCCESS;
    }

    private final String name;
    private final String filename;
    private final int cost;
    private final int maintenance;
    private final int slots;
    private final int eraNumber;
    private final TechnologyTemplate requiredTechnology;
    BuildingTemplate (String name, String filename, int cost, int maintenance, int slots, int eraNumber, TechnologyTemplate requiredTechnology) {
        this.name = name;
        this.filename = filename;
        this.cost = cost;
        this.maintenance = maintenance;
        this.slots = slots;
        this.eraNumber = eraNumber;
        this.requiredTechnology = requiredTechnology;
    }

    public boolean isAvailableToBuild(City city) {
        ArrayList<TechnologyTemplate> studiedTechnologies = TechnologyController.extractFullProgressTechnology();
        if (!studiedTechnologies.contains(this.requiredTechnology) && this.requiredTechnology != null) return false;
        if (city.getBuildings().contains(this)) return false;
        return true;
    }

    public void instantEffect(City city) {}

    public BuildingEffect continuousEffect(City city) {
        return new BuildingEffect(0, 0, 0, 0, 0);
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public TechnologyTemplate getRequiredTechnology() {
        return requiredTechnology;
    }

    @Override
    public int getCost() {
        return cost;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public int getSlots() {
        return slots;
    }

    public int getEraNumber() {
        return eraNumber;
    }
}
