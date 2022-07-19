package models.civilization.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum TechnologyTemplate {
    AGRICULTURE("Agriculture","agriculture",80,410, 20, new TechnologyTemplate[] {}),
    ANIMAL_HUSBANDRY("Animal husbandry","animal_husbandry",480,320, 35, new TechnologyTemplate[] {AGRICULTURE}),
    ARCHERY("Archery" ,"archery", 480 ,510,35, new TechnologyTemplate[] {AGRICULTURE}),
    POTTERY("Pottery" ,"pottery", 480 ,90,35, new TechnologyTemplate[]{AGRICULTURE}),
    MINING("Mining" ,"mining", 480 ,680,35, new TechnologyTemplate[]{AGRICULTURE}),
    BRONZE_WORKING("Bronze Working" ,"bronze_working",880,780, 55 , new TechnologyTemplate[] {MINING}),
    CALENDAR("Calendar" ,"calendar",880,40, 70 , new TechnologyTemplate[]{POTTERY}),
    MASONRY("Masonry" ,"masonry",880,680, 55 , new TechnologyTemplate[]{MINING}),
    THE_WHEEL("The Wheel" ,"the_wheel",880,510, 55 , new TechnologyTemplate[]{ANIMAL_HUSBANDRY}),
    TRAPPING("Trapping" ,"trapping",880,320, 55 , new TechnologyTemplate[]{ANIMAL_HUSBANDRY}),
    WRITING("Writing" ,"writing",880,140, 55 , new TechnologyTemplate[]{POTTERY}),
    CONSTRUCTION("Construction" ,"construction",1280,680, 100 , new TechnologyTemplate[]{MASONRY}),
    HORSEBACK_RIDING("Horseback Riding" ,"horseback_riding",1280,320, 100 , new TechnologyTemplate[]{THE_WHEEL}),
    IRON_WORKING("Iron Working" ,"iron_working",1680,780, 150 , new TechnologyTemplate[]{BRONZE_WORKING}),
    MATHEMATICS("Mathematics" ,"mathematics",1280,510, 100 , new TechnologyTemplate[]{THE_WHEEL,ARCHERY}),
    PHILOSOPHY("Philosophy" ,"philosophy",1680,40, 100 , new TechnologyTemplate[]{WRITING}),
    CURRENCY("Currency" ,"currency",1680,510, 250 , new TechnologyTemplate[]{MATHEMATICS}),
    CIVIL_SERVICE("Civil Service" ,"civil_service",2080,320, 400 , new TechnologyTemplate[]{PHILOSOPHY,TRAPPING}),
    CHIVALRY("Chivalry" ,"chivalry",2480,400, 440 , new TechnologyTemplate[]{CIVIL_SERVICE,HORSEBACK_RIDING,CURRENCY}),
    THEOLOGY("Theology" ,"theology",2080,40, 250 , new TechnologyTemplate[]{CALENDAR,PHILOSOPHY}),
    EDUCATION("Education" ,"education",2480,140, 440 , new TechnologyTemplate[]{THEOLOGY}),
    ENGINEERING("Engineering" ,"engineering",1680,590, 250 , new TechnologyTemplate[]{MATHEMATICS,CONSTRUCTION}),
    MACHINERY("Machinery" ,"machinery",2480,590, 440 , new TechnologyTemplate[]{ENGINEERING}),
    METAL_CASTING("Metal Casting" ,"metal_casting",2080,680, 240 , new TechnologyTemplate[]{IRON_WORKING}),
    PHYSICS("Physics" ,"physics",2480,680, 440 , new TechnologyTemplate[]{ENGINEERING,METAL_CASTING}),
    STEEL("Steel" ,"steel",2480,780, 440 , new TechnologyTemplate[]{METAL_CASTING}),
    ACOUSTICS("Acoustics"  ,"acoustics",2880,220, 650 , new TechnologyTemplate[]{EDUCATION}),
    ARCHAEOLOGY("Archaeology"  ,"archaeology",3680,40, 1300 , new TechnologyTemplate[]{ACOUSTICS}),
    BANKING("Banking" ,"banking",2880,400, 650 , new TechnologyTemplate[]{EDUCATION,CHIVALRY}),
    GUNPOWDER("Gunpowder" ,"gunpowder",2880,780, 680 , new TechnologyTemplate[]{PHYSICS,STEEL}),
    CHEMISTRY("Chemistry" ,"chemistry",3280,780, 900 , new TechnologyTemplate[]{GUNPOWDER}),
    PRINTING_PRESS("Printing Press" ,"printing_press",2880,590, 650 , new TechnologyTemplate[]{MACHINERY,PHYSICS}),
    ECONOMICS("Economics" ,"economics",3280,400, 900 , new TechnologyTemplate[]{BANKING,PRINTING_PRESS}),
    FERTILIZER("Fertilizer" ,"fertilizer",3680,780, 1300 , new TechnologyTemplate[]{CHEMISTRY}),
    MILITARY_SCIENCE("Military Science" ,"military_science",3680,680, 1300 , new TechnologyTemplate[]{ECONOMICS,CHEMISTRY}),
    SCIENTIFIC_THEORY("Scientific Theory" ,"scientific_theory",3680,220,  1300, new TechnologyTemplate[]{ACOUSTICS}),
    STEAM_POWER("Steam Power" ,"steam_power",4080,400, 1680 , new TechnologyTemplate[]{SCIENTIFIC_THEORY,MILITARY_SCIENCE}),
    RAILROAD("Railroad" ,"railroad",4480,590, 1900 , new TechnologyTemplate[]{STEAM_POWER}),
    METALLURGY("Metallurgy" ,"metallurgy",3280,680, 900 , new TechnologyTemplate[]{GUNPOWDER}),
    REPLACEABLE_PARTS("Replaceable Parts" ,"replaceable_parts",4480,220, 1900 , new TechnologyTemplate[]{STEAM_POWER}),
    RIFLING("Rifling" ,"rifling",3680,510, 1425 , new TechnologyTemplate[]{METALLURGY}),
    BIOLOGY("Biology" ,"biology",4080,40, 1680 , new TechnologyTemplate[]{ARCHAEOLOGY,SCIENTIFIC_THEORY}),
    DYNAMITE("Dynamite" ,"dynamite",4080,680 ,1900 , new TechnologyTemplate[]{FERTILIZER,RIFLING}),
    COMBUSTION("Combustion" ,"combustion",4880,590, 2200 , new TechnologyTemplate[]{REPLACEABLE_PARTS,RAILROAD,DYNAMITE}),
    ELECTRICITY("Electricity" ,"electricity",4080,140, 1900 , new TechnologyTemplate[]{BIOLOGY,STEAM_POWER}),
    RADIO("Radio" ,"radio",4480,140, 2200 , new TechnologyTemplate[]{ELECTRICITY}),
    TELEGRAPH("Telegraph" ,"telegraph",4480,40, 2200 , new TechnologyTemplate[]{ELECTRICITY});


    private String name;
    private String fileName;
    private int x;
    private int y;
    private int cost;
    private ArrayList<TechnologyTemplate> requiredTechnologies;

    TechnologyTemplate(String name ,String fileName,int x , int y , int cost, TechnologyTemplate[] technologyTemplates) {
        this.name = name;
        this.cost = cost;
        this.fileName = fileName;
        this.x = x;
        this.y = y;
        this.requiredTechnologies = new ArrayList<>(Arrays.asList(technologyTemplates));

    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getFileName() {
        return fileName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<TechnologyTemplate> getRequiredTechnologies() {
        return requiredTechnologies;
    }

}
