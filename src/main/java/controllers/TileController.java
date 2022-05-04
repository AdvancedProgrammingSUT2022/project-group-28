package controllers;

import models.civilization.enums.TechnologyTemplate;
import models.tiles.Tile;
import models.tiles.enums.ImprovementTemplate;

import java.util.ArrayList;


public class TileController extends  GameController {

    public static ArrayList<ImprovementTemplate> getTilePossibleImprovements(Tile tile) {
        ArrayList<ImprovementTemplate> result = new ArrayList<>();

        ArrayList<TechnologyTemplate> reachedTechnologies = TechnologyController.extractFullProgressTechnology();
        for (ImprovementTemplate improvement : ImprovementTemplate.values()) {
            if (!reachedTechnologies.contains(improvement.getRequiredTechnology())) continue;
            if (!improvement.isPossiblePlaceToBuild(tile)) continue;
            result.add(improvement);
        }

        return result;
    }

}
