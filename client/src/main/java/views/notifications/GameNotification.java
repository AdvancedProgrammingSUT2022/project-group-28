package views.notifications;

import java.util.ArrayList;

public class GameNotification {
    private final NotificationTemplate notificationTemplate;
    private final ArrayList<String> data;
    private final int turnNumber;
    private boolean read;

    public GameNotification(NotificationTemplate notificationTemplate, ArrayList<String> data, int turnNumber) {
        this.notificationTemplate = notificationTemplate;
        this.data = data;
        this.turnNumber = turnNumber;
        this.read = false;
    }

    public GameNotification(NotificationTemplate notificationTemplate, ArrayList<String> data, int turnNumber,
                            boolean read) {
        this.notificationTemplate = notificationTemplate;
        this.data = data;
        this.turnNumber = turnNumber;
        this.read = read;
    }

    @Override
    public String toString() {
        String result = notificationTemplate.getContent();
        int i = 0;
        while (result.contains("@@")) {
            result = result.replaceFirst("\\@\\@", data.get(i));
            i++;
        }
        result = "<<<" + notificationTemplate.getTitle() + ">>>\n" + result;
        return result;
    }

    public NotificationTemplate getNotificationTemplate() {
        return notificationTemplate;
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
