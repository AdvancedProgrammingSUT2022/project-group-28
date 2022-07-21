package models;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ServerResponse {
    public enum Response {
        NOT_ADMIN,
        NOT_FRIEND,
        INVALID_WAITING_GAME,
        INVALID_NICKNAME,
        ALREADY_FRIEND,
        INVALID_FRIENDSHIP,
        OWN_FRIENDSHIP,
        FRIENDSHIP_REQUEST_WAITING,
        IS_OFFLINE,
        IS_LISTENING,
        INVALID_TOKEN,
        LOGIN_ERROR,
        NICKNAME_EXISTS,
        USERNAME_EXISTS,
        SUCCESS
    }

    private final Response response;
    private final ArrayList<String> data;

    public ServerResponse(Response response, ArrayList<String> data) {
        this.response = response;
        this.data = data;
    }

    public static ServerResponse fromJson(String json) { return new Gson().fromJson(json, ServerResponse.class); }

    public String toJson() { return new Gson().toJson(this); }

    public Response getResponse() { return response; }

    public ArrayList<String> getData() {
        return data;
    }

}
