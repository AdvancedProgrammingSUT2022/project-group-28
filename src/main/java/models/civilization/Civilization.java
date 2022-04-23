package models.civilization;

import models.User;
import models.units.Unit;

import java.util.ArrayList;

public class Civilization {
    private User user;
    private final String name;
    private City Capital;
    private ArrayList<City> cities;
    private ArrayList<Unit> units;

    private int goldBalance;
    private int scienceBalance;
    private int happinessBalance;
    private int happiness;


    // TODO: Complete fields
    public Civilization(User user, String name) {
        this.user = user;
        this.name = name;
    }
}
