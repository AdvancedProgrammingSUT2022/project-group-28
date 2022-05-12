package views.messages;

import java.util.ArrayList;

public class GameMessage {
    private final MessageTemplate messageTemplate;
    private final ArrayList<String> data;
    private final int turnNumber;
    private boolean read;

    public GameMessage(MessageTemplate messageTemplate, ArrayList<String> data, int turnNumber) {
        this.messageTemplate = messageTemplate;
        this.data = data;
        this.turnNumber = turnNumber;
        this.read = false;
    }

    public GameMessage(MessageTemplate messageTemplate, ArrayList<String> data, int turnNumber, boolean read) {
        this.messageTemplate = messageTemplate;
        this.data = data;
        this.turnNumber = turnNumber;
        this.read = read;
    }

    @Override
    public String toString() {
        String result = messageTemplate.getMessage();
        int counter = 0;
        while (result.contains("@@")) {
            result = result.replaceFirst("\\@\\@", data.get(counter));
            counter++;
        }
        return result;
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
