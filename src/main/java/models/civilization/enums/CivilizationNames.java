package models.civilization.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum CivilizationNames {
    AMERICA("AMERICA", "MIT", new ArrayList<>(Arrays.asList("MBA", "Silicon"))),
    IRAN("Iran", "TEHRAN", new ArrayList<String>(Arrays.asList("Pars", "Abadan", "Rasht")));
    // TODO: add more civilizations
    // TODO: add color to civilizations 
    private String name;
    private String capital;
    private ArrayList<String> cities;

    CivilizationNames(String name, String capital, ArrayList<String> cities) {
        this.name = name;
        this.capital = capital;
        this.cities = cities;
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public ArrayList<String> getCities() {
        return cities;
    }
}
