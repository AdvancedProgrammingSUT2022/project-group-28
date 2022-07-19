package models.civilization;

import models.civilization.enums.TechnologyTemplate;

public class Technology {
    private TechnologyTemplate technologyTemplate;
    private int progress;


    public Technology(TechnologyTemplate technologyTemplate, int progress) {
        this.technologyTemplate = technologyTemplate;
        this.progress = progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public TechnologyTemplate getTechnologyTemplate() {
        return technologyTemplate;
    }
}
