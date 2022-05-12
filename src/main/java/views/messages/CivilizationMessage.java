package views.messages;

public enum CivilizationMessage implements MessageTemplate {
    CITY_LOSS("City loss", "@@ has been lost."),
    UNIT_CAPTURE("Unit capture", "A unit captured in @@,@@."),
    UNIT_DEATH("Unit death", "A unit died in @@,@@."),
    NEAR_ENEMY_CITY_ALERT("City alert", "There is an enemy near of the @@ int @@,@@."),
    NEAR_ENEMY_UNIT_ALERT("Unit alert", "There is an enemy near of the unit in @@,@@."),
    POPULATION_GROWTH("Population growth", "Population of @@ has been increased."),
    POPULATION_LOSS("Population loss", "Populations of @@ has been decreased."),
    COMPLETION_OF_STUDY("Technology studied", "The study of @@ completed.\n@@"), // second is the unlocked stuff
    COMPLETION_OF_CONSTRUCTION("Construction end", "@@ has been constructed in @@.");
    private String title;
    private String message;

    CivilizationMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getMessage() { return message; }
}
