package models;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ClientRequest {
    public enum Request {
        START_LISTEN,
        LOGIN,
        REGISTER;
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
