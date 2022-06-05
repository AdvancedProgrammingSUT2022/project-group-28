package controllers;

import models.User;
import views.App;
import views.Menu;
import views.enums.Message;

public class RegisterMenuController {
    public static Message checkUserRegisterData(String username, String password, String nickname) {
        if (User.getUserByUsername(username) != null) {
            return Message.USERNAME_EXISTS;
        } else if (User.getUserByNickname(nickname) != null) {
            return Message.NICKNAME_EXISTS;
        }
        addUser(username, password, nickname);
        return Message.SUCCESS;

    }

    public static Message checkUserLoginData(String username, String password) {
        User user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return Message.LOGIN_ERROR;
        }
        return Message.SUCCESS;

    }

    public static void addUser(String username, String password, String nickname) {
        User user = new User(username, nickname, password);
        User.getAllUsers().add(user);
    }

    public static void setLoggedInUser(String username){
        App.setCurrentUser(User.getUserByUsername(username));
    }
}
