package models.civilization.enums;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public enum CivilizationNames {
    AMERICA("AMERICA", Color.color(.63, .67, .29, 1), "USA" ,"MIT", new ArrayList<>(Arrays.asList("MBA", "OCW"))),
    IRAN("Iran", Color.color(.39, .87, .86, 1) ,"IRN", "SUT", new ArrayList<String>(Arrays.asList("IUT", "AUT")));
    // TODO: add more civilizations
    private String name;
    private Color color;
    private String mapSign;
    private String capital;
    private ArrayList<String> cities;

    CivilizationNames(String name, Color color, String mapSign, String capital, ArrayList<String> cities) {
        this.name = name;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public String getCapital() {
        return capital;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public String getColorHex() {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}
