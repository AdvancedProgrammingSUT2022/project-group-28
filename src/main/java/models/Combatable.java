package models;

import models.units.Unit;

public interface Combatable {
    public int defend(int attack, Unit attacker);
}
