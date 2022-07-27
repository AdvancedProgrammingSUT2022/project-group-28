package models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.util.ArrayList;

public class OnlineGame {
    private String lastGameUpdate;
    private final ArrayList<User> otherPlayers;


    public OnlineGame(ArrayList<User> players) {
        this.otherPlayers = players;
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

    public String getLastGameUpdate() { return lastGameUpdate; }
}
