package models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.util.ArrayList;

public class Chat {
    private final User user1;
    private final User user2;

    private int lastMessageIndex = 0;

    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public synchronized void addChatMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
        lastMessageIndex++;
    }

    public synchronized void editMessage(int index, String message) {
        chatMessages.get(index).edit(message);
    }

    public static Chat fromXML(String xml) {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return (Chat) xStream.fromXML(xml);
    }

    public String toXML() {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream.toXML(this);
    }

    public User getUser1() { return user1; }

    public User getUser2() { return user2; }

    public int getLastMessageIndex() { return lastMessageIndex; }

    public ArrayList<ChatMessage> getChatMessages() { return chatMessages; }
}
