package views.messages;

public enum UserMessage implements MessageTemplate {
    NICKNAME_EXISTS("Nickname exists", "Nickname ## has been taken by someone."),
    USERNAME_EXISTS("Username exists", "Username ## has been taken by someone."),
    FAILURE("Failure", "## was not successful"),
    SUCCESS("Success", "## was successful.");
    private String title;
    private String message;

    UserMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getMessage() { return message; }
}
