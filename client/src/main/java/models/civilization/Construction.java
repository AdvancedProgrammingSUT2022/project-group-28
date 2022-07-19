package models.civilization;

import models.Constructable;

public class Construction {
    private final Constructable constructionTemplate;
    private int spentProduction = 0;
    public Construction(Constructable constructionTemplate) {
        this.constructionTemplate = constructionTemplate;
    }

    public Constructable getConstructionTemplate() {
        return constructionTemplate;
    }

    public int getSpentProduction() {
        return spentProduction;
    }

    public void setSpentProduction(int spentProduction) {
        this.spentProduction = spentProduction;
    }
}
