package models;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ClientRequest {
    public enum Request {
        LOGOUT,
        UPDATE_USER,
        GET_ALL_USERS,
        SET_INITIAL_GAME,
        START_GAME,
        CREATE_GAME,
        CANCEL_GAME,
        ATTEND_GAME,
        LEAVE_GAME,
        ACCEPT_ATTEND_GAME,
        REJECT_ATTEND_GAME,
        ACCEPT_FRIENDSHIP,
        REJECT_FRIENDSHIP,
        REQUEST_FRIENDSHIP,
        GET_USER_INFO,
        GET_WAITING_GAMES,
        START_LISTEN,
        LOGIN,
        REGISTER
    }

    private final Request request;
    private final ArrayList<String> data;
    private String token;

    public ClientRequest(Request request, ArrayList<String> data) {
        this.request = request;
        this.data = data;
    }

    public ClientRequest(Request request, ArrayList<String> data, String token) {
        this.request = request;
        this.data = data;
        this.token = token;
    }

    public static ClientRequest fromJson(String json) { return new Gson().fromJson(json, ClientRequest.class); }

    public String toJson() { return new Gson().toJson(this); }

    public Request getRequest() {
        return request;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public String getToken() { return token; }
}
