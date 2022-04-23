package models.tiles;

import models.tiles.enums.ImprovementTemplate;

public class Project {
    private ImprovementTemplate improvement;
    private int spentTurns;

    public Project(ImprovementTemplate improvement) {
        this.improvement = improvement;
        this.spentTurns = 0;
    }

}
