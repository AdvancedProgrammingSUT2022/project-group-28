package models;

import models.enums.ImprovementTemplate;

public class Project {
    private ImprovementTemplate improvement;
    private int spentTurns;

    public Project(ImprovementTemplate improvement) {
        this.improvement = improvement;
        this.spentTurns = 0;
    }

}
