package models.civilization.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum TechnologyTemplate {
    AGRICULTURE("Agriculture", 20, new TechnologyTemplate[] {});

    private String name;
    private int cost;
    private ArrayList<TechnologyTemplate> requiredTechnologies;

    TechnologyTemplate(String name, int cost, TechnologyTemplate[] technologyTemplates) {
        this.name = name;
        this.cost = cost;
        this.requiredTechnologies = new ArrayList<>(Arrays.asList(technologyTemplates));

    }

}