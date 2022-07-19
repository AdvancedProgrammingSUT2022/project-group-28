package controllers;

import models.civilization.Civilization;
import models.civilization.enums.TechnologyTemplate;
import models.tiles.Tile;
import models.tiles.enums.ImprovementTemplate;
import models.units.Civilian;
import models.units.Military;
import models.units.Unit;

import java.util.ArrayList;


public class TileController extends  GameController {

    public static ArrayList<ImprovementTemplate> getTilePossibleImprovements(Tile tile) {
        ArrayList<ImprovementTemplate> result = new ArrayList<>();

        ArrayList<TechnologyTemplate> reachedTechnologies = TechnologyController.extractFullProgressTechnology();
        for (ImprovementTemplate improvement : ImprovementTemplate.values()) {
            if (!reachedTechnologies.contains(improvement.getRequiredTechnology())) continue;
            if (!improvement.isPossiblePlaceToBuild(tile)) continue;
            if (improvement == tile.getImprovement()) continue;
            if (tile.getProject() != null && improvement == tile.getProject().getImprovement()) continue;
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

    public static boolean isFullTile(Tile tile, Unit unit) {
        if (unit instanceof Civilian && tile.getCivilian() != null) return true;
        if (unit instanceof Military && tile.getMilitary() != null) return true;
        return false;
    }

    public static void freeTileUnit(Tile tile, Unit unit) {
        if (unit instanceof Civilian) tile.setCivilian(null);
        if (unit instanceof Military) tile.setMilitary(null);
    }

    public static void setTileUnit(Tile tile, Unit unit) {
        if (unit instanceof Civilian) tile.setCivilian((Civilian) unit);
        if (unit instanceof Military) tile.setMilitary((Military) unit);
    }


    public static Civilization getTileCivilization(Tile tile){
        if (tile.getCity()!=null)return tile.getCity().getCivilization();
        else if (tile.getMilitary()!=null)return tile.getMilitary().getCivilization();
        else if (tile.getCivilian()!=null)return tile.getCivilian().getCivilization();
        else if(tile.getCivilization()!=null)return tile.getCivilization();
        else return null;
    }
}
