package models.civilization.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum TechnologyTemplate {
    AGRICULTURE("Agriculture", 20, new TechnologyTemplate[] {}),
    ANIMAL_HUSBANDRY("Animal husbandry", 35, new TechnologyTemplate[] {AGRICULTURE}),
    ARCHERY("Archery" , 35 , new TechnologyTemplate[] {AGRICULTURE}),
    POTTERY("Pottery" , 35 , new TechnologyTemplate[]{AGRICULTURE}),
    MINING("Mining" , 35 , new TechnologyTemplate[]{AGRICULTURE}),
    BRONZE_WORKING("Bronze Working" , 55 , new TechnologyTemplate[] {MINING}),
    CALENDAR("Calendar" , 70 , new TechnologyTemplate[]{POTTERY}),
    MASONRY("Masonry" , 55 , new TechnologyTemplate[]{MINING}),
    THE_WHEEL("The Wheel" , 55 , new TechnologyTemplate[]{ANIMAL_HUSBANDRY}),
    TRAPPING("Trapping" , 55 , new TechnologyTemplate[]{ANIMAL_HUSBANDRY}),
    WRITING("Writing" , 55 , new TechnologyTemplate[]{POTTERY}),
    CONSTRUCTION("Construction" , 100 , new TechnologyTemplate[]{MASONRY}),
    HORSEBACK_RIDING("Horseback Riding" , 100 , new TechnologyTemplate[]{THE_WHEEL}),
    IRON_WORKING("Iron Working" , 150 , new TechnologyTemplate[]{BRONZE_WORKING}),
    MATHEMATICS("Mathematics" , 100 , new TechnologyTemplate[]{THE_WHEEL,ARCHERY}),
    PHILOSOPHY("Philosophy" , 100 , new TechnologyTemplate[]{WRITING}),
    CURRENCY("Currency" , 250 , new TechnologyTemplate[]{MATHEMATICS}),
    CIVIL_SERVICE("Civil Service" , 400 , new TechnologyTemplate[]{PHILOSOPHY,TRAPPING}),
    CHIVALRY("Chivalry" , 440 , new TechnologyTemplate[]{CIVIL_SERVICE,HORSEBACK_RIDING,CURRENCY}),
    THEOLOGY("Theology" , 250 , new TechnologyTemplate[]{CALENDAR,PHILOSOPHY}),
    EDUCATION("Education" , 440 , new TechnologyTemplate[]{THEOLOGY}),
    ENGINEERING("Engineering" , 250 , new TechnologyTemplate[]{MATHEMATICS,CONSTRUCTION}),
    MACHINERY("Machinery" , 440 , new TechnologyTemplate[]{ENGINEERING}),
    METAL_CASTING("Metal Casting" , 240 , new TechnologyTemplate[]{IRON_WORKING}),
    PHYSICS("Physics" , 440 , new TechnologyTemplate[]{ENGINEERING,METAL_CASTING}),
    STEEL("Steel" , 440 , new TechnologyTemplate[]{METAL_CASTING}),
    ACOUSTICS("Acoustics"  , 650 , new TechnologyTemplate[]{EDUCATION}),
    ARCHAEOLOGY("Archaeology"  , 1300 , new TechnologyTemplate[]{ACOUSTICS}),
    BANKING("Banking" , 650 , new TechnologyTemplate[]{EDUCATION,CHIVALRY}),
    GUNPOWDER("Gunpowder" , 680 , new TechnologyTemplate[]{PHYSICS,STEEL}),
    CHEMISTRY("Chemistry" , 900 , new TechnologyTemplate[]{GUNPOWDER}),
    PRINTING_PRESS("Printing Press" , 650 , new TechnologyTemplate[]{MACHINERY,PHYSICS}),
    ECONOMICS("Economics" , 900 , new TechnologyTemplate[]{BANKING,PRINTING_PRESS}),
    FERTILIZER("Fertilizer" , 1300 , new TechnologyTemplate[]{CHEMISTRY}),
    MILITARY_SCIENCE("Military Science" , 1300 , new TechnologyTemplate[]{ECONOMICS,CHEMISTRY}),
    SCIENTIFIC_THEORY("Scientific Theory" ,  1300, new TechnologyTemplate[]{ACOUSTICS}),
    STEAM_POWER("Steam Power" , 1680 , new TechnologyTemplate[]{SCIENTIFIC_THEORY,MILITARY_SCIENCE}),
    RAILROAD("Railroad" , 1900 , new TechnologyTemplate[]{STEAM_POWER}),
    METALLURGY("Metallurgy" , 900 , new TechnologyTemplate[]{GUNPOWDER}),
    REPLACEABLE_PARTS("Replaceable Parts" , 1900 , new TechnologyTemplate[]{STEAM_POWER}),
    RIFLING("Rifling" , 1425 , new TechnologyTemplate[]{METALLURGY}),
    BIOLOGY("Biology" , 1680 , new TechnologyTemplate[]{ARCHAEOLOGY,SCIENTIFIC_THEORY}),
    DYNAMITE("Dynamite" , 1900 , new TechnologyTemplate[]{FERTILIZER,RIFLING}),
    COMBUSTION("Combustion" , 2200 , new TechnologyTemplate[]{REPLACEABLE_PARTS,RAILROAD,DYNAMITE}),
    ELECTRICITY("Electricity" , 1900 , new TechnologyTemplate[]{BIOLOGY,STEAM_POWER}),
    RADIO("Radio" , 2200 , new TechnologyTemplate[]{ELECTRICITY}),
    TELEGRAPH("Telegraph" , 2200 , new TechnologyTemplate[]{ELECTRICITY});


    private String name;
    private int cost;
    private ArrayList<TechnologyTemplate> requiredTechnologies;

    TechnologyTemplate(String name, int cost, TechnologyTemplate[] technologyTemplates) {
        this.name = name;
        this.cost = cost;
        this.requiredTechnologies = new ArrayList<>(Arrays.asList(technologyTemplates));

    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList<TechnologyTemplate> getRequiredTechnologies() {
        return requiredTechnologies;
    }
}
