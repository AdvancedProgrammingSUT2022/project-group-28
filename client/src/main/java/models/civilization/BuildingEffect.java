package models.civilization;

public class BuildingEffect {
    private final int food;
    private final int production;
    private final int science;
    private final int gold;
    private final int happiness;

    public BuildingEffect(int food, int production, int science, int gold, int happiness) {
        this.food = food;
        this.production = production;
        this.science = science;
        this.gold = gold;
        this.happiness = happiness;
    }

    public int getFood() { return food; }

    public int getProduction() { return production; }

    public int getScience() { return science; }

    public int getGold() { return gold; }

    public int getHappiness() { return happiness; }
}
