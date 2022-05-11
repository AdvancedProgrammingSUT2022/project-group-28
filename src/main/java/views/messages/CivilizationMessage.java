package views.messages;

public class CivilizationMessage implements MessageTemplate{

    private String title;
    private String message;

    @Override
    public String getTitle() { return title; }

    @Override
    public String getMessage() { return message; }
}
