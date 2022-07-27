package models;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ServerUpdate {
    public enum Update {
        SET_INITIAL_GAME,
        ATTEND_GAME_REQUEST,
        UPDATE_GAME,
        INVITE_GAME_REQUEST,
        IN_GAME_MESSAGE,
        TRADE_REQUEST,
        TRADE_RESULT
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
