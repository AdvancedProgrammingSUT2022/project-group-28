package views.notifications;

public enum CivilizationNotification implements NotificationTemplate {
    // TODO: modify data
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
    private String content;

    CivilizationNotification(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public String getContent() {
        return content;
    }

}
