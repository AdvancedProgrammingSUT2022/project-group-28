package views.messages;

import java.util.ArrayList;

public class GameMessage {
    private final MessageTemplate messageTemplate;
    private final ArrayList<String> data;
    private boolean read;

    public GameMessage(MessageTemplate messageTemplate, ArrayList<String> data) {
        this.messageTemplate = messageTemplate;
        this.data = data;
        this.read = false;
    }

    public GameMessage(MessageTemplate messageTemplate, ArrayList<String> data, boolean read) {
        this.messageTemplate = messageTemplate;
        this.data = data;
        this.read = read;
    }

    @Override
    public String toString() {
        String result = messageTemplate.getMessage();
        int counter = 0;
        while (result.contains("##")) {
            result.replace("##", data.get(counter));
            counter++;
        }
        return result;
    }
}
