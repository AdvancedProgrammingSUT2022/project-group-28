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

    private ArrayList<Technology> studiedTechnologies;

    private int goldBalance;
    private int scienceBalance;
    private int happinessBalance;
    private int happiness;


    // TODO: Complete fields
    public Civilization(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public City getCapital() {
        return Capital;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Technology> getStudiedTechnologies() {
        return studiedTechnologies;
    }

    public int getGoldBalance() {
        return goldBalance;
    }

    public int getScienceBalance() {
        return scienceBalance;
    }

    public int getHappinessBalance() {
        return happinessBalance;
    }

    public int getHappiness() {
        return happiness;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }
}
