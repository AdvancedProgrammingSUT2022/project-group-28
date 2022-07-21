package models;

public class FriendshipRequest {
    public enum State {
        ACCEPTED,
        REJECTED,
        WAITING
    }
    private final User sender;
    private final User receiver;
    private State state = State.WAITING;

    public FriendshipRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public synchronized void accept() {
        if (!sender.getFriends().contains(receiver)) { receiver.getFriends().add(sender); }
        if (!receiver.getFriends().contains(sender)) { sender.getFriends().add(receiver); }
        state = State.ACCEPTED;
    }

    public synchronized void reject() {
        state = State.REJECTED;
    }

    public User getSender() { return sender; }

    public User getReceiver() { return receiver; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }
}
