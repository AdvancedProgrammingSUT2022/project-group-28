package controllers;

import com.sun.security.auth.UnixNumericUserPrincipal;
import models.civilization.Technology;
import models.civilization.enums.TechnologyTemplate;
import views.enums.CivilizationMessage;
import views.enums.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TechnologyController extends GameController {


    public static CivilizationMessage checkTechnologyStudyPossible(){
        Technology tempTechnology = game.getCurrentPlayer().getCurrentStudyTechnology();
        if (tempTechnology != null){
            return CivilizationMessage.STUDY_TWO_TECHNOLOGIES_SIMULTANEOUSLY;
        }
        return CivilizationMessage.SUCCESS;

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
            out = out + number + "- " +  tecknology.getName() + "\t" +
                    (int) Math.ceil((double)tecknology.getCost() / scienceBalance) + " turns\n" ;
            number++;
        }
        return out;
    }

    public static CivilizationMessage checkNumber(String input) {
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = PossibleTechnology();
        int number = Integer.parseInt(input);
        if(number <= 0 || number > possibleTechnologyTemplates.size()){
            return CivilizationMessage.OUT_OF_RANGE;
        }
        getNewTechnology(number);
        return CivilizationMessage.SUCCESS;
    }


    private static void getNewTechnology(int number){
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = PossibleTechnology();
        int index = number - 1;
        Technology technology = new Technology(possibleTechnologyTemplates.get(index) , 0);
        //TODO complete about remain breakers
        game.getCurrentPlayer().addTechnology(technology);
        game.getCurrentPlayer().setCurrentStudyTechnology(technology);

    }

}
