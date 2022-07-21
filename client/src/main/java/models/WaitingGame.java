package models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.util.ArrayList;

public class WaitingGame {
    private final User admin;
    private ArrayList<User> otherPlayers = new ArrayList<>();
    private ArrayList<User> waitingForAccept = new ArrayList<>();

    public WaitingGame(User admin) {
        this.admin = admin;
    }

    public static WaitingGame fromXML(String xml) {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return (WaitingGame) xStream.fromXML(xml);
    }

    public String toXML() {
        XStream xStream = new XStream();
        return xStream.toXML(this);
    }

    public static String waitingGamesToXML(ArrayList<WaitingGame> waitingGames) {
        XStream xStream = new XStream();
        return xStream.toXML(waitingGames);
    }

    public static ArrayList<WaitingGame> waitingGamesFromXML(String xml) {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return (ArrayList<WaitingGame>) xStream.fromXML(xml);
    }

    public static boolean isAdmin(User user, ArrayList<WaitingGame> waitingGames) {
        for (WaitingGame waitingGame : waitingGames) {
            if (user.getId() == waitingGame.admin.getId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInAnyGame(User user, ArrayList<WaitingGame> waitingGames) {
        for (WaitingGame waitingGame : waitingGames) {
            for (User player : waitingGame.getOtherPlayers()) {
                if (player.getId() == user.getId()) return true;
            }
            for (User player : waitingGame.getWaitingForAccept()) {
                if (player.getId() == user.getId()) return true;
            }
        }
        return false;
    }

    public boolean isOtherPlayer(User user) {
        for (User player : this.otherPlayers) {
            if (player.getId() == user.getId()) return true;
        }
        return false;
    }

    public boolean isWaitingForAccept(User user) {
        for (User waiter : this.waitingForAccept) {
            if (waiter.getId() == user.getId()) return true;
        }
        return false;
    }
    public User getAdmin() { return admin; }

    public ArrayList<User> getOtherPlayers() { return otherPlayers; }

    public ArrayList<User> getWaitingForAccept() { return waitingForAccept; }
}
