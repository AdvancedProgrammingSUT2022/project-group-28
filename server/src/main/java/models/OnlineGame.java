package models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.util.ArrayList;

public class OnlineGame {
    private static ArrayList<OnlineGame> onlineGames = new ArrayList<>();

    private String lastGameUpdate;
    private final ArrayList<User> otherPlayers;

    public OnlineGame(ArrayList<User> players) {
        this.otherPlayers = players;
    }

    public static ArrayList<OnlineGame> getOnlineGames() { return onlineGames; }

    public String getLastGameUpdate() { return lastGameUpdate; }

    public static OnlineGame getOnlineGameByUserID(int ID) {
        for (OnlineGame onlineGame : onlineGames) {
            for (User player : onlineGame.getOtherPlayers()) {
                if (player.getId() == ID) return onlineGame;
            }
        }
        return null;
    }

    public static OnlineGame fromXML(String xml) {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return (OnlineGame) xStream.fromXML(xml);
    }

    public String toXML() {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream.toXML(this);
    }

    public ArrayList<User> getOtherPlayers() { return otherPlayers; }

    public void setLastGameUpdate(String lastGameUpdate) { this.lastGameUpdate = lastGameUpdate; }
}
