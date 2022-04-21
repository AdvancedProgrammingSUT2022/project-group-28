package models;

import models.units.Unit;

import java.util.ArrayList;

public class Civilization {
    private User user;
    private final String name;
    private ArrayList<Unit> units;
    // TODO: Complete fields
    public Civilization(User user, String name) {
        this.user = user;
        this.name = name;
    }
}
