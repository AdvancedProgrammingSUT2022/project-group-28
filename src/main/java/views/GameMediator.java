package views;

import models.civilization.City;
import models.units.enums.UnitTemplate;

public class GameMediator {
    private static GameMediator instance;

    private GameMediator() {}

    public static GameMediator getInstance() {
        if (instance == null) instance = new GameMediator();
        return instance;
    }

    public void startUnitConstruction(UnitTemplate unitTemplate, City city) {
        // TODO: adsf
    }

}
