package models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.util.ArrayList;

public class OnlineGame {
    private static ArrayList<OnlineGame> onlineGames = new ArrayList<>();

    private final User admin;
    private final ArrayList<User> otherPlayers;

    public OnlineGame(User admin, ArrayList<User> otherPlayers) {
        this.admin = admin;
        this.otherPlayers = otherPlayers;
    }


    public static ArrayList<OnlineGame> getOnlineGames() { return onlineGames; }

    public static OnlineGame getOnlineGameByUserID(int ID) {
        for (OnlineGame onlineGame : onlineGames) {
            if (onlineGame.getAdmin().getId() == ID) return onlineGame;
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



    public User getAdmin() { return admin; }

    public ArrayList<User> getOtherPlayers() { return otherPlayers; }
}
