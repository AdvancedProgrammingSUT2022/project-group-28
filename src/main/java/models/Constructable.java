package models;

import models.civilization.City;
import views.enums.CityMessage;

public interface Constructable {
    CityMessage checkPossibilityOfConstruction(City city);
    String getName();
    int getCost();
}
