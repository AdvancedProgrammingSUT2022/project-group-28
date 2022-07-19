package models.civilization;

import models.civilization.enums.BuildingTemplate;

public class Building {
    private BuildingTemplate buildingTemplate;
    private int progress = 0;
    private int filledSlots = 0;

    public Building(BuildingTemplate buildingTemplate, int progress, int filledSlots) {
        this.buildingTemplate = buildingTemplate;
        this.progress = progress;
        this.filledSlots = filledSlots;
    }

    public BuildingTemplate getBuildingTemplate() {
        return buildingTemplate;
    }

    public void setBuildingTemplate(BuildingTemplate buildingTemplate) {
        this.buildingTemplate = buildingTemplate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getFilledSlots() {
        return filledSlots;
    }
    
    public void setFilledSlots(int filledSlots) {
        this.filledSlots = filledSlots;
    }
}
