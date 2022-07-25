package models.network;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ServerUpdate {
    public enum Update {
        SET_INITIAL_GAME,
        ATTEND_GAME_REQUEST,
        INVITE_GAME_REQUEST
    }

    private Update update;
    private ArrayList<String> data;

    public ServerUpdate(Update update, ArrayList<String> data) {
        this.update = update;
        this.data = data;
    }

    public static ServerUpdate fromJson(String json) {
        return new Gson().fromJson(json, ServerUpdate.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public Update getUpdate() { return update; }

    public ArrayList<String> getData() { return data; }
}
