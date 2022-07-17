package controllers;

import models.Game;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.Technology;
import models.civilization.enums.BuildingTemplate;
import models.civilization.enums.TechnologyTemplate;
import models.units.enums.UnitTemplate;
import views.enums.CivilizationMessage;
import views.notifications.CivilizationNotification;
import views.notifications.GameNotification;

import java.util.ArrayList;
import java.util.Arrays;

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
        Civilization civilization = game.getCurrentPlayer();
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = PossibleTechnology();
        String out = "";
        int number = 1 ;
        for (TechnologyTemplate technology: possibleTechnologyTemplates) {
            int progress = 0 ;
            if (getTechnology(technology) != null){
                progress = getTechnology(technology).getProgress();
            }
            out = out + number + "- " +  technology.getName() + "\t" +
                    (int) Math.ceil((double)(technology.getCost()-progress) / addEachTurnScienceBalance(civilization)) + " turns\n" ;
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
        getNewTechnology(changeNumberToTechnologyTemplate(number));
        return CivilizationMessage.SUCCESS;
    }


    private static void getNewTechnology(TechnologyTemplate technologyTemplate){
        if (getTechnology(technologyTemplate) == null){
            Technology technology = new Technology(technologyTemplate , game.getCurrentPlayer().getScienceBalance());
            game.getCurrentPlayer().addTechnology(technology);

        }
        game.getCurrentPlayer().setCurrentStudyTechnology(getTechnology(technologyTemplate));
    }

    private static TechnologyTemplate changeNumberToTechnologyTemplate(int number){
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = PossibleTechnology();
        int index = number - 1;
        return possibleTechnologyTemplates.get(index);
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

    public static void updateNextTurnTechnology(){
        Technology currentTechnology = game.getCurrentPlayer().getCurrentStudyTechnology();
        if (currentTechnology == null) return;
        Civilization civilization = game.getCurrentPlayer();
        updateScienceBalance();
        if (currentTechnology.getProgress() + civilization.getScienceBalance() < currentTechnology.getTechnologyTemplate().getCost()) {
            currentTechnology.setProgress(currentTechnology.getProgress() + civilization.getScienceBalance());
            civilization.setScienceStore(0);
        } else {
            int overFlow = (currentTechnology.getProgress() + civilization.getScienceBalance()) -
                    currentTechnology.getTechnologyTemplate().getCost();
            currentTechnology.setProgress(currentTechnology.getTechnologyTemplate().getCost());
            civilization.setScienceStore(overFlow);
        }
    }

    public static void updateScienceBalance() {
        Civilization civilization = game.getCurrentPlayer();
        civilization.setScienceBalance(addEachTurnScienceBalance(civilization));
    }


    // TODO: change coefficient
    public static int addEachTurnScienceBalance(Civilization civilization){
        if (civilization.getGold() < 0) return (int)((calculateThePopulation() + 3) * 0.7);
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

    public static void checkCompletionOfTechnology(Game game, Civilization civilization) {
        Technology currentStudy = civilization.getCurrentStudyTechnology();
        if (currentStudy == null) return;
        if (currentStudy.getTechnologyTemplate().getCost() == currentStudy.getProgress()) {
            TechnologyTemplate currentStudyTemplate = currentStudy.getTechnologyTemplate();

            ArrayList<String> data = new ArrayList<>(Arrays.asList(currentStudyTemplate.getName(), printCompleteTechnologyInfo()));
            GameNotification technologyCompletion = new GameNotification(CivilizationNotification.COMPLETION_OF_STUDY,
                                                                         data, game.getTurnNumber());
            civilization.addGameNotification(technologyCompletion);

            civilization.setCurrentStudyTechnology(null);
        }
    }


    public static String printCompleteTechnologyInfo() {
        ArrayList<TechnologyTemplate> fullTechnologies = extractFullProgressTechnology();
        TechnologyTemplate nameOfLastTechnology = fullTechnologies.get(fullTechnologies.size()-1);
        String out = "Resources you obtained :\n" +
                "## UNITS :\n" + extractTheObtainedUnits(nameOfLastTechnology) +
                "## BUILDINGS :\n" + extractTheObtainedBuildings(nameOfLastTechnology);
        return out;
    }

    public static String extractTheObtainedUnits(TechnologyTemplate technologyTemplate){
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
    
    public static String extractTheObtainedBuildings(TechnologyTemplate technologyTemplate){
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

    public static String printAllRemainingTechnology(){
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = new ArrayList<>();
        for(TechnologyTemplate template: TechnologyTemplate.values()){
            if(extractFullProgressTechnology().contains(template)){
                continue;
            }
            possibleTechnologyTemplates.add(template);
        }
        String out = "";
        int number = 1 ;
        for (TechnologyTemplate technology: possibleTechnologyTemplates) {
            out = out + number + "- " +  technology.getName() + "\n";
            number++;
        }
        return out;
    }

    public static CivilizationMessage checkCheatNumber(String input) {
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = new ArrayList<>();
        for(TechnologyTemplate template: TechnologyTemplate.values()){
            if(extractFullProgressTechnology().contains(template)){
                continue;
            }
            possibleTechnologyTemplates.add(template);
        }
        boolean inputValidation = input.matches("\\d+");
        if(!inputValidation){
            return CivilizationMessage.INVALID_INPUT;
        }
        int number = Integer.parseInt(input);
        if(number <= 0 || number > possibleTechnologyTemplates.size()){
            return CivilizationMessage.OUT_OF_RANGE;
        }
        if(checkNotCompleteTechnology(possibleTechnologyTemplates.get(number-1)) != null){
            Technology technology = checkNotCompleteTechnology(possibleTechnologyTemplates.get(number-1));
            technology.setProgress(technology.getTechnologyTemplate().getCost());
        }
        else {
            Technology technology = new Technology(possibleTechnologyTemplates.get(number-1), possibleTechnologyTemplates.get(number-1).getCost());
            game.getCurrentPlayer().addTechnology(technology);
        }
        return CivilizationMessage.SUCCESS;
    }

    private static Technology checkNotCompleteTechnology(TechnologyTemplate technologyTemplate){
        for (Technology technology : game.getCurrentPlayer().getStudiedTechnologies()) {
            if(technology.getTechnologyTemplate().equals(technologyTemplate)){
                return technology;
            }
        }
        return null;
    }
}
