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

    public static int getDistance(Tile tile1, Tile tile2) {
        int[] coordinates1 = tile1.getCoordinates();
        int[] coordinates2 = tile2.getCoordinates();
        
        int distance = (Math.abs(coordinates1[0] - coordinates2[0]) +
                        Math.abs(coordinates1[1] - coordinates2[1]) +
                        Math.abs(coordinates1[0] + coordinates1[1] - coordinates2[0] - coordinates2[1])) / 2;
        return distance;
        }
}
