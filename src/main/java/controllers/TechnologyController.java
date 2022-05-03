package controllers;

import models.User;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.Technology;
import models.civilization.enums.BuildingTemplate;
import models.civilization.enums.TechnologyTemplate;
import models.units.enums.UnitTemplate;
import views.GameMenu;
import views.enums.CivilizationMessage;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TechnologyController extends GameController {


    public static CivilizationMessage checkTechnologyStudyPossible(){
        Technology tempTechnology = game.getCurrentPlayer().getCurrentStudyTechnology();
        if (tempTechnology != null){
            return CivilizationMessage.CHANGE_CURRENT_STUDY_TECHNOLOGY;
        }
        return CivilizationMessage.SHOW_LIST;

    }

    public static ArrayList<TechnologyTemplate> PossibleTechnology(){

        ArrayList<TechnologyTemplate> userFullTechnologyTemplates = extractFullProgressTechnology();
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = new ArrayList<>();
        boolean flag = true;
        for (TechnologyTemplate technology: TechnologyTemplate.values()) {
            for (int i = 0; i < technology.getRequiredTechnologies().size(); i++) {
                if(!userFullTechnologyTemplates.contains(technology.getRequiredTechnologies().get(i))){
                    flag = false;
                }
            }
            if (flag && !userFullTechnologyTemplates.contains(technology)){
                possibleTechnologyTemplates.add(technology);
            }
            flag = true;
        }
        return  possibleTechnologyTemplates;
    }

    public static ArrayList<TechnologyTemplate> extractFullProgressTechnology(){
        ArrayList<Technology> userTechnologies = game.getCurrentPlayer().getStudiedTechnologies();
        ArrayList<TechnologyTemplate> userFullTechnologyTemplates = new ArrayList<>();
        for (Technology technology:userTechnologies) {
            if (technology.getTechnologyTemplate().getCost() == technology.getProgress()){
                userFullTechnologyTemplates.add(technology.getTechnologyTemplate());
            }
        }
        return userFullTechnologyTemplates;
    }
    public static String printPossibleTechnology(){
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = PossibleTechnology();
        int scienceBalance = game.getCurrentPlayer().getScienceBalance();
        String out = "";
        int number = 1 ;
        for (TechnologyTemplate tecknology: possibleTechnologyTemplates) {
            int progress = 0 ;
            if (getTechnology(tecknology) != null){
                progress = getTechnology(tecknology).getProgress();
            }
            out = out + number + "- " +  tecknology.getName() + "\t" +
                    (int) Math.ceil((double)(tecknology.getCost()-progress) / addEachTurnScienceBalance()) + " turns\n" ;
            number++;
        }
        return out;
    }

    public static CivilizationMessage checkNumber(String input) {
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = PossibleTechnology();
        boolean inputValidation = input.matches("\\d+");
        if(!inputValidation){
            return CivilizationMessage.INVALID_INPUT;
        }
        int number = Integer.parseInt(input);
        if(number <= 0 || number > possibleTechnologyTemplates.size()){
            return CivilizationMessage.OUT_OF_RANGE;
        }
        getNewTechnology(number);
        return CivilizationMessage.SUCCESS;
    }


    private static void getNewTechnology(int number){
        Civilization tempCivilization = game.getCurrentPlayer();
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = PossibleTechnology();
        ArrayList<Technology> UserTechnologies = game.getCurrentPlayer().getStudiedTechnologies();
        int index = number - 1;
        if (getTechnology(possibleTechnologyTemplates.get(index)) == null){
            Technology technology = new Technology(possibleTechnologyTemplates.get(index) , game.getCurrentPlayer().getScienceBalance());
            game.getCurrentPlayer().addTechnology(technology);

        }
        game.getCurrentPlayer().setCurrentStudyTechnology(getTechnology(possibleTechnologyTemplates.get(index)));

    }

    private static Technology getTechnology(TechnologyTemplate technologyTemplate){
        ArrayList<Technology> UserTechnologies = game.getCurrentPlayer().getStudiedTechnologies();
        for (Technology technology : UserTechnologies) {
            if(technology.getTechnologyTemplate().getName().equals(technologyTemplate.getName())){
                return technology;
            }
        }
        return null;
    }

    public static boolean updateNextTurnTechnology(){
        Technology currentTechnology = game.getCurrentPlayer().getCurrentStudyTechnology();
        Civilization civilization = game.getCurrentPlayer();
        updateScienceBalance();
        if(currentTechnology.getProgress() + civilization.getScienceBalance() < currentTechnology.getTechnologyTemplate().getCost()){
            currentTechnology.setProgress(currentTechnology.getProgress() + civilization.getScienceBalance());
            civilization.setScienceBalance(0);
        } else{
            int overFlow = (currentTechnology.getProgress() + civilization.getScienceBalance()) -
                            currentTechnology.getTechnologyTemplate().getCost();


            currentTechnology.setProgress(currentTechnology.getTechnologyTemplate().getCost());
            civilization.setScienceBalance(overFlow);
            game.getCurrentPlayer().setCurrentStudyTechnology(null);
            return true;
        }
        return false;

    }

    private static void updateScienceBalance() {
        Civilization civilization = game.getCurrentPlayer();
        civilization.setScienceBalance(civilization.getScienceBalance() + addEachTurnScienceBalance());
    }

    private static int addEachTurnScienceBalance(){
        return calculateThePopulation()+3;      //+3 is constant
    }
    private static int calculateThePopulation(){
        Civilization civilization = game.getCurrentPlayer();
        int population = 0;
        for (City city: civilization.getCities()) {
            population += city.getPopulation();
        }
        return population;
    }


    public static String printCompleteTechnologyInfo() {
        ArrayList<TechnologyTemplate> fullTechnologies = extractFullProgressTechnology();
        TechnologyTemplate nameOfLastTechnology = fullTechnologies.get(fullTechnologies.size()-1);
        String out = nameOfLastTechnology.getName() + " technology completed\n" + "Resources you obtained :\n" +
                "## UNITS :\n" + extractTheObtainedUnits(nameOfLastTechnology) +
                "## BULDINGS :\n" + extractTheObtainedBuildings(nameOfLastTechnology);
        return out;
    }

    private static String extractTheObtainedUnits(TechnologyTemplate technologyTemplate){
        String out = "";
        for (UnitTemplate unit: UnitTemplate.values()) {
            if(unit.getRequiredTechnology() != null &&
                    unit.getRequiredTechnology().getName().equals(technologyTemplate.getName())){
                out = out + "   -> " + unit.getName() + "\n";
            }
        }
        if(out.equals("")){
            out = "      nothing\n";
        }
        return out;
    }
    private static String extractTheObtainedBuildings(TechnologyTemplate technologyTemplate){
        String out = "";
        for (BuildingTemplate building: BuildingTemplate.values()) {
            if(building.getRequiredTechnology() != null &&
                    building.getRequiredTechnology().getName().equals(technologyTemplate.getName())){
                out = out + "   -> " + building.getName() + "\n";
            }
        }
        if(out.equals("")){
            out = "      nothing\n";
        }
        return out;
    }
}