package models.civilization.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum CivilizationNames {
    AMERICA("AMERICA","USA" ,"MIT", new ArrayList<>(Arrays.asList("MBA", "OCW"))),
    IRAN("Iran", "IR#","SUT", new ArrayList<String>(Arrays.asList("IUT", "AUT")));
    // TODO: add more civilizations
    // TODO: add color to civilizations 
    private String name;
    private String mapSign;
    private String capital;
    private ArrayList<String> cities;

    CivilizationNames(String name, String mapSign, String capital, ArrayList<String> cities) {
        this.name = name;
        this.mapSign =mapSign;
        this.capital = capital;
        this.cities = cities;
    }

    public String getMapSign() {
        return mapSign;
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
