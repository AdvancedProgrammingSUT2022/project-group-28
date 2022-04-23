package models.civilization;

import models.civilization.enums.BuildingTemplate;

public class Building {
    private BuildingTemplate buildingTemplate;
    private int progress = 0;
    private int filledSlots = 0;
}
