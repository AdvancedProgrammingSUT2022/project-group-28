package models;

import java.time.LocalDate;

public class ChatMessage {
    private final User sender;
    private String message;
    private LocalDate lastModified = LocalDate.now();

    public ChatMessage(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public synchronized void edit(String newMessage) {
        this.message = newMessage;
        this.lastModified = LocalDate.now();
    }

    public User getSender() { return sender; }

    public String getMessage() { return message; }

    public LocalDate getLastModified() { return lastModified; }
}
