package models.units.enums;

import controllers.CivilizationController;
import controllers.TechnologyController;
import models.Constructable;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.enums.TechnologyTemplate;
import models.tiles.enums.ResourceTemplate;
import views.enums.CityMessage;

import java.util.ArrayList;
import java.util.HashMap;

public enum UnitTemplate implements Constructable {
    WORKER("Worker", "worker", 70, 0, 0, 0, 2, 1, null,TechnologyTemplate.AGRICULTURE,UnitType.CIVILIAN),
    SETTLER("Settler", "settler", 89, 0, 0, 0, 2, 1, null,TechnologyTemplate.AGRICULTURE,UnitType.CIVILIAN) {
        @Override
        public CityMessage checkPossibilityOfConstruction(City city) {
            if (city.getPopulation() < 2) return CityMessage.CITY_NOT_GREW;
            Civilization civilization = city.getCivilization();
            civilization.setHappiness(CivilizationController.getCivilizationHappiness(civilization));
            if (civilization.getHappiness() < 0) return CityMessage.UNHAPPY_PEOPLE;
            return CityMessage.SUCCESS;
        }
    },
    WARRIOR("Warrior", "warrior",40, 6, 0, 0, 2, 1, null,TechnologyTemplate.AGRICULTURE,UnitType.MELEE),
    ARCHER("Archer", "archer", 70, 4, 6, 2, 2, 1, null,TechnologyTemplate.ARCHERY,UnitType.RANGED),
    SCOUT("Scout", "scout", 25, 4, 0, 0, 2, 1, null,TechnologyTemplate.AGRICULTURE,UnitType.MELEE),
    CHARIOT_ARCHER("Chariot Archer", "chariot_archer", 60, 3, 6, 2, 4, 1, ResourceTemplate.HORSE,TechnologyTemplate.THE_WHEEL,UnitType.RANGED),
    SPEARMAN("Spearman","spearman", 50, 7, 0, 0, 2, 1, null,TechnologyTemplate.BRONZE_WORKING,UnitType.MELEE),

    CATAPULT("Catapult", "catapult", 100, 4, 14, 2, 2, 2, ResourceTemplate.IRON, TechnologyTemplate.MATHEMATICS,UnitType.SIEGE),
    HORSEMAN("Horseman", "horseman", 80, 12, 0, 0, 4, 2, ResourceTemplate.HORSE, TechnologyTemplate.HORSEBACK_RIDING,UnitType.MELEE),
    SWORDSMAN("Swordsman", "swordsman", 80, 11, 0, 0, 2, 2, ResourceTemplate.IRON, TechnologyTemplate.IRON_WORKING,UnitType.MELEE),
    
    CROSSBOWMAN("Crossbowman", "crossbowman", 120, 6, 12, 2, 2, 3, null, TechnologyTemplate.MACHINERY,UnitType.RANGED),
    KNIGHT("Knight","knight", 150, 18, 0, 0, 3, 3, ResourceTemplate.HORSE, TechnologyTemplate.CHIVALRY,UnitType.MELEE),
    LONGSWORDSMAN("Longswordsman", "longswordsman", 150, 18, 0, 0, 3, 3, ResourceTemplate.IRON, TechnologyTemplate.STEEL,UnitType.MELEE),
    PIKEMAN("Pikeman", "pikeman", 100, 10, 0, 0, 2, 3, null, TechnologyTemplate.CIVIL_SERVICE,UnitType.MELEE),
    TREBUCHET("Trebuchet", "trebuchet", 170, 6, 20, 2, 2, 3, ResourceTemplate.IRON, TechnologyTemplate.PHYSICS,UnitType.SIEGE),
    
    CANON("Canon", "cannon", 250, 10, 26, 2, 2, 4, null, TechnologyTemplate.CHEMISTRY,UnitType.SIEGE),
    CAVALRY("Cavalry", "cavalry", 260, 25, 0, 0, 3, 4, ResourceTemplate.HORSE, TechnologyTemplate.MILITARY_SCIENCE,UnitType.MELEE),
    Lancer("Lancer", "lancer", 220, 22, 0, 0, 4, 4, ResourceTemplate.HORSE, TechnologyTemplate.METALLURGY,UnitType.MELEE),
    MUSKETMAN("Musketman", "musketman", 120, 16, 0, 0, 2, 4, null, TechnologyTemplate.GUNPOWDER,UnitType.MELEE),
    RIFLEMAN("Rifleman", "rifleman", 200, 25, 0, 0, 2, 4, null, TechnologyTemplate.RIFLING,UnitType.MELEE),
    
    ANTI_TANK("Anti-Tank Gun", "antitank_gun", 300, 32, 0, 0, 2, 5, null, TechnologyTemplate.REPLACEABLE_PARTS,UnitType.MELEE),
    ARTILLERY("Artillery", "artillery", 420, 16, 32, 3, 2, 5, null, TechnologyTemplate.DYNAMITE,UnitType.SIEGE),
    INFANTRY("Infantry", "infantry", 300, 36, 0, 0, 2, 5, null,TechnologyTemplate.REPLACEABLE_PARTS,UnitType.MELEE),
    PANZER("Panzer", "panzer",  450, 60, 0, 0, 5, 5, null, TechnologyTemplate.COMBUSTION,UnitType.MELEE),
    TANK("Tank", "tank", 450, 50, 0, 0, 4, 5, null, TechnologyTemplate.COMBUSTION,UnitType.MELEE);
    // TODO: Notes!!

    @Override
    public CityMessage checkPossibilityOfConstruction(City city) {
        ArrayList<TechnologyTemplate> studiedTechnologies = TechnologyController.extractFullProgressTechnology();
        if (!studiedTechnologies.contains(this.requiredTechnology)) return CityMessage.REQUIRED_TECHNOLOGY;
        CivilizationController.updateResources(city.getCivilization());
        HashMap<ResourceTemplate, Integer> resources = city.getCivilization().getResources();
        if (resources.containsKey(this.requiredResource) &&
            resources.get(this.requiredResource) == 0)
            return CityMessage.REQUIRED_RESOURCE;
        return CityMessage.SUCCESS;
    }

    public CityMessage checkPossibleToBuy(City city) {
        CityMessage cityMessage = this.checkPossibilityOfConstruction(city);
        if (cityMessage != CityMessage.SUCCESS) return cityMessage;
        if (city.getCivilization().getGold() < this.getCost()) return CityMessage.NOT_ENOUGH_GOLD;
        if (city.getTile().getCivilian() != null && this.unitType == UnitType.CIVILIAN) return CityMessage.FULL_TILE;
        if (city.getTile().getMilitary() != null && this.unitType != UnitType.CIVILIAN) return CityMessage.FULL_TILE;
        return CityMessage.SUCCESS;
    }

    private final String name;
    private final String filename;
    private final int cost;
    private final int combatStrength;
    private final int rangedCombatStrength;
    private final int range;
    private final int movementPoint;
    private final int eraNumber;
    private final ResourceTemplate requiredResource;
    private final TechnologyTemplate requiredTechnology;
    private final UnitType unitType;

    UnitTemplate(String name, String filename, int cost, int combatStrength, int rangedCombatStrength, int range, int movementPoint,
                 int eraNumber, ResourceTemplate requiredResource, TechnologyTemplate requiredTechnology,UnitType unitType) {
        this.name = name;
        this.filename = filename;
        this.cost = cost;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.movementPoint = movementPoint;
        this.eraNumber = eraNumber;
        this.requiredResource = requiredResource;
        this.requiredTechnology = requiredTechnology;
        this.unitType = unitType;
    }

    public TechnologyTemplate getRequiredTechnology() {
        return requiredTechnology;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public int getCost() {
        return cost;
    }

    public int getCombatStrength() {
        return combatStrength;
    }

    public int getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public int getRange() {
        return range;
    }

    public int getMovementPoint() {
        return movementPoint;
    }

    public int getEraNumber() {
        return eraNumber;
    }

    public ResourceTemplate getRequiredResource() {
        return requiredResource;
    }

    public UnitType getUnitType() {
        return unitType;
    }
}