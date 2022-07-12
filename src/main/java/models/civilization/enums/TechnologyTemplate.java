package models.civilization.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum TechnologyTemplate {
    AGRICULTURE("Agriculture","agriculture", 20, new TechnologyTemplate[] {}),
    ANIMAL_HUSBANDRY("Animal husbandry","animal_husbandry", 35, new TechnologyTemplate[] {AGRICULTURE}),
    ARCHERY("Archery" ,"archery", 35 , new TechnologyTemplate[] {AGRICULTURE}),
    POTTERY("Pottery" ,"pottery", 35 , new TechnologyTemplate[]{AGRICULTURE}),
    MINING("Mining" ,"mining", 35 , new TechnologyTemplate[]{AGRICULTURE}),
    BRONZE_WORKING("Bronze Working" ,"bronze_working", 55 , new TechnologyTemplate[] {MINING}),
    CALENDAR("Calendar" ,"calendar", 70 , new TechnologyTemplate[]{POTTERY}),
    MASONRY("Masonry" ,"masonry", 55 , new TechnologyTemplate[]{MINING}),
    THE_WHEEL("The Wheel" ,"the_wheel", 55 , new TechnologyTemplate[]{ANIMAL_HUSBANDRY}),
    TRAPPING("Trapping" ,"trapping", 55 , new TechnologyTemplate[]{ANIMAL_HUSBANDRY}),
    WRITING("Writing" ,"writing", 55 , new TechnologyTemplate[]{POTTERY}),
    CONSTRUCTION("Construction" ,"construction", 100 , new TechnologyTemplate[]{MASONRY}),
    HORSEBACK_RIDING("Horseback Riding" ,"horseback_riding", 100 , new TechnologyTemplate[]{THE_WHEEL}),
    IRON_WORKING("Iron Working" ,"iron_working", 150 , new TechnologyTemplate[]{BRONZE_WORKING}),
    MATHEMATICS("Mathematics" ,"mathematics", 100 , new TechnologyTemplate[]{THE_WHEEL,ARCHERY}),
    PHILOSOPHY("Philosophy" ,"philosophy", 100 , new TechnologyTemplate[]{WRITING}),
    CURRENCY("Currency" ,"currency", 250 , new TechnologyTemplate[]{MATHEMATICS}),
    CIVIL_SERVICE("Civil Service" ,"civil_service", 400 , new TechnologyTemplate[]{PHILOSOPHY,TRAPPING}),
    CHIVALRY("Chivalry" ,"chivalry", 440 , new TechnologyTemplate[]{CIVIL_SERVICE,HORSEBACK_RIDING,CURRENCY}),
    THEOLOGY("Theology" ,"theology", 250 , new TechnologyTemplate[]{CALENDAR,PHILOSOPHY}),
    EDUCATION("Education" ,"education", 440 , new TechnologyTemplate[]{THEOLOGY}),
    ENGINEERING("Engineering" ,"engineering", 250 , new TechnologyTemplate[]{MATHEMATICS,CONSTRUCTION}),
    MACHINERY("Machinery" ,"machinery", 440 , new TechnologyTemplate[]{ENGINEERING}),
    METAL_CASTING("Metal Casting" ,"metal_casting", 240 , new TechnologyTemplate[]{IRON_WORKING}),
    PHYSICS("Physics" ,"physics", 440 , new TechnologyTemplate[]{ENGINEERING,METAL_CASTING}),
    STEEL("Steel" ,"steel", 440 , new TechnologyTemplate[]{METAL_CASTING}),
    ACOUSTICS("Acoustics"  ,"acoustics", 650 , new TechnologyTemplate[]{EDUCATION}),
    ARCHAEOLOGY("Archaeology"  ,"archaeology", 1300 , new TechnologyTemplate[]{ACOUSTICS}),
    BANKING("Banking" ,"banking", 650 , new TechnologyTemplate[]{EDUCATION,CHIVALRY}),
    GUNPOWDER("Gunpowder" ,"gunpowder", 680 , new TechnologyTemplate[]{PHYSICS,STEEL}),
    CHEMISTRY("Chemistry" ,"", 900 , new TechnologyTemplate[]{GUNPOWDER}),
    PRINTING_PRESS("Printing Press" ,"", 650 , new TechnologyTemplate[]{MACHINERY,PHYSICS}),
    ECONOMICS("Economics" ,"", 900 , new TechnologyTemplate[]{BANKING,PRINTING_PRESS}),
    FERTILIZER("Fertilizer" ,"", 1300 , new TechnologyTemplate[]{CHEMISTRY}),
    MILITARY_SCIENCE("Military Science" ,"", 1300 , new TechnologyTemplate[]{ECONOMICS,CHEMISTRY}),
    SCIENTIFIC_THEORY("Scientific Theory" ,"",  1300, new TechnologyTemplate[]{ACOUSTICS}),
    STEAM_POWER("Steam Power" ,"", 1680 , new TechnologyTemplate[]{SCIENTIFIC_THEORY,MILITARY_SCIENCE}),
    RAILROAD("Railroad" ,"", 1900 , new TechnologyTemplate[]{STEAM_POWER}),
    METALLURGY("Metallurgy" ,"", 900 , new TechnologyTemplate[]{GUNPOWDER}),
    REPLACEABLE_PARTS("Replaceable Parts" ,"", 1900 , new TechnologyTemplate[]{STEAM_POWER}),
    RIFLING("Rifling" ,"", 1425 , new TechnologyTemplate[]{METALLURGY}),
    BIOLOGY("Biology" ,"", 1680 , new TechnologyTemplate[]{ARCHAEOLOGY,SCIENTIFIC_THEORY}),
    DYNAMITE("Dynamite" ,"", 1900 , new TechnologyTemplate[]{FERTILIZER,RIFLING}),
    COMBUSTION("Combustion" ,"", 2200 , new TechnologyTemplate[]{REPLACEABLE_PARTS,RAILROAD,DYNAMITE}),
    ELECTRICITY("Electricity" ,"", 1900 , new TechnologyTemplate[]{BIOLOGY,STEAM_POWER}),
    RADIO("Radio" ,"", 2200 , new TechnologyTemplate[]{ELECTRICITY}),
    TELEGRAPH("Telegraph" ,"", 2200 , new TechnologyTemplate[]{ELECTRICITY});


    private String name;
    private String fileName;
    private int cost;
    private ArrayList<TechnologyTemplate> requiredTechnologies;

    TechnologyTemplate(String name ,String fileName , int cost, TechnologyTemplate[] technologyTemplates) {
        this.name = name;
        this.cost = cost;
        this.fileName = fileName;
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
