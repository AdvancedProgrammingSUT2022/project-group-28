package models.tiles;

import models.tiles.enums.ImprovementTemplate;

public class Project {
    private ImprovementTemplate improvement;
    private int spentTurns;
    private boolean broken;

    public Project(ImprovementTemplate improvement) {
        this.improvement = improvement;
        this.spentTurns = 0;
        this.broken = false;
    }
    public Project(ImprovementTemplate improvement, boolean broken) {
        this.improvement = improvement;
        this.spentTurns = improvement.getTurnCost() - 3;
        this.broken = true;
    }

    public ImprovementTemplate getImprovement() {
        return improvement;
    }

    public int getSpentTurns() {
        return spentTurns;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setSpentTurns(int spentTurns) {
        this.spentTurns = spentTurns;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }
}
