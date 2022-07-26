package models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.util.ArrayList;
import java.util.Iterator;

public class WaitingGame {
    private static ArrayList<WaitingGame> waitingGames = new ArrayList<>();

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
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream.toXML(waitingGames);
    }

    public static ArrayList<WaitingGame> waitingGamesFromXML(String xml) {
        XStream xStream = new XStream();
        return (ArrayList<WaitingGame>) xStream.fromXML(xml);
    }

    public static boolean isAdmin(User user) {
        for (WaitingGame waitingGame : waitingGames) {
            if (waitingGame.getAdmin().equals(user)) return true;
        }
        return false;
    }

    public static void leaveWaitingGame(User user) {
        Iterator<WaitingGame> waitingGameIterator = WaitingGame.getWaitingGames().iterator();

        while (waitingGameIterator.hasNext()) {
            WaitingGame waitingGame = waitingGameIterator.next();
            if (waitingGame.getOtherPlayers().contains(user)) {
                waitingGame.getOtherPlayers().remove(user);
            }
            if (waitingGame.getWaitingForAccept().contains(user)) {
                waitingGame.getWaitingForAccept().remove(user);
            }

            if (user.equals(waitingGame.admin)) {
                waitingGameIterator.remove();
            }
        }
    }

    public static WaitingGame getWaitingGameByAdminId(int id) {
        for (WaitingGame waitingGame : waitingGames) {
            if (waitingGame.getAdmin().getId() == id) return waitingGame;
        }
        return null;
    }

    public static void cancelGame(User admin) {
        Iterator<WaitingGame> iterator = waitingGames.iterator();
        while (iterator.hasNext()) {
            WaitingGame  waitingGame = iterator.next();
            if (waitingGame.getAdmin().equals(admin)) {
                iterator.remove();
            }
        }
    }

    public User getAdmin() { return admin; }

    public ArrayList<User> getOtherPlayers() { return otherPlayers; }

    public ArrayList<User> getWaitingForAccept() { return waitingForAccept; }

    public static ArrayList<WaitingGame> getWaitingGames() { return waitingGames; }
}
