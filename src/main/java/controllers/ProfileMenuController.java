package controllers;

import models.User;
import views.Menu;
import views.enums.Message;

public class ProfileMenuController {

    public static Message changeNickname(String nickname) {
        User user = User.getUserByNickname(nickname);
        if (user != null) {
            return Message.CHANGE_NICKNAME_ERROR;
        }
        user.setNickname(nickname);
        return Message.SUCCESS;
    }

    public static Message changePassword(String currentPassword, String newPassword) {
        User user = Menu.getLoggedInUser();
        if (!checkPassword(user, currentPassword)) {
            return Message.INCORRECT_PASSWORD;
        } else if (currentPassword.equals(newPassword)) {
            return Message.REPETITIOUS_PASSWORD;
        }
        user.setPassword(newPassword);
        return Message.SUCCESS;
    }

    private static boolean checkPassword(User user, String password) {
        if (user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
