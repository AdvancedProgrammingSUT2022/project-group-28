package models.civilization.enums;

import models.civilization.BuildingInterface;
import models.civilization.City;
import models.civilization.Technology;


public enum BuildingTemplate implements BuildingInterface {
    BARRACKS("Barracks", 80, 1, 0, 1, TechnologyTemplate.BRONZE_WORKING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    GRANARY("Granary", 100, 1, 0,  1, TechnologyTemplate.POTTERY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    LIBRARY("Library", 80, 1, 2, 1, TechnologyTemplate.WRITING) {
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
    WALLS("Walls", 100, 1, 0, 1, TechnologyTemplate.MASONRY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    WATER_MILL("Water mill", 120, 2, 0, 1, TechnologyTemplate.THE_WHEEL) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    ARMORY("Armory", 130 , 3 , 0, 2, TechnologyTemplate.IRON_WORKING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    BURIAL_TOMB("Burial Tomb", 120 , 0 , 0, 2, TechnologyTemplate.PHILOSOPHY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    CIRCUS("Circus", 150 , 3 , 0, 2, TechnologyTemplate.HORSEBACK_RIDING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    COLOSSEUM("Colosseum", 150 , 3 , 0, 2, TechnologyTemplate.CONSTRUCTION) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    COURTHOUSE("Courthouse", 200 , 5 , 0, 2, TechnologyTemplate.MATHEMATICS) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    STABLE("Stable", 100 , 1 , 0, 2, TechnologyTemplate.HORSEBACK_RIDING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    TEMPLE("Temple", 120 , 2 , 0, 2, TechnologyTemplate.PHILOSOPHY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    CASTLE("Castle", 200 , 3 , 0, 3, TechnologyTemplate.CHIVALRY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    FORGE("Forge", 150 , 2 , 0, 3, TechnologyTemplate.METAL_CASTING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    GARDEN("Garden", 120 , 2 , 0, 3, TechnologyTemplate.THEOLOGY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    MARKET("Market", 120 , 0 , 0, 3, TechnologyTemplate.CURRENCY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    MINT("Mint", 120 , 0 , 0, 3, TechnologyTemplate.CURRENCY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    MONASTERY("Monastery", 120 , 2 , 0, 3, TechnologyTemplate.THEOLOGY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    UNIVERSITY("University", 200 , 3 , 0, 3, TechnologyTemplate.EDUCATION) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    WORKSHOP("Workshop", 100 , 2 , 0, 3, TechnologyTemplate.METAL_CASTING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    BANK("Bank", 220 , 0 , 0, 4, TechnologyTemplate.BANKING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    MILITARY_ACADEMY("Military Academy", 350 , 3 , 0, 4, TechnologyTemplate.MILITARY_SCIENCE) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    MUSEUM("Museum", 350 , 3 , 0, 4, TechnologyTemplate.ARCHAEOLOGY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    OPERA_HOUSE("Opera House", 220 , 3 , 0, 4, TechnologyTemplate.ACOUSTICS) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    PUBLIC_SCHOOL("Public School", 350 , 3 , 0, 4, TechnologyTemplate.SCIENTIFIC_THEORY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    SATRAPS_COURT("Satrap’s Court", 220 , 0 , 0, 4, TechnologyTemplate.BANKING) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    THEATER("Theater", 300 , 5 , 0, 4, TechnologyTemplate.PRINTING_PRESS) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    WINDMILL("Windmill", 180 , 2 , 0, 4, TechnologyTemplate.ECONOMICS) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    ARSENAL("Arsenal", 350 , 3 , 0, 5, TechnologyTemplate.RAILROAD) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    BROADCAST_TOWER("Broadcast Tower", 600 , 3 , 0, 5, TechnologyTemplate.RADIO) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    FACTORY("Factory", 300 , 3 , 0, 5, TechnologyTemplate.STEAM_POWER) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    HOSPITAL("Hospital", 400 , 2 , 0, 5, TechnologyTemplate.BIOLOGY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    MILITARY_BASE("Military Base", 450 , 4 , 0, 5, TechnologyTemplate.TELEGRAPH) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    },
    STOCK_EXCHANGE("Stock Exchange", 650 , 0 , 0, 5, TechnologyTemplate.ELECTRICITY) {
        @Override
        public boolean isAvailableToBuild(City city) {return true;}
        @Override
        public void giveEffect(City city) {}
    };

    // TODO: Add required techs

    private String name;
    private int cost;
    private int maintenance;
    private int slots;
    private int eraNumber;
    private TechnologyTemplate requiredTechnology;
    BuildingTemplate (String name, int cost, int maintenance, int slots, int eraNumber, TechnologyTemplate requiredTechnology) {
        this.name = name;
        this.cost = cost;
        this.maintenance = maintenance;
        this.slots = slots;
        this.eraNumber = eraNumber;
        this.requiredTechnology = requiredTechnology;
    }
}
