package models.civilization.enums;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public enum CivilizationNames {
    IRAN("Iran", Color.color(0, .58, .22, 1) ,"IRN", "SUT", new ArrayList<String>(Arrays.asList("IUT", "AUT"))),
    AMERICA("America", Color.color(.03, .19, .38, 1), "USA" ,"MIT", new ArrayList<>(Arrays.asList("MBA", "OCW"))),
    INDIA("India", Color.color(1, .56, .1, 1), "IND" ,"IIT", new ArrayList<>(Arrays.asList("IISc", "IITD"))),
    RUSSIA("Russia", Color.color(0, .44, .8, 1), "RUS" ,"MIT", new ArrayList<>(Arrays.asList("MBA", "OCW"))),
    CHINA("China", Color.color(.78, .06, .18, 1) ,"CHN", "SUT", new ArrayList<String>(Arrays.asList("IUT", "AUT")));

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
