package models;

import java.util.ArrayList;

public class User {
    public static ArrayList<User> allUsers = new ArrayList<>();
    private static int lastId = -1;
    private int id;
    private String username;
    private String nickname;
    private String password;
    private int score;

    public User(String username, String nickname, String password) {
        User.lastId++;
        this.id = User.lastId;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.score = 0;
    }

    public static User getUserByUsername(String username){
        for (User user: allUsers) {
            if (user.username.equals(username)){
                return user;
            }
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
